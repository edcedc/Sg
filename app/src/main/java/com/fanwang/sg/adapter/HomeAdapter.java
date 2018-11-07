package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BaseRecyclerviewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.event.TabSelectedEvent;
import com.fanwang.sg.utils.Constants;
import com.fanwang.sg.utils.DateUtils;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.weight.WithScrollGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.iwgang.countdownview.CountdownView;

/**
 * 作者：yc on 2018/8/28.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class HomeAdapter extends BaseRecyclerviewAdapter<DataBean> implements BGABanner.Adapter<ImageView, DataBean>, BGABanner.Delegate{

    private final int LABEL_TYPE = 1;//标签
    private final int SECOND_KILL_TYPE = 2;//秒杀
    private final int COLLAGE_TYPE = 3;//拼团
    public static final int NEW_PRODUCT_TYPE = 4;//新品推荐
    public static final int DISCOUNT_TYPE = 5;//优惠特卖专区
    public static final int FULL_GIFTS_TYPE = 7;//满增
    public static final int FULL_SUBTRACTION_TYPE = 8;//满减
    private final int IMAGE_TYPE = 6;//图片一大栏

    public HomeAdapter(Context act, List listBean) {
        super(act, listBean);
    }
    public HomeAdapter(Context act, BaseFragment root, List listBean) {
        super(act, root, listBean);
    }

    @Override
    public int getItemViewType(int position) {
        DataBean bean = listBean.get(position);
        switch (bean.getType()){
            case 1:
                return LABEL_TYPE;
            case 2:
                return SECOND_KILL_TYPE;
            case 4:
                return NEW_PRODUCT_TYPE;
            case 5:
                return DISCOUNT_TYPE;
            case 7:
                return FULL_GIFTS_TYPE;
            case 8:
                return FULL_SUBTRACTION_TYPE;
            default:
                return -1;

        }
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        final DataBean bean = listBean.get(position);
        if (holder instanceof LabelViewHolder){
            LabelViewHolder viewHolder = (LabelViewHolder) holder;
            final List<DataBean> labelList = bean.getLabelList();
            DataBean bean1 = new DataBean();
//            bean1.setName("全部");
//            bean1.setImg(R.mipmap.home_icon_more);
            bean1.setName("更多");
            bean1.setImg(R.mipmap.home_icon_more);
            labelList.add(bean1);
            HomeLabelAdapter adapter = new HomeLabelAdapter(act, labelList);
            viewHolder.gridView.setAdapter(adapter);
            viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    DataBean bean2 = labelList.get(i);
                    String name = bean2.getName();
                    int type = bean2.getType();
                    int atype = bean2.getAtype();
                    String url = bean2.getUrl();
                    if (name.equals("全部")){
                        EventBus.getDefault().post(new TabSelectedEvent(1));
                    }else {
                        UIHelper.startClassListFrg(root, bean2.getName(), bean2.getActivityId(), 0);
                    }
                }
            });
        }else if (holder instanceof Second_KillViewHolder){//秒杀 拼团
            Second_KillViewHolder viewHoler = (Second_KillViewHolder) holder;
            DataBean.Seckill_CollageBean seckill_collageBean = bean.getSeckill_collageBean();
            //秒杀
            DataBean.SecKillBean secKill = seckill_collageBean.getSecKill();
            if (secKill != null){
                long startTime = DateUtils.getSecondsFromDate(TimeUtils.getNowString());//开始时间
                long endTime = DateUtils.getSecondsFromDate(secKill.getEndTime());//结束时间秒数
                long timeDelta = DateUtils.getTimeDelta(TimeUtils.getNowString(), secKill.getEndTime());
//                if (timeDelta / 1000 > Constants.day_min){//大于二四小时就显示天
//                    viewHoler.tvTime.customTimeShow(true, true, true, true, false);
//                }
                viewHoler.tvTime.start(timeDelta);
//                viewHoler.tvTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
//                    @Override
//                    public void onEnd(CountdownView cv) {
//                        LogUtils.e("onEnd");
//                    }
//                });


//                CountdownView mCvCountdownViewTest2 = (CountdownView)findViewById(R.id.cv_countdownViewTest2);
//                mCvCountdownViewTest2.setTag("test2");
//                long time2 = (long)30 * 60 * 1000;
//                mCvCountdownViewTest2.start(time2);
                List<DataBean> prodList = secKill.getProdList();
                if (prodList != null && prodList.size() != 0){
                    HomeSecondKill2Adapter adapter1 = new HomeSecondKill2Adapter(act, prodList);
                    viewHoler.gridView1.setAdapter(adapter1);
                    viewHoler.gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            UIHelper.startSecondKillFrg(root);
                        }
                    });
                }
            }
            //拼团
            DataBean.CollageBean collage = seckill_collageBean.getCollage();
            if (collage != null){
                List<DataBean> collageList = collage.getCollageList();
                if (collageList != null && collageList.size() != 0){
                    HomeSecondKill2Adapter adapter2 = new HomeSecondKill2Adapter(act, collageList);
                    viewHoler.gridView2.setAdapter(adapter2);
                    viewHoler.gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            UIHelper.startCollagefrg(root);
                        }
                    });
                }
            }
        }else if (holder instanceof NewProductViewHolder){
            NewProductViewHolder viewHoler = (NewProductViewHolder) holder;
            if (bean.getType() == NEW_PRODUCT_TYPE){//新品专区
                DataBean.NewProductBean newProduct = bean.getNewProduct();
                viewHoler.tvTitle.setText(newProduct.getTitle());
                List<DataBean> manJianList = newProduct.getManJianList();
                if (manJianList != null && manJianList.size() != 0){
                    HomeNewArrivalsAdapter2 adapter = new HomeNewArrivalsAdapter2(act, manJianList);
                    viewHoler.gridView.setAdapter(adapter);
                    viewHoler.tvMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UIHelper.startNewArrivalsFrg(root);
                        }
                    });
                }
            }else {//优惠特区
                final DataBean.SpecialSaleBean specialSale = bean.getSpecialSale();
                viewHoler.tvTitle.setText(specialSale.getTitle());
                List<DataBean> specialSaleList = specialSale.getSpecialSaleList();
                if (specialSaleList != null && specialSaleList.size() != 0){
                    HomeNewArrivalsAdapter2 adapter = new HomeNewArrivalsAdapter2(act, specialSaleList, true);
                    viewHoler.gridView.setAdapter(adapter);
                    viewHoler.tvMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UIHelper.startDiscountFrg(root, HomeAdapter.DISCOUNT_TYPE, specialSale.getId());
                        }
                    });
                }
            }
        } else  if (holder instanceof ManZengViewHolder){//满增  满减
            ManZengViewHolder viewHolder = (ManZengViewHolder) holder;
            viewHolder.banner.setDelegate(this);
            if (bean.getType() == FULL_GIFTS_TYPE){
                DataBean.ManZengBean manZeng = bean.getManZeng();
                List<DataBean> acts = manZeng.getActs();
                if (acts != null && acts.size() != 0){
                    viewHolder.banner.setVisibility(View.VISIBLE);
                    viewHolder.banner.setAutoPlayAble(acts.size() > 1);
                    viewHolder.banner.setData(acts, new ArrayList<String>());
                    viewHolder.banner.setAdapter(this);
                }
                final List<DataBean> prodLists = manZeng.getProdLists();
                if (prodLists != null && prodLists.size() != 0){
                    HomeImgAdapter2 adapter = new HomeImgAdapter2(act, prodLists);
                    viewHolder.gridView.setAdapter(adapter);
                    viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            DataBean bean = prodLists.get(i);
                            UIHelper.startGoodsDetailsAct(bean.getId());
                        }
                    });
                }
            }else {
                DataBean.ManJianBean manJian = bean.getManJian();
                List<DataBean> acts = manJian.getActs();
                if (acts != null && acts.size() != 0){
                    viewHolder.banner.setVisibility(View.VISIBLE);
                    viewHolder.banner.setAutoPlayAble(acts.size() > 1);
                    viewHolder.banner.setData(acts, new ArrayList<String>());
                    viewHolder.banner.setAdapter(HomeAdapter.this);
                }
                final List<DataBean> prodLists = manJian.getProdLists();
                if (prodLists != null && prodLists.size() != 0){
                    HomeImgAdapter2 adapter = new HomeImgAdapter2(act, prodLists);
                    viewHolder.gridView.setAdapter(adapter);
                    viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            DataBean bean = prodLists.get(i);
                            UIHelper.startGoodsDetailsAct(bean.getId());
                        }
                    });
                }
            }
        }
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        switch (viewType){
            case LABEL_TYPE:
                return new LabelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_label_view, parent, false));
            case SECOND_KILL_TYPE:
                return new Second_KillViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_home_second, parent, false));
            case NEW_PRODUCT_TYPE:
            case DISCOUNT_TYPE:
                return new NewProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_home_new_arrivals, parent, false));
            case FULL_GIFTS_TYPE:
            case FULL_SUBTRACTION_TYPE:
                return new ManZengViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_home_img, parent, false));
            default:
                return new LabelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_label_view, parent, false));
        }
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable DataBean model, int position) {
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + model.getImg1(), (ImageView) itemView);
    }

    @Override
    public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
        DataBean bean = (DataBean) model;
        UIHelper.startHtmlAct(null, bean.getId());
    }

    class ManZengViewHolder extends RecyclerView.ViewHolder{

        BGABanner banner;
        WithScrollGridView gridView;

        public ManZengViewHolder(View itemView) {
            super(itemView);
            banner = itemView.findViewById(R.id.banner);
            gridView = itemView.findViewById(R.id.gridView);
        }
    }

    class NewProductViewHolder extends RecyclerView.ViewHolder{

        TextView tvMore;
        TextView tvTitle;
        WithScrollGridView gridView;

        public NewProductViewHolder(View itemView) {
            super(itemView);
            tvMore = itemView.findViewById(R.id.tv_more);
            tvTitle = itemView.findViewById(R.id.tv_title);
            gridView = itemView.findViewById(R.id.gridView);
        }
    }

    class Second_KillViewHolder extends RecyclerView.ViewHolder{

        CountdownView tvTime;
        WithScrollGridView gridView1, gridView2;

        public Second_KillViewHolder(View itemView) {
            super(itemView);
            gridView1 = itemView.findViewById(R.id.gridView1);
            gridView2 = itemView.findViewById(R.id.gridView2);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

    class LabelViewHolder extends RecyclerView.ViewHolder{

        WithScrollGridView gridView;

        public LabelViewHolder(View itemView) {
            super(itemView);
            gridView = itemView.findViewById(R.id.gridView);
        }
    }

}
