package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;
import com.zaaach.toprightmenu.MenuItem;

import java.util.List;

/**
 * 作者：yc on 2018/9/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface ForwardContract {

    interface View extends IBaseView{

        void setData(List<MenuItem> list);

        void onWithDrawMoneySuccess(DataBean bean);
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void onRequest();

        public abstract void confirm(String trim, String id, String withDrawMoney);

        public abstract void onWithDraw();
    }

}
