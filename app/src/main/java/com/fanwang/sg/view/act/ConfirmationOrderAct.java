package com.fanwang.sg.view.act;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.fanwang.sg.R;
import com.fanwang.sg.bean.DataBean;
import com.fanwang.sg.view.ConfirmationOrderFrg;
import com.fanwang.sg.view.GoodsDetailsFrg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.sofia.Sofia;

import java.lang.reflect.Type;
import java.util.ArrayList;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * 作者：yc on 2018/10/11.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  确认订单
 */

public class ConfirmationOrderAct extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_main);
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT)
                .navigationBarBackground(Color.TRANSPARENT);

        ConfirmationOrderFrg frg = ConfirmationOrderFrg.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("id", getIntent().getStringExtra("id"));
        bundle.putString("list", getIntent().getStringExtra("list"));
        bundle.putString("orderNo", getIntent().getStringExtra("orderNo"));
        bundle.putInt("type", getIntent().getIntExtra("type", 0));
        bundle.putInt("flag", getIntent().getIntExtra("flag", 0));
        bundle.putInt("isCredited", getIntent().getIntExtra("isCredited", 0));
        frg.setArguments(bundle);
        if (findFragment(ConfirmationOrderFrg.class) == null) {
            loadRootFragment(R.id.fl_container, frg);
        }
    }

    /**
     * Android 点击EditText文本框之外任何地方隐藏键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {

                return true;
            }
        }
        return false;
    }
}
