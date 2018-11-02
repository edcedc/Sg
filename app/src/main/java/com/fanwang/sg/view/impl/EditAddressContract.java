package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;

/**
 * 作者：yc on 2018/9/13.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface EditAddressContract {

    interface View extends IBaseView{

    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void confirm(String name, String phone, String address, String detaileAddress, int type, String id, int position, String shen, String shi, String qu);
    }

}
