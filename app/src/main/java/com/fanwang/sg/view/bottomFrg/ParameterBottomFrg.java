package com.fanwang.sg.view.bottomFrg;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.adapter.ParameterAdapter;
import com.fanwang.sg.adapter.SetClassAdapter;
import com.fanwang.sg.base.BaseBottomSheetFrag;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.weight.AddNumberView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/11.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

@SuppressLint("ValidFragment")
public class ParameterBottomFrg extends BaseBottomSheetFrag {

    private List<DataBean> listBean = new ArrayList<>();

    public ParameterBottomFrg(List<DataBean> listParameter) {
        this.listBean = listParameter;
    }

    @Override
    public int bindLayout() {
        return R.layout.p_parameter;
    }

    @Override
    public void initView(View view) {
        ListView listView = view.findViewById(R.id.listView);
        ParameterAdapter adapter = new ParameterAdapter(act, listBean);
        listView.setAdapter(adapter);
        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

}
