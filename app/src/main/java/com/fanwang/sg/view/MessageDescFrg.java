package com.fanwang.sg.view;

import android.os.Bundle;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.FMessageDescBinding;
import com.google.gson.Gson;

/**
 * 作者：yc on 2018/10/10.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  消息详情
 */

public class MessageDescFrg extends BaseFragment<BasePresenter, FMessageDescBinding>{

    private DataBean bean;

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        bean = new Gson().fromJson(bundle.getString("bean"), DataBean.class);
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_message_desc;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.message_desc));
        mB.tvTitle.setText(bean.getTitle());
        mB.tvTime.setText(bean.getCreateTime());
        mB.tvContent.setText(bean.getContent());
    }
}
