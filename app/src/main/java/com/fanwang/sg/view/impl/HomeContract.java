package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseListView;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;

/**
 * 作者：yc on 2018/8/28.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface HomeContract {

    interface View extends IBaseView{

        void setData(DataBean data);
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void onRequest(int pagerNumber);
    }

}
