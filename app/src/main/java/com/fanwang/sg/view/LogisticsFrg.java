package com.fanwang.sg.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.LogisticsAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.FLogisticsBinding;
import com.fanwang.sg.presenter.LogisticsPresenter;
import com.fanwang.sg.view.impl.LogisticsContract;
import com.fanwang.sg.weight.LinearDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/10/17.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  查看物流
 */

public class LogisticsFrg extends BaseFragment<LogisticsPresenter, FLogisticsBinding> implements LogisticsContract.View{

    private String id;
    private LogisticsAdapter adapter;
    private List<DataBean> listBean = new ArrayList<>();
    private String logisticsNo;
    private String logisticsCompany;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
        logisticsCompany = bundle.getString("logisticsCompany");
        logisticsNo = bundle.getString("logisticsNo");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_logistics;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.logistics_details));
        if (adapter == null){
            adapter = new LogisticsAdapter(act, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2, Color.parseColor("#eff0f0")));
        mB.recyclerView.setAdapter(adapter);
        mPresenter.onRequest(id);
        mB.tvName.setText("物流公司：" + logisticsCompany);
        mB.tvNumber.setText("物流单号：" + logisticsNo);
    }

    @Override
    public void setData(List<DataBean> list, String ShipperCode, String logisticCode, String image) {
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }
}
