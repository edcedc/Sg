package com.fanwang.sg.view;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.FEditAddressBinding;
import com.fanwang.sg.presenter.EditAddressPresenter;
import com.fanwang.sg.utils.Constants;
import com.fanwang.sg.utils.PickTaskTool;
import com.fanwang.sg.view.impl.EditAddressContract;
import com.fanwang.sg.weight.AddressPickTask;
import com.google.gson.Gson;

import cn.addapp.pickers.entity.City;
import cn.addapp.pickers.entity.County;
import cn.addapp.pickers.entity.Province;

/**
 * 作者：yc on 2018/9/13.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  新增 编辑地址
 */

public class EditAddressFrg extends BaseFragment<EditAddressPresenter, FEditAddressBinding> implements EditAddressContract.View, View.OnClickListener{

    private int type;
    private DataBean bean;
    private String id;
    private int position;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        type = bundle.getInt("type");
        bean = new Gson().fromJson(bundle.getString("bean"), DataBean.class);
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_edit_address;
    }

    @Override
    protected void initView(View view) {
        if (type == Constants.address_edit){
            setTitle(getString(R.string.edit_address));
            mB.tvConfirm.setText(getText(R.string.complete3));
            mB.gpAddress.setVisibility(View.GONE);
            mB.etName.setText(bean.getName());
            mB.etPhone.setText(bean.getMobile());
            mB.etAddress.setText(bean.getAddress());
            id = bean.getId();
            position = bean.getPosition();
            mB.etDetailedAddress.setText(bean.getDetailedAddress());
        }else {
            setTitle(getString(R.string.save_address));
            mB.tvConfirm.setText(getText(R.string.add_bank3));
            mB.gpAddress.setVisibility(View.VISIBLE);
        }
        mB.etAddress.setOnClickListener(this);
        mB.tvConfirm.setOnClickListener(this);
    }


    private String shen;
    private String shi;
    private String qu;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_address:
                PickTaskTool.onAddressPicker(act, new AddressPickTask.Callback() {

                    @Override
                    public void onAddressInitFailed() {
                        showToast("数据初始化失败");
                    }

                    @Override
                    public void onAddressPicked(Province province, City city, County county) {
                        shen = province.getAreaName();
                        shi = city.getAreaName();
                        qu = county.getAreaName();
                        String addressText = province.getAreaName() + city.getAreaName() + county.getAreaName();
                        mB.etAddress.setText(addressText);
                    }
                });
                break;
            case R.id.tv_confirm:
                mPresenter.confirm(mB.etName.getText().toString().trim(), mB.etPhone.getText().toString().trim(),
                        mB.etAddress.getText().toString().trim(), mB.etDetailedAddress.getText().toString().trim(), type, id, position,
                        shen, shi, qu);
                break;
        }
    }
}
