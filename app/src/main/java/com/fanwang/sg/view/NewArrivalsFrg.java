package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.MyPagerAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.FNewArrivalsBinding;
import com.fanwang.sg.presenter.NewArrivalsPresenter;
import com.fanwang.sg.view.impl.NewArrivalsContract;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/8/31.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  新品推荐
 */

public class NewArrivalsFrg extends BaseFragment<NewArrivalsPresenter, FNewArrivalsBinding> implements NewArrivalsContract.View{

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_new_arrivals;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.new_arrivals2));
        showLoadDataing();
        mPresenter.onLabel();
    }

    @Override
    public void onGetLabel(List<DataBean> listBean) {
        ArrayList<Fragment> mFragments = new ArrayList<>();
        String[] strings = new String[listBean.size()];
        for (int i = 0;i < listBean.size();i++){
            DataBean bean = listBean.get(i);
            mFragments.add(new NewArrivalsChildFrg(bean.getId()));
            strings[i] = bean.getCat_name();
        }
        mB.viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), mFragments, strings));
        mB.tbLayout.setViewPager(mB.viewPager);
//            mB.viewPager.setCurrentItem(0);
        mB.tbLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mB.viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

}
