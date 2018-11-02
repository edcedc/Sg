package com.fanwang.sg.presenter;

import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.event.RongcloudInEvent;
import com.fanwang.sg.utils.cache.ShareSessionIdCache;
import com.fanwang.sg.view.act.MainActivity;
import com.fanwang.sg.view.impl.BindPhoneContract;
import com.flyco.roundview.RoundTextView;
import com.flyco.roundview.RoundViewDelegate;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/3.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class BindPhonePresenter extends BindPhoneContract.Presenter {

    @Override
    public void bind(int mType, String phone, String code) {
        if (StringUtils.isEmpty(phone)) {
            ToastUtils.showShort(act.getString(R.string.please_phone));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            ToastUtils.showShort(act.getString(R.string.error_format));
            return;
        }
        if (StringUtils.isEmpty(code)) {
            ToastUtils.showShort(act.getString(R.string.please_code));
            return;
        }
        CloudApi.userLogin(phone, code, CloudApi.userBindingobile)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JSONObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(JSONObject object) {
                        if (object.optInt("code") == Code.CODE_SUCCESS){
                            EventBus.getDefault().post(new RongcloudInEvent());
                            User.getInstance().setLogin(true);
                            ActivityUtils.startActivity(MainActivity.class);
                            ActivityUtils.finishAllActivities();
                        }else {
                            showToast(object.optString("desc"));
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
    public void confirmState(final EditText etPhone, final EditText etPwd, final RoundTextView tvConfirm) {
        final RoundViewDelegate delegate = tvConfirm.getDelegate();
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String code = etPwd.getText().toString().trim();
                if (!StringUtils.isEmpty(code) && code.length() != 0 && editable.length() != 0) {
                    delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.reb_FE2701));
                    delegate.setBackgroundPressColor(ContextCompat.getColor(act,R.color.reb_C91E00));
                    tvConfirm.setEnabled(true);
                } else {
                    delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.black_E5E5E5));
                    delegate.setBackgroundPressColor(ContextCompat.getColor(act,R.color.black_E5E5E5));
                    tvConfirm.setEnabled(false);
                }
            }
        });
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String phone = etPhone.getText().toString().trim();
                if (!StringUtils.isEmpty(phone) && phone.length() != 0 && editable.length() != 0) {
                    delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.reb_FE2701));
                    delegate.setBackgroundPressColor(ContextCompat.getColor(act,R.color.reb_C91E00));
                    tvConfirm.setEnabled(true);
                } else {
                    delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.black_E5E5E5));
                    delegate.setBackgroundPressColor(ContextCompat.getColor(act,R.color.black_E5E5E5));
                    tvConfirm.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void code(int mType, String phone) {
        if (StringUtils.isEmpty(phone)) {
            ToastUtils.showShort(act.getString(R.string.please_phone));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            ToastUtils.showShort(act.getString(R.string.error_format));
            return;
        }
        CloudApi.userGetCode(phone, CloudApi.userGetBindingCode)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
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
                            mView.codeSuccess();
                        }else {
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
        mView.codeSuccess();
    }

}
