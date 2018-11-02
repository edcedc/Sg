package com.fanwang.sg.view;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.databinding.FBindPhoneBinding;
import com.fanwang.sg.presenter.BindPhonePresenter;
import com.fanwang.sg.utils.CountDownTimerUtils;
import com.fanwang.sg.view.impl.BindPhoneContract;

/**
 * 作者：yc on 2018/9/3.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  绑定手机号码
 */

public class BindPhoneFrg extends BaseFragment<BindPhonePresenter, FBindPhoneBinding> implements BindPhoneContract.View, View.OnClickListener{

    private int mType;

    public static BindPhoneFrg newInstance() {
        Bundle args = new Bundle();
        BindPhoneFrg fragment = new BindPhoneFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        mType = bundle.getInt("type");
        Drawable login_icon_phone = ContextCompat.getDrawable(act,R.mipmap.login_icon_phone);
        login_icon_phone.setBounds(0, 0, login_icon_phone.getIntrinsicWidth() ,login_icon_phone.getIntrinsicHeight());

        Drawable login_icon_verification = ContextCompat.getDrawable(act,R.mipmap.login_icon_verification);
        login_icon_verification.setBounds(0, 0, login_icon_verification.getIntrinsicWidth() ,login_icon_verification.getIntrinsicHeight());
        if (mType == 0){
            setTitle(getString(R.string.bing_phone));
        }else {
            setTitle(getString(R.string.update_phone));
            mB.tvConfirm.setText(getString(R.string.bing));
            mB.etPhone.setCompoundDrawables(null, null, null, null);
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_bind_phone;
    }

    @Override
    protected void initView(View view) {
        mB.tvCode.setOnClickListener(this);
        mB.tvConfirm.setOnClickListener(this);
        mPresenter.confirmState(mB.etPhone, mB.etPwd, mB.tvConfirm);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_code:
                mPresenter.code(mType, mB.etPhone.getText().toString().trim());
                break;
            case R.id.tv_confirm:
                mPresenter.bind(mType, mB.etPhone.getText().toString().trim(), mB.etPwd.getText().toString().trim());
                break;
        }
    }

    @Override
    public void bindSuccess() {

    }

    @Override
    public void codeSuccess() {
        new CountDownTimerUtils(act, 60000, 1000, mB.tvCode).start();
    }
}
