package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseListView;
import com.fanwang.sg.bean.DataBean;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

/**
 * 作者：yc on 2018/9/1.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface SearchContract {

    interface View extends IBaseListView{

        void setAefaultData(List<DataBean> list);
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void onRequest(String trim, int pageNumber, TwinklingRefreshLayout refreshLayout);

        public abstract void onAefaultRequest(int pagerNumber);
    }

}
