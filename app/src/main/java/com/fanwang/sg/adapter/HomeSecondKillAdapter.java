package com.fanwang.sg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.weight.WithScrollGridView;

import java.util.ArrayList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

/**
 * 作者：yc on 2018/8/28.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class HomeSecondKillAdapter extends BaseListViewAdapter<DataBean.Seckill_CollageBean>{


    public HomeSecondKillAdapter(Context act, BaseFragment root, List<DataBean.Seckill_CollageBean> listBean) {
        super(act, root, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_home_second, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataBean.Seckill_CollageBean bean = listBean.get(position);
        DataBean.SecKillBean secKill = bean.getSecKill();
        LogUtils.e(secKill.getEndTime());

        long time4 = (long)60000 * 60 * 5;
        viewHolder.tvTime.start(time4);
        viewHolder.tvTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                LogUtils.e("onEnd");
            }
        });

        List<DataBean> listBean = new ArrayList<>();
        listBean.add(new DataBean());
        listBean.add(new DataBean());
        HomeSecondKill2Adapter adapter1 = new HomeSecondKill2Adapter(act, listBean);
        viewHolder.gridView1.setAdapter(adapter1);
        viewHolder.gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UIHelper.startSecondKillFrg(root);
            }
        });

        List<DataBean> listBean2 = new ArrayList<>();
        listBean2.add(new DataBean());
        listBean2.add(new DataBean());
        HomeSecondKill2Adapter adapter2 = new HomeSecondKill2Adapter(act, listBean2);
        viewHolder.gridView2.setAdapter(adapter2);
        viewHolder.gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UIHelper.startCollagefrg(root);
            }
        });
        return convertView;
    }

    class ViewHolder{

        CountdownView tvTime;
        WithScrollGridView gridView1, gridView2;

        public ViewHolder(View convertView) {
            gridView1 = convertView.findViewById(R.id.gridView1);
            gridView2 = convertView.findViewById(R.id.gridView2);
            tvTime = convertView.findViewById(R.id.tv_time);
        }
    }

}
