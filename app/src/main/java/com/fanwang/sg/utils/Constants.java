package com.fanwang.sg.utils;

import android.os.Environment;

/**
 * Created by yc on 2017/9/30.
 */

public class Constants {

   public static final int ITEMCOUNT = 10;
   public static final int pageSize = 10;
   public static final int pageSize2 = 20;

   public static final int collage_process = 1001;//正在秒拼
   public static final int collage_preview = 1002;//拼团预告

   public static final int address_edit = 0;//编辑地址
   public static final int address_save = 1;//新增地址

   public static final int day_min = 86400;//一天秒数

   public static final int refund = 0;//退货退款
   public static final int return_goods = 1;//仅退款
   public static final int return_paragraph = 2;//商家未发货直接退款


   public static final String ShareID = "5bc9f7e8f1f556233100068d";

   public static final String WX_APPID = "wx99dda0a17471bc88";
   public static final String WX_SECRER = "5e8b64483b9c6122922f1b6262b82a1a";
   public static final String QQ_APPID = "1107890734";
   public static final String QQ_KEY = "QF8dwXayGiU1ybmF";

   public static final String mainPath = Environment.getExternalStorageDirectory() + "/shegou/";
   public static final String imgUrl = mainPath + "img/";

   public static final String ZFB_PAY = "2018102061789029";

   //融云
   public static final String RY_APPKEY = "pgyu6atqpel5u";
   public static final String RY_SECRET = "XuwxOXYKxTECz";

}
