package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseRecyclerviewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.weight.WithScrollListView;

import java.math.BigDecimal;
import java.util.List;

/**
 * 作者：yc on 2018/9/26.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ConfirmationOrderAdapter extends BaseRecyclerviewAdapter<DataBean>{

    public ConfirmationOrderAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        DataBean bean = listBean.get(position);
        List<DataBean> prod = bean.getProd();
        ConfirmationOrderListAdapter adapter = new ConfirmationOrderListAdapter(act, prod);
        viewHolder.listView.setAdapter(adapter);
        /*double price = 0;
        for (DataBean bean1 : prod){
            price += bean1.getRealPrice() * bean1.getNum();
        }*/
        BigDecimal allPrice = new BigDecimal(0);
        for (DataBean bean1 : prod) {
            BigDecimal num = new BigDecimal(bean1.getNum());
            BigDecimal price = bean1.getRealPrice().multiply(num);
            allPrice = allPrice.add(price);
        }
        viewHolder.tvPrice.setText(act.getString(R.string.monetary_symbol) + allPrice);
        viewHolder.tvNumberGoods.setText("共" + prod.size() + "件商品    小计");
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_confirmation_order, parent, false));
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        WithScrollListView listView;
        TextView tvNumberGoods;
        TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            listView = itemView.findViewById(R.id.listView);
            tvNumberGoods = itemView.findViewById(R.id.tv_number_goods);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }

}
