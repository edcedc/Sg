package com.fanwang.sg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.weight.CircleImageView;

import java.util.List;

/**
 * 作者：yc on 2018/9/7.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class CollageUserAdapter extends BaseListViewAdapter<DataBean>{

    public CollageUserAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_collage_user, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataBean bean = listBean.get(position);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getHead(), viewHolder.ivHead);
        return convertView;
    }

    class ViewHolder{

        CircleImageView ivHead;

        public ViewHolder(View convertView) {
            ivHead = convertView.findViewById(R.id.iv_head);
        }
    }

}
