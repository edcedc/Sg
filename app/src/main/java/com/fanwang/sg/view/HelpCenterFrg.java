package com.fanwang.sg.view;

import android.os.Bundle;
import android.view.View;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.databinding.FHelpCenterBinding;
import com.fanwang.sg.view.act.HtmlAct;
import com.lzy.okgo.model.Response;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/19.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  帮助中心
 */

public class HelpCenterFrg extends BaseFragment<BasePresenter, FHelpCenterBinding>{

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_help_center;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.help_center));

        CloudApi.appHelpCenter()
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

                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            DataBean data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                mB.tvPhone.setText("固话：" + data.getPhone());
                                mB.tvMail.setText("邮箱：" + data.getEmail());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        HelpCenterFrg.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });
    }
}
