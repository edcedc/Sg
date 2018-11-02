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

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.fanwang.sg.R;
import com.fanwang.sg.base.User;
import com.fanwang.sg.controller.CloudApi;
import com.fanwang.sg.event.RongcloudInEvent;
import com.fanwang.sg.rcloud.RCloudTool;
import com.fanwang.sg.view.MainFrg;
import com.yanzhenjie.sofia.Sofia;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by Administrator on 2018/5/9.
 *  启动页
 */

public class MainActivity extends SupportActivity {

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
        if (findFragment(MainFrg.class) == null) {
            loadRootFragment(R.id.fl_container, MainFrg.newInstance());
        }
        EventBus.getDefault().register(this);
        ajaxRongGetToken(User.getInstance().getUserInfoObj());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainRongcloudInEvent(RongcloudInEvent event){
        ajaxRongGetToken(User.getInstance().getUserInfoObj());
    }

    /**
     * 获取融云toKen
     */
    private void ajaxRongGetToken(JSONObject userExtend) {//不要问为什么有些参数不一致，找后台
        if (userExtend == null){
            userExtend = User.getInstance().getUserObj();
        }
        if (userExtend != null) {
            String username = userExtend.optString("username");
            if (username.equals("null") || StringUtils.isEmpty(username)){
                username = "路人甲";
            }
            String head = userExtend.optString("head");
            if (head.equals("null") || StringUtils.isEmpty(head)){
                head = "http://wx1.sinaimg.cn/large/006Qr37igy1fwrf1ccp1ej30hs0mijtd.jpg";
            }else {
                head = CloudApi.SERVLET_IMG_URL + head;
            }
            String userId = userExtend.optString("userId");
            if (StringUtils.isEmpty(userId)){
                userId = userExtend.optString("id");
            }
            String token = userExtend.optString("ryToken");
            if (StringUtils.isEmpty(token)){
                token = userExtend.optString("token");
            }
            LogUtils.e(userId, username, head);
            RCloudTool.getInstance().InitUser(MainActivity.this, userId, username, head, token);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
