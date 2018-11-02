package com.fanwang.sg.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.PopupWindowTool;
import com.fanwang.sg.view.impl.ForwardContract;
import com.lzy.okgo.model.Response;
import com.zaaach.toprightmenu.MenuItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ForwardPresenter extends ForwardContract.Presenter{

    @Override
    public void onRequest() {
        CloudApi.bankcardCardList()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
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
                            List<DataBean> data = baseResponseBeanResponse.body().data;
                            if (data != null && data.size() != 0){
                                List<MenuItem> list = new ArrayList<>();
                                for (DataBean bean : data){
                                    MenuItem item = new MenuItem();
                                    item.setText(bean.getBankName());
                                    item.setId(bean.getId());
                                    list.add(item);
                                }
                                mView.setData(list);
                                mView.hideLoading();
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

    @Override
    public void confirm(final String trim, final String id, final String withDrawMoney) {
        if (StringUtils.isEmpty(trim)){
            showToast(act.getString(R.string.error_forward_null));
            return;
        }
        double withMoney = Double.valueOf(withDrawMoney);
        double money = Double.valueOf(trim);
        if (money > withMoney){
            showToast(act.getString(R.string.error_forward_null2));
            return;
        }

        if (StringUtils.isEmpty(id))return;

        PopupWindowTool.showDialog(act, PopupWindowTool.dialog_forward, new PopupWindowTool.DialogListener() {
            @Override
            public void onClick() {
                CloudApi.userWithDrawMoney(trim, withDrawMoney, id)
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
                                    act.onBackPressed();
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
        });
    }

    @Override
    public void onWithDraw() {
        CloudApi.userWithDraw()
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
                            DataBean data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                mView.onWithDrawMoneySuccess(data);
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
