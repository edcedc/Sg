package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.ManagementBankAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FManagementBankBinding;
import com.fanwang.sg.event.AddBankInEvent;
import com.fanwang.sg.presenter.ManagementBankPresenter;
import com.fanwang.sg.utils.PopupWindowTool;
import com.fanwang.sg.view.impl.ManagementBankContract;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  管理银行卡
 */

public class ManagementBankFrg extends BaseFragment<ManagementBankPresenter, FManagementBankBinding> implements ManagementBankContract.View, View.OnClickListener{

    private List<DataBean> listBean = new ArrayList<>();
    private ManagementBankAdapter adapter;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_management_bank;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.my_bank));
        mB.tvConfirm.setOnClickListener(this);
        if (adapter == null) {
            adapter = new ManagementBankAdapter(act, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.setBackgroundColor(ContextCompat.getColor(act,R.color.white_f4f4f4));
        mB.recyclerView.setAdapter(adapter);
        showLoadDataing();
        mB.refreshLayout.startRefresh();
        mB.refreshLayout.setEnableLoadmore(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest();
            }

        });
        adapter.setOnClickListener(new ManagementBankAdapter.OnClickListener() {
            @Override
            public void onClick(int position, String id) {
                mPresenter.setDefaultCard(position, id);
            }

            @Override
            public void onDelter(final int position, final String id) {
                PopupWindowTool.showDialog(act, PopupWindowTool.dialog_dalete, new PopupWindowTool.DialogListener() {
                    @Override
                    public void onClick() {
                        mPresenter.delCard(position, id);
                    }
                });
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onThreadMainInEvent(AddBankInEvent event){
        hideLoading();
        DataBean bean = (DataBean) event.object;
        listBean.add(bean);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setData(List<DataBean> list) {
        listBean.clear();
        mB.refreshLayout.finishRefreshing();
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDefaultSuccess(int position, String id) {
        for (DataBean bean : listBean){
            if (id.equals(bean.getId())){
                bean.setDefaulted(1);
            }else {
                bean.setDefaulted(0);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteSuccess(int position, String id) {
        listBean.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_confirm:
                UIHelper.startAddBankFrg(this);
                break;
        }
    }
}
