package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.bean.DataBean;

import java.util.List;

/**
 * 作者：yc on 2018/9/26.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class PayAdapter extends BaseListViewAdapter<DataBean>{

    public PayAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }


    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_pay, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DataBean bean = listBean.get(position);
        viewHolder.tvText.setText(bean.getName());
        if (mPsition == position){
            viewHolder.tvText.setCompoundDrawablesWithIntrinsicBounds( null,
                    null , ContextCompat.getDrawable(act,R.mipmap.home_icon_select), null);
        }else {
            viewHolder.tvText.setCompoundDrawablesWithIntrinsicBounds( null,
                    null , ContextCompat.getDrawable(act,R.mipmap.shopcart_btn_default), null);
        }
        return convertView;
    }

    private int mPsition = -1;

    public void setmPsition(int mPsition) {
        this.mPsition = mPsition;
    }

    class ViewHolder{

        TextView tvText;

        public ViewHolder(View convertView) {
            tvText = convertView.findViewById(R.id.tv_text);
        }
    }

}
