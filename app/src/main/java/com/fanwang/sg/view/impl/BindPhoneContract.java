package com.fanwang.sg.view.impl;

import android.widget.EditText;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.flyco.roundview.RoundTextView;

/**
 * 作者：yc on 2018/9/3.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface BindPhoneContract {

    interface View extends IBaseView{
        void bindSuccess();

        void codeSuccess();

    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void bind(int mType, String phone, String pwd);

        public abstract void confirmState(EditText etPhone, EditText etPwd, RoundTextView tvConfirm);

        public abstract void code(int mType, String phone);
    }

}
