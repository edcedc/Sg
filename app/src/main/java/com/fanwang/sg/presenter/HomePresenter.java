package com.fanwang.sg.presenter;

import android.os.Handler;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.sg.bean.BaseListBean;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.rcloud.RCloudTool;
import com.fanwang.sg.view.impl.HomeContract;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/8/28.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class HomePresenter extends HomeContract.Presenter{

    @Override
    public void onRequest(int pagerNumber) {
        CloudApi.userMain()
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
                            if (null != data){
                                mView.setData(data);
                                mView.hideLoading();
                                RCloudTool.RC_APP_CUSTOMER = data.getRongyunid();
                            }
                        }else {
                            mView.showLoadEmpty();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {
//                        mView.hideLoading();
                    }
                });
    }

}
