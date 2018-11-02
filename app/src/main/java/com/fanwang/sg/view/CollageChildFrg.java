package com.fanwang.sg.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.CollageChildAdapter;
import com.fanwang.sg.adapter.SecondKillChildAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.BNotTitleRecyclerBinding;
import com.fanwang.sg.presenter.CollageChildPresenter;
import com.fanwang.sg.view.impl.CollageChildContract;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/8/30.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

@SuppressLint("ValidFragment")
public class CollageChildFrg extends BaseFragment<CollageChildPresenter, BNotTitleRecyclerBinding> implements CollageChildContract.View{

    private List<DataBean> listBean = new ArrayList<>();
    private CollageChildAdapter adapter;
    private boolean isRefresh = false;
    private int state;


    public CollageChildFrg(int i){
        this.state = i;
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

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!isRefresh){
            isRefresh = true;
            showLoadDataing();
            mB.refreshLayout.startRefresh();
        }
    }

    @Override
    protected void initView(View view) {
        if (adapter == null){
            adapter = new CollageChildAdapter(act, listBean, state);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2, Color.parseColor("#eff0f0")));
        mB.recyclerView.setAdapter(adapter);
        mB.refreshLayout.setEnableLoadmore(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1, state);
            }
        });
        setSwipeBackEnable(false);
    }

    @Override
    public void setData(List<DataBean> list) {
        mB.refreshLayout.finishRefreshing();
        if (list.size() != 0){
            hideLoading();
            if (pagerNumber == 1) {
                listBean.clear();
                mB.refreshLayout.finishRefreshing();
            } else {
                mB.refreshLayout.finishLoadmore();
            }
            listBean.addAll(list);
            adapter.notifyDataSetChanged();
        }else {
            showLoadEmpty();
        }
    }

}
