package com.fanwang.sg.adapter;

import android.content.Context;
import android.support.constraint.Group;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.weight.WithScrollGridView;
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

public class ReturnGoodsAdapter extends BaseExpandableListAdapter {

    private Context act;
    private List<DataBean> listBean = new ArrayList<>();

    public ReturnGoodsAdapter(Context act, List listBean) {
        this.act = act;
        this.listBean = listBean;
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
        if (bean.getType() == 0){//退货退款
            DataBean.OrderSalesReturnBean orderSalesReturn = bean.getOrderSalesReturn();
            if (orderSalesReturn != null){
                int state = orderSalesReturn.getState();
                if (state == 0){
                    viewHolder.tvOrderNumber.setText("退款退货");
                }else {
                    viewHolder.tvOrderNumber.setText("已发货，仅退款");
                }
                switch (orderSalesReturn.getAudit()){
                    case 0:
                        viewHolder.tvState.setText("未审核");
                        break;
                    case 1:
                        viewHolder.tvState.setText("审核成功");
                        break;
                    case 2:
                        viewHolder.tvState.setText("审核失败");
                        break;
                    default:
                        viewHolder.tvState.setText("审核关闭");
                        break;
                }
            }
        }else {
            viewHolder.tvOrderNumber.setText("未发货，仅退款");
            DataBean.OrderRefundBean orderRefund = bean.getOrderRefund();
            if (orderRefund != null){
                switch (orderRefund.getAudit()){
                    case 0:
                        viewHolder.tvState.setText("未审核");
                        break;
                    case 1:
                        viewHolder.tvState.setText("审核成功");
                        break;
                    case 2:
                        viewHolder.tvState.setText("审核失败");
                        break;
                    default:
                        viewHolder.tvState.setText("审核关闭");
                        break;
                }
            }
        }
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
        viewHolder.tvDiscount.setVisibility(View.GONE);

        viewHolder.tvNumber.setText("x" + cartBean.getNum());
        String specificationValues = cartBean.getSpecificationValues();
        if (!StringUtils.isEmpty(specificationValues)){
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
        }

        viewHolder.lyPrice.setVisibility(View.GONE);
        viewHolder.lyButton.setVisibility(View.VISIBLE);
        viewHolder.tvConfirm.setVisibility(View.VISIBLE);
        viewHolder.tvConfirm2.setVisibility(View.GONE);
        viewHolder.tvConfirm.setText(act.getString(R.string.view_details));

        if (bean.getIsGroupPurchase() == 1){
            viewHolder.tvDiscount.setVisibility(View.VISIBLE);
            viewHolder.tvDiscount.setText("拼团");
        }else {
            viewHolder.tvDiscount.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startOrderDetailsAct(bean.getId(), act.getString(R.string.after_details));
            }
        });
        viewHolder.tvConfirm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.startOrderDetailsAct(bean.getId(), act.getString(R.string.after_details));
            }
        });
        return convertView;
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
        View lyPrice;
        View lyButton;

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
            lyPrice = convertView.findViewById(R.id.ly_price);
            lyButton = convertView.findViewById(R.id.ly_button);
        }

    }

}
