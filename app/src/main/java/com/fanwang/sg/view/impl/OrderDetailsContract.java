package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;

/**
 * 作者：yc on 2018/9/10.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface OrderDetailsContract {

    interface View extends IBaseView{

        void setData(DataBean bean);

        void showRefund(DataBean bean);

        void payType(int payType, String data);

        void onFinish();
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void onRequest(String id);

        public abstract void orderConfirmReceipt(String id);

        public abstract void orderShowRefund(String id);

        public abstract void orderCancel(String id);

        public abstract void pay(String id, int payType);

        public abstract void orderShutDown(String id);
    }

}
