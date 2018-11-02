package com.fanwang.sg.event;

/**
 * 作者：yc on 2018/10/15.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class RefreshOrderInEvent {

    public int isRefund;

    public RefreshOrderInEvent(int isRefund) {
        this.isRefund = isRefund;
    }

    public RefreshOrderInEvent(){

    }

}
