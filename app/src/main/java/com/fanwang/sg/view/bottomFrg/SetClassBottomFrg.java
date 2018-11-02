package com.fanwang.sg.view.bottomFrg;

import android.annotation.SuppressLint;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.SetClassAdapter;
import com.fanwang.sg.base.BaseBottomSheetFrag;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.weight.AddNumberView;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/11.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

@SuppressLint("ValidFragment")
public class SetClassBottomFrg extends BaseBottomSheetFrag implements View.OnClickListener {

    private List<DataBean> listBean = new ArrayList<>();
    private SetClassAdapter adapter;
    private String image;
    private String choice;
    private String price;
    private TextView tvStock;
    private TextView tvPrice;
    private AddNumberView tvAddNumber;
    private String id;
    private String skuId;
    private TextView tvSet;

    public SetClassBottomFrg(List<DataBean> listBean, String image, String choice, String price, String id) {
        this.listBean = listBean;
        this.image = image;
        this.choice = choice;
        this.price = price;
        this.id = id;
    }

    @Override
    public int bindLayout() {
        return R.layout.p_set_class;
    }

    @Override
    public void initView(View view) {
        ImageView ivImg = view.findViewById(R.id.iv_img);
        tvPrice = view.findViewById(R.id.tv_price);
        tvStock = view.findViewById(R.id.tv_stock);
        tvSet = view.findViewById(R.id.tv_set);
        final RecyclerView recyclerView = view.findViewById(R.id.listView);
        tvAddNumber = view.findViewById(R.id.tv_add_number);
        view.findViewById(R.id.tv_confirm).setOnClickListener(this);
        view.findViewById(R.id.fy_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + image, ivImg);
        tvPrice.setText(price);

        tvSet.setText(choice);
        final SetClassAdapter adapter = new SetClassAdapter(act, listBean);
        recyclerView.setLayoutManager(new LinearLayoutManager(act));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        setStockPrice(listBean);
        adapter.setOnItemClickListener(new SetClassAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int i, List<DataBean> listBean) {
                setStockPrice(listBean);
            }
        });
    }

    //计算库存和价钱
    private void setStockPrice(List<DataBean> listBean){
        this.listBean = listBean;
        int stock = 0;
        double price = 0;
        StringBuilder sb = new StringBuilder();//计算是否每列选中
        StringBuilder sbName = new StringBuilder();
        for (DataBean bean : listBean){
            List<DataBean> entries = bean.getEntries();
            for (DataBean bean1 : entries){
                if (bean1.isSelected()){
//                    stock += bean1.getStock();
//                    price += bean1.getRealPrice();
                    sb.append(bean1.getValue()).append(",");
                    sbName.append(bean1.getValue()).append("、");
                }
            }
        }
        if (sb.length() != 0){
            String[] split = sb.toString().split(",");
            String skuId = sb.deleteCharAt(sb.length() - 1).toString();
            if (listBean.size() == split.length){
                searchPriceAndStock(skuId);
            }
            tvSet.setText(sbName.deleteCharAt(sbName.length() - 1).toString());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_confirm:
                StringBuilder sb = new StringBuilder();
                StringBuilder sb1 = new StringBuilder();
                for (DataBean bean : listBean){
                    if (!bean.isAllSelected()){
                        showToast(getString(R.string.error_2));
                        return;
                    }
                    for (DataBean bean1 : bean.getEntries()){
                        if (bean1.isSelected()){
                            sb.append(bean1.getId()).append("、");
                            sb1.append(bean1.getValue()).append("、");
                        }
                    }
                }
                String substring = sb.substring(0, sb.length() - 1);//id
                String substring1 = sb1.substring(0, sb1.length() - 1);//属性
                int number = tvAddNumber.getNumber();
                if (StringUtils.isEmpty(skuId) || tvAddNumber.getTotalStock() == 0 || number == 0){
                    showToast("当前无库存");
                    return;
                }
                if (listener != null){
                    dismiss();
                    listener.colse(number, price, skuId, substring1);
                }
                break;
        }
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }
    public interface OnClickListener{
        void colse(int number, String price, String ids, String className);
    }


    private void searchPriceAndStock(String sbId){
        CloudApi.productSearchPriceAndStock(id, sbId)
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
                                skuId = data.getId();
                                price = data.getRealPrice() + "";
                                tvPrice.setText(act.getString(R.string.monetary_symbol) + price);
                                tvStock.setText("库存" + data.getStock() + "件");
                                tvAddNumber.setTotalStock(data.getStock());
                            }else {
                                showToast("当前无库存");
                                skuId = null;
                                tvPrice.setText(act.getString(R.string.monetary_symbol) + "0");
                                tvStock.setText("库存" + 0 + "件");
                                tvAddNumber.setTotalStock(0);
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
