package com.fanwang.sg.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.fanwang.sg.weight.AddressPickTask;

import cn.addapp.pickers.entity.City;
import cn.addapp.pickers.entity.County;
import cn.addapp.pickers.entity.Province;

/**
 * 作者：yc on 2018/9/13.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class PickTaskTool {

    public static void onAddressPicker(Context act, AddressPickTask.Callback callback) {
        AddressPickTask task = new AddressPickTask((Activity) act);
        task.setHideProvince(false);
        task.setHideCounty(false);
        task.setCallback(callback);
        task.execute("广东省", "广州市", "天河区");
    }

}
