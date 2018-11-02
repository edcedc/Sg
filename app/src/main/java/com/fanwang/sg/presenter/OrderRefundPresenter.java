package com.fanwang.sg.presenter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.event.RefreshOrderInEvent;
import com.fanwang.sg.utils.Constants;
import com.fanwang.sg.view.impl.OrderRefundContract;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/10/15.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class OrderRefundPresenter extends OrderRefundContract.Presenter{

    @Override
    public void confirm(String id, int type, List<LocalMedia> localMediaList, int cargoType, int state, String content, String orderNumber, String logistics, String price, List<DataBean> listOrderDetails) {
        if (StringUtils.isEmpty(id))return;
        if (StringUtils.isEmpty(content)){
            showToast(act.getString(R.string.error_reason_null));
            return;
        }
        JSONArray array = new JSONArray();
        for (DataBean bean : listOrderDetails){
            if (bean.isSelect()){
                JSONObject object = new JSONObject();
                object.put("orderDetailsId", bean.getId());
                object.put("num", bean.getNum());
                array.add(object);
            }
        }
        switch (type){
            case Constants.return_paragraph:
                orderRefund(id, localMediaList, content);
                break;
            case Constants.return_goods:
                if (StringUtils.isEmpty(price)){
                    showToast("退款金额不能为空");
                    return;
                }
                orderSalesReturn(id, localMediaList, content, array.toJSONString(), type, cargoType, price, orderNumber);
                break;
            case Constants.refund:
                if (StringUtils.isEmpty(logistics) || StringUtils.isEmpty(orderNumber)){
                    showToast(act.getString(R.string.error_collection));
                    return;
                }
                orderSalesReturn(id, localMediaList, content, array.toJSONString(), type, cargoType, price, orderNumber);
                break;
        }

    }

    private void orderSalesReturn(final String orderId, List<LocalMedia> localMediaList, String content, String jsonOrderDetails, int state,
                                  int cargoType, String refundAmount, String logisticsOrder){
        CloudApi.orderSalesReturn(orderId, localMediaList, content, jsonOrderDetails, state, cargoType, refundAmount, logisticsOrder)
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
                            EventBus.getDefault().post(new RefreshOrderInEvent(1));
//                            act.onBackPressed();
                            UIHelper.startOrderDetailsAct(orderId);
                            act.finish();
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

    private void orderRefund(String id, List<LocalMedia> localMediaList, String content){
        CloudApi.orderrefund(id, content, localMediaList)
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
                            EventBus.getDefault().post(new RefreshOrderInEvent(1));
                            act.onBackPressed();
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

}
