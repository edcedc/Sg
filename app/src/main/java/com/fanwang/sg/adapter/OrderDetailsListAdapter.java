package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseRecyclerviewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.flyco.roundview.RoundTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 作者：yc on 2018/9/10.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class OrderDetailsListAdapter extends BaseRecyclerviewAdapter<DataBean>{

    public OrderDetailsListAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    private int isGroupPurchase;

    public void setIsGroupPurchase(int isGroupPurchase) {
        this.isGroupPurchase = isGroupPurchase;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = listBean.get(position);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getShowImg(), viewHolder.ivImg);
        viewHolder.tvName.setText(bean.getName());
        viewHolder.tvPrice.setText(act.getString(R.string.monetary_symbol) + bean.getPrice());
        viewHolder.tvDiscount.setText("拼团");
        viewHolder.tvDiscount.setVisibility(isGroupPurchase == 1 ? View.VISIBLE : View.GONE);
        viewHolder.tvNumber.setText("x" + bean.getNum());
        String specificationValues = bean.getSpecificationValues();
        if (!StringUtils.isEmpty(specificationValues)){
            try {
                JSONArray array = new JSONArray(specificationValues);
                if (array != null && array.length() != 0){
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0;i < array.length();i++){
                        JSONObject object = array.optJSONObject(i);
                        sb.append(object.optString("value")).append(",");
                    }
                    viewHolder.tvClass.setText(sb.deleteCharAt(sb.length() - 1).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_order_details_list, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImg;
        TextView tvName;
        TextView tvClass;
        RoundTextView tvDiscount;
        TextView tvPrice;
        TextView tvNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvClass = itemView.findViewById(R.id.tv_class);
            tvDiscount = itemView.findViewById(R.id.tv_discount);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvNumber = itemView.findViewById(R.id.tv_number);
        }
    }

}
