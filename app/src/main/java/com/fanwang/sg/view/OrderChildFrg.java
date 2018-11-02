package com.fanwang.sg.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.OrderChildAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FOrderChildBinding;
import com.fanwang.sg.event.RefreshOrderInEvent;
import com.fanwang.sg.presenter.OrderChildPresenter;
import com.fanwang.sg.view.bottomFrg.PayBottomFrg;
import com.fanwang.sg.view.impl.OrderChildContract;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/4.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

@SuppressLint("ValidFragment")
public class OrderChildFrg extends BaseFragment<OrderChildPresenter, FOrderChildBinding> implements OrderChildContract.View{

    private int mType;

    public OrderChildFrg(int type) {
        this.mType = type;
    }

    private List<DataBean> listBean = new ArrayList<>();
    private OrderChildAdapter adapter;
    private boolean isRefresh = false;
    private PayBottomFrg payBottomFrg;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_order_child;
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);
        if (adapter == null){
            adapter = new OrderChildAdapter(act, this, listBean, mType);
        }
        mB.listView.setAdapter(adapter);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1, mType);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber += 1, mType);
            }
        });
        mB.listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        EventBus.getDefault().register(this);
        if (payBottomFrg == null) {
            payBottomFrg = new PayBottomFrg();
        }
        adapter.setOnClickListener(new OrderChildAdapter.OnClickListener() {
            @Override
            public void onClick(String text, int position, final DataBean bean) {
                String id = bean.getId();
                if (text.equals(getString(R.string.view_details))){
                    UIHelper.startOrderDetailsAct(id);
                }else if (text.equals(getString(R.string.confirm_receipt_goods))){
                    mPresenter.orderConfirmReceipt(id);
                }else if (text.equals(getString(R.string.to_share))){
                    UIHelper.startInvitationCollageFrg(OrderChildFrg.this, bean, 0);
                }else if (text.equals(getString(R.string.look_logistics))){
                    UIHelper.startLogisticsFrg(OrderChildFrg.this, id, bean.getLogisticsCompany(), bean.getLogisticsNo());
                }else if (text.equals(getString(R.string.payment))){
                    if (payBottomFrg != null && !payBottomFrg.isShowing()) {
                        payBottomFrg.show(getFragmentManager(), "dialog");
                        payBottomFrg.setPayState(true);
                    }
                    payBottomFrg.setOnClickListener(new PayBottomFrg.OnClickListener() {
                        @Override
                        public void onClick(int payType) {
                            if (payType <= 1 && bean.getIntegral() == 1){
                                showToast(act.getString(R.string.integral_balance));
                                return;
                            }
                            mPresenter.pay(bean.getId(), payType);
                        }

                    });
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMainRefreshInEvent(RefreshOrderInEvent event){
        mB.refreshLayout.startRefresh();
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
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
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
        int groupCount = mB.listView.getCount();
        for (int i=0; i<groupCount; i++){
            mB.listView.expandGroup(i);
        };
    }

    @Override
    public void payType(int payType, String data) {
        switch (payType){
            case 0:
                pay(data);
                break;
            case 1:
                try {
                    wxPay(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
