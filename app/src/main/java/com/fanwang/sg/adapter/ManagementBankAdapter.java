package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseRecyclerviewAdapter;
import com.fanwang.sg.bean.DataBean;

import java.util.List;

/**
 * 作者：yc on 2018/9/6.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ManagementBankAdapter extends BaseRecyclerviewAdapter<DataBean>{

    public ManagementBankAdapter(Context act, List listBean) {
        super(act, listBean);
    }

    private int mPosition = -1;

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);
        viewHolder.tvTitle.setText(bean.getBankName());
        viewHolder.tvBankNumber.setText(bean.getCardNumber());



        viewHolder.cb_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean.getDefaulted() == 1)return;
                if (listener != null){
                    listener.onClick(position, bean.getId());
                }
            }
        });
        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean.getDefaulted() == 1){
                    showToast("默认银行卡不能删除");
                    return;
                }
                if (listener != null){
                    listener.onDelter(position, bean.getId());
                }
            }
        });

        if (bean.getDefaulted() == 1){
            viewHolder.cb_.setCompoundDrawablesWithIntrinsicBounds( ContextCompat.getDrawable(act,R.mipmap.shopcart_btn_selected),
                    null , null, null);
        }else {
            viewHolder.cb_.setCompoundDrawablesWithIntrinsicBounds( ContextCompat.getDrawable(act,R.mipmap.shopcart_btn_default),
                    null , null, null);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }
    public interface OnClickListener{
        void onClick(int position, String id);
        void onDelter(int position, String id);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_bank, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvBankNumber;
        TextView tvDelete;
        TextView cb_;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvBankNumber = itemView.findViewById(R.id.tv_bank_number);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            cb_ = itemView.findViewById(R.id.cb_);
        }
    }

}
