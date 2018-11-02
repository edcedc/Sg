package com.fanwang.sg.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.NewArrivalsChildAdapter;
import com.fanwang.sg.adapter.SecondKillChildAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.BNotTitleRecyclerBinding;
import com.fanwang.sg.presenter.NewArrivalsChildPresenter;
import com.fanwang.sg.view.impl.NewArrivalsChildContract;
import com.fanwang.sg.weight.GridDividerItemDecoration;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/8/31.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  新品专区
 */

@SuppressLint("ValidFragment")
public class NewArrivalsChildFrg extends BaseFragment<NewArrivalsChildPresenter, BNotTitleRecyclerBinding> implements NewArrivalsChildContract.View{

    private List<DataBean> listBean = new ArrayList<>();
    private NewArrivalsChildAdapter adapter;
    private String id;

    public NewArrivalsChildFrg(String i){
        this.id = i;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.b_not_title_recycler;
    }

    private boolean isRefresh = false;

    @Override
    protected void initView(View view) {
        hideLoading();
        if (adapter == null){
            adapter = new NewArrivalsChildAdapter(act, listBean);
        }
        mB.recyclerView.setLayoutManager(new GridLayoutManager(act, 2));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());

//        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2, Color.parseColor("#eff0f0")));
        mB.recyclerView.addItemDecoration(new GridDividerItemDecoration(20, ContextCompat.getColor(act,R.color.white_f4f4f4)));
        mB.recyclerView.setAdapter(adapter);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 20;
        params.leftMargin = 20;
        params.rightMargin = 20;
        mB.recyclerView.setLayoutParams(params);

        if (!isRefresh){
            isRefresh = true;
            mB.refreshLayout.startRefresh();
        }
        mB.refreshLayout.setEnableLoadmore(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1, id);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber += 1, id);
            }
        });
        setSwipeBackEnable(false);
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }

    @Override
    public void setData(Object data) {
        List<DataBean> list = (List<DataBean>) data;
        if (pagerNumber == 1) {
            listBean.clear();
            mB.refreshLayout.finishRefreshing();
        } else {
            mB.refreshLayout.finishLoadmore();
        }
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
