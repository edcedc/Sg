package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.HomeAdapter;
import com.fanwang.sg.adapter.NewArrivalsChildAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseListView;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.databinding.FDiscountBinding;
import com.fanwang.sg.presenter.DiscountPresenter;
import com.fanwang.sg.utils.Constants;
import com.fanwang.sg.utils.DateUtils;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.view.impl.DiscountContract;
import com.fanwang.sg.weight.GridDividerItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.iwgang.countdownview.CountdownView;

/**
 * 作者：yc on 2018/8/31.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  优惠特卖专区
 */

public class DiscountFrg extends BaseFragment<DiscountPresenter, FDiscountBinding> implements DiscountContract.View, BGABanner.Delegate, BGABanner.Adapter<ImageView, DataBean>{

    private List<DataBean> listBean = new ArrayList<>();
    private NewArrivalsChildAdapter adapter;
    private CountdownView tvTime;
    private int mType;
    private String id;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        mType = bundle.getInt("type");
        id = bundle.getString("id");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_discount;
    }

    @Override
    protected void initView(View view) {
        switch (mType){
            case HomeAdapter.DISCOUNT_TYPE:
                setTitle(getString(R.string.discount));
                break;
            case HomeAdapter.FULL_GIFTS_TYPE:
                setTitle(getString(R.string.full_zone));
                break;
            case HomeAdapter.FULL_SUBTRACTION_TYPE:
                setTitle(getString(R.string.full_subtraction));
                break;
        }
        tvTime = view.findViewById(R.id.tv_time);
        if (adapter == null){
            adapter = new NewArrivalsChildAdapter(act, listBean);
        }
        mB.recyclerView.setLayoutManager(new GridLayoutManager(act, 2));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());

//        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2, Color.parseColor("#eff0f0")));
        mB.recyclerView.addItemDecoration(new GridDividerItemDecoration(20, ContextCompat.getColor(act,R.color.white_f4f4f4)));
        mB.recyclerView.setAdapter(adapter);
        mB.refreshLayout.startRefresh();
        showLoadDataing();
        mB.refreshLayout.setEnableLoadmore(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1, id);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber += 1, id);
            }
        });
        mB.banner.setDelegate(this);
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setData(Object data) {
        List<DataBean> list = (List<DataBean>) data;
        if (pagerNumber == 1) {
            listBean.clear();
            mB.refreshLayout.finishRefreshing();
        } else {
            mB.refreshLayout.finishLoadmore();
        }
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable DataBean model, int position) {
        itemView.setScaleType(ImageView.ScaleType.FIT_XY);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + model.getImage(), itemView);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {

    }

    @Override
    public void setBannerData(DataBean bean) {
        List<DataBean> imgs = new ArrayList<>();
        DataBean bean1 = new DataBean();
        bean1.setImage(bean.getImg1());
        imgs.add(bean1);
        mB.banner.setAutoPlayAble(imgs.size() > 1);
        mB.banner.setData(imgs, new ArrayList<String>());
        mB.banner.setAdapter(this);
        mB.tvTitle.setText("距离活动结束");
        long timeDelta = DateUtils.getTimeDelta(TimeUtils.getNowString(), bean.getEndTime());
        if (timeDelta / 1000 > Constants.day_min){//大于二四小时就显示天
            mB.tvTime.customTimeShow(true, true, true, true, false);
        }
        mB.tvTime.start(timeDelta);
        mB.tvTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                LogUtils.e("onEnd");
            }
        });
        mB.tvContent.setText(bean.getRemark());
//        mB.tvContent.setText(Html.fromHtml(bean.getRemark()));
    }

}
