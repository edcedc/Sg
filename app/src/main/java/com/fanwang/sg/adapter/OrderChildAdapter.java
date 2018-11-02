package com.fanwang.sg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.weight.WithScrollListView;
import com.flyco.roundview.RoundTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/4.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class OrderChildAdapter extends BaseExpandableListAdapter {

    private Context act;
    private List<DataBean> listBean = new ArrayList<>();
    private int mType;
    private BaseFragment root;

    public OrderChildAdapter(Context act, BaseFragment root, List listBean, int mType) {
        this.act = act;
        this.listBean = listBean;
        this.mType = mType;
        this.root = root;
    }

    //        获取分组的个数
    @Override
    public int getGroupCount() {
        return listBean.size();
    }

    //        获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return listBean.get(groupPosition).getListOrderDetails().size();
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return listBean.get(groupPosition);
    }

    //        获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listBean.get(groupPosition).getListOrderDetails();
    }

    //        获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //        获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //        分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_order, null);
            viewHolder = new GroupViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(groupPosition);
        viewHolder.tvOrderNumber.setText("订单号：" + bean.getOrderNo());
        String title = null;
        switch (bean.getState()){
            case 1001:
                title = "待付款";
                break;
            case 10000:
            case 1000:
                title = "待成团";
                break;
            case 1002:
                title = "待发货";
                break;
            case 1003:
                title = "待收货";
                break;
            case 1005:
                title = "已退款";
                break;
            case 1004:
                title = "交易完成";
                break;
            case 1006:
                title = "交易关闭";
                break;
            default:
                title = "全部";
                break;
        }
        viewHolder.tvState.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_order_child, null);
            viewHolder = new ChildViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(groupPosition);
        final DataBean cartBean = bean.getListOrderDetails().get(childPosition);
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + cartBean.getShowImg(), viewHolder.ivImg);
        viewHolder.tvName.setText(cartBean.getName());
        viewHolder.tvPrice.setText(act.getString(R.string.monetary_symbol) + cartBean.getPrice());
