package com.fanwang.sg.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by edison on 2018/4/12.
 */

public class CountDownTimerUtils extends CountDownTimer {

    private TextView view;

    private final long mMillisInFuture = 6000;

    private long mCountDownInterval = 1000;

    private Context act;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountDownTimerUtils(Context act, long millisInFuture, long countDownInterval, TextView view) {
        super(millisInFuture, countDownInterval);
//        this.mCountDownInterval = millisInFuture;
//        this.mCountDownInterval = countDownInterval;
        this.view = view;
        this.act = act;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        view.setEnabled(false);
        view.setEms(6);
        view.setText(millisUntilFinished / 1000 + "秒");
    }

    @Override
    public void onFinish() {
        if (act != null) {
            view.setEnabled(true);
            view.setEms(6);
            view.setText("获取验证码");
        }
    }
}
