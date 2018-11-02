package com.fanwang.sg.view;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.OrderDetailsCollageAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.databinding.FCollageSuccessBinding;
import com.fanwang.sg.view.act.ConfirmationOrderAct;
import com.fanwang.sg.view.act.GoodsDetailsAct;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/28.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  拼团成功
 */

public class CollageSuccessFrg extends BaseFragment<BasePresenter, FCollageSuccessBinding>{

    private String id;
    private List<DataBean> listCollageBean = new ArrayList<>();
    private OrderDetailsCollageAdapter collageAdapter;

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {
        id = bundle.getString("id");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_collage_success;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.successful_collage));
        if (collageAdapter == null){
            collageAdapter = new OrderDetailsCollageAdapter(act, listCollageBean);
        }
        mB.gvCollage.setAdapter(collageAdapter);
        mB.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.finish();
            }
        });

        CloudApi.orderDetailGroupBookingSuccess(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<List<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<List<DataBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){

                            List<DataBean> listUser = baseResponseBeanResponse.body().data;
                            int collageNum = 3;
                            int collageNums = collageNum - listUser.size();
                            if (collageNums > 3 && listUser.size() < 3){
                                listCollageBean.addAll(listUser);
                                for (int i = 0;i < 3 - listUser.size();i++){
                                    listCollageBean.add(new DataBean());
                                }
                            }else if (collageNum > 3 && listUser.size() > 3){
                                for (int i = 0;i < 3;i++){
                                    listCollageBean.addAll(listUser);
                                }
                            }else if (collageNum < 3 && listUser.size() < collageNum){
                                listCollageBean.addAll(listUser);
                                for (int i = 0;i < (collageNum - listUser.size());i++){
                                    listCollageBean.add(new DataBean());
                                }
                            }else {
                                listCollageBean.addAll(listUser);
                            }
                            collageAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        CollageSuccessFrg.this.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        hideLoading();
                    }
                });
    }

    @Override
    public boolean onBackPressedSupport() {
        ActivityUtils.finishActivity(GoodsDetailsAct.class);
        ActivityUtils.finishActivity(ConfirmationOrderAct.class);
        return super.onBackPressedSupport();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
