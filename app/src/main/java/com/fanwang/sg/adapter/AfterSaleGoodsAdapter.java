package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseRecyclerviewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.utils.PopupWindowTool;
import com.flyco.roundview.RoundLinearLayout;

import java.util.List;

/**
 * 作者：yc on 2018/10/16.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class AfterSaleGoodsAdapter extends BaseRecyclerviewAdapter<DataBean>{

    public AfterSaleGoodsAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);
        viewHolder.lyChoice.setVisibility(View.GONE);
        viewHolder.ivClose.setVisibility(View.VISIBLE);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.getShowImg(), viewHolder.ivImg);
        viewHolder.tvName.setText(bean.getName());
        viewHolder.tvPrice.setText(act.getString(R.string.monetary_symbol) + bean.getPrice());
        viewHolder.etNumber.setText(bean.getNum() + "");
        viewHolder.clAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*PopupWindowTool.shouCartNumber(act, cartBean.getStock(), cartBean.getNum(), new PopupWindowTool.CartNumberListener() {
                    @Override
                    public void onClick(int integer) {
                        if (childClickListener != null){
                            childClickListener.onCartNumer(groupPosition, childPosition, cartBean.getSkuId(), integer, cartBean.getId());
                        }
                    }
                });*/
            }
        });
        viewHolder.cbTotla.setChecked(bean.isSelect());
        viewHolder.cbTotla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b ;
                if (bean.isSelect()){
                    bean.setSelect(false);
                    b = false;
                }else {
                    bean.setSelect(true);
                    b = true;
                }
                bean.setSelect(b);

                List<DataBean> prod = bean.getProd();
                for (DataBean bean1 : prod){
                    bean1.setSelect(b);
                }
                notifyDataSetChanged();

                if (listener != null){
                    for (DataBean bean1 : listBean){
                        if (bean1.isSelect()){
                            listener.onParentCbUpdate(true);
                        }else {
                            listener.onParentCbUpdate(false);
                            break;
                        }
                    }
                }
            }
        });
    }

    private CartAdapter.onParentCbUpdateListener listener;
    public void setParentCbUpdateListener(CartAdapter.onParentCbUpdateListener listener){
        this.listener = listener;
    }
    public interface onParentCbUpdateListener{
        void onParentCbUpdate(boolean isUpdate);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_cart_child, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox cbTotla;
        ImageView ivImg;
        TextView tvChoice;
        TextView tvName;
        TextView tvPrice;
        RoundLinearLayout lyChoice;
        EditText etNumber;
        ImageView ivClose;
        View clAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            cbTotla = itemView.findViewById(R.id.cb_totla);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvChoice = itemView.findViewById(R.id.tv_choice);
            tvPrice = itemView.findViewById(R.id.tv_price);
            lyChoice = itemView.findViewById(R.id.ly_choice);
            ivClose = itemView.findViewById(R.id.iv_close);
            etNumber = itemView.findViewById(R.id.et_number);
            clAdd = itemView.findViewById(R.id.cl_add);
        }
    }

}
