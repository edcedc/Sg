package com.fanwang.sg.utils;

import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 作者：yc on 2018/6/28.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class DateUtils {

    /**
     * 日期转换成秒数
     * */
    public static long getSecondsFromDate(String expireDate){
        if(expireDate==null||expireDate.trim().equals(""))
            return 0;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=null;
        try{
            date=sdf.parse(expireDate);
            return (long)(date.getTime()/1000);
        }
        catch(ParseException e)
        {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     *  加半小时
     * @param day
     * @return
     */
    public static String addDateMinut(String day, int minute)//返回的是字符串型的时间，输入的
    //是String day, int x
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
        //引号里面个格式也可以是 HH:mm:ss或者HH:mm等等，很随意的，不过在主函数调用时，要和输入的变
        //量day格式一致
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);// 24小时制
        date = cal.getTime();
        cal = null;
        return format.format(date);

    }
    /**
     *  减多少分钟
     * @param day
     * @return
     */
    public static String reduceDateMinut(String day, int minute, String simple)//返回的是字符串型的时间，输入的
    //是String day, int x
    {
        SimpleDateFormat format = new SimpleDateFormat(simple);// 24小时制
        //引号里面个格式也可以是 HH:mm:ss或者HH:mm等等，很随意的，不过在主函数调用时，要和输入的变
        //量day格式一致
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, -minute);// 24小时制
        date = cal.getTime();
        cal = null;
        return format.format(date);

    }

    /**
     *  项目需求获取7天
     * @return
     */
    public static List<String> exportOneweekinfo(){
        List<String> list = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        for (int i = 1;i < 8;i++){
            c.add(Calendar.DATE, i);
            Date d = c.getTime();
            String day = format.format(d);
            LogUtils.e(day);
            list.add(day);
        }
        return list;
    }

    /**
     * 判断time是否在from，to之内
     * @param time 指定日期
     * @param from 开始日期
     * @param to   结束日期
     * @return
     */
    public static boolean belongCalendar(Date time, Date from, Date to) {
        Calendar date = Calendar.getInstance();
        date.setTime(time);
        Calendar after = Calendar.getInstance();
        after.setTime(from);
        Calendar before = Calendar.getInstance();
        before.setTime(to);
        if (date.after(after) && date.before(before)) {
            return true;
        } else {
            return false;
        }
    }


    // java日期增加或减少小时，当months为负数时为减少的小时数，当days为正整数时为增加的小时数
    public static Date addHours(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hours);
        Date newDay = cal.getTime();
        return newDay;
    }

    /***
     * 两个日期相差多少秒
     * @param dateStr1  :yyyy-MM-dd HH:mm:ss
     * @param dateStr2 :yyyy-MM-dd HH:mm:ss

     */
    public static long getTimeDelta(String dateStr1,String dateStr2){
        long between = 0;
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date begin = dfs.parse(dateStr1);
            java.util.Date end = dfs.parse(dateStr2);
             between=(end.getTime()-begin.getTime()) / 1000;//除以1000是为了转换成秒
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (long) between * 1000;
    }


    /**
     *  加几天
     */
    public static String addTime(String time, int dayNumber){
        if (StringUtils.isEmpty(time))return "";
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sf.parse(time); // 指定日期
            Date newDate = addDate(date, dayNumber); // 指定日期加上20天
            return sf.format(newDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    private static Date addDate(Date date,long day) throws ParseException {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
        time+=day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }


    public static String parseDate(String createTime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            Date date = format.parse(createTime);
            String format1 = format.format(addDate(date, -1));
            String format2 = format.format(addDate(date, 0));
            String format3 = format.format(addDate(date, +1));
            String format4 = format.format(addDate(date, +2));
            if (createTime.equals(format1)){
                return "昨天";
            }else if (createTime.equals(format2)){
                return "今天";
            }else if (createTime.equals(format3)){
                return "明天";
            }else if (createTime.equals(format4)){
                return "后天";
            }else {
                String[] split = createTime.split(" ");
                return split[0];
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return createTime;
    }
}
