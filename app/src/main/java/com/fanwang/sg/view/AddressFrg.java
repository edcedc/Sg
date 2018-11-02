package com.fanwang.sg.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.AddressAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FAddressBinding;
import com.fanwang.sg.event.AddressInEvent;
import com.fanwang.sg.event.ChoiceAddressInEvent;
import com.fanwang.sg.presenter.AddressPresenter;
import com.fanwang.sg.utils.Constants;
import com.fanwang.sg.utils.PopupWindowTool;
import com.fanwang.sg.view.impl.AddressContract;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/13.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  我的地址
 */

public class AddressFrg extends BaseFragment<AddressPresenter, FAddressBinding> implements AddressContract.View, View.OnClickListener{

    private List<DataBean> listBean = new ArrayList<>();
    private AddressAdapter adapter;
    private int type;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        type = bundle.getInt("type");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_address;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.my_address));
        mB.tvConfirm.setOnClickListener(this);
        if (adapter == null){
            adapter = new AddressAdapter(act, this, listBean, type);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 10, Color.parseColor("#eff0f0")));
        mB.recyclerView.setAdapter(adapter);
        showLoadDataing();
        mB.refreshLayout.startRefresh();
        mB.refreshLayout.setEnableLoadmore(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1);
            }
        });
        adapter.setOnClickListener(new AddressAdapter.OnClickListener() {
            @Override
            public void onClick(int position, String id) {
                mPresenter.onDefaultAddress(position, id);
            }

            @Override
            public void onDelter(int position, String id) {
                mPresenter.onDelete(position, id);
            }

            @Override
            public void onEdit(int position, DataBean bean) {
                UIHelper.startEditAddressFrg(AddressFrg.this, Constants.address_edit, bean);
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listBean.size() == 0){
            EventBus.getDefault().post(new ChoiceAddressInEvent(null));
        }
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setData(List<DataBean>  list) {
        listBean.clear();
        mB.refreshLayout.finishRefreshing();
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDefaultAddressSuccess(int position, String id) {
        for (DataBean bean : listBean){
            if (id.equals(bean.getId())){
                bean.setIsChoice(1);
            }else {
                bean.setIsChoice(0);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onThreadMainInEvent(AddressInEvent event){
        DataBean bean = (DataBean) event.object;
        if (event.type == Constants.address_edit){
            listBean.remove(event.position);
            listBean.add(event.position, bean);
            adapter.notifyItemChanged(event.position);
        }else {
            hideLoading();
            listBean.add(0, bean);
            adapter.notifyItemInserted(event.position);
            adapter.notifyItemChanged(event.position);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_confirm:
                UIHelper.startEditAddressFrg(AddressFrg.this, Constants.address_save);
                break;
        }
    }

}
