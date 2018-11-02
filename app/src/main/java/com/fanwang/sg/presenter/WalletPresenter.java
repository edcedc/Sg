package com.fanwang.sg.presenter;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.MeAdapter;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.view.impl.WalletContract;
import com.fanwang.sg.weight.LinearDividerItemDecoration;

import org.json.JSONObject;

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

public class WalletPresenter extends WalletContract.Presenter{

    private String[] str = {"我的分销", "管理银行卡", "提现记录", "明细"};

    @Override
    public void listview(RecyclerView recyclerView, List<DataBean> listBean, MeAdapter adapter) {
        for (int i = 0; i < str.length; i++) {
            DataBean bean = new DataBean();
            bean.setName(str[i]);
            listBean.add(bean);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(act));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  10));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void userInto() {
        CloudApi.userUserInfo()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

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
                            JSONObject data = object.optJSONObject("data");
                            if (data != null){
                               mView.onUserInfo(data.optDouble("balance"));
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
