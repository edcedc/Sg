package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.bean.DataBean;

import java.util.List;

/**
 * 作者：yc on 2018/10/19.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ShareAdapter extends BaseListViewAdapter<DataBean>{

    public ShareAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_share, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataBean bean = listBean.get(position);

        viewHolder.tvText.setText(bean.getName());

        try {
        viewHolder.tvText.setCompoundDrawablesWithIntrinsicBounds(null,
                ContextCompat.getDrawable(act,Integer.valueOf(bean.getImg())), null, null);

        } catch (Exception e){
        }
        return convertView;
    }

    class ViewHolder{

        TextView tvText;

        public ViewHolder(View convertView) {
            tvText = convertView.findViewById(R.id.tv_text);
        }
    }

}
