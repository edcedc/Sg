package com.fanwang.sg.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DividerItemDecoration;
import android.text.Html;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.OrderDetailsCollageAdapter;
import com.fanwang.sg.adapter.OrderDetailsListAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.FInvitationCollageBinding;
import com.fanwang.sg.utils.FileSaveUtils;
import com.fanwang.sg.view.bottomFrg.ParameterBottomFrg;
import com.fanwang.sg.view.bottomFrg.ShareOrderBottomFrg;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/10/18.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  邀请拼团
 */

public class InvitationCollageFrg extends BaseFragment<BasePresenter, FInvitationCollageBinding> implements View.OnClickListener{


    private DataBean bean;
    private List<DataBean> listBean = new ArrayList<>();
    private OrderDetailsListAdapter adapter;

    private List<DataBean> listCollageBean = new ArrayList<>();
    private OrderDetailsCollageAdapter collageAdapter;

    private ShareOrderBottomFrg shareOrderBottomFrg;

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        bean = new Gson().fromJson(bundle.getString("bean"), DataBean.class);
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_invitation_collage;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.collage_rule2));
        mB.ivDownload.setOnClickListener(this);
        mB.ivWx.setOnClickListener(this);
        mB.ivWxP.setOnClickListener(this);
        if (adapter == null){
            adapter = new OrderDetailsListAdapter(act, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2, Color.parseColor("#eff0f0")));
        mB.recyclerView.setAdapter(adapter);
        if (collageAdapter == null){
            collageAdapter = new OrderDetailsCollageAdapter(act, listCollageBean);
        }
        mB.gvCollage.setAdapter(collageAdapter);

        int collageNum = bean.getCollageNum();
        List<DataBean> listUser = bean.getListUser();
        int collageNums = collageNum - listUser.size();
        setisGroupPurchase(bean);
        String collage_title = "还差" + "<font color='#FE2701'> " + collageNums + " </font>" + "赶快邀请好友来";
        mB.tvCollageTitle.setText(Html.fromHtml(collage_title));
        mB.tvCollageTime.setText("拼团剩余时间：");

        List<DataBean> listOrderDetails = bean.getListOrderDetails();
        if (listOrderDetails != null && listOrderDetails.size() != 0){
            listBean.addAll(listOrderDetails);
            adapter.setIsGroupPurchase(bean.getIsGroupPurchase());
            adapter.notifyDataSetChanged();
            double allPrice = 0;
            for (DataBean bean1 : listOrderDetails){
                allPrice += bean1.getNum() * bean1.getPrice();
            }
        }
        if (shareOrderBottomFrg == null){
            shareOrderBottomFrg = new ShareOrderBottomFrg(bean);
        }
    }


    //是否拼团单
    private void setisGroupPurchase(DataBean bean){
        List<DataBean> listUser = bean.getListUser();
        if (listUser != null && listUser.size() != 0){//是否拼团那种
            int collageNum = bean.getCollageNum();
            int collageNums = collageNum - listUser.size();
            mB.gpCollage.setVisibility(View.VISIBLE);
            if (collageNums > 3 && listUser.size() < 3){
                listCollageBean.addAll(listUser);
                for (int i = 0;i < 3 - listUser.size();i++){
                    listCollageBean.add(new DataBean());
                }
            }else if (collageNum > 3 && listUser.size() > 3){
                for (int i = 0;i < 3;i++){
                    listCollageBean.addAll(listUser);
                }
            }else if (collageNum < 3 && listUser.size() < collageNum){
                listCollageBean.addAll(listUser);
                for (int i = 0;i < (collageNum - listUser.size());i++){
                    listCollageBean.add(new DataBean());
                }
            }else {
                listCollageBean.addAll(listUser);
            }
            collageAdapter.notifyDataSetChanged();
//            mB.gpPendingDelivery.setVisibility(View.VISIBLE);
            mB.gpCollage.setVisibility(View.VISIBLE);
            mB.gpCollageRule.setVisibility(View.GONE);
        }else {

        }
    }

    @Override
    public void onClick(View view) {
        if (shareOrderBottomFrg != null && !shareOrderBottomFrg.isShowing()){
            shareOrderBottomFrg.show(getChildFragmentManager(), "dialog");
        }

        switch (view.getId()){
            case R.id.iv_download:
                shareOrderBottomFrg.setOnClickListener(new ShareOrderBottomFrg.OnClickListener() {
                    @Override
                    public void click(Bitmap imgBitmap) {
                        FileSaveUtils.save(act, imgBitmap);
                        showToast("保存成功");
                    }
                });
                break;
            case R.id.iv_wx:
                shareOrderBottomFrg.setOnClickListener(new ShareOrderBottomFrg.OnClickListener() {
                    @Override
                    public void click(Bitmap imgBitmap) {
                        UMImage imagelocal = new UMImage(act, imgBitmap);//本地文件
                        imagelocal.setThumb(new UMImage(act, R.mipmap.login_logo));
                        imagelocal.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图

                        new ShareAction((Activity) act).withMedia(imagelocal )
                                .setPlatform(SHARE_MEDIA.WEIXIN)
                                .setCallback(shareListener).share();
                    }
                });
                break;
            case R.id.iv_wx_p:
                shareOrderBottomFrg.setOnClickListener(new ShareOrderBottomFrg.OnClickListener() {
                    @Override
                    public void click(Bitmap imgBitmap) {
                        UMImage imagelocal = new UMImage(act, imgBitmap);//本地文件
                        imagelocal.setThumb(new UMImage(act, R.mipmap.login_logo));
                        imagelocal.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图

                        new ShareAction((Activity) act).withMedia(imagelocal )
                                .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                .setCallback(shareListener).share();
                    }
                });
                break;
        }
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
