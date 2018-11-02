package com.zaaach.toprightmenu;

import android.app.Activity;
import android.view.View;

import java.util.List;


/**
 * Created by yc on 2018/1/17.
 */

public class TopRightMenuTool {

    public static void TopRightMenu(Activity act, List<MenuItem> menuItems, View view, TopRightMenu.OnMenuItemClickListener listener) {
        TopRightMenu mTopRightMenu = new TopRightMenu(act);
        mTopRightMenu
                .setHeight(DisplayUtil.dip2px(act, menuItems.size() >= 5 ? 5 * 45 : menuItems.size() * 32))     //默认高度480
                .setWidth(view.getWidth())
                .showIcon(false)
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(listener)
                .showAsDropDown(view, 0, 0);
    }

    public static void TopRightMenu(Activity act, List<MenuItem> menuItems, View view, int xoff, TopRightMenu.OnMenuItemClickListener listener) {
        new TopRightMenu(act)
                .setHeight(DisplayUtil.dip2px(act, menuItems.size() >= 5 ? 5 * 41 : menuItems.size() * 41))     //默认高度480
                .setWidth(view.getWidth())
                .showIcon(false)
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(listener)
                .showAsDropDown(view, xoff, 0);
    }

    public static void TopRightMenu(Activity act, List<MenuItem> menuItems, View view, int width, int xoff, TopRightMenu.OnMenuItemClickListener listener) {
        new TopRightMenu(act)
                .setHeight(DisplayUtil.dip2px(act, menuItems.size() >= 5 ? 5 * 41 : menuItems.size() * 41))     //默认高度480
                .setWidth(width)
                .showIcon(false)
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(listener)
                .showAsDropDown(view, xoff, 0);
    }


}
