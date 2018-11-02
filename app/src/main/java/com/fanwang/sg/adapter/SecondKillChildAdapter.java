package com.fanwang.sg.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseRecyclerviewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.weight.StrikethroughTextView;
import com.flyco.roundview.RoundFrameLayout;
import com.flyco.roundview.RoundTextView;
import com.flyco.roundview.RoundViewDelegate;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

/**
 * 作者：yc on 2018/8/30.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class SecondKillChildAdapter extends BaseRecyclerviewAdapter<DataBean>{

    private int overdue;
    private String endTime;
    private String startTime;

    public SecondKillChildAdapter(Context act, List<DataBean> listBean, int overdue, String endTime, String startTime) {
        super(act, listBean);
        this.overdue = overdue;
        this.endTime = endTime;
        this.startTime = startTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getImage(), viewHolder.ivImg);
        viewHolder.tvTitle.setText(bean.getName());
        viewHolder.tvPrice.setText(act.getString(R.string.monetary_symbol) + bean.getMarketPrice());
        viewHolder.tvPrice2.setText(act.getString(R.string.monetary_symbol) + bean.getRealPrice());
        final int totalStock = bean.getTotalStock();//总库存
        final int surplusStock = bean.getSurplusStock();//剩余

        String content = "剩余" + "<font color='#FE2701'> " + surplusStock + " </font>" + "件";
        viewHolder.tvNumber.setText(Html.fromHtml(content));
        RoundViewDelegate delegate = viewHolder.tvConfirm.getDelegate();

        viewHolder.progressBar.setMax(100);
        if (surplusStock != 0){
            switch (overdue){//0进行中 1未开启 2过期
                case 0:
                    delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.reb_FE2701));
                    viewHolder.tvConfirm.setText("马上抢");
//                    viewHolder.tvConfirm.setEnabled(true);
                    break;
                case 1:
                    delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.black_A1A1A1));
                    viewHolder.tvConfirm.setText("已过期");
//                    viewHolder.tvConfirm.setEnabled(false);
                    break;
                case 2:
                    delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.black_A1A1A1));
                    viewHolder.tvConfirm.setText("未开启");
//                    viewHolder.tvConfirm.setEnabled(false);
                    break;
            }

            double progress = 0;
            if (totalStock > 0 && totalStock != surplusStock) {
                progress = surplusStock * 100 / totalStock;
                if (progress <= 0) {
                    progress = 1;
                }
            }
//            // 创建一个数值格式化对象
//            NumberFormat numberFormat = NumberFormat.getInstance();
//            // 设置精确到小数点后2位
//            numberFormat.setMaximumFractionDigits(2);
//            String result = numberFormat.format((float) surplusStock / (float) totalStock * 1000);
            int i = (int) (progress);
            viewHolder.progressBar.setProgress(i);
            viewHolder.tvBarNumber.setText(i + "%");
        }else {
            delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.black_A1A1A1));
            viewHolder.tvConfirm.setText("抢光了");
//            viewHolder.tvConfirm.setEnabled(false);
            viewHolder.progressBar.setProgress(100);
            viewHolder.tvBarNumber.setText(100 + "%");
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startGoodsDetailsAct(bean.getId(), overdue, endTime, startTime);
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_second, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImg;
        TextView tvTitle;
        TextView tvNumber;
        TextView tvPrice;
        TextView tvBarNumber;
        StrikethroughTextView tvPrice2;
        RoundTextView tvConfirm;
        RoundFrameLayout skBar;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvNumber = itemView.findViewById(R.id.tv_number);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvPrice2 = itemView.findViewById(R.id.tv_price2);
            tvConfirm = itemView.findViewById(R.id.tv_confirm);
            tvBarNumber = itemView.findViewById(R.id.tv_bar_number);
            skBar = itemView.findViewById(R.id.sk_bar);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

}
