package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.User;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FMeBinding;
import com.fanwang.sg.presenter.MePresenter;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.utils.cache.ShareSessionIdCache;
import com.fanwang.sg.view.impl.MeContract;
import com.fanwang.sg.weight.MessageView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.json.JSONObject;

/**
 * 作者：yc on 2018/8/28.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  我的
 */

public class MeFrg extends BaseFragment<MePresenter, FMeBinding> implements MeContract.View, View.OnClickListener{

    private MessageView messageView;

    public static MeFrg newInstance() {
        Bundle args = new Bundle();
        MeFrg fragment = new MeFrg();
        fragment.setArguments(args);
        return fragment;
    }

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
        return R.layout.f_me;
    }

    @Override
    protected void initView(View view) {
        messageView = view.findViewById(R.id.messageView);
        messageView.initMsgView(this);
        mB.lyHead.setOnClickListener(this);
        mB.lyOrder.setOnClickListener(this);

        mPresenter.order_gridview(mB.gridView1, act);
        mPresenter.laber_gridview(mB.gridView2, act, this);
        mPresenter.listview(mB.listView, this);

        mB.refreshLayout.setEnableLoadmore(false);
        setSwipeBackEnable(false);
        setRefreshLayout(mB.refreshLayout, new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                mPresenter.onUserInfo();
            }
        });
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setSofia(true);
        if (!StringUtils.isEmpty(ShareSessionIdCache.getInstance(act).getSessionId())){
            messageView.setVisibility(View.VISIBLE);
            setData(User.getInstance().getUserInfoObj());
            messageView.setMessageNum();
            JSONObject userInfoObj = User.getInstance().getUserInfoObj();
            if (!isRefresh && userInfoObj == null){
                mB.refreshLayout.setEnableRefresh(true);
                mB.refreshLayout.startRefresh();
            }
        }else {
            mB.refreshLayout.setEnableRefresh(false);
            messageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideLoading() {
        isRefresh = true;
        mB.refreshLayout.finishRefreshing();
        mB.refreshLayout.setEnableRefresh(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ly_head://点击登录
                if (User.getInstance().isLogin()){
                    UIHelper.startUpdateDataAct();
                }else {
                    UIHelper.startLoginAct();
                }
                break;
            case R.id.ly_order://我的订单
                if (!User.getInstance().isLogin()){
                    UIHelper.startLoginAct();
                    return;
                }
                UIHelper.startOrderAct(0);
                break;
        }
    }

    @Override
    public void setData(JSONObject bean) {
        if (bean == null)return;
        String image = bean.optString("head");
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + image, mB.ivHead, true);
        String nickname = bean.optString("nickname");
        if (!nickname.equals("null")){
            mB.tvName.setText(nickname);
        }else {
            mB.tvName.setText("未设置");
        }

        int sex = bean.optInt("sex");
        if (sex != 0){
            if (sex == 1){
                mB.tvName.setCompoundDrawablesWithIntrinsicBounds( null,null ,
                        ContextCompat.getDrawable(act,R.mipmap.icon_4), null);
            }else {
                mB.tvName.setCompoundDrawablesWithIntrinsicBounds(null,
                        null , ContextCompat.getDrawable(act,R.mipmap.my_icon_femalesign), null);
            }
        }
    }
}
