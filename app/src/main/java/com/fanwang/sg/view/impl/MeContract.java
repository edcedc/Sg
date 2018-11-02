package com.fanwang.sg.view.impl;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.weight.WithScrollGridView;

import org.json.JSONObject;

/**
 * 作者：yc on 2018/9/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface MeContract {

    interface View extends IBaseView{

        void setData(JSONObject bean);
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void order_gridview(WithScrollGridView gridView, Activity act);

        public abstract void laber_gridview(WithScrollGridView gridView, Activity act, BaseFragment root);

        public abstract void listview(RecyclerView listView, BaseFragment root);

        public abstract void onUserInfo();
    }

}
