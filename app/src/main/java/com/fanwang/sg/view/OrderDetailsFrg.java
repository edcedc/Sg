package com.fanwang.sg.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DividerItemDecoration;
import android.text.Html;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.OrderDetailsCollageAdapter;
import com.fanwang.sg.adapter.OrderDetailsListAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FOrderDetailsBinding;
import com.fanwang.sg.event.PayInEvent;
import com.fanwang.sg.event.RefreshOrderInEvent;
import com.fanwang.sg.presenter.OrderDetailsPresenter;
import com.fanwang.sg.utils.Constants;
import com.fanwang.sg.utils.DateUtils;
import com.fanwang.sg.view.bottomFrg.PayBottomFrg;
import com.fanwang.sg.view.impl.OrderDetailsContract;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.flyco.roundview.RoundViewDelegate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 * 作者：yc on 2018/9/7.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 * 订单详情
 */

public class OrderDetailsFrg extends BaseFragment<OrderDetailsPresenter, FOrderDetailsBinding> implements OrderDetailsContract.View, View.OnClickListener {

    private String id;
    private int isRefund;
    private List<DataBean> listOrderDetails;
    private int cargoType;//0已收货，1未收货
    private String title;
    private PayBottomFrg payBottomFrg;
    private int state;//订单订单

    public static OrderDetailsFrg newInstance() {
        Bundle args = new Bundle();
        OrderDetailsFrg fragment = new OrderDetailsFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private List<DataBean> listBean = new ArrayList<>();
    private OrderDetailsListAdapter adapter;

    private List<DataBean> listCollageBean = new ArrayList<>();
    private OrderDetailsCollageAdapter collageAdapter;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
        title = bundle.getString("title");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_order_details;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView(View view) {
        if (StringUtils.isEmpty(title)) {
            setTitle(getString(R.string.order_details));
            mPresenter.onRequest(id);
        } else {
            setTitle(title);
            mPresenter.orderShowRefund(id);
        }
        mB.tvConfirm.setOnClickListener(this);
        mB.btSubmit1.setOnClickListener(this);
        mB.btSubmit2.setOnClickListener(this);
        if (adapter == null) {
            adapter = new OrderDetailsListAdapter(act, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2, Color.parseColor("#eff0f0")));
        mB.recyclerView.setAdapter(adapter);
        if (collageAdapter == null) {
            collageAdapter = new OrderDetailsCollageAdapter(act, listCollageBean);
        }
        mB.gvCollage.setAdapter(collageAdapter);
        showLoadDataing();
        EventBus.getDefault().register(this);
        if (payBottomFrg == null) {
            payBottomFrg = new PayBottomFrg();
        }
    }

    @Subscribe
    public void onMainThreadPay(PayInEvent event) {
        mPresenter.onRequest(id);
        EventBus.getDefault().post(new RefreshOrderInEvent());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onMainRefreshInEvent(RefreshOrderInEvent event) {
        LogUtils.e("onMainRefreshInEvent");
        isRefund = event.isRefund;
        mPresenter.onRequest(id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                UIHelper.startInvitationCollageFrg(this, bean, 1);
                break;
            case R.id.bt_submit1://申请退款
                getTextData(mB.btSubmit1.getText().toString());
                break;
            case R.id.bt_submit2:
                getTextData(mB.btSubmit2.getText().toString());
                break;
        }
    }

    private void getTextData(String text) {
        if (text.equals(getString(R.string.application_refund))) {
            /*if (refund_num != 0){
                showToast("不允许重复申请退款订单");
                return;
            }*/
            if (cargoType == Constants.return_paragraph) {
                UIHelper.startOrderRefundFrg(this, id, Constants.return_paragraph, listOrderDetails, state);
            } else {
                UIHelper.startAfterSaleGoodsFrg(this, id, listOrderDetails, state);
            }
        } else if (text.equals(getString(R.string.confirm_receipt_goods))) {
            mPresenter.orderConfirmReceipt(id);
        } else if (text.equals(getString(R.string.payment))) {
            if (payBottomFrg != null && !payBottomFrg.isShowing()) {
                payBottomFrg.show(getFragmentManager(), "dialog");
                payBottomFrg.setPayState(true);
            }
            payBottomFrg.setOnClickListener(new PayBottomFrg.OnClickListener() {
                @Override
                public void onClick(int payType) {
                    mPresenter.pay(id, payType);
                }

            });
        } else if (text.equals(getString(R.string.cancel_order))) {

        } else if (text.equals(getString(R.string.clone2))) {
            mPresenter.orderShutDown(id);
        }
    }

    private DataBean bean;

    @Override
    public void setData(DataBean bean) {
        this.bean = bean;
        listBean.clear();
        listCollageBean.clear();
        mB.tvUser.setText("收货人：" +
                bean.getRecName() +
                "     " + bean.getRecMobile());
        mB.tvAddress.setText(bean.getRecAddress());

        StringBuilder sbTime = new StringBuilder();
        sbTime.append("订单编号：" + bean.getOrderNo() + "\n")
                .append("下单时间：" + bean.getCreateTime());

        isRefund = bean.getIsRefund();
        RoundViewDelegate delegate1 = mB.btSubmit1.getDelegate();
        RoundViewDelegate delegate2 = mB.btSubmit2.getDelegate();
        //1001待付款、\r\n1002待发货、\r\n1003待收货、\r\n1004交易完成\r\n1005退款/售后\r\n1006已关闭/10000待成团
        state = bean.getState();
        int refund_num = bean.getRefund_num();
        int isGroupPurchase = bean.getIsGroupPurchase();
        if (isGroupPurchase == 1 && (state == 10000 || state == 1000)) {//待成团
            mB.tvState.setText("待成团");
            mB.tvConfirm.setVisibility(View.VISIBLE);
            int collageNum = bean.getCollageNum();
            List<DataBean> listUser = bean.getListUser();
            int collageNums = collageNum - listUser.size();
            setisGroupPurchase(bean);
            mB.gpCollage.setVisibility(View.VISIBLE);
            String collage_title = "还差" + "<font color='#FE2701'> " + collageNums + " </font>" + "赶快邀请好友来";
            mB.tvCollageTitle.setText(Html.fromHtml(collage_title));
            mB.tvCollageTime.setText("拼团剩余时间：");
            sbTime.append("\n" + "创建时间：" + bean.getCreateTime());
            setDownTime(mB.tvCollageTime2, bean.getCreateTime());
        } else if (state == 1002) {
            mB.tvState.setText("等待商家发货");
            mB.constraintLayout3.setVisibility(View.VISIBLE);
            mB.constraintLayout3.setVisibility(View.VISIBLE);
            mB.btSubmit1.setVisibility(View.VISIBLE);
            mB.btSubmit2.setVisibility(View.GONE);
            mB.btSubmit1.setText(getString(R.string.application_refund));
            cargoType = Constants.return_paragraph;
            sbTime.append("\n" + "创建时间：" + bean.getCreateTime());
            if (isGroupPurchase == 1) {
                sbTime.append("\n" + "成团时间：" + bean.getCreateTime());
            }
        } else if (state == 1001 && bean.getIsGroupPurchase() == 1) {//待成团 待付款
            mB.tvState.setText("等待买家付款");
            mB.gpTitleTime.setVisibility(View.VISIBLE);
            mB.tvText.setText("还剩");
            mB.tvText1.setText("自动关闭订单");
            setDownTime(mB.tvTime, bean.getCreateTime());
            mB.tvConfirm.setVisibility(View.VISIBLE);
            int collageNum = bean.getCollageNum();
            List<DataBean> listUser = bean.getListUser();
            int collageNums = collageNum - listUser.size();
            setisGroupPurchase(bean);
            mB.tvCollageTitle.setText("正在为您保留该团位置，请尽快付款");
            mB.constraintLayout3.setVisibility(View.VISIBLE);
            mB.btSubmit1.setVisibility(View.VISIBLE);
            mB.btSubmit2.setVisibility(View.VISIBLE);
            mB.btSubmit1.setText("付款");
            mB.btSubmit2.setText("取消订单");
        } else if (state == 1003) {
            mB.tvState.setText("商家已发货");
            mB.gpTitleTime.setVisibility(View.VISIBLE);
            mB.tvText.setText("还剩");
            mB.tvText1.setText("自动确认收货");
            setDownTime(mB.tvTime, DateUtils.addTime(bean.getCreateTime(), 7));
            mB.constraintLayout3.setVisibility(View.VISIBLE);
            if (bean.getIsGroupPurchase() == 1) {//  `isGroupPurchase` smallint(1) NOT NULL DEFAULT '0' COMMENT '是否团购单1是0不是',
                setisGroupPurchase(bean);
//                mB.gpPendingDelivery.setVisibility(View.VISIBLE);

            } else {
//                mB.gpGoodsEceived.setVisibility(View.VISIBLE);
            }
            mB.constraintLayout3.setVisibility(View.VISIBLE);
            mB.btSubmit1.setVisibility(View.VISIBLE);
            mB.btSubmit1.setText(getString(R.string.confirm_receipt_goods));
            mB.btSubmit2.setText(getString(R.string.application_refund));
            if (refund_num > 2) {
                mB.btSubmit2.setVisibility(View.GONE);
            } else {
                mB.btSubmit2.setVisibility(View.VISIBLE);
            }

            sbTime.append("\n" + "支付时间：" + bean.getPayfinishedTime()).append("\n" + "发货时间：" + bean.getDeliveryTime());
            if (isGroupPurchase == 1) {
                sbTime.append("\n" + "成团时间：" + bean.getCreateTime());
            }
            cargoType = Constants.refund;
        } else if (state == 1006) {
            mB.tvState.setText("未发货，仅退款");
            mB.gpTitleTime.setVisibility(View.GONE);
            mB.tvState2.setVisibility(View.VISIBLE);
            switch (isRefund) {
                case 1:
                    mB.tvState2.setText("已申请");
                    break;
                case 2:
                    mB.tvState2.setText("申请退款失败");
                    break;
                case 3:
                    mB.tvState2.setText("申请退款成功");
                    break;
            }
            mB.constraintLayout3.setVisibility(View.GONE);
            mB.constraintLayout1.setVisibility(View.GONE);
        } else if (state == 1004) {
            mB.tvState.setText("交易完成");
            mB.gpTitleTime.setVisibility(View.GONE);
            mB.tvState2.setVisibility(View.GONE);
            mB.constraintLayout3.setVisibility(View.GONE);
        } else if (state == 1005) {

        } else if (state == 1001) {
            mB.tvState.setText("待付款");
            mB.gpTitleTime.setVisibility(View.VISIBLE);
            mB.tvText.setText("还剩");
            mB.tvText1.setText("关闭订单");
            setDownTime(mB.tvTime, DateUtils.addDateMinut(bean.getCreateTime(), 10));
            mB.constraintLayout3.setVisibility(View.VISIBLE);
            mB.btSubmit1.setVisibility(View.VISIBLE);
            mB.btSubmit2.setVisibility(View.VISIBLE);
            mB.btSubmit1.setText(getString(R.string.payment));
            mB.btSubmit2.setText(getString(R.string.cancel_order));
        }
        mB.tvDetails.setText(sbTime.toString());

        if (bean.getIntegral() == 1){//积分商品没有退款
            mB.constraintLayout3.setVisibility(View.GONE);
        }else {
            mB.constraintLayout3.setVisibility(View.VISIBLE);
        }

        listOrderDetails = bean.getListOrderDetails();
        if (listOrderDetails != null && listOrderDetails.size() != 0) {
            listBean.addAll(listOrderDetails);
            adapter.setIsGroupPurchase(bean.getIsGroupPurchase());
            adapter.notifyDataSetChanged();
            double allPrice = 0;
            for (DataBean bean1 : listOrderDetails) {
                allPrice += bean1.getNum() * bean1.getPrice();
            }
            String content = "订单总价：" + "<font color='#FE2701'> " + allPrice + " </font>";
            mB.tvPrice.setText(Html.fromHtml(content));
        }
    }

    @Override
    public void showRefund(DataBean bean) {
        listBean.clear();
        int type = bean.getType();//0退货，1退款
        StringBuilder sbTime = new StringBuilder();
        sbTime.append("售后单号：" + bean.getOrderNo() + "\n");
        mB.constraintLayout1.setVisibility(View.GONE);
        mB.tvState2.setVisibility(View.VISIBLE);
        DataBean.OrderSalesReturnBean orderSalesReturn = bean.getOrderSalesReturn();
        if (orderSalesReturn != null) {
            sbTime.append("\n申请时间：" + orderSalesReturn.getCreateTime()).append("\n审核时间：" + orderSalesReturn.getUpdateTime());
            String content = orderSalesReturn.getContent();
            if (StringUtils.isEmpty(content)) {
                mB.lyReason.setVisibility(View.GONE);
            } else {
                mB.lyReason.setVisibility(View.VISIBLE);
                mB.tvReason.setText(content);
            }
            int state = orderSalesReturn.getState();
            if (state == 0) {
                mB.tvState.setText("退款退货");
            } else {
                mB.tvState.setText("已发货，仅退款");
            }
            switch (orderSalesReturn.getAudit()) {
                case 0:
                    mB.tvState2.setText("未审核");
                    mB.constraintLayout3.setVisibility(View.VISIBLE);
                    mB.btSubmit1.setVisibility(View.VISIBLE);
                    mB.btSubmit1.setText(getString(R.string.clone2));
                    break;
                case 1:
                    mB.tvState2.setText("审核成功");
                    mB.constraintLayout3.setVisibility(View.GONE);
                    break;
                case 2:
                    mB.tvState2.setText("审核失败");
                    mB.constraintLayout3.setVisibility(View.GONE);
                    break;
                case 3:
                    mB.tvState2.setText("审核关闭");
                    mB.constraintLayout3.setVisibility(View.GONE);
                    break;
            }
        } else {
            mB.tvState.setText("未发货，仅退款");
            DataBean.OrderRefundBean orderRefund = bean.getOrderRefund();
            if (orderRefund != null) {
                switch (orderRefund.getAudit()) {
                    case 0:
                        mB.tvState2.setText("未审核");
                        mB.constraintLayout3.setVisibility(View.VISIBLE);
                        mB.btSubmit1.setVisibility(View.VISIBLE);
                        mB.btSubmit1.setText(getString(R.string.clone2));
                        break;
                    case 1:
                        mB.tvState2.setText("审核成功");
                        mB.constraintLayout3.setVisibility(View.GONE);
                        break;
                    case 2:
                        mB.tvState2.setText("审核失败");
                        mB.constraintLayout3.setVisibility(View.GONE);
                        break;
                    case 3:
                        mB.tvState2.setText("审核关闭");
                        mB.constraintLayout3.setVisibility(View.GONE);
                        break;
                }
            }
        }
        listOrderDetails = bean.getListOrderDetails();
        if (listOrderDetails != null && listOrderDetails.size() != 0) {
            listBean.addAll(listOrderDetails);
            adapter.setIsGroupPurchase(bean.getIsGroupPurchase());
            adapter.notifyDataSetChanged();
            double allPrice = 0;
            for (DataBean bean1 : listOrderDetails) {
                allPrice += bean1.getNum() * bean1.getPrice();
            }
            String content = "订单总价：" + "<font color='#FE2701'> " + allPrice + " </font>";
            mB.tvPrice.setText(Html.fromHtml(content));
        }
        mB.tvDetails.setText(sbTime.toString());

    }

    @Override
    public void payType(int payType, String data) {
        switch (payType) {
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

    //设置倒计时
    private void setDownTime(CountdownView tvTime, String time) {
        //1001待付款、\r\n1002待发货、\r\n1003待收货、\r\n1004交易完成\r\n1005退款/售后\r\n1006已关闭/10000待成团
        long timeDelta = 0;
//        long timeDelta = TimeUtils.string2Millis(time);
        if (bean.getState() == 1001 && bean.getIsGroupPurchase() == 1) {//是否团购单1是0不是',
            timeDelta = timeDelta + 600000;
        }else {
            timeDelta = DateUtils.getTimeDelta(TimeUtils.getNowString(), DateUtils.addTime(time, 1));
        }
        LogUtils.e(timeDelta);
        if (timeDelta / 1000 > Constants.day_min) {//大于二四小时就显示天
            tvTime.customTimeShow(true, true, true, true, false);
        } else {
            tvTime.customTimeShow(false, true, true, true, false);
        }
        tvTime.start(timeDelta);
        tvTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                mPresenter.onRequest(id);
            }
        });
    }

    //是否拼团单
    private void setisGroupPurchase(DataBean bean) {
        List<DataBean> listUser = bean.getListUser();
        if (listUser != null && listUser.size() != 0) {//是否拼团那种
            int collageNum = bean.getCollageNum();
            int collageNums = collageNum - listUser.size();
            if (collageNums > 3 && listUser.size() < 3) {
                listCollageBean.addAll(listUser);
                for (int i = 0; i < 3 - listUser.size(); i++) {
                    listCollageBean.add(new DataBean());
                }
            } else if (collageNum > 3 && listUser.size() > 3) {
                for (int i = 0; i < 3; i++) {
                    listCollageBean.addAll(listUser);
                }
            } else if (collageNum < 3 && listUser.size() < collageNum) {
                listCollageBean.addAll(listUser);
                for (int i = 0; i < (collageNum - listUser.size()); i++) {
                    listCollageBean.add(new DataBean());
                }
            } else {
                listCollageBean.addAll(listUser);
            }
            collageAdapter.notifyDataSetChanged();
//            mB.gpPendingDelivery.setVisibility(View.VISIBLE);
            mB.gpCollage.setVisibility(View.VISIBLE);
//            mB.gpCollageRule.setVisibility(View.GONE);
        } else {

        }
    }


}
