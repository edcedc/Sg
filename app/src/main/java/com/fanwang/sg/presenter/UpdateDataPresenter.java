package com.fanwang.sg.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.view.impl.UpdateDataContract;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
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

public class UpdateDataPresenter extends UpdateDataContract.Presenter{

    @Override
    public void updateHead(final String path) {
        update(path, null, 0);
    }

    @Override
    public void updateSex(int sex) {
        update(null, null, sex);
    }

    private void update(final String path, final String nick, final int sex){
        CloudApi.userModifyUserInfo(path, nick, sex)
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
                        if (object.optInt("code") == Code.CODE_SUCCESS){
                            JSONObject userInfoObj = User.getInstance().getUserInfoObj();
                            try {
                                if (!StringUtils.isEmpty(path)){
                                    mView.onUpdateHeadSuccess(path);
                                    userInfoObj.put("head", object.optString("data"));
                                }
                                if (!StringUtils.isEmpty(nick)){
                                    userInfoObj.put("nickname", nick);
                                }
                                if (sex != 0){
                                    userInfoObj.put("sex", sex);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

}
