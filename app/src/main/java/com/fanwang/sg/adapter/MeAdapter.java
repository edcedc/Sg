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
 * Created by edison on 2018/5/5.
 */

public class MeAdapter extends BaseRecyclerviewAdapter {

    private boolean isImg = false;

    public MeAdapter(Context act, List listBean) {
        super(act, listBean);
    }
    public MeAdapter(Context act, List listBean, boolean isImg) {
        super(act, listBean);
        this.isImg = isImg;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = (DataBean) listBean.get(position);
        if (bean != null){
            viewHolder.tvTitle.setText(bean.getName());
            if (isImg){
                viewHolder.ivImg.setVisibility(View.VISIBLE);
                viewHolder.ivImg.setBackgroundResource(bean.getImg());
            }

            if (bean.getName().equals("清理缓存")){
                viewHolder.tvContent.setText(bean.getContent());
                viewHolder.tvContent.setVisibility(View.VISIBLE);
            }else {
                viewHolder.tvContent.setVisibility(View.GONE);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        listener.onClick(v, position);
                    }
                }
            });
        }
    }

    private OnClickListener listener;
    public interface OnClickListener{
        void onClick(View v, int position);
    }
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_me, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImg;
        TextView tvTitle;
        TextView tvContent;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvTitle = itemView.findViewById(R.id.tv_title);
            view = itemView.findViewById(R.id.view);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }

}
