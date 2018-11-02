package com.fanwang.sg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.weight.WithScrollGridView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 作者：yc on 2018/8/29.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class HomeImgAdapter extends BaseListViewAdapter<DataBean>{

    private int type;

    public HomeImgAdapter(Context act, BaseFragment root, List<DataBean> listBean, int type) {
        super(act, root, listBean);
        this.type = type;
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_home_img, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DataBean bean = listBean.get(position);
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean());
        list.add(new DataBean());
        list.add(new DataBean());
        list.add(new DataBean());
        HomeImgAdapter2 adapter = new HomeImgAdapter2(act, list);
        viewHolder.gridView.setAdapter(adapter);
        return convertView;
    }

    class ViewHolder{

        BGABanner banner;
        WithScrollGridView gridView;

        public ViewHolder(View convertView) {
            banner = convertView.findViewById(R.id.banner);
            gridView = convertView.findViewById(R.id.gridView);
        }
    }

}
