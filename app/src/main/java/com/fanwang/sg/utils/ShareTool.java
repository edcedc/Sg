package com.fanwang.sg.utils;

import android.app.Activity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.sg.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/5/11.
 *  分享工具
 */

public class ShareTool {

    private static class LazyHolder {
        private static final ShareTool INSTANCE = new ShareTool();
    }

    private ShareTool() {
    }

    public static final ShareTool getInstance() {
        return LazyHolder.INSTANCE;
    }

//    private ShareTool() {
//        throw new UnsupportedOperationException("u can't instantiate me...");
//    }

    /****************************分享***********************************/
    private ShareAction shareAction;

    public ShareAction shareAction(final Activity act, final String url) {
        String ss = "api/";
        String s = (String) url;
        if (s.contains(ss)){
            s = s.replace(ss, "");
        }
        final String finalS = s;
        LogUtils.e(finalS);
        return shareAction = new ShareAction(act).setDisplayList(
                SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.SINA)
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        UMWeb web = new UMWeb(finalS);
                        web.setTitle("飞鸽传媒");
                        web.setDescription("广告详情");
                        web.setThumb(new UMImage(act, R.mipmap.login_logo));
                        new ShareAction(act)
                                .withMedia(web)
                                .setPlatform(share_media)
                                .setCallback(listener(act))
                                .share();
                    }

                    @Override
                    protected void finalize() throws Throwable {
                        super.finalize();
                    }
                });
    }

    public void release(Activity act) {
        UMShareAPI.get(act).release();
    }

    private CustomShareListener listener(Activity act) {
        return new CustomShareListener(act);
    }

    private class CustomShareListener implements UMShareListener {

        private WeakReference<Activity> mActivity;

        private CustomShareListener(Activity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {
            ToastUtils.showShort(platform + " 正在启动");
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtils.showShort(platform + " 分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            LogUtils.e(t.getMessage());
            ToastUtils.showShort(platform + " 分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtils.showShort(platform + " 分享取消了");
        }
    }


    /****************************授权***********************************/
    public void Authorization(Activity act, UMAuthListener listener){
        boolean b = UMShareAPI.get(act).isAuthorize(act, SHARE_MEDIA.WEIXIN);
        if (b){
            LogUtils.e("删除授权");
            UMShareAPI.get(act).deleteOauth(act, SHARE_MEDIA.WEIXIN, listener);
        }else {
            LogUtils.e("授权");
            UMShareAPI.get(act).doOauthVerify(act, SHARE_MEDIA.WEIXIN, listener);
        }
    }

    /****************************登陆***********************************/
    public void ShareLogin(Activity act){

    }


}
