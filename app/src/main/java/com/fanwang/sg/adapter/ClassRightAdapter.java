package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.base.BaseRecyclerviewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.weight.SizeImageView;

import java.util.List;

/**
 * 作者：yc on 2018/8/29.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ClassRightAdapter extends BaseRecyclerviewAdapter<DataBean>{

    public ClassRightAdapter(Context act, BaseFragment root, List listBean) {
        super(act, root, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);
        viewHolder.ivImg.setWH(1, 1, true);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getImage_url(), viewHolder.ivImg);
        viewHolder.tvPrice.setText(bean.getCat_name());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startClassListFrg(root, bean.getCat_name(), bean.getId(), 1);
            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_classify_right, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        SizeImageView ivImg;
        TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }

}
