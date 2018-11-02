package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseListView;

/**
 * 作者：yc on 2018/9/4.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface OrderChildContract {

    interface View extends IBaseListView{

        void payType(int payType, String s);
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void onRequest(int pagerNumber, int type);

        public abstract void orderConfirmReceipt(String id);

        public abstract void pay(String id, int payType);
    }

}
