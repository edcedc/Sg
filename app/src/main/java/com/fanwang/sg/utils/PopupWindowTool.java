package com.fanwang.sg.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.weight.AddNumberView;
import com.fanwang.sg.weight.WPopupWindow;

/**
 * 作者：yc on 2018/8/23.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class PopupWindowTool {

    public static final int dialog_dalete = 0;//是否删除
    public static final int dialog_forward = 1;//提现成功
    public static final int cache_size = 2;//清除缓存
    public static final int delete_address = 3;//确认删除该收货地址吗？
    public static final int order_confirmReceipt = 4;//确认收获

    public static void showDialog(Context act, final int type, final DialogListener listener){
        View wh = LayoutInflater.from(act).inflate(R.layout.p_dialog, null);
        final WPopupWindow popupWindow = new WPopupWindow(wh);
        popupWindow.showAtLocation(wh, Gravity.CENTER, 0, 0);
        TextView tvTitle = wh.findViewById(R.id.tv_title);
        TextView btCancel = wh.findViewById(R.id.bt_cancel);
        TextView btSubmit = wh.findViewById(R.id.bt_submit);
        View view = wh.findViewById(R.id.view);
        switch (type){
            case dialog_dalete:
                tvTitle.setText("是否删除");
                break;
            case dialog_forward:
                tvTitle.setText("您的提现已提交\n将于3-5个工作日到账");
                btCancel.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
                btSubmit.setText("我知道了");
                break;
            case cache_size:
                tvTitle.setText("确认清楚缓存数据");
                break;
            case delete_address:
                tvTitle.setText("确认删除该收货地址吗？");
                break;
            case order_confirmReceipt:
                tvTitle.setText("请确认您已经收到货物，否则可能财物两空！");
                break;
        }
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onClick();
                }
                popupWindow.dismiss();
            }
        });
    }

    public interface DialogListener{
        void onClick();
    }

    private static int integer = 0;
    public static void shouCartNumber(final Context act, final int stock, int num, final CartNumberListener listener){
        View wh = LayoutInflater.from(act).inflate(R.layout.p_cart_number, null);
        final WPopupWindow popupWindow = new WPopupWindow(wh);
        popupWindow.showAtLocation(wh, Gravity.CENTER, 0, 0);
        final EditText etNumber = wh.findViewById(R.id.et_number);
        etNumber.setText(num + "");
        wh.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardUtils.hideSoftInput(etNumber);
                popupWindow.dismiss();
            }
        });

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                etNumber.setFocusable(true);
                etNumber.setFocusableInTouchMode(true);
                etNumber.requestFocus();
                KeyboardUtils.showSoftInput(etNumber);
            }
        }, 200);

        wh.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = etNumber.getText().toString().trim();
                if (!StringUtils.isEmpty(number)){
                    integer = Integer.valueOf(number);
                }
                if (integer > stock){
                    ToastUtils.showShort("大于当前库存");
                    return;
                }
                if (integer == 0){
                    ToastUtils.showShort("不能为0");
                    return;
                }
                if (listener != null){
                    listener.onClick(integer);
                }
                KeyboardUtils.hideSoftInput(etNumber);
                popupWindow.dismiss();
            }
        });

        wh.findViewById(R.id.rf_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = etNumber.getText().toString().trim();
                if (!StringUtils.isEmpty(number)){
                    integer = Integer.valueOf(number);
                }
                if (integer > 1){
                    etNumber.setText((--integer) + "");
                }
            }
        });
        wh.findViewById(R.id.rf_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = etNumber.getText().toString().trim();
                if (!StringUtils.isEmpty(number)){
                    integer = Integer.valueOf(number);
                }
                etNumber.setText((++integer) + "");
            }
        });
    }

    public interface CartNumberListener{
        void onClick(int integer);
    }

}
