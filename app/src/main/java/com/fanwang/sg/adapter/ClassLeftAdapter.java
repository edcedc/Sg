package com.fanwang.sg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.bean.DataBean;

import java.util.List;

/**
 * 作者：yc on 2018/8/29.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ClassLeftAdapter extends BaseListViewAdapter<DataBean>{

    public ClassLeftAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    private int mPosition = -1;

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_classify_left, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataBean bean = listBean.get(position);
        viewHolder.tvName.setText(bean.getCat_name());
        if (mPosition == position){
            viewHolder.tvName.setBackgroundResource(R.color.white);
        }else {
            viewHolder.tvName.setBackgroundResource(R.color.white_f4f4f4);
        }
        return convertView;
    }

    class ViewHolder{

        TextView tvName;

        public ViewHolder(View convertView) {
            tvName = convertView.findViewById(R.id.tv_name);
        }
    }

}
