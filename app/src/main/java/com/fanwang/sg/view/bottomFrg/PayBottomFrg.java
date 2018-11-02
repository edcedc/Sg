package com.fanwang.sg.view.bottomFrg;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.adapter.PayAdapter;
import com.fanwang.sg.base.BaseBottomSheetFrag;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.controller.UIHelper;
import com.fanwang.sg.event.CartInEvent;
import com.fanwang.sg.view.act.ConfirmationOrderAct;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：yc on 2018/9/26.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  支付类型
 */

public class PayBottomFrg extends BaseBottomSheetFrag{

    @Override
    public int bindLayout() {
        return R.layout.f_pay;
    }
    private String[] pays= {"支付宝", "微信支付", "余额支付"};

    private int payType = 0;

    private PayAdapter adapter;

    private boolean isPayState = false;//页面关闭状态

    public boolean isPayState() {
        return isPayState;
    }

    public void setPayState(boolean payState) {
        isPayState = payState;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().post(new CartInEvent());
        super.onDestroy();
//        if (!isPayState){
//            UIHelper.startOrderAct(0);
//            ActivityUtils.finishActivity(ConfirmationOrderAct.class);
//        }
    }

    @Override
    public void initView(View view) {
        ListView listView = view.findViewById(R.id.listView);
        List<DataBean> listBean = new ArrayList<>();
        for (String s : pays){
            DataBean bean = new DataBean();
            bean.setName(s);
            listBean.add(bean);
        }
        if (adapter == null){
            adapter = new PayAdapter(act, listBean);
        }
        listView.setAdapter(adapter);
        adapter.setmPsition(0);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                payType = i;
                switch (i){
                    case 0:
                        break;
                    case 1:
                        break;
                    default:
                        break;

                }
                adapter.setmPsition(i);
                adapter.notifyDataSetChanged();
            }
        });
        view.findViewById(R.id.fy_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onClick(payType);
                }
                dismiss();
            }
        });
    }

    private OnClickListener listener;

    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

    public interface OnClickListener{
        void onClick(int payType);
    }

}
