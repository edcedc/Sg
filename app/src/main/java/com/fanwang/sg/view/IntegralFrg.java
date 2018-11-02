package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.DistributionAdapter;
import com.fanwang.sg.adapter.IntegralAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FIntegralBinding;
import com.fanwang.sg.presenter.BaseListPresenter;
import com.fanwang.sg.view.impl.BaseListContract;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/6.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  我的积分
 */

public class IntegralFrg extends BaseFragment<BaseListPresenter, FIntegralBinding> implements BaseListContract.View, View.OnClickListener{

    private List<DataBean> listBean = new ArrayList<>();
    private IntegralAdapter adapter;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_integral;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.integral_rule));
        mB.tvForward.setOnClickListener(this);
        if (adapter == null){
            adapter = new IntegralAdapter(act, listBean);
        }
        setRecyclerViewType(mB.recyclerView);
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  2));
        mB.recyclerView.setAdapter(adapter);
        mB.refreshLayout.startRefresh();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(CloudApi.userCreditsProd, pagerNumber = 1);

            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPresenter.onRequest(CloudApi.userCreditsProd, pagerNumber += 1);
            }
        });

        CloudApi.userUserInfo()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject object) {
                        if (object.optInt("code") == Code.CODE_SUCCESS){
                            JSONObject data = object.optJSONObject("data");
                            if (data != null){
                                mB.tvTitle.setText(data.optString("credits"));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        IntegralFrg.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });
    }

    @Override
    public void setRefreshLayoutMode(int totalRow) {
        super.setRefreshLayoutMode(listBean.size(), totalRow, mB.refreshLayout);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        super.setRefreshLayout(pagerNumber, mB.refreshLayout);
    }

    @Override
    public void setData(Object data) {
        List<DataBean> list = (List<DataBean>) data;
        if (pagerNumber == 1) {
            listBean.clear();
            mB.refreshLayout.finishRefreshing();
        } else {
            mB.refreshLayout.finishLoadmore();
        }
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_forward:
                UIHelper.startHtmlAct(null, getString(R.string.integral_rule));
                break;
        }
    }
}
