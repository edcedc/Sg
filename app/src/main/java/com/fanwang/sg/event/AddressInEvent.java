package com.fanwang.sg.event;

/**
 * 作者：yc on 2018/9/25.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class AddressInEvent {

    public Object object;

    public int type;

    public int position;

    public AddressInEvent(Object object, int type, int position) {
        this.object = object;
        this.type = type;
        this.position = position;
    }
}
