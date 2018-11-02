package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.MyPagerAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.FSecondKillBinding;
import com.fanwang.sg.presenter.SecondKillPresenter;
import com.fanwang.sg.utils.DateUtils;
import com.fanwang.sg.view.impl.SecondKillContract;
import com.flyco.tablayout.listener.OnTabSelectListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/8/30.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  秒杀抢购
 */

public class SecondKillFrg extends BaseFragment<SecondKillPresenter, FSecondKillBinding> implements SecondKillContract.View{

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_second_kill;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.second_killing_buying));
        showLoadDataing();
        mPresenter.onLabel();
    }

    @Override
    public void onGetLabel(List<DataBean> listBean) {
        ArrayList<Fragment> mFragments = new ArrayList<>();
        String[] strings = new String[listBean.size()];
        for (int i = 0;i < listBean.size();i++){
            DataBean bean = listBean.get(i);
            String[] split = bean.getCreateTime().split(" ");
            String[] split1 = split[1].split(":");
            String time = split1[0] + ":" + split1[1];
            String startTime = bean.getStartTime();
            String dateTime = DateUtils.parseDate(startTime);
            LogUtils.e(dateTime);
            strings[i] = time + "\n" + dateTime;
            List<DataBean> product = bean.getProduct();
            if (product != null && product.size() != 0){
                mFragments.add(new SecondKillChildFrg(bean.getId(), product, bean.getOverdue(), bean.getEndTime(), startTime));
            }else {
                mFragments.add(new SecondKillChildFrg(bean.getId(), null, bean.getOverdue(), bean.getEndTime(), startTime));
            }
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
