package com.fanwang.sg.view;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.Group;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.CollageOrderAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.bean.ProductProductDetailBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FGoodsDetailsBinding;
import com.fanwang.sg.event.CustomerServiceInEvent;
import com.fanwang.sg.event.RefreshOrderInEvent;
import com.fanwang.sg.presenter.GoodsDetailsPresenter;
import com.fanwang.sg.rcloud.RCloudTool;
import com.fanwang.sg.utils.Constants;
import com.fanwang.sg.utils.DateUtils;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.utils.HtmlFormat;
import com.fanwang.sg.view.bottomFrg.ParameterBottomFrg;
import com.fanwang.sg.view.bottomFrg.SetClassBottomFrg;
import com.fanwang.sg.view.bottomFrg.ShareBottomFrg;
import com.fanwang.sg.view.bottomFrg.ShareGoodsBottomFrg;
import com.fanwang.sg.view.impl.GoodsDetailsContract;
import com.flyco.roundview.RoundTextView;
import com.flyco.roundview.RoundViewDelegate;
import com.yanzhenjie.sofia.Sofia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.iwgang.countdownview.CountdownView;

/**
 * 作者：yc on 2018/9/11.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 * 订单详情
 */

public class GoodsDetailsFrg extends BaseFragment<GoodsDetailsPresenter, FGoodsDetailsBinding> implements GoodsDetailsContract.View, View.OnClickListener, BGABanner.Delegate, BGABanner.Adapter<ImageView, DataBean> {

    private String id;
    private String image;
    private String businessId;
    private int type;
    private boolean collect;
    private String skuId;//规格里面获取库存接口得到的
    private int goods_number;//商品数量
    private DataBean submitOrderProdBean;
    private String choice;
    private int secKillState = -1;
    private CollageOrderAdapter collageOrderAdapter;
    private TextView tvFrame;
    private ShareBottomFrg shareBottomFrg;
    private ShareGoodsBottomFrg shareGoodsBottomFrg;
    private String startTime;
    private String endTime;
    private int overdue;
    private int isCredited;
    private int collageType;//拼团状态

    public static GoodsDetailsFrg newInstance() {
        Bundle args = new Bundle();
        GoodsDetailsFrg fragment = new GoodsDetailsFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private View topLayout;
    private Toolbar toolbar;
    private View status_view;
    private TextView tvCollection;
    private TextView tvShare;
    private RoundTextView tvAddCart;
    private RoundTextView tvPurchase;
    private Group gpAddCart;
    private RoundTextView tvRobbing;
    private Group gpRobbing;

    private ParameterBottomFrg parameterBottomFrg;
    private SetClassBottomFrg setClassBottomFrg;

    private List<DataBean> listBean = new ArrayList<>();//记录本页数据做成跟购物车列表一样
    private List<DataBean> listAllSkuBena = new ArrayList<>();//记录sku规格数据
    private int flag = -1;//如果是拼团商品需要传此字段1表示拼团够0表示单独购

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
        startTime = bundle.getString("startTime");
        endTime = bundle.getString("endTime");
        overdue = bundle.getInt("overdue");
        collageType = bundle.getInt("collageType");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_goods_details;
    }

