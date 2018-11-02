package com.fanwang.sg.view;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.AppUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FAboutBinding;

/**
 * 作者：yc on 2018/9/6.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  关于我们
 */

public class AboutUsFrg extends BaseFragment<BasePresenter, FAboutBinding>{

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_about;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.abouit_us));
        mB.refreshLayout.setPureScrollModeOn();
        mB.tvVersion.setText(AppUtils.getAppVersionName());
        mB.lyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startHtmlAct(null, getString(R.string.service_agreement));
            }
        });
    }

}
