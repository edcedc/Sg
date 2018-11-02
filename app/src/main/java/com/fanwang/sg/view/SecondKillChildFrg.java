package com.fanwang.sg.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.SecondKillChildAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.BNotTitleRecyclerBinding;
import com.fanwang.sg.presenter.SecondKillChildPresenter;
import com.fanwang.sg.view.impl.SecondKillChildContract;
import com.fanwang.sg.weight.LinearDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/8/30.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  秒杀抢购
 */

@SuppressLint("ValidFragment")
public class SecondKillChildFrg extends BaseFragment<SecondKillChildPresenter, BNotTitleRecyclerBinding> implements SecondKillChildContract.View{

    private String id;
    private List<DataBean> listBean = new ArrayList<>();
    private SecondKillChildAdapter adapter;
    private int overdue;
    private String endTime;
    private String startTime;

    public SecondKillChildFrg(String id, List<DataBean> product, int overdue, String endTime, String startTime){
        this.id = id;
        this.listBean = product;
        this.overdue = overdue;
        this.endTime = endTime;
        this.startTime = startTime;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView(View view) {
        if (adapter == null){
            adapter = new SecondKillChildAdapter(act, listBean, overdue, endTime, startTime);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2, Color.parseColor("#eff0f0")));
        mB.recyclerView.setAdapter(adapter);
        if (!isRefresh){
            isRefresh = true;
            mB.refreshLayout.startRefresh();
        }
        mB.refreshLayout.setEnableRefresh(false);
        mB.refreshLayout.setEnableLoadmore(false);
        hideLoading();
        /*setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1, id);
            }
        });*/
        setSwipeBackEnable(false);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setData(List<DataBean> list) {
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
