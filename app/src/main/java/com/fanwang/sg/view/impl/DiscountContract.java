package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseListView;
import com.fanwang.sg.bean.DataBean;

/**
 * 作者：yc on 2018/8/31.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface DiscountContract {

    interface View extends IBaseListView{

        void setBannerData(DataBean bean);
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void onRequest(int pagerNumber, String id);
    }

}
