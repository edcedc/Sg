package com.fanwang.sg.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.weight.SizeImageView;
import com.flyco.roundview.RoundTextView;

import java.util.List;

/**
 * 作者：yc on 2018/8/29.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class HomeNewArrivalsAdapter2 extends BaseListViewAdapter<DataBean>{

    public HomeNewArrivalsAdapter2(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    public HomeNewArrivalsAdapter2(Context act, List<DataBean> listBean, boolean isDiscount) {
        super(act, listBean);
        this.isDiscount = isDiscount;
    }

    private boolean isDiscount = false;

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_home_new_arrivals_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getImage(), viewHolder.ivImg);
        viewHolder.tvPrice.setText(act.getString(R.string.monetary_symbol2) + bean.getRealPrice());
        viewHolder.tvPrice2.setText(bean.getZhekou() + "折");
        viewHolder.tvPrice2.setVisibility(isDiscount == true ? View.VISIBLE : View.GONE);
        viewHolder.ivImg.setWH(1, 1, true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startGoodsDetailsAct(bean.getId());
            }
        });
        return convertView;
    }

    class ViewHolder{

        SizeImageView ivImg;
        TextView tvPrice;
        RoundTextView tvPrice2;

        @SuppressLint("WrongViewCast")
        public ViewHolder(View convertView) {
            ivImg = convertView.findViewById(R.id.iv_img);
            tvPrice = convertView.findViewById(R.id.tv_price);
            tvPrice2 = convertView.findViewById(R.id.tv_price2);
        }
    }

}
