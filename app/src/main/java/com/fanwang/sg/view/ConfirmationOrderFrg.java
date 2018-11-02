package com.fanwang.sg.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.ConfirmationOrderAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FConfirmationOrderBinding;
import com.fanwang.sg.event.ChoiceAddressInEvent;
import com.fanwang.sg.event.PayInEvent;
import com.fanwang.sg.presenter.ConfirmationOrderPresenter;
import com.fanwang.sg.view.act.ConfirmationOrderAct;
import com.fanwang.sg.view.impl.ConfirmationOrderContract;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/26.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  确认订单
 */

public class ConfirmationOrderFrg extends BaseFragment<ConfirmationOrderPresenter, FConfirmationOrderBinding> implements ConfirmationOrderContract.View, View.OnClickListener{

    private List<DataBean> listBean;
    private ConfirmationOrderAdapter adapter;
    private String addressId;
    private int type;//商品类型
    private int flag;
    private String id;
    private String orderNo;
    private int isCredited;

    public static ConfirmationOrderFrg newInstance() {
        Bundle args = new Bundle();
        ConfirmationOrderFrg fragment = new ConfirmationOrderFrg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        Type type = new TypeToken<ArrayList<DataBean>>() {}.getType();
        listBean = new Gson().fromJson(bundle.getString("list"), type);
        id = bundle.getString("id");
        this.type = bundle.getInt("type");
        flag = bundle.getInt("flag");
        orderNo = bundle.getString("orderNo");
        isCredited = bundle.getInt("isCredited");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_confirmation_order;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.confirmation_order));
        mB.lyAddress.setOnClickListener(this);
        mB.tvConfirm.setOnClickListener(this);
        if (adapter == null) {
            adapter = new ConfirmationOrderAdapter(act, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2));
        mB.recyclerView.setAdapter(adapter);
        /*double allPrice = 0;
        for (DataBean bean : listBean){
            List<DataBean> prod = bean.getProd();
            for (DataBean bean1 : prod){
                allPrice += bean1.getRealPrice() * bean1.getNum();
            }
        }*/
       // BigDecimal allPrice = new BigDecimal(0);
        //LogUtils.e("user_head-"+allPrice);

        BigDecimal allPrice = new BigDecimal(0);
        for (DataBean bean : listBean) {
            List<DataBean> crarChild = bean.getProd();
            for (DataBean bean1 : crarChild) {
                BigDecimal num = new BigDecimal(bean1.getNum());
                BigDecimal price = bean1.getRealPrice().multiply(num);
                allPrice = allPrice.add(price);
            }
        }

        mB.tvPrice.setText("" + getText(R.string.monetary_symbol) +allPrice);
        mPresenter.onAddressList();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onBackPressedSupport() {
        if (mPresenter.isSubmitOrderState()){
            UIHelper.startOrderAct(0);
        }
        return super.onBackPressedSupport();
    }

    @Override
    public void setData(List<DataBean> list) {
        for (DataBean bean : list){
            if (bean.getIsChoice() == 1){
                setAddress(bean);
                return;
            }
        }
        setAddress(list.get(0));
    }

    @Subscribe
    public void onMainThreadPay(PayInEvent event){
        switch (newType) {//0普通商品1秒杀商品2拼团商品   傻逼后台返回类型跟上传类型不一致 脑残
            case 0:
            case 1:
                goodsType(newCode, newType, newFlag);
                break;
            case 2:
                break;
        }
    }

    private int newType, newFlag, newPayType, newCode;
    @Override
    public void setPayType(int payType, int type, int flag, int code, String data) {
        this.newType = type;
        this.newFlag = flag;
        this.newPayType = payType;
        this.newCode = code;
        switch (payType){//先判断支付类型
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
            case 2:
                goodsType(code, type, flag);
                break;
        }
    }

    //支付成功的状态
    private void goodsType(int code, int type, int flag){
        switch (type) {//商品类型
            case 0:
            case 1:
                UIHelper.startPayStateFrg(this, code);
                break;
            case 2:
                if (flag == 1){//是否参团
                    UIHelper.startOrderAct(0);
                    ActivityUtils.finishActivity(ConfirmationOrderAct.class);
                }else if (flag == 2){
                    UIHelper.startCollageSuccessFrg(this,  id);
                }else {
                    UIHelper.startOrderAct(0);
                    ActivityUtils.finishActivity(ConfirmationOrderAct.class);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ly_address:
                UIHelper.startAddressFrg(this, 1);
                break;
            case R.id.tv_confirm:
                mPresenter.onConfirm(flag, type, listBean, addressId, this, id, orderNo, isCredited);
                break;
        }
    }

    @Subscribe
    public void onMainThreadAddressInEvent(ChoiceAddressInEvent event){
        setAddress(event.bean);
    }

    private void setAddress(DataBean bean){
        if (bean == null){
            mB.tvAddressTips.setVisibility(View.VISIBLE);
            addressId = null;
            mB.tvUser.setText("");
            mB.tvAddress.setText("");
            return;
        }
        mB.tvAddressTips.setVisibility(View.GONE);
        addressId = bean.getId();
        mB.tvUser.setText("收货人：" +
                bean.getName() +
                "     " + bean.getMobile());
        mB.tvAddress.setText(bean.getAddress() + bean.getDetailedAddress());
    }

}
