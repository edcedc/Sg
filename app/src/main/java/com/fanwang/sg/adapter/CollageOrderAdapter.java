package com.fanwang.sg.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.BaseFragment;
import com.fanwang.sg.base.BaseListViewAdapter;
import com.fanwang.sg.base.User;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.bean.ProductProductDetailBean;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.utils.GlideLoadingUtils;
import com.flyco.roundview.RoundTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/22.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class CollageOrderAdapter extends BaseListViewAdapter<DataBean>{

    public CollageOrderAdapter(Context act, BaseFragment root, List listBean) {
        super(act, root, listBean);
    }

    private ProductProductDetailBean submitOrderBean;

    public void setSubmitOrderBean(ProductProductDetailBean submitOrderBean) {
        this.submitOrderBean = submitOrderBean;
    }

    private List<DataBean> listAllSkuBena = new ArrayList<>();

    public void setListAllSkuBena(List<DataBean> listAllSkuBena) {
        this.listAllSkuBena = listAllSkuBena;
    }

    private String choice;

    public void setChoice(String choice) {
        this.choice = choice;
    }

    private int collageNum = -1;

    public void setCollageNum(int collageNum) {
        this.collageNum = collageNum;
    }

    @Override
    protected View getCreateVieww(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_collage_order, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        DataBean.TeamLeaderBean teamLeader = bean.getTeamLeader();
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + teamLeader.getHead(), viewHolder.ivHead);
        viewHolder.tvName.setText(teamLeader.getNickname());
        viewHolder.tvUserNumber.setText(Html.fromHtml("还差" + "<font color='#FE2701'> " + (collageNum - bean.getCurrentCollageNum()) + " </font>" + "人拼成"));
        viewHolder.tvTime.setText("剩余" + bean.getEndTime());

        final List<DataBean> listUser = bean.getListUser();
        viewHolder.tvJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.e(bean.getUserId(), User.getInstance().getUserId());
                if (bean.getUserId() == User.getInstance().getUserId()){
                    return;
                }
                UIHelper.startParticipateCollageFrg(root, submitOrderBean, position, listUser, listAllSkuBena, choice);
            }
        });
        return convertView;
    }

    class ViewHolder{

        ImageView ivHead;
        TextView tvName;
        TextView tvUserNumber;
        TextView tvTime;
        RoundTextView tvJoinGroup;

        public ViewHolder(View convertView) {
            ivHead = convertView.findViewById(R.id.iv_head);
            tvName = convertView.findViewById(R.id.tv_name);
            tvUserNumber = convertView.findViewById(R.id.tv_user_number);
            tvTime = convertView.findViewById(R.id.tv_time);
            tvJoinGroup = convertView.findViewById(R.id.tv_join_group);
        }
    }

}
