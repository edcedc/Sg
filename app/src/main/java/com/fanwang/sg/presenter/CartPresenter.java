package com.fanwang.sg.presenter;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.PopupWindowTool;
import com.fanwang.sg.view.impl.CartContract;
import com.lzy.okgo.model.Response;

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
 * 作者：yc on 2018/8/29.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class CartPresenter extends CartContract.Presenter {
    @Override
    public void onRequest(int pagerNumber) {
        CloudApi.cartCartList()
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
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS) {
                            List<DataBean> data = baseResponseBeanResponse.body().data;
                            if (data != null && data.size() != 0) {
                                /*for (DataBean bean : data) {
                                    List<DataBean> prod = bean.getProd();
                                    for (DataBean cartBean : prod){
                                        String specificationValues = cartBean.getSpecificationValues();
                                        if (!StringUtils.isEmpty(specificationValues)){
                                            try {
                                                JSONArray array = new JSONArray(specificationValues);
                                                if (array != null && array.length() != 0) {
                                                    StringBuilder sb = new StringBuilder();
                                                    for (int i = 0; i < array.length(); i++) {
                                                        JSONObject object = array.optJSONObject(i);
                                                        sb.append(object.optString("value")).append(",");
                                                    }
                                                    String values = sb.deleteCharAt(sb.length() - 1).toString();
//                                                    cartBean.setSpecificationValues(values);
                                                }
                                            } catch (JSONException e) {
                                                LogUtils.e(e.getMessage());
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }*/
                                mView.setData(data);
                            }
                        }else {
                            mView.onCartListNull();
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
    public void onCartNum(final int groupPosition, final int childPosition, final String skuid, final int num, String id) {
        CloudApi.cartModifyCartNum(num, skuid, id)
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
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS) {
                            mView.onCartNumSuccess(groupPosition, childPosition, skuid, num);
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

    @Override
    public void onMovecollect(List<DataBean> listBean) {
        String allId = getAllId(listBean);
        if (StringUtils.isEmpty(allId)) return;
        CloudApi.cartMvecollect(1002, allId)
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
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS) {

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

    @Override
    public void onDelCart(final List<DataBean> listBean) {
        if (listBean.size() == 0) {
            return;
        }
        final List<DataBean> groups = new ArrayList<>();
        for (DataBean bean : listBean) {
            List<DataBean> prod = bean.getProd();
            for (DataBean bean1 : prod) {
                if (bean1.isSelect()) {
                    groups.add(bean1);
                }
            }
            List<DataBean> crarChild = bean.getProd();
            for (int j = 0; j < crarChild.size(); j++) {
                DataBean cartBean = crarChild.get(j);
                if (cartBean.isSelect()) {
                    crarChild.remove(j);
                }
            }
        }
        if (groups.size() == 0) return;
        PopupWindowTool.showDialog(act, PopupWindowTool.dialog_dalete, new PopupWindowTool.DialogListener() {
            @Override
            public void onClick() {
                StringBuilder sb = new StringBuilder();
                for (DataBean bean : groups) {
                    if (bean.isSelect()) {
                        sb.append(bean.getId()).append(",");
                    }
                }
                if (sb.length() == 0) return;
                sb = sb.deleteCharAt(sb.length() - 1);
                CloudApi.cartDelCart(sb.toString())
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
                                if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS) {
                                    mView.onDelCartSuccess(groups);
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
        });


    }

    @Override
    public void allSku(final String pid, final String image, final String choice, final double realPrice, final int groupPosition, final int childPosition, final String id) {
        CloudApi.productAllSku(pid)
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
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS) {
                            DataBean data = baseResponseBeanResponse.body().data;
                            if (data != null) {
                                String specificationItems = data.getSpecificationItems();
                                try {
                                    JSONArray array = new JSONArray(specificationItems);
                                    if (array != null && array.length() != 0) {
                                        List<DataBean> listBean = new ArrayList<>();
                                        StringBuilder sb = new StringBuilder();
                                        int mStock = 0;
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject object = array.optJSONObject(i);
                                            DataBean bean = new DataBean();
                                            bean.setName(object.optString("name"));
                                            sb.append(bean.getName()).append("、");
                                            JSONArray entries = object.optJSONArray("entries");
                                            if (entries != null && entries.length() != 0) {
                                                List<DataBean> entries1 = bean.getEntries();

                                                for (int j = 0; j < entries.length(); j++) {
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
                                        mView.onAllSku(listBean, substring, mStock, image, choice, realPrice, pid, groupPosition, childPosition, id);
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
    public void cartModifyCartSku(String ids, String pid, final String price, final String className, final int groupPosition, final int childPosition, String id) {
        CloudApi.cartModifyCartSku(id, ids)
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
                        if (baseResponseBeanResponse.body().code == Code.CODE_SUCCESS) {
                            mView.onCartModifyCartSkuSuccess(price, className,  groupPosition,  childPosition);
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

    private String getAllId(List<DataBean> listBean) {
        if (listBean.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (DataBean bean : listBean) {
            List<DataBean> crarChild = bean.getProd();
            for (DataBean bean1 : crarChild) {
                if (bean1.isSelect()) {
                    sb.append(bean1.getId()).append(",");
                }
            }
        }
        if (sb.length() != 0) {
            sb = sb.deleteCharAt(sb.length() - 1);
        } else {
            showToast(act.getString(R.string.error_collection));
            return null;
        }
        return sb.toString();
    }

}
