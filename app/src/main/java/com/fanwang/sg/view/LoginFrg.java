package com.fanwang.sg.view;

import android.os.Bundle;
import android.view.View;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.databinding.FLoginBinding;
import com.fanwang.sg.event.RongcloudInEvent;
import com.fanwang.sg.presenter.LoginPresenter;
import com.fanwang.sg.utils.CountDownTimerUtils;
import com.fanwang.sg.utils.DataListTool;
import com.fanwang.sg.utils.cache.SharedAccount;
import com.fanwang.sg.view.act.MainActivity;
import com.fanwang.sg.view.impl.LoginContract;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;
import com.zaaach.toprightmenu.TopRightMenuTool;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

/**
 * 作者：yc on 2018/8/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 * 登录
 */

public class LoginFrg extends BaseFragment<LoginPresenter, FLoginBinding> implements LoginContract.View, View.OnClickListener {

    public static LoginFrg newInstance() {
        Bundle args = new Bundle();
        LoginFrg fragment = new LoginFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void loginSuccess() {
//        MeFrg.isRefresh = false;
        EventBus.getDefault().post(new RongcloudInEvent());
        ActivityUtils.startActivity(MainActivity.class);
        ActivityUtils.finishAllActivities();
    }

    @Override
    public void codeSuccess() {
        new CountDownTimerUtils(act, 60000, 1000, mB.tvCode).start();
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_login;
    }

    @Override
    protected void initView(View view) {
        mB.fyClose.setOnClickListener(this);
        mB.tvCode.setOnClickListener(this);
        mB.tvConfirm.setOnClickListener(this);
        mB.ivWx.setOnClickListener(this);
        mB.ivQq.setOnClickListener(this);
        mPresenter.confirmState(mB.etPhone, mB.etPwd, mB.tvConfirm);
        mB.fyClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.finish();
            }
        });
    }



    private List<MenuItem> menuOrderRefundItems = DataListTool.getOrderRefundList();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                mPresenter.code(mB.etPhone.getText().toString().trim());
                break;
            case R.id.tv_confirm:
                mPresenter.login(mB.etPhone.getText().toString().trim(), mB.etPwd.getText().toString().trim());
                break;
            case R.id.iv_wx:
//                boolean b = UMShareAPI.get(act).isAuthorize(act, SHARE_MEDIA.WEIXIN);
                UMShareAPI.get(act).doOauthVerify(act, SHARE_MEDIA.WEIXIN, listener);
                break;
            case R.id.iv_qq:
//                boolean b = UMShareAPI.get(act).isAuthorize(act, SHARE_MEDIA.QQ);
                UMShareAPI.get(act).doOauthVerify(act, SHARE_MEDIA.QQ, listener);
                break;
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
    }

    private UMAuthListener listener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
//            showLoading();
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//            hideLoading();
            LogUtils.e("onComplete");
            if (map != null && map.size() != 0){
                String wx_openid = "";
                String wx_token = "";
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    LogUtils.e(entry.getKey(), entry.getValue());
                    if (entry.getKey().equals("openid")){
                        wx_openid = entry.getValue();
                    }
                    if (entry.getKey().equals("access_token")){
                        wx_token = entry.getValue();
                    }
                }
                mPresenter.wxLogin(LoginFrg.this, wx_openid, wx_token);
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable t) {
//            hideLoading();
            LogUtils.e("失败：" + t.getMessage());
            showToast(t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
//            hideLoading();
            LogUtils.e("取消了");
        }
    };


}
