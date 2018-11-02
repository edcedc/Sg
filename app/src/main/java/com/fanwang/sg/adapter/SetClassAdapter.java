package com.fanwang.sg.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.library.AutoFlowLayout;
import com.example.library.FlowAdapter;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseRecyclerviewAdapter;
import com.fanwang.sg.bean.DataBean;

import java.util.List;

/**
 * 作者：yc on 2018/9/11.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class SetClassAdapter extends BaseRecyclerviewAdapter<DataBean> {

    public SetClassAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected void onBindViewHolde(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final DataBean bean = listBean.get(position);
        viewHolder.tvTitle.setText(bean.getName());

        viewHolder.flowLayout.setMultiChecked(false);
        final List<DataBean> entries = bean.getEntries();
        for (DataBean bean1 : entries){
            if (bean1.isSelected()){
                bean.setAllSelected(true);
                break;
            }
        }

        viewHolder.flowLayout.removeAllViews();
        viewHolder.flowLayout.setAdapter(new FlowAdapter(entries) {
            @Override
            public View getView(int i) {
                View view = View.inflate(act, R.layout.i_class, null);
                TextView tvText = view.findViewById(R.id.tv_text);
                DataBean bean = entries.get(i);
                if (bean != null){
                    tvText.setText(bean.getValue());
                    int[][] states = new int[][]{
                            new int[]{-android.R.attr.state_selected}, // unchecked
                            new int[]{android.R.attr.state_selected}  // checked
                    };
                    int[] colors = new int[]{
                            Color.parseColor("#666666"),
                            Color.parseColor("#FE2701")
                    };
                    tvText.setTextColor(new ColorStateList(states, colors));
                    tvText.setSelected(bean.isSelected());
                }
                return view;
            }
        });
        viewHolder.flowLayout.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int i, View view) {
                DataBean bean = entries.get(i);
                if (bean.isSelected()){
                    bean.setSelected(false);
                    setAllSelected(false, position);
                }else {
                    bean.setSelected(true);
                    setAllSelected(true, position);
                    for (DataBean bean1 : entries){
                        if (!bean.getId().equals(bean1.getId())){
                            bean1.setSelected(false);
                        }
                    }
                }
                notifyDataSetChanged();
                if (listener != null){
                    listener.onItemClick(i, listBean);
                }
            }
        });
    }

    private void setAllSelected(boolean Selected, int position){
        DataBean bean = listBean.get(position);
        bean.setAllSelected(Selected);
    }

    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void onItemClick(int i, List<DataBean> listBean);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_set_class, parent, false));
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        AutoFlowLayout flowLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            flowLayout = itemView.findViewById(R.id.fl_layout);
        }
    }

}
