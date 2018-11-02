package com.fanwang.sg.presenter;

import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.view.impl.LogisticsContract;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/10/17.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class LogisticsPresenter extends LogisticsContract.Presenter{

    @Override
    public void onRequest(String id) {
        CloudApi.ordershowExpressInfo(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoadDataing();
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
                            String data1 = object.optString("data");
                            JSONObject data = null;
                            try {
                                data = new JSONObject(data1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (data != null){
                                boolean success = data.optBoolean("Success");
                                if (success){
                                    mView.hideLoading();
                                    JSONArray traces = data.optJSONArray("Traces");
                                    if (traces != null && traces.length() != 0){
                                        List<DataBean> list = new ArrayList<>();
                                        for (int i = 0;i < traces.length();i++){
                                            JSONObject object1 = traces.optJSONObject(i);
                                            DataBean bean = new DataBean();
                                            bean.setCreate_time(object1.optString("AcceptTime"));
                                            bean.setContent(object1.optString("AcceptStation"));
                                            list.add(bean);
                                        }
                                        mView.setData(list, data.optString("ShipperCode"), data.optString("LogisticCode"), data.optString("image"));
                                    }
                                }else {
                                    mView.showLoadEmpty();
                                }
                                showToast(data.optString("Reason"));
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

}
