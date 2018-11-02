package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FPayStateBinding;
import com.fanwang.sg.view.act.ConfirmationOrderAct;
import com.fanwang.sg.view.act.GoodsDetailsAct;

/**
 * 作者：yc on 2018/9/26.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  支付成功还是失败
 */

public class PayStateFrg extends BaseFragment<BasePresenter, FPayStateBinding>{

    private int state;

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        state = bundle.getInt("state");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_pay_state;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.pay_state));
        if (state == 1){

            mB.tvText.setText(getString(R.string.pay_success));
            mB.tvText.setCompoundDrawablesWithIntrinsicBounds( null,
                    ContextCompat.getDrawable(act,R.mipmap.icon_5) , null, null);
        }else {
            mB.tvText.setText(getString(R.string.pay_error));
            mB.tvText.setCompoundDrawablesWithIntrinsicBounds( null,
                    ContextCompat.getDrawable(act,R.mipmap.icon_6) , null, null);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        UIHelper.startOrderAct(0);
        ActivityUtils.finishActivity(ConfirmationOrderAct.class);
        ActivityUtils.finishActivity(GoodsDetailsAct.class);
        return super.onBackPressedSupport();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
