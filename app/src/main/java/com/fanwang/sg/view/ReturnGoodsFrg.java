package com.fanwang.sg.view;

import android.os.Bundle;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.OrderChildAdapter;
import com.fanwang.sg.adapter.ReturnGoodsAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.databinding.FReturnBinding;
import com.fanwang.sg.presenter.BaseListPresenter;
import com.fanwang.sg.view.impl.BaseListContract;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/6.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 * 退货/退款
 */

public class ReturnGoodsFrg extends BaseFragment<BaseListPresenter, FReturnBinding> implements BaseListContract.View {

    private List<DataBean> listBean = new ArrayList<>();
    private ReturnGoodsAdapter adapter;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_return;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.refund));
        if (adapter == null) {
            adapter = new ReturnGoodsAdapter(act, listBean);
        }
        mB.listView.setAdapter(adapter);
        showLoadDataing();
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(CloudApi.orderPageRefund, pagerNumber = 1);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(CloudApi.orderPageRefund, pagerNumber += 1);
            }
        });
        mB.listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
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
//        adapter.notifyDataSetChanged();
        ((BaseExpandableListAdapter) mB.listView.getExpandableListAdapter()).notifyDataSetChanged();
        int groupCount = mB.listView.getCount();
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            mB.listView.collapseGroup(i);
            mB.listView.expandGroup(i);
        }


        /*mB.listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int ixx) {

            }
        });*/


    }
}
