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
import com.fanwang.sg.view.PresentRecordFrg;

import java.util.List;

/**
 * 作者：yc on 2018/9/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class DistributionAdapter extends BaseRecyclerviewAdapter<DataBean>{

    private int mType;

    public DistributionAdapter(Context act, List<DataBean> listBean, int type) {
        super(act, listBean);
        this.mType = type;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = listBean.get(position);
        switch (mType){//0付款，1获得
            case PresentRecordFrg.PRESENT_RECORD:
                viewHolder.tvTitle.setText(bean.getBankName());
                viewHolder.tvPrice.setText("+" + bean.getWithdraw_money());
                viewHolder.tvTime.setText(bean.getCreate_time());
                break;
            case PresentRecordFrg.DETAILED:
                viewHolder.tvTitle.setText(bean.getRecord());
                viewHolder.tvPrice.setText((bean.getType() == 0 ? "-" : "+") + bean.getMoney());
                viewHolder.tvTime.setText(bean.getCreateTime());
                break;
            case PresentRecordFrg.DISTRIBUTION:
                viewHolder.tvTitle.setText(bean.getRecord());
                viewHolder.tvPrice.setText("+" + bean.getMoney());
                break;
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_distribution, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle;
        private TextView tvTime;
        private TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }

}
