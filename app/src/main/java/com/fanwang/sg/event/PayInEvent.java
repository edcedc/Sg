package com.fanwang.sg.event;

/**
 * 作者：yc on 2018/10/22.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class PayInEvent {

    public boolean isPay = false;

    public PayInEvent(boolean isPay) {
        this.isPay = isPay;
    }

    public PayInEvent() {
    }

}
