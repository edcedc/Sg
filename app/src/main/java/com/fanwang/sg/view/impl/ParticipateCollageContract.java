package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;

import java.util.List;

/**
 * 作者：yc on 2018/9/29.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface ParticipateCollageContract {

    interface View extends IBaseView{

    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void onSubmitOrder(BaseFragment root, int type, String id, List<DataBean> listBean, int secKillState, int flag, String orderNo);
    }

}