//        viewHolder.tvDiscount.setText("拼团");
        viewHolder.tvNumber.setText("x" + cartBean.getNum());
        String specificationValues = cartBean.getSpecificationValues();
        try {
            JSONArray array = new JSONArray(specificationValues);
            if (array != null && array.length() != 0){
                StringBuilder sb = new StringBuilder();
                for (int i = 0;i < array.length();i++){
                    JSONObject object = array.optJSONObject(i);
                    sb.append(object.optString("value")).append(",");
                }
                viewHolder.tvClass.setText(sb.deleteCharAt(sb.length() - 1).toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        viewHolder.tvAllNumber.setText("共" + bean.getListOrderDetails().size() + "件商品" + "  小计  ");

//        double allPrice = 0;
//        for (DataBean bean1 : bean.getListOrderDetails()){
//            allPrice = bean1.getPrice() * bean1.getNum();
//        }
        viewHolder.tvAllPrice.setText(act.getString(R.string.monetary_symbol) + bean.getRealPayMoney());

        int listOrderSize = bean.getListOrderDetails().size() - 1;
        if (childPosition == bean.getListOrderDetails().size() - 1){
            viewHolder.lyPrice.setVisibility(View.VISIBLE);
        }else {
            viewHolder.lyPrice.setVisibility(View.GONE);
        }
        switch (bean.getState()){
            case 1001:
                viewHolder.tvConfirm.setText(act.getString(R.string.payment));
                displayButton(childPosition, listOrderSize, viewHolder.lyButton, View.VISIBLE, viewHolder.gridView, View.GONE);
                viewHolder.tvConfirm2.setVisibility(View.GONE);
                List<DataBean> listUser = bean.getListUser();
                if (listUser != null && listUser.size() != 0){
                    CollageUserAdapter adapter = new CollageUserAdapter(act, listUser);
                    viewHolder.gridView.setAdapter(adapter);
                    displayButton(childPosition, listOrderSize, viewHolder.lyButton, View.VISIBLE, viewHolder.gridView, View.VISIBLE);
                }else {
                    displayButton(childPosition, listOrderSize, viewHolder.lyButton, View.VISIBLE, viewHolder.gridView, View.GONE);
                }
                break;
            case 10000:
            case 1000:
                viewHolder.tvConfirm.setText(act.getString(R.string.to_share));
                displayButton(childPosition, listOrderSize, viewHolder.lyButton, View.VISIBLE, viewHolder.gridView, View.GONE);
                viewHolder.tvConfirm2.setVisibility(View.GONE);
                List<DataBean> listOrderDetails = bean.getListUser();
                if (listOrderDetails != null && listOrderDetails.size() != 0){
                    CollageUserAdapter adapter = new CollageUserAdapter(act, listOrderDetails);
                    viewHolder.gridView.setAdapter(adapter);
                    displayButton(childPosition, listOrderSize, viewHolder.lyButton, View.VISIBLE, viewHolder.gridView, View.VISIBLE);
                }else {
                    displayButton(childPosition, listOrderSize, viewHolder.lyButton, View.VISIBLE, viewHolder.gridView, View.GONE);
                }
                break;
            case 1002:
                displayButton(childPosition, listOrderSize, viewHolder.lyButton, View.GONE, viewHolder.gridView, View.GONE);
                break;
            case 1003:
                displayButton(childPosition, listOrderSize, viewHolder.lyButton, View.VISIBLE, viewHolder.gridView, View.GONE);
                viewHolder.tvConfirm.setVisibility(View.VISIBLE);
                viewHolder.tvConfirm2.setVisibility(View.VISIBLE);
                viewHolder.tvConfirm.setText(act.getString(R.string.confirm_receipt_goods));
                viewHolder.tvConfirm2.setText(act.getString(R.string.look_logistics));
//                viewHolder.gridView.setVisibility(View.GONE);
//                if (childPosition == bean.getListOrderDetails().size() - 1){
//                    viewHolder.tvConfirm2.setVisibility(View.VISIBLE);
//                }else {
//                    viewHolder.tvConfirm2.setVisibility(View.GONE);
//                }
                break;
            case 1005:
                displayButton(childPosition, listOrderSize, viewHolder.lyButton, View.VISIBLE, viewHolder.gridView, View.GONE);
                viewHolder.tvConfirm.setVisibility(View.VISIBLE);
                viewHolder.tvConfirm2.setVisibility(View.GONE);
                viewHolder.tvConfirm.setText(act.getString(R.string.view_details));
                break;
            case 1004:
            case 1006:
                displayButton(childPosition, listOrderSize, viewHolder.lyButton, View.GONE, viewHolder.gridView, View.GONE);
                break;
            default:
                viewHolder.tvConfirm.setText("出问题");
                break;
        }

        if (bean.getIsGroupPurchase() == 1){
            viewHolder.tvDiscount.setVisibility(View.VISIBLE);
            viewHolder.tvDiscount.setText("拼团");
        }else {
            viewHolder.tvDiscount.setVisibility(View.GONE);
        }


        final ChildViewHolder finalViewHolder = viewHolder;
        viewHolder.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onClick(finalViewHolder.tvConfirm.getText().toString(), groupPosition, bean);
                }
            }
        });
        viewHolder.tvConfirm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onClick(finalViewHolder.tvConfirm2.getText().toString(), groupPosition, bean);
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startOrderDetailsAct(bean.getId());
            }
        });
        return convertView;
    }

    //设置最后一栏的显示状态
    private void displayButton(int childPosition, int listOrderSize, View lyButton, int gone, View gridView, int gone1){
        if (childPosition == listOrderSize){
            lyButton.setVisibility(gone);
            gridView.setVisibility(gone1);
        }else {
            lyButton.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
        }
    }

    private OnClickListener listener;
    public interface OnClickListener{
        void onClick(String text, int groupPosition, DataBean bean);
    }
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    class GroupViewHolder{

        TextView tvOrderNumber;
        TextView tvState;
        WithScrollListView listView;

        public GroupViewHolder(View convertView) {
            tvOrderNumber = convertView.findViewById(R.id.tv_order_number);
            tvState = convertView.findViewById(R.id.tv_state);
            listView = convertView.findViewById(R.id.recyclerView);
        }
    }

    class ChildViewHolder{

        ImageView ivImg;
        TextView tvName;
        RoundTextView tvDiscount;
        TextView tvClass;
        TextView tvPrice;
        TextView tvNumber;
        TextView tvAllPrice;
        TextView tvAllNumber;
        RoundTextView tvConfirm;
        RoundTextView tvConfirm2;
        GridView gridView;
        View lyButton;
        View lyPrice;


        public ChildViewHolder(View convertView) {
            ivImg = convertView.findViewById(R.id.iv_img);
            tvName = convertView.findViewById(R.id.tv_name);
            tvDiscount = convertView.findViewById(R.id.tv_discount);
            tvClass = convertView.findViewById(R.id.tv_class);
            tvPrice = convertView.findViewById(R.id.tv_price);
            tvNumber = convertView.findViewById(R.id.tv_number);

            tvAllPrice = convertView.findViewById(R.id.tv_allPrice);
            tvAllNumber = convertView.findViewById(R.id.tv_allNumber);
            tvConfirm = convertView.findViewById(R.id.tv_confirm);
            tvConfirm2 = convertView.findViewById(R.id.tv_confirm2);
            gridView = convertView.findViewById(R.id.gridView);
            lyButton = convertView.findViewById(R.id.ly_button);
            lyPrice = convertView.findViewById(R.id.ly_price);
        }

    }

}
