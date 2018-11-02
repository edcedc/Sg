package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;
import java.util.List;

/**
 * 作者：yc on 2018/10/17.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface LogisticsContract {

    interface View extends IBaseView {

        void setData(List<DataBean> list, String ShipperCode, String logisticCode, String image);

    }

    abstract class Presenter extends BasePresenter<View> {

        public abstract void onRequest(String id);

    }

}
