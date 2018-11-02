package com.fanwang.sg.presenter;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.event.AddressInEvent;
import com.fanwang.sg.view.impl.EditAddressContract;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/13.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class EditAddressPresenter extends EditAddressContract.Presenter{

    @Override
    public void confirm(String name, String phone, String address, String detaileAddress, final int type, final String id, final int position, String shen, String shi, String qu) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(phone) || StringUtils.isEmpty(address) || StringUtils.isEmpty(detaileAddress)){
            showToast(act.getString(R.string.error_));return;
        }
        if (phone.length() != 11 || !RegexUtils.isMobileSimple(phone)){
            showToast(act.getString(R.string.error_3));return;
        }
        CloudApi.userAddAddress(name, phone, address, detaileAddress, id, shen, shi, qu)
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
                                EventBus.getDefault().post(new AddressInEvent(data, type, position));
                                act.onBackPressed();
                            }
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

}

