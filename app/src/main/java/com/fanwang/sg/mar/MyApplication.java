package com.fanwang.sg.mar;

import android.content.Context;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.request.target.ViewTarget;
import com.fanwang.sg.R;
import com.fanwang.sg.base.User;
import com.fanwang.sg.service.InitializeService;
import com.fanwang.sg.utils.cache.ShareSessionIdCache;
import com.fanwang.sg.view.act.LoginAct;
import com.nanchen.crashmanager.CrashApplication;
import com.tencent.smtt.sdk.QbSdk;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class MyApplication extends CrashApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ViewTarget.setTagId(R.id.tag_glide);//项目glide 图片ID找不到  会报null

        InitializeService.start(this);
        initRCloud();
    }

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    private void initRCloud() {
        RongIM.init(this);
        RongIM.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(RongIMClient.ConnectionStatusListener.ConnectionStatus connectionStatus) {
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

}
