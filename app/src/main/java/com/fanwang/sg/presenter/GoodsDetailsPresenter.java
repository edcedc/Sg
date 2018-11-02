package com.fanwang.sg.presenter;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.bean.ProductProductDetailBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.event.CartInEvent;
import com.fanwang.sg.view.impl.GoodsDetailsContract;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/12.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class GoodsDetailsPresenter extends GoodsDetailsContract.Presenter{

    @Override
    public void ajaxDetail(String id) {
        CloudApi.productProductDetail(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoadDataing();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<ProductProductDetailBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<ProductProductDetailBean>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            ProductProductDetailBean data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                mView.setData(data);
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

    @Override
    public void allSku(String id) {
        CloudApi.productAllSku(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            DataBean data = baseResponseBeanResponse.body().data;
                            if (data != null){
                                String specificationItems = data.getSpecificationItems();
                                try {
                                    JSONArray array = new JSONArray(specificationItems);
                                    if (array != null && array.length() != 0){
                                        List<DataBean> listBean = new ArrayList<>();
                                        StringBuilder sb = new StringBuilder();
                                        int mStock = 0;
                                        for (int i = 0;i < array.length();i++){
                                            JSONObject object = array.optJSONObject(i);
                                            DataBean bean = new DataBean();
                                            bean.setName(object.optString("name"));
                                            sb.append(bean.getName()).append("、");
                                            JSONArray entries = object.optJSONArray("entries");
                                            if (entries != null && entries.length() != 0){
                                                List<DataBean> entries1 = bean.getEntries();

                                                for (int j = 0;j < entries.length();j++){
                                                    JSONObject object1 = entries.optJSONObject(j);
                                                    DataBean bean1 = new DataBean();
                                                    bean1.setValue(object1.optString("value"));
                                                    bean1.setSelected(object1.optBoolean("isSelected"));
                                                    bean1.setId(object1.optString("id"));
                                                    bean1.setCost(object1.optInt("cost"));
                                                    int stock = object1.optInt("stock");
                                                    mStock += stock;
                                                    bean1.setStock(stock);
                                                    bean1.setRealPrice(new BigDecimal(object1.optDouble("realPrice")));
                                                    entries1.add(bean1);
                                                }
                                                bean.setEntries(entries1);
                                            }
                                            listBean.add(bean);
                                        }

                                        String substring = sb.substring(0, sb.length() - 1);
                                        mView.onAllSku(listBean, substring, mStock);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

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

    @Override
    public void onCollection(boolean collect, String id) {
        if (collect){
            collect = false;
        }else {
            collect = true;
        }
        final boolean finalCollect = collect;
        CloudApi.userCollect(id, 1002)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        mView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            mView.onCollectionSuccess(finalCollect);
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
    public void isCollect(boolean collect, TextView tvCollection) {
        if (collect){//是否收藏

            tvCollection.setCompoundDrawablesWithIntrinsicBounds( null,
                    ContextCompat.getDrawable(act,R.mipmap.home_icon_collection_selected) , null, null);
//            tvCollection.setText("取消收藏");
        }else {
            tvCollection.setCompoundDrawablesWithIntrinsicBounds(null,
                    ContextCompat.getDrawable(act,R.mipmap.home_icon_collection_default) , null, null);
//            tvCollection.setText("收藏");
        }
    }

    @Override
    public void onAddCart(int type, String id, String skuId, int goodsNumber, BaseFragment root, List<DataBean> listBean, int secKillState, int flag, int isCredited) {
        switch (type){
            case 1001://普通商品  加入购物车
                if (StringUtils.isEmpty(skuId)){
                    showToast(act.getString(R.string.error_stock));
                    return;
                }
                addCart(id, skuId, goodsNumber);
                break;
            case 1002:

                break;
            case 1003://拼团  单独购买
                onSubmitOrder(root, type, id, listBean, secKillState, flag, isCredited);
                break;
        }
    }

    @Override
    public void onSubmitOrder(BaseFragment root, int type, String id, List<DataBean> listBean, int secKillState, int flag, int isCredited) {
        /*switch (type){//预防后期改
            case 1001://普通商品
                UIHelper.startConfirmationOrderFrg(root, id, listBean, type, flag, null, 1);
                break;
            case 1002://抢购商品
                UIHelper.startConfirmationOrderFrg(root, id, listBean, type, flag, null, 1);
                break;
            case 1003://拼团商品
                UIHelper.startConfirmationOrderFrg(root, id, listBean, type, flag, null, 1);
                break;
        }*/
        UIHelper.startConfirmationOrderAct(id, listBean, type, flag, null, isCredited);
    }


    private void addCart(String id, String skuId, int number){
        CloudApi.userAddCart(id, skuId, number)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<DataBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<DataBean>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            EventBus.getDefault().post(new CartInEvent());
                            LogUtils.e("要去刷新购物车");
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
