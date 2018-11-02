package com.fanwang.sg.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.MeAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FSetBinding;
import com.fanwang.sg.utils.PopupWindowTool;
import com.fanwang.sg.utils.cache.ShareSessionIdCache;
import com.fanwang.sg.view.act.MainActivity;
import com.fanwang.sg.weight.LinearDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/6.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  设置
 */

public class SetFrg extends BaseFragment<BasePresenter, FSetBinding>{

    public static SetFrg newInstance() {
        Bundle args = new Bundle();
        SetFrg fragment = new SetFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private String[] strs = {"更改手机号码", "关于我们", "清理缓存"};
    private List<DataBean> listBean = new ArrayList<>();

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_set;
    }

    @Override
    protected void initView(View view) {
        setTitle(getString(R.string.set));
        long cacheSize = CacheDiskUtils.getInstance().getCacheSize();
        final List<DataBean> listBean = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            DataBean bean = new DataBean();
            bean.setName(strs[i]);
            bean.setContent(cacheSize + "");
            listBean.add(bean);
        }
        final MeAdapter adapter = new MeAdapter(act, listBean);
        mB.recyclerView.setLayoutManager(new LinearLayoutManager(act));
        mB.recyclerView.setHasFixedSize(true);
        mB.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mB.recyclerView.addItemDecoration(new LinearDividerItemDecoration(act, DividerItemDecoration.VERTICAL,  10));
        mB.recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new MeAdapter.OnClickListener() {
            @Override
            public void onClick(View v, int position) {
                switch (position){
                    case 0:
                        UIHelper.startBindPhoneFrg(SetFrg.this, 1);
                        break;
                    case 1:
                        UIHelper.startAboutUsFrg(SetFrg.this);
                        break;
                    case 2://关于我们
                        PopupWindowTool.showDialog(act, PopupWindowTool.cache_size, new PopupWindowTool.DialogListener() {
                            @Override
                            public void onClick() {
                                showLoading();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean clear = CacheDiskUtils.getInstance().clear();
                                        if (clear){
                                            listBean.get(2).setContent(0 + "");
                                            adapter.notifyItemChanged(3);
                                        }
                                        hideLoading();
                                    }
                                }, 1000);

                            }
                        });
                        break;
                }
            }
        });
        mB.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.getInstance().setUserInfoObj(null);
                User.getInstance().setUserObj(null);
                ShareSessionIdCache.getInstance(act).remove();
                User.getInstance().setLogin(false);
                ActivityUtils.startActivity(MainActivity.class);
                ActivityUtils.finishAllActivities();
            }
        });
        setSwipeBackEnable(false);
    }
}
