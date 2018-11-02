package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.databinding.FMainBinding;
import com.fanwang.sg.event.TabSelectedEvent;
import com.fanwang.sg.weight.buttonBar.BottomBar;
import com.fanwang.sg.weight.buttonBar.BottomBarTab;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 作者：yc on 2018/7/25.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class MainFrg extends BaseFragment<BasePresenter, FMainBinding> implements IBaseView{

    public static MainFrg newInstance() {
        Bundle args = new Bundle();
        MainFrg fragment = new MainFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private final int FIRST = 0;
    private final int SECOND = 1;
    private final int THIRD = 2;
    private final int FOUR = 3;

    private SupportFragment[] mFragments = new SupportFragment[5];


    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_main;
    }

    @Override
    protected void initView(View view) {

        mB.bottomBar
                .addItem(new BottomBarTab(_mActivity, R.mipmap.home_icon_home_selected, "首页"))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.home_icon_classification_selected, "分类"))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.home_icon_shopcart_selected, "购物车"))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.home_icon_my_selected, "我的"));
        mB.bottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                if (position == SECOND){
//                    if (!isUserLogin())return;
                }
                showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
//                EventBusActivityScope.getDefault(_mActivity).post(new TabSelectedEvent(position));
            }
        });
        mB.bottomBar.setCurrentItem(0);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onMainInEvent(TabSelectedEvent event){
        mB.bottomBar.setCurrentItem(event.position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SupportFragment firstFragment = findChildFragment(HomeFrg.class);
        if (firstFragment == null) {
            mFragments[FIRST] = HomeFrg.newInstance();
            mFragments[SECOND] = ClassifyFrg.newInstance();
            mFragments[THIRD] = CartFrg.newInstance();
            mFragments[FOUR] = MeFrg.newInstance();

            loadMultipleRootFragment(R.id.fl_container,
                    FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOUR]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findChildFragment(ClassifyFrg.class);
            mFragments[THIRD] = findChildFragment(CartFrg.class);
            mFragments[FOUR] = findChildFragment(MeFrg.class);
        }
        setSwipeBackEnable(false);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
//        setSofia(false);
    }

    /**
     * start other BrotherFragment
     */
    public void startBrotherFragment(SupportFragment targetFragment) {
        start(targetFragment);
    }

}
