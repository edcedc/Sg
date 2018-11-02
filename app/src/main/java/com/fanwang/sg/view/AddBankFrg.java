package com.fanwang.sg.view;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.databinding.FAddBankBinding;
import com.fanwang.sg.event.AddBankInEvent;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/6.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  添加银行卡
 */

public class AddBankFrg extends BaseFragment<BasePresenter, FAddBankBinding>{

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_add_bank;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.add_bank2));
        mB.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mB.etName.getText().toString().trim();
                String number = mB.etNumber.getText().toString().trim();
                String bankName = mB.etBankName.getText().toString().trim();
                String branchName = mB.etBranchName.getText().toString().trim();
                if (StringUtils.isEmpty(name) || StringUtils.isEmpty(number) || StringUtils.isEmpty(bankName) || StringUtils.isEmpty(branchName)){
                    showToast(getString(R.string.error_));
                    return;
                }
                CloudApi.bankcardAddBankCard(name, number, bankName, branchName)
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoading();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }

                            @Override
                            public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                                if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                    DataBean data = baseResponseBeanResponse.body().data;
                                    if (data != null){
                                        EventBus.getDefault().post(new AddBankInEvent(data));
                                        act.onBackPressed();
                                    }
                                }
                                showToast(baseResponseBeanResponse.body().desc);
                            }

                            @Override
                            public void onError(Throwable e) {
                                AddBankFrg.this.onError(e);
                            }

                            @Override
                            public void onComplete() {
                                hideLoading();
                            }
                        });

            }
        });
    }
}
