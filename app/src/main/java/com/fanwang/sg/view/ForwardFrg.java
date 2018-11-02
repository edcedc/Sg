package com.fanwang.sg.view;

import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.FForwardBinding;
import com.fanwang.sg.presenter.ForwardPresenter;
import com.fanwang.sg.utils.PopupWindowTool;
import com.fanwang.sg.view.impl.ForwardContract;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;
import com.zaaach.toprightmenu.TopRightMenuTool;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  提现
 */

public class ForwardFrg extends BaseFragment<ForwardPresenter, FForwardBinding> implements ForwardContract.View, View.OnClickListener{

    private List<MenuItem> menuItems = new ArrayList<>();
    private String id;
    private String withDrawMoney;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_forward;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.put_forward));
        mPresenter.onRequest();
        mPresenter.onWithDraw();
        mB.lyBank.setOnClickListener(this);
        mB.tvConfirm.setOnClickListener(this);
    }

    @Override
    public void setData(List<MenuItem> list) {
        menuItems.addAll(list);
    }

    @Override
    public void onWithDrawMoneySuccess(DataBean bean) {
        withDrawMoney = bean.getWithDrawMoney();
        String content = "可提现金额：" + "<font color='#FE2701'> " + withDrawMoney + " </font>";
        mB.tvBalance.setText(Html.fromHtml(content));
        mB.tvWithdrawRate.setText("温馨提示：额外扣除银行手续费，费率" +
                bean.getWithdrawRate() +
                "%，最低0.1");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ly_bank:
                if (menuItems.size() == 0)return;
                TopRightMenuTool.TopRightMenu(act, menuItems, mB.lyBank, new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        MenuItem item = menuItems.get(position);
                        id = item.getId();
                        mB.tvBankNumber.setText("提现到：" + item.getText());
                    }
                });
                break;
            case R.id.tv_confirm:
                mPresenter.confirm(mB.etPrice.getText().toString().trim(), id, withDrawMoney);
                break;
        }
    }
}
