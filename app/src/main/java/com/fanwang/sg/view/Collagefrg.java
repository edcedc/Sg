package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.MyPagerAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.FCollageBinding;
import com.fanwang.sg.utils.Constants;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * 作者：yc on 2018/8/30.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class Collagefrg extends BaseFragment<BasePresenter, FCollageBinding> implements IBaseView{

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] strings = {"正在秒拼", "拼团预告"};

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_collage;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.collage2));
        mFragments.add(new CollageChildFrg(Constants.collage_process));
        mFragments.add(new CollageChildFrg(Constants.collage_preview));
        mB.viewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), mFragments, strings));
        mB.tbLayout.setViewPager(mB.viewPager);
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
