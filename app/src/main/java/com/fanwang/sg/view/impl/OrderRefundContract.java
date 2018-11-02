package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

/**
 * 作者：yc on 2018/10/15.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface OrderRefundContract {

    interface View extends IBaseView {

    }

    abstract class Presenter extends BasePresenter<View> {


        public abstract void confirm(String id, int type, List<LocalMedia> localMediaList, int cargoType, int state, String content, String orderNumber, String logistics, String price, List<DataBean> listOrderDetails);
    }


}
