package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseListView;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;

import java.util.List;

/**
 * 作者：yc on 2018/9/13.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface AddressContract {

    interface View extends IBaseView{

        void setData(List<DataBean> list);

        void onDefaultAddressSuccess(int position, String id);

        void onDeleteSuccess(int position, String id);
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void onRequest(int pagerNumber);

        public abstract void onDefaultAddress(int position, String id);

        public abstract void onDelete(int position, String id);
    }

}
