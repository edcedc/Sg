package com.fanwang.sg.view;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FUpdatePwdBinding;
import com.lzy.okgo.model.Response;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/6.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  更改密码
 */

public class UpdatePwdFrg extends BaseFragment<BasePresenter, FUpdatePwdBinding>{

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_update_pwd;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.update_pwd));

        mB.refreshLayout.setPureScrollModeOn();
        mB.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old = mB.etOldPwd.getText().toString();
                String pwd1 = mB.etNewPwd.getText().toString();
                String pwd2 = mB.etNewPwd2.getText().toString();
                if (StringUtils.isEmpty(old)){
                    mB.etOldPwd.setError(getString(R.string.error_pwd));
                    return;
                }
                if (StringUtils.isEmpty(pwd1)){
                    mB.etNewPwd.setError(getString(R.string.error_pwd2));
                    return;
                }
                if (StringUtils.isEmpty(pwd2)){
                    mB.etNewPwd2.setError(getString(R.string.error_pwd3));
                    return;
                }
                if (!pwd1.equals(pwd2)){
                    showToast(getString(R.string.error_pwd4));
                    return;
                }
                /*CloudApi.userUpdatePassword(old, pwd1)
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoading();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Response<BaseResponseBean>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                addDisposable(d);
                            }

                            @Override
                            public void onNext(Response<BaseResponseBean> baseResponseBeanResponse) {
                                if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                                    User.getInstance().setUserObj(null);
                                    User.getInstance().setLogin(false);
                                    ActivityUtils.finishAllActivities();
                                }
                                ToastUtils.showShort(baseResponseBeanResponse.body().desc);
                            }

                            @Override
                            public void onError(Throwable e) {
                                ModifyPasswordFrg.super.onError(e);
                            }

                            @Override
                            public void onComplete() {
                                hideLoading();
                            }
                        });*/
            }
        });
    }
}
