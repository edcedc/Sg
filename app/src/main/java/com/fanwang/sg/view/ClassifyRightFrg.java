package com.fanwang.sg.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.ClassRightAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseListView;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.BNotTitleRecyclerBinding;
import com.fanwang.sg.presenter.ClassifyRightPresenter;
import com.fanwang.sg.view.impl.ClassifyRightContract;
import com.fanwang.sg.weight.GridDividerItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/8/29.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  分类右边
 */

@SuppressLint("ValidFragment")
public class ClassifyRightFrg extends BaseFragment<ClassifyRightPresenter, BNotTitleRecyclerBinding> implements ClassifyRightContract.View{

    private List<DataBean> listBean = new ArrayList<>();
    private ClassRightAdapter rightAdapter;

    private int mPosition;
    private String id;
    private boolean isRefresh = false;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        LogUtils.e(mPosition, id);
        if (!isRefresh){
            isRefresh = true;
            mB.refreshLayout.startRefresh();
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.b_not_title_recycler;
    }

    @Override
    protected void initView(View view) {

        mB.refreshLayout.setBackgroundColor(ContextCompat.getColor(act,R.color.white));
        if (rightAdapter == null) {
            rightAdapter = new ClassRightAdapter(act, this, listBean);
        }
        mB.recyclerView.setLayoutManager(new GridLayoutManager(act, 3));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());

        mB.recyclerView.addItemDecoration(new GridDividerItemDecoration(30, ContextCompat.getColor(act,R.color.white)));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 30;
        params.leftMargin = 30;
        params.rightMargin = 30;
        mB.recyclerView.setLayoutParams(params);
        mB.recyclerView.setAdapter(rightAdapter);
        mB.refreshLayout.setEnableLoadmore(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(id);
            }
        });
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
        rightAdapter.notifyDataSetChanged();
    }
}
