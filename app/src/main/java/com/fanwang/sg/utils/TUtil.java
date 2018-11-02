package com.fanwang.sg.utils;

import com.blankj.utilcode.util.LogUtils;

import java.lang.reflect.ParameterizedType;

/**
 * 类转换初始化
 */
public class TUtil {
    public static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
        } catch (InstantiationException e) {
            LogUtils.e(e.getMessage());
        } catch (IllegalAccessException e) {
            LogUtils.e(e.getMessage());
        } catch (ClassCastException e) {
            LogUtils.e(e.getMessage());
        }
        return null;
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
