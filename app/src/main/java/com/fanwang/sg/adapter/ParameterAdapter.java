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
 * 作者：yc on 2018/9/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ParameterAdapter extends BaseListViewAdapter<DataBean>{

    public ParameterAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_parameter, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataBean bean = listBean.get(position);
        viewHolder.tvName.setText(bean.getName());
        viewHolder.tvContent.setText(bean.getContent());
        return convertView;
    }

    class ViewHolder{

        TextView tvName;
        TextView tvContent;

        public ViewHolder(View convertView) {
            tvName = convertView.findViewById(R.id.tv_name);
            tvContent = convertView.findViewById(R.id.tv_content);
        }
    }

}
