package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseListView;
import com.fanwang.sg.base.IBaseView;

/**
 * 作者：yc on 2018/9/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface DistributionContract {

    interface View extends IBaseListView{
        void setData(Object data, String allMoney);

        void onUserInto(String s);
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void onRequest(int pagerNumber);

        public abstract void userInto();
    }

}
