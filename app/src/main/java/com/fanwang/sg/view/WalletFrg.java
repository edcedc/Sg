package com.fanwang.sg.view;

import android.os.Bundle;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.MeAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FWalletBinding;
import com.fanwang.sg.presenter.WalletPresenter;
import com.fanwang.sg.view.impl.WalletContract;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/4.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  我的钱包
 */

public class WalletFrg extends BaseFragment<WalletPresenter, FWalletBinding> implements WalletContract.View, MeAdapter.OnClickListener, View.OnClickListener{

    public static WalletFrg newInstance() {
        Bundle args = new Bundle();
        WalletFrg fragment = new WalletFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private List<DataBean> listBean = new ArrayList<>();
    private MeAdapter adapter;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_wallet;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.my_wallet));
        if (adapter == null) {
            adapter = new MeAdapter(act, listBean);
        }
        mPresenter.listview(mB.recyclerView, listBean, adapter);
        adapter.setOnClickListener(this);

        mB.tvForward.setOnClickListener(this);
        mB.tvTitle.setText("账户余额（元）");
        setSwipeBackEnable(false);
        mPresenter.userInto();
    }

    @Override
    public void onClick(View v, int position) {
        switch (position){
            case 0://我的分销
                UIHelper.startDistributionFrg(this, 1);
                break;
            case 1://管理银行卡
                UIHelper.startManagementBankFrg(this);
                break;
            case 2://提现记录
                UIHelper.startPresentRecordFrg(this, PresentRecordFrg.PRESENT_RECORD);
                break;
            case 3://明细
                UIHelper.startPresentRecordFrg(this, PresentRecordFrg.DETAILED);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_forward://提现
                UIHelper.startForwardFrg(this);
                break;
        }
    }

    @Override
    public void onUserInfo(double balance) {
        mB.tvPrice.setText("" + balance);

    }
}
