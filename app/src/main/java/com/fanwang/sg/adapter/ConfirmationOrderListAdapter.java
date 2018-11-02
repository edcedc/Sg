package com.fanwang.sg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.flyco.roundview.RoundLinearLayout;

import java.util.List;

/**
 * 作者：yc on 2018/9/26.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ConfirmationOrderListAdapter extends BaseListViewAdapter<DataBean>{

    public ConfirmationOrderListAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_confirmation_order_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataBean bean = listBean.get(position);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getImage(), viewHolder.ivImg);
        viewHolder.tvName.setText(bean.getName());
        viewHolder.tvNumber.setText("x" + bean.getNum());
        viewHolder.tvChoice.setText(bean.getSpecificationValues());
        viewHolder.tvPrice.setText(act.getString(R.string.monetary_symbol) + bean.getRealPrice());
        return convertView;
    }

    class ViewHolder{

        ImageView ivImg;
        TextView tvChoice;
        TextView tvName;
        TextView tvPrice;
        TextView tvNumber;

        public ViewHolder(View itemView) {
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvChoice = itemView.findViewById(R.id.tv_choice);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvNumber = itemView.findViewById(R.id.tv_number);
        }
    }
}
