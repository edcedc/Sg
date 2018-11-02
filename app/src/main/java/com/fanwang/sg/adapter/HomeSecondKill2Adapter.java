package com.fanwang.sg.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.weight.SizeImageView;
import com.fanwang.sg.weight.StrikethroughTextView;

import java.util.List;

/**
 * 作者：yc on 2018/8/28.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class HomeSecondKill2Adapter extends BaseListViewAdapter<DataBean>{

    public HomeSecondKill2Adapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_home_second2, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataBean bean = listBean.get(position);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getImage(), viewHolder.ivImg);
        viewHolder.tvPrice.setText(act.getString(R.string.monetary_symbol2) + bean.getRealPrice());
        viewHolder.tvPrice2.setText(act.getString(R.string.monetary_symbol2) + bean.getMarketPrice());
        viewHolder.ivImg.setWH(1, 1, true);
        return convertView;
    }

    class ViewHolder{

        SizeImageView ivImg;
        TextView tvPrice;
        StrikethroughTextView tvPrice2;

        public ViewHolder(View convertView) {
            ivImg = convertView.findViewById(R.id.iv_img);
            tvPrice = convertView.findViewById(R.id.tv_price);
            tvPrice2 = convertView.findViewById(R.id.tv_price2);
        }
    }

}
