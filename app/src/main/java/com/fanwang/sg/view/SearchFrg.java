package com.fanwang.sg.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.SearchAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.FSearchBinding;
import com.fanwang.sg.presenter.SearchPresenter;
import com.fanwang.sg.view.impl.SearchContract;
import com.fanwang.sg.weight.GridDividerItemDecoration;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/8/31.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  搜索
 */

public class SearchFrg extends BaseFragment<SearchPresenter, FSearchBinding> implements SearchContract.View, View.OnClickListener{

    private List<DataBean> listBean1 = new ArrayList<>();
    private SearchAdapter adapter1;
    private List<DataBean> listBean2 = new ArrayList<>();
    private SearchAdapter adapter2;
    private int type;
    private View ivSearchImg;
    private View tvCancel;
    private EditText etSearch;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_search;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.fy_close).setVisibility(View.VISIBLE);
        ivSearchImg = view.findViewById(R.id.iv_search_img);
        tvCancel = view.findViewById(R.id.tv_cancel);
        ivSearchImg.setVisibility(View.INVISIBLE);
        tvCancel.setVisibility(View.GONE);
        view.findViewById(R.id.fy_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop();
            }
        });
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        etSearch = view.findViewById(R.id.et_searh);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //判断是否是“完成”键
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    type = 1;
                    mPresenter.onRequest(etSearch.getText().toString().trim(), pagerNumber = 1, mB.refreshLayout);
                    return true;
                }
                return false;
            }
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (editable.length() == 0){
                            ivSearchImg.setVisibility(View.INVISIBLE);
                            tvCancel.setVisibility(View.GONE);
                        }else {
                            ivSearchImg.setVisibility(View.GONE);
                            tvCancel.setVisibility(View.VISIBLE);
                        }
                    }
                }, 300);
            }
        });
        if (adapter1 == null){
            adapter1 = new SearchAdapter(act, listBean1);
        }
        setRecyclerView(mB.recyclerView, adapter1, 0);
        if (adapter2 == null){
            adapter2 = new SearchAdapter(act, listBean2);
        }
        setRecyclerView(mB.recyclerView1, adapter2, 1);

        mB.refreshLayout.startRefresh();
        showLoadDataing();
//        mB.refreshLayout.setEnableLoadmore(true);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                if (type == 0){
                    mPresenter.onAefaultRequest(pagerNumber = 1);
                }else {
                    mPresenter.onRequest(etSearch.getText().toString().trim(), pagerNumber = 1, mB.refreshLayout);
                }
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (type == 0){
                    mPresenter.onAefaultRequest(pagerNumber += 1);
                }else {
                    mPresenter.onRequest(etSearch.getText().toString().trim(), pagerNumber += 1, mB.refreshLayout);
                }
            }
        });
    }

    private void setRecyclerView(RecyclerView recyclerView, SearchAdapter adapter, final int type){
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (type == 0){

            recyclerView.setLayoutManager(new GridLayoutManager(act, 2));
            recyclerView.addItemDecoration(new GridDividerItemDecoration(20, ContextCompat.getColor(act,R.color.white_f4f4f4)));
        }else {
            recyclerView.setLayoutManager(new LinearLayoutManager(act));
            recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL, 2, Color.parseColor("#eff0f0")));
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                type = 0;
                etSearch.setText("");
                mB.gp1.setVisibility(View.VISIBLE);
                mB.gp2.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean1.size(), totalRow, mB.refreshLayout);
        super.setRefreshLayoutMode(listBean2.size(), totalRow, mB.refreshLayout);
    }


    @Override
    public void setData(Object data) {//搜索
        List<DataBean> list = (List<DataBean>) data;
        mB.gp1.setVisibility(View.GONE);
        mB.gp2.setVisibility(View.VISIBLE);
        if (pagerNumber == 1) {
            listBean2.clear();
            mB.refreshLayout.finishRefreshing();
        } else {
            mB.refreshLayout.finishLoadmore();
        }
        listBean2.addAll(list);
        adapter2.notifyDataSetChanged();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setAefaultData(List<DataBean> data) {//默认
        List<DataBean> list = (List<DataBean>) data;
        mB.gp1.setVisibility(View.VISIBLE);
        mB.gp2.setVisibility(View.GONE);
        if (pagerNumber == 1) {
            listBean1.clear();
            mB.refreshLayout.finishRefreshing();
        } else {
            mB.refreshLayout.finishLoadmore();
        }
        listBean1.addAll(list);
        adapter1.notifyDataSetChanged();
    }
}
