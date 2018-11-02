package com.fanwang.sg.view.impl;

import android.widget.TextView;

import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.bean.ProductProductDetailBean;
import com.fanwang.sg.view.GoodsDetailsFrg;

import java.util.List;

/**
 * 作者：yc on 2018/9/12.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public interface GoodsDetailsContract {

    interface View extends IBaseView{

        void setData(ProductProductDetailBean data);

        void onAllSku(List<DataBean> listBean, String substring, int allStock);

        void onCollectionSuccess(boolean collect);
    }

    abstract class Presenter extends BasePresenter<View>{

        public abstract void ajaxDetail(String id);

        public abstract void allSku(String id);

        public abstract void onCollection(boolean collect, String id);

        public abstract void isCollect(boolean collect, TextView tvCollection);

        public abstract void onAddCart(int type, String id, String skuId, int goodsNumber, BaseFragment root, List<DataBean> listBean, int secKillState, int flag, int isCredited);

        public abstract void onSubmitOrder(BaseFragment root, int type, String id, List<DataBean> listBean, int secKillState, int flag, int isCredited);
    }

}
