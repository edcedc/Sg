package com.fanwang.sg.presenter;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.HomeLabelAdapter;
import com.fanwang.sg.adapter.MeAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.BaseResponseBean;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.callback.Code;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.view.impl.MeContract;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.fanwang.sg.weight.WithScrollGridView;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 作者：yc on 2018/9/5.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class MePresenter extends MeContract.Presenter{

    private String[] labelStr1 = {"待付款", "待成团", "待发货", "待收货"};
    private int[] labelImg1 = {R.mipmap.my_icon_pendingpayment, R.mipmap.my_icon_pendingregiment, R.mipmap.my_icon_pending_delivery, R.mipmap.my_received};

    private String[] labelStr2 = {"我的钱包", "我的收藏", "我的积分", "我的分销", "退货/退款"};
    private int[] labelImg2 = {R.mipmap.my_icon_wallet, R.mipmap.my_icon_collection, R.mipmap.my_icon_integral, R.mipmap.my_icon_distribution, R.mipmap.my_icon_refund};

    private int[] imgs = {R.mipmap.my_icon_setup, R.mipmap.my_icon_center};
    private String[] strs = {"设置", "帮助中心"};


    @Override
    public void order_gridview(WithScrollGridView gridView, Activity act) {
        final List<DataBean> listBean1 = new ArrayList<>();
        for (int i = 0;i < labelStr1.length;i++){
            DataBean bean1 = new DataBean();
            bean1.setName(labelStr1[i]);
            bean1.setImg(labelImg1[i]);
            listBean1.add(bean1);
        }
        HomeLabelAdapter adapter1 = new HomeLabelAdapter(act, listBean1);
        gridView.setNumColumns(labelStr1.length);
        gridView.setAdapter(adapter1);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!User.getInstance().isLogin()){
                    UIHelper.startLoginAct();
                    return;
                }
                switch (i){
                    case 0:
                        UIHelper.startOrderAct(1);
                        break;
                    case 1:
                        UIHelper.startOrderAct(2);
                        break;
                    case 2:
                        UIHelper.startOrderAct(3);
                        break;
                    case 3:
                        UIHelper.startOrderAct(4);
                        break;
                }
            }
        });
    }

    @Override
    public void laber_gridview(WithScrollGridView gridView, Activity act, final BaseFragment root) {
        List<DataBean> listBean2 = new ArrayList<>();
        for (int i = 0;i < labelStr2.length;i++){
            DataBean bean1 = new DataBean();
            bean1.setName(labelStr2[i]);
            bean1.setImg(labelImg2[i]);
            listBean2.add(bean1);
        }
        HomeLabelAdapter adapter2 = new HomeLabelAdapter(act, listBean2);
        gridView.setNumColumns(labelStr2.length);
        gridView.setAdapter(adapter2);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!User.getInstance().isLogin()){
                    UIHelper.startLoginAct();
                    return;
                }
                switch (i){
                    case 0://我的钱包
                        UIHelper.startWalletAct();
                        break;
                    case 1://我的收藏
                        UIHelper.startCollectionFrg(root);
                        break;
                    case 2://我的积分
                        UIHelper.startIntegralFrg(root);
                        break;
                    case 3://我的分销
                        UIHelper.startDistributionFrg(root, 0);
                        break;
                    case 4://退货 退款
                        UIHelper.startReturnGoodsFrg(root);
                        break;
                }
            }
        });
    }

    @Override
    public void listview(RecyclerView listView, final BaseFragment root) {
        List<DataBean> listBean = new ArrayList<>();
        for (int i = 0; i < imgs.length; i++) {
            DataBean bean = new DataBean();
            bean.setImg(imgs[i]);
            bean.setName(strs[i]);
            listBean.add(bean);
        }
        MeAdapter adapter = new MeAdapter(act, listBean, true);
        listView.setLayoutManager(new LinearLayoutManager(act));
        listView.setHasFixedSize(true);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  10));
        listView.setAdapter(adapter);
        adapter.setOnClickListener(new MeAdapter.OnClickListener() {
            @Override
            public void onClick(View v, int position) {
                switch (position){
                    case 0:
                        if (!User.getInstance().isLogin()){
                            UIHelper.startLoginAct();
                            return;
                        }
                        UIHelper.startSetAct();
                        break;
                    case 1:
                        UIHelper.startHelpCenterFrgFrg(root);
                        break;
                }
            }
        });
    }

    @Override
    public void onUserInfo() {
        CloudApi.userUserInfo()
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

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
                            JSONObject data = object.optJSONObject("data");
                            if (data != null){
                                User.getInstance().setLogin(true);
                                mView.setData(data);
                                User.getInstance().setUserInfoObj(data);
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

}
