package com.fanwang.sg.view.act;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.fanwang.sg.R;
import com.fanwang.sg.event.CameraInEvent;
import com.fanwang.sg.view.LoginFrg;
import com.fanwang.sg.view.MainFrg;
import com.fanwang.sg.view.SplashFrg;
import com.fanwang.sg.view.UpdateDataFrg;
import com.yanzhenjie.sofia.Sofia;

import org.greenrobot.eventbus.EventBus;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * Created by Administrator on 2018/5/9.
 *  启动页
 */

public class UpdateDataAct extends SwipeBackActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_main);
//        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
//            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
//        }
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT)
                .navigationBarBackground(Color.TRANSPARENT);
        if (findFragment(UpdateDataFrg.class) == null) {
            loadRootFragment(R.id.fl_container, UpdateDataFrg.newInstance());
        }

//        Sofia.with(this)
//                .invasionStatusBar()
//                .statusBarBackground(Color.TRANSPARENT)
//                .navigationBarBackground(Color.TRANSPARENT);
        setSwipeBackEnable(false);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            EventBus.getDefault().post(new CameraInEvent(requestCode, data));
        }
    }

}
