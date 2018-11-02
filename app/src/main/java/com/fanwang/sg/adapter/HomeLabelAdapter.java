package com.fanwang.sg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.GlideLoadingUtils;

import java.util.List;

/**
 * 作者：yc on 2018/8/28.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class HomeLabelAdapter extends BaseListViewAdapter<DataBean>{

    public HomeLabelAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_home_label, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataBean bean = listBean.get(position);
        if (bean != null){
            String image = bean.getImage();
            if (listBean.size() - 1 == position){
                viewHolder.ivImg.setImageResource(bean.getImg());
            }else if (!StringUtils.isEmpty(image)){
                GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + image, viewHolder.ivImg);
            }else {
                viewHolder.ivImg.setImageResource(bean.getImg());
            }
            viewHolder.tvText.setText(bean.getName());
        }
        return convertView;
    }

    class ViewHolder{

        TextView tvText;
        ImageView ivImg;

        public ViewHolder(View convertView) {
            tvText = convertView.findViewById(R.id.tv_text);
            ivImg = convertView.findViewById(R.id.iv_img);
        }
    }

}
