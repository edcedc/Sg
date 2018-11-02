package com.fanwang.sg.presenter;

import com.fanwang.sg.bean.BaseListBean;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.event.RefreshOrderInEvent;
import com.fanwang.sg.utils.PopupWindowTool;
import com.fanwang.sg.view.impl.OrderChildContract;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/4.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class OrderChildPresenter extends OrderChildContract.Presenter{

    @Override
    public void onRequest(int pagerNumber, int type) {
        CloudApi.orderDetailSpellGroupSuccess(pagerNumber, type)
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
                                    mView.hideLoading();
                                }else {
                                    mView.showLoadEmpty();
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

                    }
                });
    }

    @Override
    public void orderConfirmReceipt(final String id) {
        PopupWindowTool.showDialog(act, PopupWindowTool.order_confirmReceipt, new PopupWindowTool.DialogListener() {
            @Override
            public void onClick() {
                CloudApi.orderConfirmReceipt(id)
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
                                    EventBus.getDefault().post(new RefreshOrderInEvent());
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

    @Override
    public void pay(final String id, final int payType) {
        CloudApi.orderPayment(id, payType)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject object) {
                        int code = object.optInt("code");
                        if (code == Code.CODE_SUCCESS){
                            switch (payType){
                                case 0:
                                case 1:
                                    mView.payType(payType, object.optString("data"));
                                    break;
                                case 2:
                                    EventBus.getDefault().post(new RefreshOrderInEvent());
                                    break;
                            }
                        }
                        showToast(object.optString("desc"));
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
