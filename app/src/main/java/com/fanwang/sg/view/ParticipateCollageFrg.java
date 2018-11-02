package com.fanwang.sg.view;

import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.CollageOrderAdapter;
import com.fanwang.sg.adapter.OrderDetailsCollageAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.bean.ProductProductDetailBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.databinding.FParticipateCollageBinding;
import com.fanwang.sg.presenter.ParticipateCollagePresenter;
import com.fanwang.sg.utils.Constants;
import com.fanwang.sg.utils.DateUtils;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.view.bottomFrg.SetClassBottomFrg;
import com.fanwang.sg.view.impl.ParticipateCollageContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 * 作者：yc on 2018/9/29.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  参与拼团
 */

public class ParticipateCollageFrg extends BaseFragment<ParticipateCollagePresenter, FParticipateCollageBinding> implements ParticipateCollageContract.View, View.OnClickListener{

    private ProductProductDetailBean bean;

    private List<DataBean> listBean = new ArrayList<>();//记录本页数据做成跟购物车列表一样
    private List<DataBean> listAllSkuBena = new ArrayList<>();//记录sku规格数据
    private int flag = -1;//如果是拼团商品需要传此字段1表示拼团够0表示单独购
    private DataBean submitOrderProdBean;
    private List<DataBean> listCollageBean = new ArrayList<>();
    private OrderDetailsCollageAdapter collageAdapter;
    private int position;
    private List<DataBean> listUser = new ArrayList<>();
    private SetClassBottomFrg setClassBottomFrg;
    private String skuId;
    private String image;
    private String id;
    private int secKillState = -1;
    private String choice;
    private int type;
    private String orderNo;


    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        bean = new Gson().fromJson(bundle.getString("bean"), ProductProductDetailBean.class);
        position = bundle.getInt("position");
        Type type = new TypeToken<ArrayList<DataBean>>() {}.getType();
        listUser = new Gson().fromJson(bundle.getString("list"), type);
        listAllSkuBena = new Gson().fromJson(bundle.getString("listAllSkuBena"), type);
        choice = bundle.getString("choice");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_participate_collage;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.participate_collage));
        mB.tvConfirm.setOnClickListener(this);
        if (collageAdapter == null){
            collageAdapter = new OrderDetailsCollageAdapter(act, listCollageBean);
        }
        mB.gvCollage.setAdapter(collageAdapter);

        DataBean submitOrderBean = new DataBean();//存储要传的数据要确认订单
        submitOrderBean.setId(bean.getId());
        List<DataBean> submitProdBean = new DataBean().getProd();
        submitOrderProdBean = new DataBean();
        submitOrderProdBean.setId(bean.getId());
        submitOrderProdBean.setImage(bean.getImage());
        submitOrderProdBean.setName(bean.getName());
        submitProdBean.add(submitOrderProdBean);
        submitOrderBean.setProd(submitProdBean);
        listBean.add(submitOrderBean);

        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getImage(), mB.ivImg);
        mB.tvPrice.setText(getString(R.string.monetary_symbol) + bean.getRealPrice());
        mB.tvPrice2.setText("单买价" + getString(R.string.monetary_symbol) + bean.getMarketPrice());
        mB.tvName.setText(bean.getName());
        DataBean bean1 = bean.getCollageOrderList().get(position);
        mB.tvNumber.setText("已拼" + bean1.getCurrentCollageNum() + "件");
        orderNo = bean1.getOrderNo();

        image = bean.getImage();
        id = bean.getId();
        type = bean.getType();
        int collageNum = bean.getCollageNum();
        int collageNums = collageNum - listUser.size();
        if (collageNums > 3 && listUser.size() < 3){
            listCollageBean.addAll(listUser);
            for (int i = 0;i < 3 - listUser.size();i++){
                listCollageBean.add(new DataBean());
            }
        }else if (collageNum > 3 && listUser.size() > 3){
            for (int i = 0;i < 3;i++){
                listCollageBean.addAll(listUser);
            }
        }else if (collageNum < 3 && listUser.size() < collageNum){
            listCollageBean.addAll(listUser);
            for (int i = 0;i < (collageNum - listUser.size());i++){
                listCollageBean.add(new DataBean());
            }
        }else {
            listCollageBean.addAll(listUser);
        }
        collageAdapter.notifyDataSetChanged();
//        String collage_title = "还差" + "<font color='#FE2701'> " + collageNums + " </font>" + "赶快邀请好友来";
//        mB.tvCollageTitle.setText(Html.fromHtml(collage_title));
        mB.tvCollageTime.setText("拼团剩余时间：");
//        ProductProductDetailBean.CollageBean collage = bean.getCollage();


        long timeDelta = DateUtils.getTimeDelta(TimeUtils.getNowString(), bean1.getEndTime());
        if (timeDelta / 1000 > Constants.day_min){//大于二四小时就显示天
            mB.tvCollageTime2.customTimeShow(true, true, true, true, false);
        }
        mB.tvCollageTime2.start(timeDelta);
        mB.tvCollageTime2.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                LogUtils.e("onEnd");
            }
        });

    }

    private void showClassBottom(final List<DataBean> listAllSkuBena, String image, String choice, String price, final String id, final int mType){
        if (setClassBottomFrg == null){
            setClassBottomFrg = new SetClassBottomFrg(listAllSkuBena, image, choice, price, id);
        }
        setClassBottomFrg.setOnClickListener(new SetClassBottomFrg.OnClickListener() {
            @Override
            public void colse(int number, String price, String ids, String className) {
                skuId = ids;
                submitOrderProdBean.setNum(number);
                submitOrderProdBean.setRealPrice(new BigDecimal(price));
                submitOrderProdBean.setSpecificationValues(className);
                submitOrderProdBean.setSkuId(ids);
                mPresenter.onSubmitOrder(ParticipateCollageFrg.this, type, id, listBean, secKillState, 2, orderNo);
            }
        });
        if (mType != -1){
            if (setClassBottomFrg != null && !setClassBottomFrg.isShowing()){
                setClassBottomFrg.show(getChildFragmentManager(), "dialog");
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_confirm:
                /*if (StringUtils.isEmpty(skuId)){
//                    showToast(act.getString(R.string.error_stock));
                    showClassBottom(listAllSkuBena, image, choice, mB.tvPrice.getText().toString(), id, 2);
                    return;
                }else {
                    mPresenter.onSubmitOrder(this, type, id, listBean, secKillState, flag);
                }*/
                showClassBottom(listAllSkuBena, image, choice, mB.tvPrice.getText().toString(), id, 2);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (setClassBottomFrg != null && setClassBottomFrg.isShowing()){
            setClassBottomFrg.dismiss();
        }
    }

}
