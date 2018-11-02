package com.fanwang.sg.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.fanwang.sg.utils.PopupWindowTool;
import com.fanwang.sg.weight.WithScrollListView;
import com.flyco.roundview.RoundLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/8/29.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class CartAdapter extends BaseExpandableListAdapter {

    private Context act;
    private List<DataBean> listBean = new ArrayList<>();

    public CartAdapter(Context act, List listBean) {
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
        return listBean.get(groupPosition).getProd().size();
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return listBean.get(groupPosition);
    }

    //        获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listBean.get(groupPosition).getProd();
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
            convertView = View.inflate(act, R.layout.i_cart, null);
            viewHolder = new GroupViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(groupPosition);
        viewHolder.cbTotla.setChecked(bean.isSelect());
        viewHolder.tvName.setText(bean.getName() + "  >");
        viewHolder.cbTotla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b ;
                if (bean.isSelect()){
                    bean.setSelect(false);
                    b = false;
                }else {
                    bean.setSelect(true);
                    b = true;
                }
                List<DataBean> prod = bean.getProd();
                for (DataBean bean1 : prod){
                    bean1.setSelect(b);
                }
                notifyDataSetChanged();

                if (listener != null){
                    for (DataBean bean1 : listBean){
                        if (bean1.isSelect()){
                            listener.onParentCbUpdate(true);
                        }else {
                            listener.onParentCbUpdate(false);
                            break;
                        }
                    }
                }

                if (groupClickListener != null){
                    groupClickListener.onGroupClick(groupPosition);
                }

            }
        });
        if (listener != null){
            for (DataBean bean1 : listBean){
                if (bean1.isSelect()){
                    listener.onParentCbUpdate(true);
                }else {
                    listener.onParentCbUpdate(false);
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
            convertView = View.inflate(act, R.layout.i_cart_child, null);
            viewHolder = new ChildViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(groupPosition);
        final DataBean cartBean = bean.getProd().get(childPosition);

        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + cartBean.getImage(), viewHolder.ivImg);
        viewHolder.tvName.setText(cartBean.getName());
        String specificationValues = cartBean.getSpecificationValues();
//        viewHolder.tvChoice.setText(specificationValues);
        try {
            JSONArray array = new JSONArray(specificationValues);
            if (array != null && array.length() != 0){
                StringBuilder sb = new StringBuilder();
                for (int i = 0;i < array.length();i++){
                    JSONObject object = array.optJSONObject(i);
                    sb.append(object.optString("value")).append(",");
                }
                String values = sb.deleteCharAt(sb.length() - 1).toString();
//                cartBean.setSpecificationValues(values);
                viewHolder.tvChoice.setText(values);
            }
        } catch (JSONException e) {
            LogUtils.e(e.getMessage());
            e.printStackTrace();
        }
        viewHolder.tvPrice.setText(act.getString(R.string.monetary_symbol) + cartBean.getRealPrice());
        viewHolder.cbTotla.setChecked(cartBean.isSelect());
        viewHolder.etNumber.setText(cartBean.getNum() + "");
        viewHolder.clAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindowTool.shouCartNumber(act, cartBean.getStock(), cartBean.getNum(), new PopupWindowTool.CartNumberListener() {
                    @Override
                    public void onClick(int integer) {
                        if (childClickListener != null){
                            childClickListener.onCartNumer(groupPosition, childPosition, cartBean.getSkuId(), integer, cartBean.getId());
                        }
                    }
                });
            }
        });
        if (childPosition == bean.getProd().size() - 1){

        }else {

        }


        final ChildViewHolder finalViewHolder = viewHolder;
        viewHolder.cbTotla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b ;
                if (cartBean.isSelect()){
                    cartBean.setSelect(false);
                    b = false;
                }else {
                    cartBean.setSelect(true);
                    b = true;
                }
                cartBean.setSelect(b);
                finalViewHolder.cbTotla.setChecked(b);

                List<DataBean> crarChild = listBean.get(groupPosition).getProd();
                for (DataBean bean1 : crarChild){
                    if (bean1.isSelect()){
                        bean.setSelect(true);
                    }else {
                        bean.setSelect(false);
                        break;
                    }
                }
                notifyDataSetChanged();
                if (childClickListener != null){
                    childClickListener.onChildClick(groupPosition, childPosition);
                }
            }
        });
        viewHolder.lyChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupClickListener != null){
                    groupClickListener.onSetClass(cartBean.getImage(), finalViewHolder.tvChoice.getText().toString(),
                            cartBean.getRealPrice().doubleValue(), cartBean.getPid(), groupPosition, childPosition, cartBean.getId());
                }
            }
        });
        return convertView;
    }


    private OnGroupClickListener groupClickListener;
    public void setGroupClickListener(OnGroupClickListener listener){
        this.groupClickListener = listener;
    }
    public interface OnGroupClickListener{
        void onGroupClick(int groupPosition);
        void onSetClass(String image, String choice, double realPrice, String pid, int groupPosition, int childPosition, String id);
    }

    private OnChildClickListener childClickListener;
    public void setChildClickListener(OnChildClickListener listener){
        this.childClickListener = listener;
    }
    public interface OnChildClickListener {
        void onChildClick(int groupPosition, int childPosition);
        void onCartNumer(int groupPosition, final int childPosition, String skuid, int num, String id);
    }

    private onParentCbUpdateListener listener;
    public void setParentCbUpdateListener(onParentCbUpdateListener listener){
        this.listener = listener;
    }
    public interface onParentCbUpdateListener{
        void onParentCbUpdate(boolean isUpdate);
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    class GroupViewHolder{

        CheckBox cbTotla;
        TextView tvName;
        WithScrollListView recyclerView;
        LinearLayout lyChoice;

        public GroupViewHolder(View itemView) {
            cbTotla = itemView.findViewById(R.id.cb_totla);
            tvName = itemView.findViewById(R.id.tv_name);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            lyChoice = itemView.findViewById(R.id.ly_choice);
        }
    }

    class ChildViewHolder{
        CheckBox cbTotla;
        ImageView ivImg;
        TextView tvChoice;
        TextView tvName;
        TextView tvPrice;
        RoundLinearLayout lyChoice;
        View clAdd;
        EditText etNumber;

        public ChildViewHolder(View itemView) {
            cbTotla = itemView.findViewById(R.id.cb_totla);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvChoice = itemView.findViewById(R.id.tv_choice);
            tvPrice = itemView.findViewById(R.id.tv_price);
            lyChoice = itemView.findViewById(R.id.ly_choice);
            clAdd = itemView.findViewById(R.id.cl_add);
            etNumber = itemView.findViewById(R.id.et_number);

        }
    }

}
