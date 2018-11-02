package com.fanwang.sg.utils;


import com.zaaach.toprightmenu.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edison on 2018/4/27.
 */

public class DataListTool {

    /**
     *  退货退款
     */
    public static List<MenuItem> getOrderRefundList() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("已收到货"));
        menuItems.add(new MenuItem("未收到货"));
        return menuItems;
    }

    /**
     *  物流
     */
    public static List<MenuItem> getLogisticsList() {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("顺丰快递"));
        menuItems.add(new MenuItem("圆通快递"));
        menuItems.add(new MenuItem("申通快递"));
        return menuItems;
    }

}
