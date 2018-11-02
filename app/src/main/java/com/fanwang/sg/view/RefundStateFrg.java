package com.fanwang.sg.view;

import android.os.Bundle;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FRefundStateBinding;
import com.fanwang.sg.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/10/16.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  选择退货状态
 */

public class RefundStateFrg extends BaseFragment<BasePresenter, FRefundStateBinding> implements View.OnClickListener{

    private String id;
    private List<DataBean> listOrderDetails = new ArrayList<>();
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
        return R.layout.f_refund_state;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.after_sale_type));
        mB.tvReturn.setOnClickListener(this);
        mB.tvReturnGoods.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_return:
                UIHelper.startOrderRefundFrg(this, id, Constants.refund, listOrderDetails, state);
                break;
            case R.id.tv_return_goods:
                UIHelper.startOrderRefundFrg(this, id, Constants.return_goods, listOrderDetails, state);
                break;
        }
    }
}
