package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseRecyclerviewAdapter;
import com.fanwang.sg.bean.DataBean;

import java.util.List;

/**
 * 作者：yc on 2018/10/17.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class LogisticsAdapter extends BaseRecyclerviewAdapter<DataBean>{
    public LogisticsAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = listBean.get(position);
        String create_time = bean.getCreate_time();
        String[] split = create_time.split(" ");
        viewHolder.tvTime.setText(split[0] + "\n" + split[1]);
        viewHolder.tvContent.setText(bean.getContent());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_logistics, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTime;
        ImageView ivImg;
        TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }

}
