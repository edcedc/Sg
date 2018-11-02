package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BaseRecyclerviewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.event.ChoiceAddressInEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 作者：yc on 2018/9/13.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class AddressAdapter extends BaseRecyclerviewAdapter<DataBean>{

    public AddressAdapter(Context act, BaseFragment root, List listBean, int type) {
        super(act, root, listBean);
        this.mType = type;
    }

    private int mType;

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);
        viewHolder.tvName.setText(bean.getName());
        viewHolder.tvPhone.setText(bean.getMobile());
        viewHolder.tvAddress.setText(bean.getAddress() + bean.getDetailedAddress());


        if (bean.getIsChoice() == 1){
            viewHolder.cb_.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(act,R.mipmap.home_btn_selected),
                    null , null, null);
        }else {
            viewHolder.cb_.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(act,R.mipmap.home_btn_default),
                    null , null, null);
        }

        viewHolder.cb_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean.getIsChoice() == 1)return;
                if (listener != null){
                    listener.onClick(position, bean.getId());
                }
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mType != 0){
                    EventBus.getDefault().post(new ChoiceAddressInEvent(bean));
                    root.pop();
                }
            }
        });

        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)listener.onDelter(position, bean.getId());
            }
        });
        viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bean.setPosition(position);
                if (listener != null)listener.onEdit(position, bean);
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
        void onEdit(int position, DataBean bean);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_address, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        TextView tvPhone;
        TextView tvAddress;
        TextView cb_;
        TextView tvEdit;
        TextView tvDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);
            tvAddress = itemView.findViewById(R.id.tv_address);
            cb_ = itemView.findViewById(R.id.cb_);
            tvEdit = itemView.findViewById(R.id.tv_edit);
            tvDelete = itemView.findViewById(R.id.tv_delete);
        }
    }

}
