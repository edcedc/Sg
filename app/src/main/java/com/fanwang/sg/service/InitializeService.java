package com.fanwang.sg.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.request.target.ViewTarget;
import com.fanwang.sg.BuildConfig;
import com.fanwang.sg.R;
import com.fanwang.sg.base.User;
import com.fanwang.sg.mar.MyApplication;
import com.fanwang.sg.utils.Constants;
import com.fanwang.sg.utils.cache.ShareSessionIdCache;
import com.fanwang.sg.view.act.ConfirmationOrderAct;
import com.fanwang.sg.view.act.LoginAct;
import com.fanwang.sg.view.act.MainActivity;
import com.ganxin.library.LoadDataLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.nanchen.crashmanager.UncaughtExceptionHandlerImpl;
import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp3.OkHttpClient;

/**
 * Created by mingjun on 16/8/25.
 */
public class InitializeService extends IntentService {

    private static final String ACTION_INIT_WHEN_APP_CREATE = "com.anly.githubapp.service.action.INIT";

    public InitializeService() {
        super("InitializeService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                performInit();
            }
        }
    }

    private void performInit() {
        Utils.init(this);
        LogUtils.e("performInit begin:" + System.currentTimeMillis());
        initOkGo();
//        LogUtils.getConfig().setLogSwitch(false);
        // 设置崩溃后自动重启 APP
//        UncaughtExceptionHandlerImpl.getInstance().init(this, BuildConfig.DEBUG, true, 0, MainActivity.class);
        initLoad();
        initShare();
//        initRCloud();
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtils.e("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
        LogUtils.e("performInit end:" + System.currentTimeMillis());
    }

    private void initRCloud() {
        RongIM.init(this);
        RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
                switch (connectionStatus){
                    case CONNECTED://连接成功。
                        LogUtils.e("--------------------连接成功");
                        break;
                    case DISCONNECTED://断开连接。
                        LogUtils.e("--------------------断开连接");
                        break;
                    case CONNECTING://连接中。
                        LogUtils.e("--------------------链接中");
                        break;
                    case NETWORK_UNAVAILABLE://网络不可用。
                        LogUtils.e("--------------------网络不可用");
                        break;
                    case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                        LogUtils.e("--------------------掉线");
                        ToastUtils.showShort("当前账号在其它地方登录");
                        ActivityUtils.startActivity(LoginAct.class);
                        User.getInstance().setUserObj(null);
                        User.getInstance().setUserInfoObj(null);
                        User.getInstance().setLogin(false);
                        ShareSessionIdCache.getInstance(Utils.getApp()).remove();
                        ActivityUtils.finishAllActivities();
                        break;
                }
            }
        });
    }

    private void initShare() {
        UMConfigure.init(this, Constants.ShareID, "Umeng", UMConfigure.DEVICE_TYPE_PHONE,"");
        PlatformConfig.setWeixin(Constants.WX_APPID, Constants.WX_SECRER);
        PlatformConfig.setQQZone(Constants.QQ_APPID, Constants.QQ_KEY);
        //设置LOG开关，默认为false
        UMConfigure.setLogEnabled(true);
    }

    private void initLoad() {
        LoadDataLayout.getBuilder()
                .setLoadingText(getString(R.string.custom_loading_text))
                .setLoadingTextSize(16)
                .setLoadingTextColor(R.color.colorPrimary)
                //.setLoadingViewLayoutId(R.layout.custom_loading_view) //如果设置了自定义loading视图,上面三个方法失效
                .setEmptyImgId(R.drawable.ic_empty)
//                .setErrorImgId(R.drawable.ic_error)
//                .setNoNetWorkImgId(R.drawable.ic_no_network)
//                .setEmptyImageVisible(true)
//                .setErrorImageVisible(true)
//                .setNoNetWorkImageVisible(true)
                .setEmptyText(getString(R.string.custom_empty_text))
//                .setErrorText(getString(R.string.custom_error_text))
//                .setNoNetWorkText(getString(R.string.custom_nonetwork_text))
                .setAllTipTextSize(16)
                .setAllTipTextColor(R.color.text_color_child)
                .setAllPageBackgroundColor(R.color.pageBackground)
                .setReloadBtnText(getString(R.string.custom_reloadBtn_text))
                .setReloadBtnTextSize(16)
                .setReloadBtnTextColor(R.color.colorPrimary)
                .setReloadBtnBackgroundResource(R.drawable.selector_btn_normal)
                .setReloadBtnVisible(false)
                .setReloadClickArea(LoadDataLayout.FULL);
    }

    private void initOkGo() {
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("fanwang", "yc");    //header不支持中文，不允许有特殊字符
        //        headers.put("commonHeaderKey2", "commonHeaderValue2");
        com.lzy.okgo.model.HttpParams params = new com.lzy.okgo.model.HttpParams();
        //        params.put("sessionId", User.getInstance().getSessionId());     //param支持中文,直接传,不要自己编码
        //----------------------------------------------------------------------------------------//

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.SEVERE);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
//        builder.addInterceptor(new ChuckInterceptor(this));

        //超时时间设置，默认60秒
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        //        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失

        //https相关设置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
//        HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        //		builder.hostnameVerifier(new SafeHostnameVerifier());

        /*try {
            //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
            HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("yc.bks"), "123456", getAssets().open("wq.cert"));
            builder.sslSocketFactory(sslParams4.sSLSocketFactory, sslParams4.trustManager);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage().toString());
        }*/

        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(MyApplication.get(this))                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数
    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private class SafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                for (X509Certificate certificate : chain) {
                    certificate.checkValidity(); //检查证书是否过期，签名是否通过等
                }
            } catch (Exception e) {
                throw new CertificateException(e);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 这里只是我谁便写的认证规则，具体每个业务是否需要验证，以及验证规则是什么，请与服务端或者leader确定
     * 重要的事情说三遍，以下代码不要直接使用
     */
    private class SafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //验证主机名是否匹配
            //return hostname.equals("server.jeasonlzy.com");
            return true;
        }
    }
}

