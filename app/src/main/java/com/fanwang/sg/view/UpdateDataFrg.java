package com.fanwang.sg.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.User;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FUpdateDataBinding;
import com.fanwang.sg.event.CameraInEvent;
import com.fanwang.sg.presenter.UpdateDataPresenter;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.utils.PictureSelectorTool;
import com.fanwang.sg.view.bottomFrg.CameraBottomFrg;
import com.fanwang.sg.view.bottomFrg.SexBottomFrg;
import com.fanwang.sg.view.impl.UpdateDataContract;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/3.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  我的资料
 */

public class UpdateDataFrg extends BaseFragment<UpdateDataPresenter, FUpdateDataBinding> implements UpdateDataContract.View, View.OnClickListener{

    public static UpdateDataFrg newInstance() {
        Bundle args = new Bundle();
        UpdateDataFrg fragment = new UpdateDataFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private CameraBottomFrg cameraBottomFrg;
    private List<LocalMedia> localMediaList = new ArrayList<>();

    private SexBottomFrg sexBottomFrg;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_update_data;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.me_data));
        mB.ivHead.setOnClickListener(this);
        mB.lyNick.setOnClickListener(this);
        mB.lySex.setOnClickListener(this);
        EventBus.getDefault().register(this);

        JSONObject userInfoObj = User.getInstance().getUserInfoObj();
        if (userInfoObj != null){
            GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + userInfoObj.optString("head"), mB.ivHead, true);
            String nickname = userInfoObj.optString("nickname");
            if (!nickname.equals("null")){
                mB.tvNick.setText(nickname);
            }
            int sex = userInfoObj.optInt("sex");
            if (sex != 0){
                mB.tvSex.setText(sex == 1 ? "男" : "女");
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainThreadInEvent(CameraInEvent event) {
        if (cameraBottomFrg != null && cameraBottomFrg.isShowing())cameraBottomFrg.dismiss();
        localMediaList.clear();
        localMediaList.addAll(PictureSelector.obtainMultipleResult((Intent) event.getObject()));
        String path = localMediaList.get(0).getCutPath();
        mPresenter.updateHead(path);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_head:
                showCamear(CameraInEvent.HEAD_CAMEAR, CameraInEvent.HEAD_PHOTO, false);
                break;
            case R.id.ly_nick:
                UIHelper.startUpdateNickFrg(this, mB.tvNick.getText().toString());
                break;
            case R.id.ly_sex:
                showSex();
                break;
        }
    }

    private void showSex(){
        if (sexBottomFrg == null){
            sexBottomFrg = new SexBottomFrg();
        }
        sexBottomFrg.show(getChildFragmentManager(), "dialog");
        sexBottomFrg.setSexListener(new SexBottomFrg.onSexListener() {
            @Override
            public void sex(int sex) {
                mPresenter.updateSex(sex);
                mB.tvSex.setText(sex == 1 ? "男" : "女");
            }
        });
    }

    private void showCamear(final int camear, final int photo, final boolean circleDimmedLayer){
        if (cameraBottomFrg == null){
            cameraBottomFrg = new CameraBottomFrg();
        }
        cameraBottomFrg.show(getChildFragmentManager(), "dialog");
        cameraBottomFrg.setCameraListener(new CameraBottomFrg.onCameraListener() {
            @Override
            public void camera() {
                PictureSelectorTool.PictureSelectorImage(act, camear, circleDimmedLayer);
                if (cameraBottomFrg != null && cameraBottomFrg.isShowing())cameraBottomFrg.dismiss();
            }

            @Override
            public void photo() {
                PictureSelectorTool.photo(act, photo, circleDimmedLayer);
                if (cameraBottomFrg != null && cameraBottomFrg.isShowing())cameraBottomFrg.dismiss();
            }
        });
    }

    @Override
    public void onUpdateHeadSuccess(String path) {
        Glide.with(act).load(path).into(mB.ivHead);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        JSONObject userObj = User.getInstance().getUserInfoObj();
        if (userObj != null){
            String nickname = userObj.optString("nickname");
            if (!nickname.equals("null")){
                mB.tvNick.setText(nickname);
            }
            int sex = userObj.optInt("sex");

        }
    }
}
