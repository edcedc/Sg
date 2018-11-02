package com.fanwang.sg.presenter;

import android.os.Handler;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.bean.BaseListBean;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.view.act.HtmlAct;
import com.fanwang.sg.view.impl.DiscountContract;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/8/31.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class DiscountPresenter extends DiscountContract.Presenter{
    @Override
    public void onRequest(int pagerNumber, String id) {
        CloudApi.activitySpecialAreaBanner(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            DataBean data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                mView.setBannerData(data);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        CloudApi.activitySpecialArea(pagerNumber, id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<BaseListBean<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<BaseListBean<DataBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            BaseListBean<DataBean> data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                List<DataBean> list = data.getList();
                                if (list != null && list.size() != 0){
                                    mView.setData(list);
                                }
                                mView.setRefreshLayoutMode(data.getTotalRow());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoading();
                    }
                });
    }
}
