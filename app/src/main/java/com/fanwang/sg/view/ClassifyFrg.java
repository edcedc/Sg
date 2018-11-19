package com.fanwang.sg.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.ClassLeftAdapter;
import com.fanwang.sg.adapter.ClassRightAdapter;
import com.fanwang.sg.adapter.HomeAdapter;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BasePresenter;
import com.fanwang.sg.base.IBaseView;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.databinding.FClassifyBinding;
import com.fanwang.sg.presenter.ClassifyPresenter;
import com.fanwang.sg.view.impl.ClassifyContract;
import com.fanwang.sg.weight.GridDividerItemDecoration;
import com.fanwang.sg.weight.LinearDividerItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * 作者：yc on 2018/8/28.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  分类
 */

public class ClassifyFrg extends BaseFragment<ClassifyPresenter, FClassifyBinding> implements ClassifyContract.View, View.OnClickListener{

    public static ClassifyFrg newInstance() {
        Bundle args = new Bundle();
        ClassifyFrg fragment = new ClassifyFrg();
        fragment.setArguments(args);
        return fragment;
    }

    private List<DataBean> listLeft = new ArrayList<>();
    private ClassLeftAdapter leftAdapter;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    protected int bindLayout() {
        return R.layout.f_classify;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.iv_search_img).setVisibility(View.INVISIBLE);

        EditText etSearch = view.findViewById(R.id.et_searh);
        etSearch.setOnClickListener(this);
        etSearch.setFocusable(false);
        etSearch.setLongClickable(false);
        etSearch.setTextIsSelectable(false);
        if (leftAdapter == null){
            leftAdapter = new ClassLeftAdapter(act, listLeft);
        }
        mB.lvLeft.setAdapter(leftAdapter);
        mB.lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                leftAdapter.setmPosition(i);
                leftAdapter.notifyDataSetChanged();
                SwitchTo(i);
            }
        });
        showLoadDataing();
        mPresenter.onLeftRequest();
        setSwipeBackEnable(false);
    }

    @Override
    public void setData(List<DataBean> list) {
        if (list.size() != 0){
            hideLoading();
            listLeft.addAll(list);
            leftAdapter.setmPosition(0);
            leftAdapter.notifyDataSetChanged();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    DataBean bean = list.get(i);
                    fragments.add(createListFragments(i, bean.getId()));
                }
                for (Fragment f : fragments) {
                    transaction.add(R.id.fl_container, f);
                }
                transaction.commit();
                SwitchTo(0);
            }
        }else {
            showLoadEmpty();
        }
    }

    private ClassifyRightFrg createListFragments(int position, String ids) {
        ClassifyRightFrg fragment = new ClassifyRightFrg();
        Bundle bundle = new Bundle();
        bundle.putString("id", ids);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * 切换
     */
    private void SwitchTo(int position) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        for (Fragment f : fragments){
            transaction.hide(f);
        }
        transaction.show(fragments.get(position));
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_searh:
                UIHelper.startSearchFrg(this);
                break;
        }
    }
}
