package com.fanwang.sg.view.bottomFrg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.ShareAdapter;
import com.fanwang.sg.base.BaseBottomSheetFrag;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.utils.Constants;
import com.fanwang.sg.utils.FileSaveUtils;
import com.fanwang.sg.utils.ImageUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 作者：yc on 2018/10/19.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  分享
 */

public class ShareBottomFrg extends BaseBottomSheetFrag {

    private GridView gridView;
    private ShareAdapter adapter;

    @Override
    public int bindLayout() {
        return R.layout.f_share;
    }

    private View viewLayout;

    public void setViewLayout(View viewLayout) {
        this.viewLayout = viewLayout;
    }

    private Bitmap imgBitmap;

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    private String shareUrl;

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    @Override
    public void initView(View view) {
        gridView = view.findViewById(R.id.gridView);
        view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        final String[] str = {"保存相册", "微信", "微信朋友圈", "QQ"};
        int[] img = {R.mipmap.home_icon_preservation, R.mipmap.home_icon_wechat, R.mipmap.home_icon_friends, R.mipmap.home_icon_qq};
        List<DataBean> listBean = new ArrayList<>();
        for (int i = 0;i < str.length;i++){
            DataBean bean = new DataBean();
            bean.setName(str[i]);
            bean.setImg(img[i] + "");
            listBean.add(bean);
        }
        if (adapter == null){
            adapter = new ShareAdapter(act, listBean);
        }
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UMImage imagelocal = null;
                UMWeb weblocal = null;
                if (imgBitmap == null){
                    weblocal = new UMWeb(shareUrl);
                    weblocal.setTitle("生成海报");
                    weblocal.setThumb(new UMImage(act, R.mipmap.login_logo));
                    weblocal.setDescription("快去注册吧");
                }else {
                    imagelocal = new UMImage(act, imgBitmap);//本地文件
                    imagelocal.setThumb(new UMImage(act, R.mipmap.login_logo));
                    imagelocal.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
                }
                switch (i){
                    case 0:
                        if (imgBitmap == null){
                            showToast("当前不能保存");
                            return;
                        }
                        FileSaveUtils.save(act, imgBitmap);
                        showToast("保存成功");
                        break;
                    case 1:
                        if (imagelocal == null){
                            new ShareAction((Activity) act)
                                    .withMedia(weblocal)
                                    .setPlatform(SHARE_MEDIA.WEIXIN)
                                    .setCallback(shareListener)
                                    .share();
                        }else {
                            new ShareAction((Activity) act)
                                    .withMedia(imagelocal)
                                    .setPlatform(SHARE_MEDIA.WEIXIN)
                                    .setCallback(shareListener)
                                    .share();
                        }

                        break;
                    case 2:
                        if (imagelocal == null){
                            new ShareAction((Activity) act)
                                    .withMedia(weblocal)
                                    .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                    .setCallback(shareListener)
                                    .share();
                        }else {
                            new ShareAction((Activity) act)
                                    .withMedia(imagelocal)
                                    .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                    .setCallback(shareListener)
                                    .share();
                        }
                        break;
                    case 3:
                        if (imagelocal == null){
                            new ShareAction((Activity) act)
                                    .withMedia(weblocal)
                                    .setPlatform(SHARE_MEDIA.QQ)
                                    .setCallback(shareListener)
                                    .share();
                        }else {
                            new ShareAction((Activity) act)
                                    .withMedia(imagelocal)
                                    .setPlatform(SHARE_MEDIA.QQ)
                                    .setCallback(shareListener)
                                    .share();
                        }
                        break;
                }
                dismiss();
            }
        });
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            showToast("成功");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            LogUtils.e(platform, t.getMessage());
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            showToast("分享取消");
        }
    };

}
