package com.fanwang.sg.presenter;

import android.os.Handler;

import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.PopupWindowTool;
import com.fanwang.sg.view.impl.AddressContract;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/13.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class AddressPresenter extends AddressContract.Presenter{

    @Override
    public void onRequest(int pagerNumber) {
        CloudApi.userAddressList()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<List<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<List<DataBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            List<DataBean> list = baseResponseBeanResponse.body().data;
                            if (list != null){
                                mView.setData(list);
                            }
                        }else {
//                            mView.showLoadEmpty();
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

    @Override
    public void onDefaultAddress(final int position, final String id) {
        CloudApi.userSetDefaultAddress(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
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
                            mView.onDefaultAddressSuccess(position, id);
                        }
                        showToast(baseResponseBeanResponse.body().desc);
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

    @Override
    public void onDelete(final int position, final String id) {
        PopupWindowTool.showDialog(act, PopupWindowTool.delete_address, new PopupWindowTool.DialogListener() {
            @Override
            public void onClick() {
                CloudApi.userDelAddress(id)
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                mView.showLoading();
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
                                    mView.onDeleteSuccess(position, id);
                                }
                                showToast(baseResponseBeanResponse.body().desc);
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
        });

    }
}
