package com.fanwang.sg.base;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;

/**
 * 作者：yc on 2018/8/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public abstract class BasePresenter<T> {

    public Activity act;

    public T mView;

    public void init(T v) {
        this.mView = v;
    }


    protected void showToast(String s){
        ToastUtils.showShort(s);
    }

}
