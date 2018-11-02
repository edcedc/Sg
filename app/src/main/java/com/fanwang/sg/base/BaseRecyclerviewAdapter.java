package com.fanwang.sg.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yc on 2017/11/22.
 */

public abstract class BaseRecyclerviewAdapter<T> extends RecyclerView.Adapter {


    protected List<T> listBean = new ArrayList<>();
    protected Context act;
    protected BaseFragment root;

    public BaseRecyclerviewAdapter(Context act, List<T> listBean) {
        this.listBean = listBean;
        this.act = act;
    }
    public BaseRecyclerviewAdapter(Context act, BaseFragment root, List<T> listBean) {
        this.listBean = listBean;
        this.act = act;
        this.root = root;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolde(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindViewHolde(holder, position);
    }

    protected abstract void onBindViewHolde(RecyclerView.ViewHolder holder, int position);

    protected abstract RecyclerView.ViewHolder onCreateViewHolde(ViewGroup parent, int viewType);


    @Override
    public int getItemCount() {
        return listBean.size();
    }

    protected void showToast(String title) {
        ToastUtils.showShort(title);
    }


}
