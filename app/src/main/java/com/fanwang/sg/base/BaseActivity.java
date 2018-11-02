package com.fanwang.sg.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.sg.R;
import com.yanzhenjie.sofia.Sofia;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * 作者：yc on 2018/7/25.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public abstract class BaseActivity<VB extends ViewDataBinding> extends SupportActivity {

    protected Activity act;
    protected VB mB;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mB = DataBindingUtil.setContentView(this,this.bindLayout());;
        act = this;
        // 初始化参数
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        initParms(bundle);

        initView();

        Sofia.with(act)
                .statusBarLightFont()
                .invasionStatusBar()
                .statusBarBackgroundAlpha(0)
                .statusBarBackground(ContextCompat.getColor(act, R.color.colorPrimary));
    }

    protected abstract int bindLayout();

    protected abstract void initParms(Bundle bundle);

    protected abstract void initView();

    protected void setCenterTitle(String title){
        title(title, 0, null, -1);
    }
    protected void setTitle(String title){
        title(title, 1, null, -1);
    }
    protected void setTitle(String title, String right){
        title(title, 2, right, -1);
    }
    protected void setTitle(String title, int rightImg){
        title(title, 1, null, rightImg);
    }

    private void title(String title, int type, String rightText, int img) {
        final AppCompatActivity mAppCompatActivity = (AppCompatActivity) act;
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView topTitle = findViewById(R.id.top_title);
        TextView topRight = findViewById(R.id.top_right);
        FrameLayout topRightFy = findViewById(R.id.top_right_fy);
        //需要调用该函数才能设置toolbar的信息
        mAppCompatActivity.setSupportActionBar(toolbar);
        switch (type){
            case 0:
                mAppCompatActivity.getSupportActionBar().setTitle("");
                topTitle.setVisibility(View.VISIBLE);
                topTitle.setText(title);
                toolbar.setNavigationIcon(null);
                break;
            case 1:
                topTitle.setVisibility(View.GONE);
                mAppCompatActivity.getSupportActionBar().setTitle(title);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                break;
            case 2:
                topTitle.setVisibility(View.GONE);
                mAppCompatActivity.getSupportActionBar().setTitle(title);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                topRight.setText(rightText);
                topRightFy.setVisibility(View.VISIBLE);
                topRightFy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setOnRightClickListener();
                    }
                });
                break;
        }
    }

    protected void setOnRightClickListener() {

    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime;

    protected boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    private ProgressDialog dialog;

    public void showLoading() {
        mHandler.sendEmptyMessage(handler_load);
    }

    public void hideLoading() {
        mHandler.sendEmptyMessage(handler_hide);
    }

    public void onError(Throwable e) {
        mHandler.sendEmptyMessage(handler_hide);
        LogUtils.e(e.getMessage());
        showToast(e.getMessage());
    }

    private final int handler_load = 0;
    private final int handler_hide = 1;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case handler_load:
                    if (dialog != null && dialog.isShowing()) return;
                    dialog = new ProgressDialog(act);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setMessage("请求网络中...");
                    dialog.show();
                    break;
                case handler_hide:
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
            }
        }
    };

    protected void showToast(final String str){
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ToastUtils.setGravity(Gravity.CENTER, 0, 0);
//                ToastUtils.showLong(str);
                ToastUtils.showShort(str);
            }
        });
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        super.onDestroy();
    }
}
