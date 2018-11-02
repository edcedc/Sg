package com.fanwang.sg.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.CollageChildAdapter;
import com.fanwang.sg.adapter.MessageAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.databinding.BRecyclerBinding;
import com.fanwang.sg.presenter.BaseListPresenter;
import com.fanwang.sg.view.impl.BaseListContract;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/13.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  消息中心
 */

public class MessageFrg extends BaseFragment<BaseListPresenter, BRecyclerBinding> implements BaseListContract.View{

    private List<DataBean> listBean = new ArrayList<>();
    private MessageAdapter adapter;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.b_recycler;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.message));
        if (adapter == null){
            adapter = new MessageAdapter(act, this, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2, Color.parseColor("#eff0f0")));
        mB.recyclerView.setAdapter(adapter);
        showLoadDataing();
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(CloudApi.userMessagesList, pagerNumber = 1);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(CloudApi.userMessagesList,pagerNumber += 1);
            }
        });
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
