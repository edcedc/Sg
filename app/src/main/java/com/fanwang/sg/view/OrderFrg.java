package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.MyPagerAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.databinding.FOrderBinding;
import com.fanwang.sg.weight.TabEntity;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 作者：yc on 2018/9/3.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  我的订单
 */

public class OrderFrg extends BaseFragment<BasePresenter, FOrderBinding>{

    private int frgPosition;

    public static OrderFrg newInstance() {
        Bundle args = new Bundle();
        OrderFrg fragment = new OrderFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private String[] mTitles = {"全部", "待付款", "待成团", "待发货", "待收货"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        frgPosition = bundle.getInt("position");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_order;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.my_order));
        setSwipeBackEnable(false);
        mFragments.add(new OrderChildFrg(-1));
        mFragments.add(new OrderChildFrg(1001));
        mFragments.add(new OrderChildFrg(1000));
        mFragments.add(new OrderChildFrg( 1002));
        mFragments.add(new OrderChildFrg( 1003));
        mB.viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), mFragments));
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i]));
        }
        mB.tablayout.setTabData(mTabEntities);
        mB.tablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mB.viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mB.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mB.tablayout.setCurrentTab(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
//        mB.viewPager.setCurrentItem(4);
        mB.viewPager.setOffscreenPageLimit(4);
        mB.viewPager.setCurrentItem(frgPosition);
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }

}
