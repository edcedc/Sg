package com.fanwang.sg.presenter;

import android.os.Handler;

import com.fanwang.sg.bean.BaseListBean;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.view.impl.ClassListContract;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/1.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ClassListpresenter extends ClassListContract.Presenter{
    @Override
    public void onRequest(int pagerNumber, String id) {
        CloudApi.categoryGetProductByCatTo(pagerNumber, id)
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
                    public void onNext(Response<BaseResponseBean<BaseListBean<DataBean>>> beanResponse) {
                        if (beanResponse.body().code == Code.CODE_SUCCESS){
                            BaseListBean<DataBean> data = beanResponse.body().data;
                            if (data != null){
                                List<DataBean> list = data.getList();
                                if (list != null && list.size() != 0){
                                    mView.hideLoading();
                                    mView.setData(list);
                                    mView.setRefreshLayoutMode(data.getTotalRow());
                                }else {
                                    mView.showLoadEmpty();
                                }
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
    }
}
