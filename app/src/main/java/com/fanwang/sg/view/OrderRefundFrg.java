package com.fanwang.sg.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.ImageAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.databinding.FOrderRefundBinding;
import com.fanwang.sg.event.CameraInEvent;
import com.fanwang.sg.presenter.OrderRefundPresenter;
import com.fanwang.sg.utils.Constants;
import com.fanwang.sg.utils.DataListTool;
import com.fanwang.sg.utils.PictureSelectorTool;
import com.fanwang.sg.view.bottomFrg.CameraBottomFrg;
import com.fanwang.sg.view.impl.OrderRefundContract;
import com.fanwang.sg.weight.FullyGridLayoutManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;
import com.zaaach.toprightmenu.TopRightMenuTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/10/15.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  退货退款
 */

public class OrderRefundFrg extends BaseFragment<OrderRefundPresenter, FOrderRefundBinding> implements OrderRefundContract.View, View.OnClickListener{

    private ImageAdapter imageAdapter;
    private List<LocalMedia> localMediaList = new ArrayList<>();
    private CameraBottomFrg cameraBottomFrg;

    private List<MenuItem> menuOrderRefundItems = DataListTool.getOrderRefundList();
    private List<MenuItem> menuLogisticsItems = DataListTool.getLogisticsList();
    private int OrderRefundPosition = -1;
    private int type;
    private String id;
    private int cargoType = -1;//0已收货，1未收货
    private int state = -1;//0 退货退货、1退货
    private List<DataBean> listOrderDetails = new ArrayList<>();

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {
        type = bundle.getInt("type");
        id = bundle.getString("id");
        Type type = new TypeToken<ArrayList<DataBean>>() {}.getType();
        listOrderDetails = new Gson().fromJson(bundle.getString("list"), type);
        state = bundle.getInt("state");
    }

    @Override
    protected int bindLayout() {
        return R.layout.f_order_refund;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.top_return_goods));
        mB.tvConfirm.setOnClickListener(this);
        mB.lyLogistics.setOnClickListener(this);
        mB.lyRefundState.setOnClickListener(this);
        if (imageAdapter == null) {
            imageAdapter = new ImageAdapter(act, new ImageAdapter.onAddPicClickListener() {
                @Override
                public void onAddPicClick() {
                    showCamear(CameraInEvent.REFUND_CAMEAR, CameraInEvent.REFUND_PHOTO, false);
                }
            });
        }
        mB.recyclerView.setLayoutManager(new FullyGridLayoutManager(act, 3, GridLayoutManager.VERTICAL, false));
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PictureSelectorTool.PictureMediaType(act, localMediaList, position);
            }
        });
        EventBus.getDefault().register(this);

        double allPrice = 0;
        for (DataBean bean : listOrderDetails){
            allPrice += bean.getPrice() * bean.getNum();
        }
        switch (type){
            case Constants.return_paragraph:
                mB.tvText.setVisibility(View.VISIBLE);
                mB.tvText.setText("退款金额：" + getString(R.string.monetary_symbol) + allPrice);
                break;
            case Constants.refund:
                mB.lyRefundState.setVisibility(View.VISIBLE);
                mB.lyRefundState.setEnabled(false);
                mB.tvRefund.setText("已收到货");
                mB.lyLogistics.setVisibility(View.VISIBLE);
                cargoType = 0;
                mB.recyclerView.setVisibility(View.VISIBLE);
                break;
            case Constants.return_goods:
                mB.tvRefund.setText("已收到货");
                cargoType = 0;
                mB.lyRefundState.setVisibility(View.VISIBLE);
                mB.lyPrice.setVisibility(View.VISIBLE);
                mB.recyclerView.setVisibility(View.VISIBLE);
                mB.tvText.setVisibility(View.VISIBLE);
                mB.tvText.setText("退款金额：" + getString(R.string.monetary_symbol) + allPrice);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainThreadInEvent(CameraInEvent event) {
        if (cameraBottomFrg != null && cameraBottomFrg.isShowing())cameraBottomFrg.dismiss();
        localMediaList.clear();
        localMediaList.addAll(PictureSelector.obtainMultipleResult((Intent) event.getObject()));
        imageAdapter.setList(localMediaList);
        imageAdapter.notifyDataSetChanged();
    }

    private void showCamear(final int camear, final int photo, final boolean circleDimmedLayer){
        if (cameraBottomFrg == null){
            cameraBottomFrg = new CameraBottomFrg();
        }
        cameraBottomFrg.show(getChildFragmentManager(), "dialog");
        cameraBottomFrg.setCameraListener(new CameraBottomFrg.onCameraListener() {
            @Override
            public void camera() {
                PictureSelectorTool.PictureSelectorImage(act, localMediaList, photo);
                if (cameraBottomFrg != null && cameraBottomFrg.isShowing())cameraBottomFrg.dismiss();
            }

            @Override
            public void photo() {
                PictureSelectorTool.photo(act, camear, circleDimmedLayer);
                if (cameraBottomFrg != null && cameraBottomFrg.isShowing())cameraBottomFrg.dismiss();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_confirm:
                mPresenter.confirm(id, type, localMediaList, cargoType, state, mB.etText.getText().toString(),
                        mB.etOrderNumber.getText().toString(), mB.tvLogistics.getText().toString(),
                        mB.etPrice.getText().toString(), listOrderDetails);
                break;
            case R.id.ly_logistics:
                TopRightMenuTool.TopRightMenu(act, menuLogisticsItems, mB.tvLogistics, new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        MenuItem item = menuLogisticsItems.get(position);
                        mB.tvLogistics.setText(item.getText());
                        mB.etOrderNumber.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case R.id.ly_refund_state:
                TopRightMenuTool.TopRightMenu(act, menuOrderRefundItems, mB.tvRefund, new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        MenuItem item = menuOrderRefundItems.get(position);
                        mB.tvRefund.setText(item.getText());
                        cargoType = position;
                        OrderRefundPosition = position;
                    }
                });
                break;
        }
    }
}
