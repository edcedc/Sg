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
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.utils.cache.ShareSessionIdCache;
import com.fanwang.sg.view.act.MainActivity;
import com.fanwang.sg.view.impl.LoginContract;
import com.flyco.roundview.RoundTextView;
import com.flyco.roundview.RoundViewDelegate;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/8/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class LoginPresenter extends LoginContract.Presenter {

    @Override
    public void login(String phone, String code) {
        if (StringUtils.isEmpty(phone)){
            showToast(act.getString(R.string.please_phone));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showToast(act.getString(R.string.error_format));
            return;
        }
        if (StringUtils.isEmpty(code)){
            showToast(act.getString(R.string.please_code));
            return;
        }
        CloudApi.userLogin(phone, code, CloudApi.userLogin)
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
                            User.getInstance().setLogin(true);
                            JSONObject data = object.optJSONObject("data");
                            if (data != null){
                                User.getInstance().setUserObj(data);
                                ShareSessionIdCache.getInstance(Utils.getApp()).save(data.optString("sessionId"));
                                mView.loginSuccess();
                            }
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
                if (!StringUtils.isEmpty(code) && code.length() != 0 && editable.length() != 0){
                    delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.reb_FE2701));
                    delegate.setBackgroundPressColor(ContextCompat.getColor(act,R.color.reb_C91E00));
                    tvConfirm.setEnabled(true);
                }else {
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
                if (!StringUtils.isEmpty(phone) && phone.length() != 0 && editable.length() != 0){
                    delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.reb_FE2701));
                    delegate.setBackgroundPressColor(ContextCompat.getColor(act,R.color.reb_C91E00));
                    tvConfirm.setEnabled(true);
                }else {
                    delegate.setBackgroundColor(ContextCompat.getColor(act,R.color.black_E5E5E5));
                    delegate.setBackgroundPressColor(ContextCompat.getColor(act,R.color.black_E5E5E5));
                    tvConfirm.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void code(String phone) {
        if (StringUtils.isEmpty(phone)){
            showToast(act.getString(R.string.please_phone));
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showToast(act.getString(R.string.error_format));
            return;
        }
        CloudApi.userGetCode(phone, CloudApi.userGetCode)
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
    }

    @Override
    public void wxLogin(final BaseFragment root, String wx_openid, String wx_token) {
        CloudApi.wxLogin(wx_openid, wx_token)
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
                    public void onNext(JSONObject jsonObject) {
                        CloudApi.userWeChatId(jsonObject.optString("headimgurl"), jsonObject.optString("nickname"),
                                jsonObject.optString("openid"), jsonObject.optInt("sex"))
                                .doOnSubscribe(new Consumer<Disposable>() {
                                    @Override
                                    public void accept(Disposable disposable) throws Exception {

                                    }
                                })
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<JSONObject>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        mView.addDisposable(d);
                                    }

                                    @Override
                                    public void onNext(JSONObject jsonObject) {
                                        if (jsonObject.optInt("code") == Code.CODE_SUCCESS){
                                            JSONObject data = jsonObject.optJSONObject("data");
                                            if (data.optInt("code") == Code.CODE_SUCCESS){
                                                JSONObject obj = data.optJSONObject("data");
                                                if (obj != null){
                                                    String mobile = obj.optString("mobile");
                                                    ShareSessionIdCache.getInstance(Utils.getApp()).save(obj.optString("sessionId"));
                                                    if (mobile.equals("null")){
                                                        UIHelper.startBindPhoneFrg(root, 0);
                                                        ToastUtils.showShort("未绑定手机号码");
                                                    }else {
                                                        ToastUtils.showShort(obj.optString("desc"));
                                                        mView.loginSuccess();
                                                    }
                                                }
                                            }
                                        }else {
                                            ToastUtils.showShort(jsonObject.optString("desc"));
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
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {
//                        listener.hideLoading();
                    }
                });
    }


}
