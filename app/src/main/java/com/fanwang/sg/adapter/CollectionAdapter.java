package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseRecyclerviewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.weight.RoundImageView;
import com.flyco.roundview.RoundTextView;

import java.util.List;

/**
 * 作者：yc on 2018/9/6.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class CollectionAdapter extends BaseRecyclerviewAdapter<DataBean>{


    public CollectionAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);
        viewHolder.tvPrice2.setVisibility(View.GONE);
        viewHolder.tvDiscount.setVisibility(View.GONE);
        viewHolder.tvState.setVisibility(View.GONE);

        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getImage(), viewHolder.ivImg);
        viewHolder.tvTitle.setText(bean.getName());
        viewHolder.tvPrice.setText(act.getString(R.string.monetary_symbol) + bean.getMarketPrice());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startGoodsDetailsAct(bean.getId());
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_search, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        RoundImageView ivImg;
        TextView tvTitle;
        TextView tvPrice;
        TextView tvPrice2;
        RoundTextView tvDiscount;
        RoundTextView tvState;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvPrice2 = itemView.findViewById(R.id.tv_price2);
            tvDiscount = itemView.findViewById(R.id.tv_discount);
            tvState = itemView.findViewById(R.id.tv_state);
        }
    }


}