    @Override
    protected void initView(View view) {
        setSwipeBackEnable(false);
        topLayout = view.findViewById(R.id.top_layout);
        toolbar = view.findViewById(R.id.toolbar);
        status_view = view.findViewById(R.id.status_view);
        final View top_view = view.findViewById(R.id.top_view);
        top_view.setVisibility(View.GONE);
        toolbar.setTitle("");
        final AppCompatActivity act = (AppCompatActivity) this.act;
        act.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act.onBackPressed();
            }
        });
        mB.tvPromotion.setOnClickListener(this);
        mB.tvChoice.setOnClickListener(this);
        mB.tvService.setOnClickListener(this);
        mB.tvParameter.setOnClickListener(this);
        mB.tvGoodsInto.setOnClickListener(this);
        tvCollection = view.findViewById(R.id.tv_collection);
        tvCollection.setOnClickListener(this);
        tvShare = view.findViewById(R.id.tv_share);
        tvShare.setOnClickListener(this);
        view.findViewById(R.id.tv_customer).setOnClickListener(this);
        tvAddCart = view.findViewById(R.id.tv_add_cart);
        tvAddCart.setOnClickListener(this);
        tvPurchase = view.findViewById(R.id.tv_purchase);
        tvPurchase.setOnClickListener(this);
        gpAddCart = view.findViewById(R.id.gp_add_cart);
        tvRobbing = view.findViewById(R.id.tv_robbing);
        tvRobbing.setOnClickListener(this);
        gpRobbing = view.findViewById(R.id.gp_robbing);
        tvFrame = view.findViewById(R.id.tv_frame);
        mB.banner.setDelegate(this);
        mPresenter.ajaxDetail(id);
        mB.scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int headerHeight = mB.banner.getHeight() * 2;
                int scrollDistance = Math.min(scrollY, headerHeight);
                int statusAlpha = (int) ((float) scrollDistance / (float) headerHeight * 255F);
                setAnyBarAlpha(statusAlpha);
                top_view.setVisibility(statusAlpha >= 130 ? View.VISIBLE : View.GONE);
                if (statusAlpha >= 50) {
                    toolbar.setTitle(getString(R.string.goods_details));
                } else {
                    toolbar.setTitle("");
                }
            }
        });
        setAnyBarAlpha(0);
        if (shareBottomFrg == null) {
            shareBottomFrg = new ShareBottomFrg();
        }
        if (shareGoodsBottomFrg == null) {
            shareGoodsBottomFrg = new ShareGoodsBottomFrg();
        }
    }

    private void setAnyBarAlpha(int alpha) {
        topLayout.getBackground().mutate().setAlpha(alpha);
        toolbar.getBackground().mutate().setAlpha(alpha);
        status_view.getBackground().mutate().setAlpha(alpha);
        Sofia.with(act)
                .statusBarBackgroundAlpha(alpha);
    }

    @Override
    public void onClick(View view) {
        if (!User.getInstance().isLogin()) {
            UIHelper.startLoginAct();
            return;
        }
        switch (view.getId()) {
            case R.id.tv_customer://客服
                EventBus.getDefault().post(new CustomerServiceInEvent());
                break;
            case R.id.tv_collection://收藏
//                UIHelper.startRongcloudListAct();
                mPresenter.onCollection(collect, id);
                break;
            case R.id.tv_promotion://促销
                break;
            case R.id.tv_choice://选择
                showClassBottom(listBean, image, choice, mB.tvPrice.getText().toString(), id, 0);
                break;
            case R.id.tv_service://服务
                break;
            case R.id.tv_parameter://促销
                if (parameterBottomFrg != null && !parameterBottomFrg.isShowing()) {
                    parameterBottomFrg.show(getChildFragmentManager(), "dialog");
                }
                break;
            case R.id.tv_goods_into://进入商家店铺
                UIHelper.startShopFrg(this, businessId, 1);
                break;
            case R.id.tv_add_cart://单独购买 加入购物车
                if (StringUtils.isEmpty(skuId)) {
//                    showToast(act.getString(R.string.error_stock));
                    showClassBottom(listBean, image, choice, mB.tvPrice.getText().toString(), id, 1);
                    return;
                } else {
                    mPresenter.onAddCart(type, id, skuId, goods_number, GoodsDetailsFrg.this, listBean, secKillState, goods_number, isCredited);
                }
                break;
            case R.id.tv_purchase://立即购买
                if (StringUtils.isEmpty(skuId)) {
//                    showToast(act.getString(R.string.error_stock));
                    showClassBottom(listBean, image, choice, mB.tvPrice.getText().toString(), id, 2);
                    return;
                } else {
                    mPresenter.onSubmitOrder(this, type, id, listBean, secKillState, flag, isCredited);
                }
                break;
            case R.id.tv_robbing://马上抢之类
                if (StringUtils.isEmpty(skuId)) {
//                    showToast(act.getString(R.string.error_stock));
                    showClassBottom(listBean, image, choice, mB.tvPrice.getText().toString(), id, 3);
                    return;
                } else {
                    mPresenter.onSubmitOrder(this, type, id, listBean, secKillState, flag, isCredited);
                }
                break;
            case R.id.tv_share:
                if (shareGoodsBottomFrg != null && !shareGoodsBottomFrg.isShowing()) {
                    shareGoodsBottomFrg.show(getChildFragmentManager(), "dialog");
                }
                shareGoodsBottomFrg.setOnClickListener(new ShareGoodsBottomFrg.OnClickListener() {
                    @Override
                    public void click(Bitmap imgFile) {
                        if (shareBottomFrg != null && !shareBottomFrg.isShowing()) {
//                            shareBottomFrg.setViewLayout(mB.layout);
                            shareBottomFrg.setImgBitmap(imgFile);
                            shareBottomFrg.show(getChildFragmentManager(), "dialog");
                        }
                    }
                });
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void setData(ProductProductDetailBean bean) {
        shareGoodsBottomFrg.setDate(bean);
        listBean.clear();
        DataBean submitOrderBean = new DataBean();//存储要传的数据要确认订单
        submitOrderBean.setId(id);
        List<DataBean> submitProdBean = new DataBean().getProd();
        submitOrderProdBean = new DataBean();
        submitOrderProdBean.setId(id);
        submitOrderProdBean.setImage(bean.getImage());
        submitOrderProdBean.setName(bean.getName());
        submitProdBean.add(submitOrderProdBean);
        submitOrderBean.setProd(submitProdBean);
        listBean.add(submitOrderBean);

        List<DataBean> prodImageList = bean.getProdImageList();
        if (prodImageList != null && prodImageList.size() != 0) {
            mB.banner.setAutoPlayAble(prodImageList.size() > 1);
            mB.banner.setData(prodImageList, new ArrayList<String>());
            mB.banner.setAdapter(GoodsDetailsFrg.this);
            mB.banner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mB.banner.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    ViewGroup.LayoutParams params = mB.banner.getLayoutParams();
                    params.height = mB.banner.getWidth();
                    mB.banner.setLayoutParams(params);
                }
            });
        }
        String zhekou = bean.getZhekou();
        boolean manjian = bean.isManjian();
        boolean manzeng = bean.isManzeng();
        int specialSale = bean.getSpecialSale();
        isCredited = bean.getIsCredited();
        mB.tvCredited.setVisibility(isCredited == 0 ? View.GONE : View.VISIBLE);//是否积分商品
        type = bean.getType();
        if (type == 1001 && isCredited == 0) {//普通商品
            gpAddCart.setVisibility(View.VISIBLE);
            mB.gpCollageProcess.setVisibility(View.GONE);
            mB.linearlayout.setBackgroundColor(ContextCompat.getColor(act, R.color.white));
            mB.tvPrice.setTextColor(ContextCompat.getColor(act, R.color.reb_FE2701));
            if (!StringUtils.isEmpty(zhekou)) {
                mB.tvDiscount.setText(zhekou + "折");
                mB.tvDiscount.setVisibility(View.VISIBLE);
            }
            if (manjian) {
                mB.tvDiscountState.setText("满减");
                mB.tvDiscountState.setVisibility(View.VISIBLE);
            } else if (manzeng) {
                mB.tvDiscountState.setText("满增");
                mB.tvDiscountState.setVisibility(View.VISIBLE);
            }
            mB.tvSpecial.setVisibility(specialSale == 1 ? View.VISIBLE : View.GONE);
            mB.linearlayout1.setVisibility(View.VISIBLE);
        } else if (type == 1002 && isCredited == 0) {//抢购商品  秒杀一样
            mB.gpCollageProcess.setVisibility(View.GONE);
            mB.linearlayout.setBackgroundColor(ContextCompat.getColor(act, R.color.reb_FE2701));
            mB.tvUserNumber.setVisibility(View.VISIBLE);
            mB.tvUserNumber.setText("秒杀抢购");
            gpRobbing.setVisibility(View.VISIBLE);
            final RoundViewDelegate delegate = tvRobbing.getDelegate();
            ProductProductDetailBean.SecKillBean secKill = bean.getSecKill();
            if (secKill != null) {
                secKillState = secKill.getState();
//                    long startTime = TimeUtils.string2Millis(secKill.getStartTime());
//                    long endTime = TimeUtils.string2Millis(secKill.getEndTime());
//                    long nowMills = TimeUtils.getNowMills();//当前时间
//                    LogUtils.e(startTime, endTime, nowMills);
                if (overdue == 2) {//0进行中 1已过期 2未开启
                    mB.tvTimeTitle.setText("距离开始还有：");
                    mB.tvTime.setVisibility(View.VISIBLE);
                    tvRobbing.setText("即将开抢");
                    tvRobbing.setTextColor(act.getColor(R.color.white));
                    delegate.setBackgroundColor(act.getColor(R.color.reb_FE2701));
                    long timeDelta = DateUtils.getTimeDelta(TimeUtils.getNowString(), startTime);
                    if (timeDelta / 1000 > Constants.day_min) {//大于二四小时就显示天
                        mB.tvTime.customTimeShow(true, true, true, true, false);
                    }
                    mB.tvTime.start(timeDelta);
                    mB.tvTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                        @Override
                        public void onEnd(CountdownView cv) {
                            mPresenter.ajaxDetail(id);
//                                LogUtils.e("onEnd");
//                                mB.tvTimeTitle.setText("活动已结束");
//                                mB.tvTime.setVisibility(View.GONE);
//                                tvRobbing.setText("活动已结束");
//                                tvRobbing.setEnabled(false);
//                                tvRobbing.setTextColor(ContextCompat.getColor(act, R.color.black_3E3A39));
//                                delegate.setBackgroundColor(ContextCompat.getColor(act, R.color.black_E5E5E5));
                        }
                    });
                } else if (overdue == 0) {
                    mB.tvTimeTitle.setText("距离结束还有：");
                    mB.tvTime.setVisibility(View.VISIBLE);
                    tvRobbing.setText("马上抢");
                    tvRobbing.setTextColor(act.getColor(R.color.white));
                    delegate.setBackgroundColor(act.getColor(R.color.reb_FE2701));
                    tvRobbing.setEnabled(true);
                    long timeDelta = DateUtils.getTimeDelta(TimeUtils.getNowString(), endTime);
                    if (timeDelta / 1000 > Constants.day_min) {//大于二四小时就显示天
                        mB.tvTime.customTimeShow(true, true, true, true, false);
                    }
                    mB.tvTime.start(timeDelta);
                    mB.tvTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                        @Override
                        public void onEnd(CountdownView cv) {
                            LogUtils.e("onEnd");
                            mB.tvTimeTitle.setText("活动已结束");
                            mB.tvTime.setVisibility(View.GONE);
                            tvRobbing.setText("活动已结束");
                            tvRobbing.setEnabled(false);
                            tvRobbing.setTextColor(ContextCompat.getColor(act, R.color.black_3E3A39));
                            delegate.setBackgroundColor(ContextCompat.getColor(act, R.color.black_E5E5E5));
                        }
                    });
                } else if (overdue == 1) {
                    mB.tvTimeTitle.setText("活动已结束");
                    mB.tvTime.setVisibility(View.GONE);
                    tvRobbing.setText("活动已结束");
                    tvRobbing.setEnabled(false);
                    tvRobbing.setTextColor(act.getColor(R.color.black_3E3A39));
                    delegate.setBackgroundColor(act.getColor(R.color.black_E5E5E5));
                } else {
                    LogUtils.e("哪里出问题了");
                }
            }
        } else if (type == 1003 && isCredited == 0) {//拼团商品
            mB.linearlayout.setBackgroundColor(ContextCompat.getColor(act, R.color.reb_FF4E2F));
            mB.tvUserNumber.setVisibility(View.VISIBLE);
            mB.tvDiscount.setVisibility(View.GONE);
            mB.tvDiscountState.setVisibility(View.GONE);
            mB.tvSpecial.setVisibility(View.GONE);
            mB.tvUserNumber.setText(bean.getCollageNum() + "人团");
            mB.tvChoice.setVisibility(View.GONE);
            ProductProductDetailBean.CollageBean collage = bean.getCollage();
            if (collage != null) {
                switch (collageType) {
                    case Constants.collage_preview:
                        mB.tvTimeTitle.setText("距离拼团开始还有：");
                        gpRobbing.setVisibility(View.VISIBLE);
                        gpAddCart.setVisibility(View.GONE);
                        tvRobbing.setText("拼团未开始");
                        tvRobbing.setEnabled(false);
                        break;
                    case Constants.collage_process:
                        mB.tvTimeTitle.setText("距离拼团结束还有：");
                        gpAddCart.setVisibility(View.VISIBLE);
                        gpRobbing.setVisibility(View.GONE);
                        tvPurchase.setText("发起拼团" + getString(R.string.monetary_symbol) + bean.getRealPrice());
                        tvAddCart.setText("单独购买" + getString(R.string.monetary_symbol) + bean.getMarketPrice());
                        break;
                }
                mB.tvTime.setVisibility(View.VISIBLE);
                long timeDelta = DateUtils.getTimeDelta(TimeUtils.getNowString(), collage.getEndTime());
                if (timeDelta / 1000 > Constants.day_min) {//大于二四小时就显示天
                    mB.tvTime.customTimeShow(true, true, true, true, false);
                }
                mB.tvTime.start(timeDelta);
                mB.tvTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(CountdownView cv) {
                        LogUtils.e("onEnd");
                    }
                });
            }
            mB.gpCollageProcess.setVisibility(View.VISIBLE);
            List<DataBean> collageOrderList = bean.getCollageOrderList();//判断当前有无参团人数
            if (collageOrderList != null && collageOrderList.size() != 0) {
                mB.gpBeingCollage.setVisibility(View.VISIBLE);
                collageOrderAdapter = new CollageOrderAdapter(act, this, collageOrderList);
                mB.lvCollage.setAdapter(collageOrderAdapter);
                flag = 1;
                //tvPurchase.setText("参与拼团" + getString(R.string.monetary_symbol) + bean.getRealPrice());
                mB.view4.setVisibility(View.VISIBLE);
                collageOrderAdapter.setCollageNum(bean.getCollageNum());
                collageOrderAdapter.setSubmitOrderBean(bean);
                collageOrderAdapter.notifyDataSetChanged();
            }
            flag = 1;
        } else if (isCredited == 1) {
            gpAddCart.setVisibility(View.GONE);
            gpRobbing.setVisibility(View.VISIBLE);
            mB.tvPrice.setTextColor(act.getColor(R.color.reb_FE2701));
            tvRobbing.setText("立即抢购");
        }

        mB.tvPrice.setText(getString(R.string.monetary_symbol) + bean.getRealPrice());
        mB.tvPrice2.setText(getString(R.string.monetary_symbol) + bean.getMarketPrice());
        mB.tvTitle.setText(bean.getName());
        String youHui = bean.getYouHui();
        if (!StringUtils.isEmpty(youHui)) {//是否促销
            mB.tvPromotion.setVisibility(View.VISIBLE);
            mB.tvPromotion.setText(Html.fromHtml("促销" + "<font color='#3E3A39'> " + youHui + " </font>"));
        } else {
            mB.tvPromotion.setVisibility(View.GONE);
        }

        String service = bean.getService();//服务
        if (!StringUtils.isEmpty(service)) {
            mB.tvService.setVisibility(View.VISIBLE);
            mB.tvService.setText(Html.fromHtml("服务" + "<font color='#3E3A39'> " + service + " </font>"));
        } else {
            mB.tvService.setVisibility(View.GONE);
        }

        String parameterValues = bean.getParameterValues();//规格
        if (!StringUtils.isEmpty(parameterValues)) {
            try {
                JSONArray array = new JSONArray(parameterValues);
                if (array != null && array.length() != 0) {
                    mB.tvParameter.setVisibility(View.VISIBLE);
                    StringBuilder sb = new StringBuilder();
                    List<DataBean> listParameter = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.optJSONObject(i);
                        String name = jsonObject.optString("name");
                        String value = jsonObject.optString("value");
                        sb.append(name).append("、");
                        DataBean bean1 = new DataBean();
                        bean1.setName(name);
                        bean1.setContent(value);
                        listParameter.add(bean1);
                    }
                    mB.tvParameter.setText(Html.fromHtml("参数" + "<font color='#3E3A39'> " + sb.substring(0, sb.length() - 1) + " </font>"));
                    if (parameterBottomFrg == null) {
                        parameterBottomFrg = new ParameterBottomFrg(listParameter);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            mB.tvParameter.setVisibility(View.GONE);
        }
        collect = bean.isCollect();
        mPresenter.isCollect(collect, tvCollection);

        if (bean.getIsMarketable() == 0) {//是否下架 1是 0不是
            hideLoading();
            mB.tvChoice.setVisibility(View.GONE);
            setMarketable("该商品已下架");
        } else {
            mB.tvChoice.setVisibility(View.VISIBLE);
            image = bean.getImage();
            mPresenter.allSku(id);
        }

        ProductProductDetailBean.BusinessBean business = bean.getBusiness();
        if (business != null) {
            submitOrderBean.setName(business.getName());
            GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + business.getLogo(), mB.ivGoodsHead);
            mB.tvGoodsTitle.setText(business.getName());
            mB.tvGoodsNumber.setText("数量：" + business.getProdNum());
            businessId = business.getId();
        }

        String specialSales = bean.getIntroduction();
        if (!StringUtils.isEmpty(specialSales)) {
            mB.webView.loadDataWithBaseURL(null, HtmlFormat.getNewContent(specialSales), "text/html", "utf-8", null);
        }
        if (bean.getSkustock() == 0) {
            mB.tvTimeTitle.setText("活动已结束");
            mB.tvTime.setVisibility(View.GONE);
            tvRobbing.setText("已抢光");
            tvRobbing.setEnabled(false);
            tvRobbing.setTextColor(act.getColor(R.color.black_3E3A39));
            RoundViewDelegate delegate = tvRobbing.getDelegate();
            delegate.setBackgroundColor(act.getColor(R.color.black_E5E5E5));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAllSku(List<DataBean> listBean, String substring, int allStock) {
        this.listAllSkuBena = listBean;
        /*switch (type){
            case 1002:
                if (allStock == 0){
                    mB.tvTimeTitle.setText("活动已结束");
                    mB.tvTime.setVisibility(View.GONE);
                    tvRobbing.setText("已抢光");
                    tvRobbing.setEnabled(false);
                    tvRobbing.setTextColor(ContextCompat.getColor(act,R.color.black_3E3A39));
                    delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.black_E5E5E5));
                }
                break;
        }*/
        choice = substring + "分类";
        showClassBottom(listBean, image, choice, mB.tvPrice.getText().toString(), id, -1);
        mB.tvChoice.setText(Html.fromHtml("选择" + "<font color='#3E3A39'> " + choice + " </font>"));

        if (collageOrderAdapter != null) {
            collageOrderAdapter.setChoice(choice);
            collageOrderAdapter.setListAllSkuBena(listBean);
        }
//        collageOrderAdapter.notifyDataSetChanged();
    }

    private void showClassBottom(final List<DataBean> listBean, String image, String choice, String price, final String id, final int mType) {
        if (setClassBottomFrg == null) {
            setClassBottomFrg = new SetClassBottomFrg(listBean, image, choice, price, id);
        }
        setClassBottomFrg.setOnClickListener(new SetClassBottomFrg.OnClickListener() {
            @Override
            public void colse(int number, String price, String ids, String className) {
                skuId = ids;
                goods_number = number;
                submitOrderProdBean.setNum(goods_number);
                submitOrderProdBean.setRealPrice(new BigDecimal(price));
                submitOrderProdBean.setSpecificationValues(className);
                submitOrderProdBean.setSkuId(ids);
                mB.tvChoice.setText(Html.fromHtml("选择" + "<font color='#3E3A39'> " + className + " </font>"));
                if (mType == 1) {
                    mPresenter.onAddCart(type, id, skuId, goods_number, GoodsDetailsFrg.this, listBean, secKillState, flag, isCredited);
                } else if (mType == 2) {
                    mPresenter.onSubmitOrder(GoodsDetailsFrg.this, type, id, listBean, secKillState, flag, isCredited);
                }else if (mType == 3){
                    mPresenter.onSubmitOrder(GoodsDetailsFrg.this, type, id, listBean, secKillState, flag, isCredited);
                }
            }
        });
        if (mType != -1) {
            if (setClassBottomFrg != null && !setClassBottomFrg.isShowing()) {
                setClassBottomFrg.show(getChildFragmentManager(), "dialog");
            }
        }
    }

    @Override
    public void onCollectionSuccess(boolean collect) {
        this.collect = collect;
        mPresenter.isCollect(collect, tvCollection);
    }


    @Override
    public void onDestroy() {
        mB.webView.removeAllViews();
        mB.webView.destroy();
        super.onDestroy();
        if (setClassBottomFrg != null && setClassBottomFrg.isShowing()) {
            setClassBottomFrg.dismiss();
        }
        if (parameterBottomFrg != null && parameterBottomFrg.isShowing()) {
            parameterBottomFrg.dismiss();
        }
        if (shareGoodsBottomFrg != null && shareGoodsBottomFrg.isShowing()) {
            shareGoodsBottomFrg.dismiss();
        }
        if (shareBottomFrg != null && shareBottomFrg.isShowing()) {
            shareBottomFrg.dismiss();
        }
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable DataBean model, int position) {
        itemView.setScaleType(ImageView.ScaleType.FIT_XY);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + model.getImageUrl(), itemView);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {

    }

    //是否上架
    private void setMarketable(String content) {
        tvFrame.setText(content);
        tvFrame.setVisibility(View.VISIBLE);
        tvRobbing.setEnabled(false);
        tvShare.setEnabled(false);
        tvAddCart.setEnabled(false);
        tvPurchase.setEnabled(false);
    }

}
