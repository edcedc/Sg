package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseListView;
import com.fanwang.sg.bean.DataBean;

/**
 * 作者：yc on 2018/9/21.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface ShopContract {

    interface View extends IBaseListView {

        void onBusinessMainSuccess(DataBean bean);

        void onPagerNum(int totalRow);
    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onBusinessMain(String id);

        public abstract void onRequest(int i, String id);
    }

}
