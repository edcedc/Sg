package com.fanwang.sg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.weight.WithScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/8/29.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class HomeNewArrivalsAdapter extends BaseListViewAdapter<DataBean>{

    public HomeNewArrivalsAdapter(Context act, BaseFragment root, List listBean) {
        super(act, root, listBean);
    }

    public HomeNewArrivalsAdapter(Context act, BaseFragment root, List listBean, boolean isDiscount, int type) {
        super(act, root, listBean);
        this.isDiscount = isDiscount;
        this.type = type;
    }

    private boolean isDiscount = false;
    private int type;

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_home_new_arrivals, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        viewHolder.tvTitle.setText(bean.getTitle());
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean());
        list.add(new DataBean());
        list.add(new DataBean());
        list.add(new DataBean());
        HomeNewArrivalsAdapter2 adapter = new HomeNewArrivalsAdapter2(act, list, isDiscount);
        viewHolder.gridView.setAdapter(adapter);
        viewHolder.tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bean.getType() == HomeAdapter.NEW_PRODUCT_TYPE){
                    UIHelper.startNewArrivalsFrg(root);
                }else {
//                    UIHelper.startDiscountFrg(root, type);
                }
            }
        });
        viewHolder.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UIHelper.startGoodsDetailsAct(bean.getId());
            }
        });
        return convertView;
    }

    class ViewHolder {

        TextView tvMore;
        TextView tvTitle;
        WithScrollGridView gridView;

        public ViewHolder(View convertView) {
            tvMore = convertView.findViewById(R.id.tv_more);
            tvTitle = convertView.findViewById(R.id.tv_title);
            gridView = convertView.findViewById(R.id.gridView);
        }
    }

}
