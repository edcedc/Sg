package com.fanwang.sg.view.impl;


import android.widget.EditText;

import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.flyco.roundview.RoundTextView;

/**
 * Created by Simple on 2018/7/13.
 */

public interface LoginContract {

    interface View extends IBaseView {
        void loginSuccess();

        void codeSuccess();
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void login(String phone, String pwd);

        public abstract void confirmState(EditText etPhone, EditText etPwd, RoundTextView tvConfirm);

        public abstract void code(String phone);

        public abstract void wxLogin(BaseFragment root, String wx_openid, String wx_token);
    }
}
