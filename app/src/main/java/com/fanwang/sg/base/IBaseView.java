package com.fanwang.sg.base;

import com.hannesdorfmann.mosby.mvp.MvpView;

import io.reactivex.disposables.Disposable;

/**
 * Created by edison on 2018/4/8.
 */

public interface IBaseView extends MvpView{

    void showLoading();

    void hideLoading();

    void onError(Throwable e);

    void addDisposable(Disposable d);

    void showLoadDataing();

    void showLoadEmpty();

}
