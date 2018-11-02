package com.fanwang.sg.view.impl;

import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;

import java.util.List;

/**
 * 作者：yc on 2018/8/29.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface CartContract {

    interface View extends IBaseView{

        void setData(List<DataBean> list);

        void onCartNumSuccess(int groupPosition, int childPosition, String skuid, int num);

        void onDelCartSuccess(List<DataBean> listBean);

        void onAllSku(List<DataBean> listBean, String substring, int mStock, String image, String choice, double realPrice, String pid, int groupPosition, int childPosition, String id);

        void onCartModifyCartSkuSuccess(String price, String className, int groupPosition, int childPosition);

        void onCartListNull();
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void onRequest(int pagerNumber);

        public abstract void onCartNum(int groupPosition, int childPosition, String skuid, int num, String id);

        public abstract void onMovecollect(List<DataBean> listBean);

        public abstract void onDelCart(List<DataBean> listBean);

        public abstract void allSku(String pid, String image, String choice, double realPrice, int groupPosition, int childPosition, String id);

        public abstract void cartModifyCartSku(String ids, String pid, String price, String className, int groupPosition, int childPosition, String id);
    }

}
