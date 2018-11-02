package com.fanwang.sg.presenter;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.event.CartInEvent;
import com.fanwang.sg.view.act.ConfirmationOrderAct;
import com.fanwang.sg.view.act.GoodsDetailsAct;
import com.fanwang.sg.view.bottomFrg.PayBottomFrg;
import com.fanwang.sg.view.impl.ConfirmationOrderContract;
import com.lzy.okgo.model.Response;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
/**
 * 作者：yc on 2018/9/26.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class ConfirmationOrderPresenter extends ConfirmationOrderContract.Presenter {

    private PayBottomFrg payBottomFrg;

    @Override
    public void onAddressList() {
        CloudApi.userAddressList()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<List<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<List<DataBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS) {
                            List<DataBean> list = baseResponseBeanResponse.body().data;
                            if (list != null && list.size() != 0) {
                                mView.setData(list);
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

    private BaseFragment root;

    public void onConfirm(final int flag, int type, List<DataBean> listBean, final String addressId, BaseFragment root, final String id, final String orderNo, int isCredited) {
        this.root = root;
        if (StringUtils.isEmpty(addressId)) {
            showToast(act.getString(R.string.error_address));
            return;
        }
        if (payBottomFrg == null) {
            payBottomFrg = new PayBottomFrg();
        }
        switch (type) {//0普通商品1秒杀商品2拼团商品   傻逼后台返回类型跟上传类型不一致 脑残
            case 1001:
                type = 0;
                break;
            case 1002:
                type = 1;
                break;
            case 1003:
                type = 2;
                break;
        }
        StringBuilder sbId = new StringBuilder();
        final JSONArray array = new JSONArray();
        for (DataBean bean : listBean) {
            List<DataBean> prod = bean.getProd();
            for (DataBean bean1 : prod) {
                sbId.append(bean1.getId()).append(",");
                JSONObject object = new JSONObject();
                try {
                    object.put("sku", bean1.getSkuId());
                    object.put("num", bean1.getNum());
                    array.put(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        String cartId = sbId.substring(0, sbId.length() - 1);
        final int finalType = type;
        orderSubmitOrder(addressId, -1, array.toString(), finalType, flag, -1, orderNo, cartId, isCredited);
    }

    private int mFlag;//状态重复
    private void orderSubmitOrder(String addressId, final int payment, String sku_num,
                                  final int type, int flag, int coj, String orderNo, final String id, final int isCredited) {
        {
            mFlag = flag;
            if (flag == 2){
                flag = 1;
            }
            CloudApi.orderSubmitOrder(addressId, payment, sku_num, type, flag, coj, orderNo, id, isCredited)
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
                        public void onNext(final JSONObject object) {
                            if (object.optInt("code") == Code.CODE_SUCCESS) {
                                /*int payType;
                                switch (payment){
                                    case 1001:
                                        payType = 2;
                                        break;
                                    case 1002:
                                        payType = 1;
                                        break;
                                    default:
                                        payType = 0;
                                        break;
                                }*/
                                setSubmitOrderState(true);
                                if (payBottomFrg != null && !payBottomFrg.isShowing()) {
                                    payBottomFrg.show(root.getChildFragmentManager(), "dialog");
                                }
                                payBottomFrg.setOnClickListener(new PayBottomFrg.OnClickListener() {
                                    @Override
                                    public void onClick(int payType) {
                                        if (payType <= 1 && isCredited == 1){
                                            showToast(act.getString(R.string.integral_balance));
                                            return;
                                        }
                                        pay(root, type, mFlag, object.optString("data"), payType);
                                    }

                                });
                            }
                            showToast(object.optString("desc"));
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


    private void pay(final BaseFragment root, final int type, final int flag, final String id, final int payType){
        CloudApi.orderPayment(id, payType)
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
                            mView.setPayType(payType, type, mFlag, object.optInt("code"), object.optString("data"));
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

}
