package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.DistributionAdapter;
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
 * 作者：yc on 2018/9/6.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 * 提现记录
 */

public class PresentRecordFrg extends BaseFragment<BaseListPresenter, BRecyclerBinding> implements BaseListContract.View {


    public static final int PRESENT_RECORD = 0;//提现记录
    public static final int DETAILED = 1;//明细
    public static final int DISTRIBUTION = 2;//分销


    private List<DataBean> listBean = new ArrayList<>();
    private DistributionAdapter adapter;
    private int type;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        type = bundle.getInt("type");
        switch (type){
            case PRESENT_RECORD:
                setTitle(getString(R.string.present_record));
                break;
            case DETAILED:
                setTitle(getString(R.string.detailed));
                break;
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.b_recycler;
    }

    @Override
    protected void initView(View view) {
        if (adapter == null) {
            adapter = new DistributionAdapter(act, listBean, type);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2));
        mB.recyclerView.setAdapter(adapter);
        showLoadDataing();
        mB.refreshLayout.startRefresh();
        switch (type){
            case PRESENT_RECORD:
                mB.refreshLayout.setEnableLoadmore(true);
                break;
            case DETAILED:
                mB.refreshLayout.setEnableLoadmore(false);
                break;
        }
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                switch (type){
                    case PRESENT_RECORD:
                        mPresenter.onRequest(CloudApi.userWithDrawRecord);
                        break;
                    case DETAILED:
                        mPresenter.onRequest(CloudApi.userDetailed, pagerNumber = 1);
                        break;
                }
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                switch (type){
                    case PRESENT_RECORD:
//                        mPresenter.onRequest(CloudApi.userWithDrawRecord, pagerNumber += 1);
                        break;
                    case DETAILED:
                        mPresenter.onRequest(CloudApi.userDetailed, pagerNumber += 1);
                        break;
                }
            }
        });
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        mB.refreshLayout.finishRefreshing();
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
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
