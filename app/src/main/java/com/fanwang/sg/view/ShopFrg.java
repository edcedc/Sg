package com.fanwang.sg.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.HomeNewArrivalsAdapter2;
import com.fanwang.sg.adapter.MessageAdapter;
import com.fanwang.sg.adapter.NewArrivalsChildAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.databinding.FShopBinding;
import com.fanwang.sg.presenter.ShopPresenter;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.view.impl.ShopContract;
import com.fanwang.sg.weight.GridDividerItemDecoration;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/21.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  店铺
 */

public class ShopFrg extends BaseFragment<ShopPresenter, FShopBinding> implements ShopContract.View{

    private String id;
    private NewArrivalsChildAdapter adapter;
    private List<DataBean> listBean = new ArrayList<>();

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_shop;
    }

    @Override
    protected void initView(View view) {
        if (adapter == null){
            adapter = new NewArrivalsChildAdapter(act, listBean);
        }
        mB.recyclerView.setLayoutManager(new GridLayoutManager(act, 2));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setBackgroundColor(ContextCompat.getColor(act,R.color.white_f4f4f4));

        mB.recyclerView.addItemDecoration(new GridDividerItemDecoration(20, ContextCompat.getColor(act,R.color.white_f4f4f4)));
        mB.recyclerView.setAdapter(adapter);
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onBusinessMain(id);
                mPresenter.onRequest(pagerNumber = 1, id);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber += 1, id);
            }
        });
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }

    @Override
    public void setData(Object data) {
        if (pagerNumber == 1) {
            listBean.clear();
            mB.refreshLayout.finishRefreshing();
        } else {
            mB.refreshLayout.finishLoadmore();
        }
        listBean.addAll((List<DataBean>) data);
        adapter.notifyDataSetChanged();
        mB.tvGoodsNumber.setText("数量：" + totalRow);
    }

    @Override
    public void onBusinessMainSuccess(DataBean bean) {//推荐商品
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getLogo(), mB.ivGoodsHead);
        mB.tvGoodsTitle.setText(bean.getName());
        setTitle(bean.getName());
        HomeNewArrivalsAdapter2 adapter = new HomeNewArrivalsAdapter2(act, bean.getRecommendProd());
        mB.gridView.setAdapter(adapter);
    }

    private int totalRow;
    @Override
    public void onPagerNum(int totalRow) {
        this.totalRow = totalRow;
        mB.tvGoodsNumber.setText("数量：" + totalRow);
    }
}
