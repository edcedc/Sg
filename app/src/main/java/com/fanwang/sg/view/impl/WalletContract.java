package com.fanwang.sg.view.impl;

import android.support.v7.widget.RecyclerView;

import com.fanwang.sg.adapter.MeAdapter;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;

import java.util.List;

/**
 * 作者：yc on 2018/9/4.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface WalletContract {

    interface View extends IBaseView{

        void onUserInfo(double balance);
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void listview(RecyclerView recyclerView, List<DataBean> listBean, MeAdapter adapter);

        public abstract void userInto();
    }

}
