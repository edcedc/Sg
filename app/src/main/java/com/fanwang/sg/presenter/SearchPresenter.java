package com.fanwang.sg.presenter;

import android.os.Handler;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.bean.ProductSearchProdBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.view.impl.SearchContract;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lzy.okgo.model.Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/1.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class SearchPresenter extends SearchContract.Presenter{

    @Override
    public void onRequest(String trim, int pageNumber, final TwinklingRefreshLayout refreshLayout) {
        if (StringUtils.isEmpty(trim)){
            ToastUtils.showShort(act.getString(R.string.error_search_null));
            return;
        }
        refreshLayout.startRefresh();
        CloudApi.productSearchProd(trim)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<List<ProductSearchProdBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<List<ProductSearchProdBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            List<ProductSearchProdBean> data = baseResponseBeanResponse.body().data;
                            List<DataBean> listBean = new ArrayList<>();
                            if (data != null && data.size() != 0){
                                for (ProductSearchProdBean prodBean : data){
                                    DataBean bean = new DataBean();
                                    bean.setName(prodBean.getName());
                                    bean.setMarketPrice(new BigDecimal(prodBean.getMarketPrice()));
                                    bean.setRealPrice(new BigDecimal(prodBean.getRealPrice()));
                                    bean.setId(prodBean.getId());
                                    bean.setZhekou(prodBean.getZhekou() + "");
                                    bean.setImage(prodBean.getImage());
                                    bean.setType(1);
                                    listBean.add(bean);
                                }
                                mView.hideLoading();
                                mView.setData(listBean);
                            }
                        }else {
                            mView.showLoadEmpty();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {
//                        mView.hideLoading();
                    }
                });
    }

    @Override
    public void onAefaultRequest(int pagerNumber) {
        CloudApi.list2(CloudApi.productProdRecommend)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<BaseResponseBean<List<DataBean>>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mView.addDisposable(d);
                    }

                    @Override
                    public void onNext(Response<BaseResponseBean<List<DataBean>>> baseResponseBeanResponse) {
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS){
                            List<DataBean> data = baseResponseBeanResponse.body().data;
                            if (data != null && data.size() != 0){
                                mView.hideLoading();
                                mView.setAefaultData(data);
                            }
                        }else {
                            mView.showLoadEmpty();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {
//                        mView.hideLoading();
                    }
                });
    }

}
