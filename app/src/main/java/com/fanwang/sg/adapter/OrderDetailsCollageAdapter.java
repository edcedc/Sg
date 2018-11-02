package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.base.BaseRecyclerviewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.weight.CircleImageView;

import java.util.List;

/**
 * 作者：yc on 2018/9/11.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class OrderDetailsCollageAdapter extends BaseListViewAdapter<DataBean> {

    public OrderDetailsCollageAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    private int mCollageNum;

    public void setmCollageNum(int mCollageNum) {
        this.mCollageNum = mCollageNum;
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_order_details_collage, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataBean bean = listBean.get(position);
        String head = bean.getHead();
        if (!StringUtils.isEmpty(head)){
            GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getHead(), viewHolder.ivHead);
        }else {
            viewHolder.ivHead.setImageResource(R.mipmap.icon_1);
        }
        if (position == 0){
            viewHolder.tvName.setText("团长");
        }else {
            viewHolder.tvName.setText(bean.getNickname());
        }
        return convertView;
    }


    class ViewHolder{

        CircleImageView ivHead;
        TextView tvName;

        public ViewHolder(View convertView) {
            ivHead = convertView.findViewById(R.id.iv_head);
            tvName = convertView.findViewById(R.id.tv_name);
        }
    }

}
