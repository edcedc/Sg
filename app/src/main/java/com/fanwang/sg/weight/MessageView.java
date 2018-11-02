package com.fanwang.sg.weight;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.lzy.okgo.model.Response;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/13.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class MessageView extends FrameLayout{

    public MessageView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MessageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MessageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MessageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private TextView tvMsgNumber;
    private BaseFragment root;

    private void init(Context context){
        View inflate = LayoutInflater.from(context).inflate(R.layout.include_msg, this, true);
        tvMsgNumber = findViewById(R.id.tv_msg_number);
        inflate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startMessageFrg(root);
                tvMsgNumber.setText("0");
                tvMsgNumber.setVisibility(GONE);
            }
        });


    }

    public void setMessageNum(){
        if (User.getInstance().isLogin()){
            CloudApi.userUnread()
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {

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
                                    int num = data.getNum();
                                    if (num != 0){
                                        tvMsgNumber.setVisibility(VISIBLE);
                                        tvMsgNumber.setText(num + "");
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    public void initMsgView(BaseFragment fragment){
        this.root = fragment;
    }

    public void setCircleNumber(int number){
        if (number <= 0){
            tvMsgNumber.setVisibility(GONE);
        }else {
            tvMsgNumber.setVisibility(VISIBLE);
            tvMsgNumber.setText(number + "");
        }
    }

}
