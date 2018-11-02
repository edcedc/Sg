package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;

import java.util.List;

/**
 * 作者：yc on 2018/9/26.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface ConfirmationOrderContract {

    interface View extends IBaseView{


        void setData(List<DataBean> list);

        void setPayType(int payType, int type, int flag, int code, String data);
    }

    abstract class Presenter extends BasePresenter<View>{

        boolean isSubmitOrderState = false;//记录订单支付状态 默认不支付

        public boolean isSubmitOrderState() {
            return isSubmitOrderState;
        }

        public void setSubmitOrderState(boolean submitOrderState) {
            isSubmitOrderState = submitOrderState;
        }

        public abstract void onAddressList();

        public abstract void onConfirm(int flag, int type, List<DataBean> listBean, String addressId, BaseFragment root, String id, String orderNo, int isCredited);
    }

}
