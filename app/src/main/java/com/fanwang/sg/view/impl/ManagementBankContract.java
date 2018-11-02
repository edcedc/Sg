package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseListView;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;

import java.util.List;

/**
 * 作者：yc on 2018/9/6.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface ManagementBankContract {

    interface View extends IBaseView{

        void setData(List<DataBean> list);

        void onDefaultSuccess(int position, String id);

        void onDeleteSuccess(int position, String id);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest();

        public abstract void setDefaultCard(int position, String id);

        public abstract void delCard(int position, String id);
    }

}
