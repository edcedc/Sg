package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseRecyclerviewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.utils.Constants;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.flyco.roundview.RoundTextView;
import com.flyco.roundview.RoundViewDelegate;

import java.util.List;

/**
 * 作者：yc on 2018/8/30.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class CollageChildAdapter extends BaseRecyclerviewAdapter<DataBean>{

    private int state;

    public CollageChildAdapter(Context act, List listBean, int state) {
        super(act, listBean);
        this.state = state;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getImage(), viewHolder.ivImg);
        viewHolder.tvTitle.setText(bean.getName());
        viewHolder.tvPrice.setText(act.getString(R.string.monetary_symbol) + bean.getRealPrice());
        viewHolder.tvDiscount.setText(bean.getCollageNum() + "人团");
        viewHolder.tvPrice2.setText("单买价" + act.getString(R.string.monetary_symbol) + bean.getMarketPrice());
        RoundViewDelegate delegate = viewHolder.tvConfirm.getDelegate();


        if (state == Constants.collage_process){
            delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.reb_FE2701));
            viewHolder.tvConfirm.setText("去拼团");
            viewHolder.tvConfirm.setEnabled(true);
            viewHolder.tvTime.setVisibility(View.GONE);
            /*viewHolder.tvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UIHelper.startGoodsDetailsAct(bean.getId());
                }
            });*/
        }else {
            delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.black_898989));
            viewHolder.tvConfirm.setText("未开始");
            viewHolder.tvConfirm.setEnabled(false);

            String startTime = bean.getStartTime();
            if (!StringUtils.isEmpty(startTime)){
                String[] split = startTime.split("-");
                String s = split[0] + "年" + split[1] + "月" + split[2];
                viewHolder.tvTime.setText(s.substring(0, s.length() - 3));
                viewHolder.tvTime.setVisibility(View.VISIBLE);
            }
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startGoodsDetailsAct(bean.getId(), state);
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_collage, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImg;
        TextView tvTitle;
        TextView tvPrice;
        RoundTextView tvDiscount;
        TextView tvPrice2;
        RoundTextView tvConfirm;
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDiscount = itemView.findViewById(R.id.tv_discount);
            tvPrice2 = itemView.findViewById(R.id.tv_price2);
            tvConfirm = itemView.findViewById(R.id.tv_confirm);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }

}
