package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.constraint.Group;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.CartAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FCartBinding;
import com.fanwang.sg.event.CartInEvent;
import com.fanwang.sg.presenter.CartPresenter;
import com.fanwang.sg.view.bottomFrg.SetClassBottomFrg;
import com.fanwang.sg.view.impl.CartContract;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/8/28.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 * 购物车
 */

public class CartFrg extends BaseFragment<CartPresenter, FCartBinding> implements CartContract.View, View.OnClickListener {

    public static CartFrg newInstance() {
        Bundle args = new Bundle();
        CartFrg fragment = new CartFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private CheckBox cbTotla;
    private TextView tvPrice;
    private TextView tvSettlement;
    private TextView topRight;
    private Group gpSettlement, gpDelete;

    private List<DataBean> listBean = new ArrayList<>();
    private CartAdapter adapter;

    private SetClassBottomFrg classBottomFrg;

    private boolean isRefresh = false;

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_cart;
    }

    @Override
    protected void initView(View view) {
        setCenterTitle(getString(R.string.shopping_cart), getString(R.string.administration));
        EventBus.getDefault().register(this);
        topRight = view.findViewById(R.id.top_right);
        cbTotla = view.findViewById(R.id.cb_totla);
        cbTotla.setOnClickListener(this);
        tvPrice = view.findViewById(R.id.tv_price);
        tvSettlement = view.findViewById(R.id.tv_settlement);
        tvSettlement.setOnClickListener(this);
        gpSettlement = view.findViewById(R.id.gp_settlement);
        view.findViewById(R.id.tv_collection).setOnClickListener(this);
        view.findViewById(R.id.tv_delete).setOnClickListener(this);
        gpDelete = view.findViewById(R.id.gp_delete);

        if (adapter == null) {
            adapter = new CartAdapter(act, listBean);
        }
        mB.recyclerView.setAdapter(adapter);
        mB.refreshLayout.setEnableLoadmore(false);
//        showLoadDataing();
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onRequest(pagerNumber = 1);
            }
        });
        adapter.setParentCbUpdateListener(new CartAdapter.onParentCbUpdateListener() {
            @Override
            public void onParentCbUpdate(boolean isUpdate) {
                cbTotla.setChecked(isUpdate);
            }
        });
        adapter.setGroupClickListener(new CartAdapter.OnGroupClickListener() {
            @Override
            public void onGroupClick(int groupPosition) {
                setPriceText();
            }

            @Override
            public void onSetClass(String image, String choice, double realPrice, String pid, int groupPosition, int childPosition, String id) {
                mPresenter.allSku(pid, image, choice, realPrice, groupPosition, childPosition, id);
            }
        });
        adapter.setChildClickListener(new CartAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(boolean checked, int groupPosition, int childPosition) {

                setPriceText();
            }

            @Override
            public void onCartNumer(int groupPosition, int childPosition, String skuid, int num, String id) {
                mPresenter.onCartNum(groupPosition, childPosition, skuid, num, id);
            }
        });
        mB.recyclerView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
        setSwipeBackEnable(false);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        mB.refreshLayout.finishRefreshing();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onCartThrearInEvent(CartInEvent event) {
        mB.refreshLayout.startRefresh();
        LogUtils.e("刷新了");
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!isRefresh) {
            mB.refreshLayout.startRefresh();
        } else {
            mPresenter.onRequest(pagerNumber = 1);
        }
    }

    @Override
    protected void setOnRightClickListener() {
        super.setOnRightClickListener();
        String rightText = topRight.getText().toString();
        if (rightText.equals(getString(R.string.administration))) {//管理
            gpSettlement.setVisibility(View.GONE);
            gpDelete.setVisibility(View.VISIBLE);
            topRight.setText(getString(R.string.complete));
        } else {//完成
            gpSettlement.setVisibility(View.VISIBLE);
            gpDelete.setVisibility(View.GONE);
            topRight.setText(getString(R.string.administration));
        }
    }

    @Override
    public void setData(List<DataBean> list) {
        isRefresh = true;
        mB.refreshLayout.finishRefreshing();
        listBean.clear();
        listBean.addAll(list);
        adapter.notifyDataSetChanged();
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            mB.recyclerView.collapseGroup(i);
            mB.recyclerView.expandGroup(i);
        }

    }

    @Override
    public void onCartNumSuccess(int groupPosition, int childPosition, String skuid, int num) {
        DataBean bean = listBean.get(groupPosition);
        DataBean bean1 = bean.getProd().get(childPosition);
        bean1.setNum(num);
        adapter.notifyDataSetChanged();
        getAllPrice();
    }

    @Override
    public void onDelCartSuccess(List<DataBean> groups) {
        mB.refreshLayout.startRefresh();
//        listBean.removeAll(groups);
//        for (DataBean bean : listBean) {
//            List<DataBean> prod = bean.getProd();
//            if (prod != null && prod.size() == 0) {
//                listBean.remove(bean);
//            }
//        }
//        adapter.notifyDataSetChanged();

    }


    @Override
    public void onAllSku(List<DataBean> listBean, String substring, int mStock, String image, String choice, double realPrice, final String pid, final int groupPosition, final int childPosition, final String id) {
        if (classBottomFrg == null) {
            classBottomFrg = new SetClassBottomFrg(listBean, image, choice, realPrice + "", pid);
        }
        classBottomFrg.show(getChildFragmentManager(), "dialog");
        classBottomFrg.setOnClickListener(new SetClassBottomFrg.OnClickListener() {
            @Override
            public void colse(int number, String price, String ids, String className) {
                mPresenter.cartModifyCartSku(ids, pid, price, className, groupPosition, childPosition, id);
            }
        });
    }

    @Override
    public void onCartModifyCartSkuSuccess(String price, String className, int groupPosition, int childPosition) {
        DataBean bean = listBean.get(groupPosition).getProd().get(childPosition);
        bean.setRealPrice(BigDecimal.valueOf(Double.valueOf(price)));
        bean.setSpecificationValues(className);
        adapter.notifyDataSetChanged();
        getAllPrice();
    }

    @Override
    public void onCartListNull() {
        listBean.clear();
        adapter.notifyDataSetChanged();
        cbTotla.setChecked(false);
        topRight.setText(getString(R.string.complete));
        tvSettlement.setText("结算 (0)");
        tvPrice.setText(getString(R.string.monetary_symbol) + "0");
    }

    //只要点击就重新计算价格，这样准确率高
    private BigDecimal getAllPrice() {
        BigDecimal allPrice = new BigDecimal(0);
        for (DataBean bean : listBean) {
            List<DataBean> crarChild = bean.getProd();
            for (DataBean bean1 : crarChild) {
                if (bean1.isSelect()) {
                    BigDecimal num = new BigDecimal(bean1.getNum());
                    BigDecimal price = bean1.getRealPrice().multiply(num);
                    allPrice = allPrice.add(price);
                    LogUtils.e(allPrice, bean1.getRealPrice(), num);
                }
            }
        }
        return allPrice;
    }

    private double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal((int) value2);
        return b1.multiply(b2).doubleValue();
    }


    //设置价格
    private void setPriceText() {
        tvPrice.setText(getString(R.string.monetary_symbol) + getAllPrice());
        tvSettlement.setText("结算 (" + getAllPrice() +
                ")");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_settlement://结算
                List<DataBean> dataBeanList = new ArrayList<>();
                List<DataBean> cBeanList = new ArrayList<>();
                DataBean dataBean = null;
                //循环外层
                for (DataBean bean : listBean) {
                    for (DataBean bean1 : bean.getProd()) {//每一项
                        if (bean1.isSelect()){//加入选中的
                            dataBean = new DataBean();
                            cBeanList = new ArrayList<>();
                            cBeanList.add(bean1);
                            dataBean.setProd(cBeanList);
                            dataBeanList.add(dataBean);
                        }
                    }
                }
//                UIHelper.startConfirmationOrderFrg(this, null, listBean, 1001, 0, null, 0);
                UIHelper.startConfirmationOrderAct(null, dataBeanList, 1001, 0, null, 0);

                break;
            case R.id.tv_collection://移入收藏夹
                mPresenter.onMovecollect(listBean);
                break;
            case R.id.tv_delete://删除
                mPresenter.onDelCart(listBean);

                break;
            case R.id.cb_totla:
                boolean checked = cbTotla.isChecked();
                for (DataBean bean : listBean) {
                    bean.setSelect(checked);
                    for (DataBean bean1 : bean.getProd()) {
                        bean1.setSelect(checked);
                    }
                }
                adapter.notifyDataSetChanged();
                setPriceText();
                break;
        }
    }
}
