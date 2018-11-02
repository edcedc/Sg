package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.DistributionAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FDistributtionBinding;
import com.fanwang.sg.presenter.BaseListPresenter;
import com.fanwang.sg.presenter.DistributionPresenter;
import com.fanwang.sg.view.impl.BaseListContract;
import com.fanwang.sg.view.impl.DistributionContract;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  我的分销
 */

public class DistributionFrg extends BaseFragment<DistributionPresenter, FDistributtionBinding> implements DistributionContract.View, View.OnClickListener{


    private List<DataBean> listBean = new ArrayList<>();
    private DistributionAdapter adapter;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_distributtion;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.my_distribution));
        mB.tvPosters.setOnClickListener(this);
        mB.tvRules.setOnClickListener(this);
        if (adapter == null){
            adapter = new DistributionAdapter(act, listBean, PresentRecordFrg.DISTRIBUTION);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  2));
        mB.recyclerView.setAdapter(adapter);
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1);

            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(pagerNumber += 1);
            }
        });
        mPresenter.userInto();
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setData(Object data, String allMoney) {
        List<DataBean> list = (List<DataBean>) data;
        if (pagerNumber == 1) {
            listBean.clear();
            mB.refreshLayout.finishRefreshing();
        } else {
            mB.refreshLayout.finishLoadmore();
        }
        listBean.addAll(list);
        mB.tvPrice.setText(allMoney + "");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onUserInto(String profit) {
        mB.tvPrice.setText(profit);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_posters:
                UIHelper.startPostersFrg(this);
                break;
            case R.id.tv_rules:
                UIHelper.startHtmlAct(null, getString(R.string.distribution_rules));
                break;
        }
    }
}
