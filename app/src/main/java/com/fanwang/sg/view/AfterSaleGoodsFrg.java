package com.fanwang.sg.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.widget.CheckBox;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.AddressAdapter;
import com.fanwang.sg.adapter.AfterSaleGoodsAdapter;
import com.fanwang.sg.adapter.CartAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FAfterSaleGoodsBinding;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/10/16.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  选择售后商品
 */

public class AfterSaleGoodsFrg extends BaseFragment<BasePresenter, FAfterSaleGoodsBinding> implements View.OnClickListener{

    private String id;
    private List<DataBean> listOrderDetails = new ArrayList<>();
    private AfterSaleGoodsAdapter adapter;

    private CheckBox cbTotla;
    private int state;

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
        Type type = new TypeToken<ArrayList<DataBean>>() {}.getType();
        listOrderDetails = new Gson().fromJson(bundle.getString("list"), type);
        state = bundle.getInt("state");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_after_sale_goods;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.after_sale_goods));
        cbTotla = view.findViewById(R.id.cb_totla);
        view.findViewById(R.id.tv_settlement).setOnClickListener(this);
        cbTotla.setOnClickListener(this);
        if (adapter == null){
            adapter = new AfterSaleGoodsAdapter(act, listOrderDetails);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2, Color.parseColor("#eff0f0")));
        mB.recyclerView.setAdapter(adapter);
        adapter.setParentCbUpdateListener(new CartAdapter.onParentCbUpdateListener() {
            @Override
            public void onParentCbUpdate(boolean isUpdate) {
                cbTotla.setChecked(isUpdate);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cb_totla:
                for (DataBean bean : listOrderDetails){
                    bean.setSelect(true);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_settlement:
                for (DataBean bean : listOrderDetails){
                     if (!bean.isSelect()){
                         break;
                     }else {
                         UIHelper.startOrderRefundStateFrg(this, id, listOrderDetails, state);
                     }
                }
                break;
        }
    }
}
