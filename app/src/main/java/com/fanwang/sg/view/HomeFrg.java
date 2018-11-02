package com.fanwang.sg.view;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.HomeAdapter;
import com.fanwang.sg.adapter.HomeImgAdapter2;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FHomeBinding;
import com.fanwang.sg.presenter.HomePresenter;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.utils.ZXingUtils;
import com.fanwang.sg.view.impl.HomeContract;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.fanwang.sg.weight.MessageView;
import com.google.zxing.WriterException;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 作者：yc on 2018/8/28.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  首页
 */

public class HomeFrg extends BaseFragment<HomePresenter, FHomeBinding> implements HomeContract.View, BGABanner.Delegate, BGABanner.Adapter<ImageView, DataBean>, View.OnClickListener{

    private MessageView messageView;

    public static HomeFrg newInstance() {
        Bundle args = new Bundle();
        HomeFrg fragment = new HomeFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private List<DataBean> listBean = new ArrayList<>();
    private HomeAdapter adapter;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_home;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView(View view) {
        EditText etSearch = view.findViewById(R.id.et_searh);
        etSearch.setOnClickListener(this);
        etSearch.setFocusable(false);
        etSearch.setLongClickable(false);
        etSearch.setTextIsSelectable(false);
        messageView = view.findViewById(R.id.iv_search_img);
        messageView.initMsgView(this);
        messageView.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new HomeAdapter(act, this, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  4));
        mB.recyclerView.setAdapter(adapter);
        mB.refreshLayout.startRefresh();
        mB.refreshLayout.setEnableLoadmore(false);
        showLoadDataing();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1);
            }
        });

        mB.banner.setDelegate(this);
        setSwipeBackEnable(false);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
        messageView.setMessageNum();
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable DataBean model, int position) {
        String image = model.getImage();//后台傻逼做法
        if (StringUtils.isEmpty(image)){
            image = model.getImg1();
        }
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + image, itemView);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
        DataBean bean = (DataBean) model;
        switch (bean.getType()){//1001商品1002店铺1003外链
            case 1001:
                UIHelper.startGoodsDetailsAct(bean.getIds());
                break;
            case 1002:
                UIHelper.startShopFrg(this, bean.getIds(), 0);
                break;
            case 1003:
                UIHelper.startHtmlAct(null, bean.getName(), bean.getLink(), 0);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_searh:
                UIHelper.startSearchFrg(this);
                break;
        }
    }

    @Override
    public void setData(DataBean bean) {
        listBean.clear();
        List<DataBean> adsList = bean.getAdsList();//轮播
        if (null != adsList && adsList.size() != 0){
            mB.banner.setAutoPlayAble(adsList.size() > 1);
            mB.banner.setData(adsList, new ArrayList<String>());
            mB.banner.setAdapter(HomeFrg.this);

            /*mB.banner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mB.banner.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    ViewGroup.LayoutParams params = mB.banner.getLayoutParams();
                    params.height = mB.banner.getWidth();
                    mB.banner.setLayoutParams(params);
                }
            });*/
        }

        List<DataBean> labelList = bean.getLabelList();//label
        if (null != labelList && labelList.size() != 0){
            DataBean labelListBean = new DataBean();
            labelListBean.setType(1);
            labelListBean.setLabelList(labelList);
            listBean.add(labelListBean);
        }

        DataBean kill_collage = new DataBean(); //拼团  秒杀
        DataBean.Seckill_CollageBean seckillCollageBean = new DataBean.Seckill_CollageBean();
        kill_collage.setType(2);
        DataBean.SecKillBean secKill = bean.getSecKill();//秒杀
        if (null != secKill){
            seckillCollageBean.setSecKill(secKill);
        }
        DataBean.CollageBean collage = bean.getCollage();//拼团
        if (null != collage){
            seckillCollageBean.setCollage(collage);
        }
        kill_collage.setSeckill_collageBean(seckillCollageBean);
        listBean.add(kill_collage);

        DataBean new_product = new DataBean();//新品推荐
        new_product.setType(4);
        DataBean.NewProductBean newProduct = bean.getNewProduct();
        if (newProduct != null){
            new_product.setNewProduct(newProduct);
            listBean.add(new_product);
        }

        DataBean specialSale = new DataBean();//优惠专区
        specialSale.setType(5);
        DataBean.SpecialSaleBean saleBean = bean.getSpecialSale();
        if (saleBean != null){
            specialSale.setId(saleBean.getId());
            specialSale.setSpecialSale(saleBean);
            listBean.add(specialSale);
        }

        /*DataBean manZeng = new DataBean();//满赠
        manZeng.setType(7);
        DataBean.ManZengBean manZengBean = bean.getManZeng();
        if (manZengBean != null){
            manZeng.setManZeng(manZengBean);
            listBean.add(manZeng);
        }

        DataBean manJian = new DataBean();//满减
        manJian.setType(8);
        DataBean.ManJianBean manJianBean = bean.getManJian();
        if (manJianBean != null){
            manJian.setManJian(manJianBean);
            listBean.add(manJian);
        }*/

        adapter.notifyDataSetChanged();
        mB.refreshLayout.finishRefreshing();
        showLoadEmpty();

        DataBean.ManZengBean manZengBean = bean.getManZeng();//满赠
        List<DataBean> manzengActs = manZengBean.getActs();
        if (manzengActs != null && manzengActs.size() != 0){
            mB.bannerManzeng.setVisibility(View.VISIBLE);
            mB.bannerManzeng.setAutoPlayAble(manzengActs.size() > 1);
            mB.bannerManzeng.setData(manzengActs, new ArrayList<String>());
            mB.bannerManzeng.setAdapter(this);
            mB.bannerManzeng.setDelegate(new BGABanner.Delegate() {
                @Override
                public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
                    DataBean bean = (DataBean) model;
                    UIHelper.startDiscountFrg(HomeFrg.this, HomeAdapter.FULL_GIFTS_TYPE, bean.getId());
                }
            });
        }else {
            mB.bannerManzeng.setVisibility(View.GONE);
        }
        final List<DataBean> manZengLists = manZengBean.getProdLists();
        if (manZengLists != null && manZengLists.size() != 0){
            mB.gvManzeng.setVisibility(View.VISIBLE);
            HomeImgAdapter2 adapter = new HomeImgAdapter2(act, manZengLists);
            mB.gvManzeng.setAdapter(adapter);
            mB.gvManzeng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DataBean bean = manZengLists.get(i);
                    UIHelper.startGoodsDetailsAct(bean.getId());
                }
            });
        }else {
            mB.gvManzeng.setVisibility(View.GONE);
        }

        DataBean.ManJianBean manJianBean = bean.getManJian();//满减
        List<DataBean> manJianActs = manJianBean.getActs();
        if (manJianActs != null && manJianActs.size() != 0){
            mB.bannerManjian.setVisibility(View.VISIBLE);
            mB.bannerManjian.setAutoPlayAble(manJianActs.size() > 1);
            mB.bannerManjian.setData(manJianActs, new ArrayList<String>());
            mB.bannerManjian.setAdapter(this);
            mB.bannerManjian.setDelegate(new BGABanner.Delegate() {
                @Override
                public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
                    DataBean bean = (DataBean) model;
                    UIHelper.startDiscountFrg(HomeFrg.this, HomeAdapter.FULL_SUBTRACTION_TYPE, bean.getId());
                }
            });
        }else {
            mB.bannerManjian.setVisibility(View.GONE);
        }
        final List<DataBean> manJianLists = manJianBean.getProdLists();
        if (manJianLists != null && manJianLists.size() != 0){
            mB.gvManjian.setVisibility(View.VISIBLE);
            HomeImgAdapter2 adapter = new HomeImgAdapter2(act, manZengLists);
            mB.gvManjian.setAdapter(adapter);
            mB.gvManjian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DataBean bean = manJianLists.get(i);
                    UIHelper.startGoodsDetailsAct(bean.getId());
                }
            });
        }else {
            mB.gvManjian.setVisibility(View.GONE);
        }

    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }
}
