package com.fanwang.sg.presenter;

import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.view.impl.PostersContract;
import com.lzy.okgo.model.Response;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/10/22.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class PostersPresenter extends PostersContract.Presenter{
    @Override
    public void onAppAboutUs() {
        CloudApi.appAboutUs()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoadDataing();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
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
                                mView.appAboutUsSucces(data);
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
