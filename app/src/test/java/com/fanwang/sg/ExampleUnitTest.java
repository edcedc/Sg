package com.fanwang.sg;

import com.blankj.utilcode.util.LogUtils;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        String strTime1 = "2018-09-29 19:15:00";
        String strTime2 = "2018-09-29 19:20:00";
//        getTimeDifference(strTime1, strTime2);
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date= myFormatter.parse( strTime1);
        java.util.Date mydate= myFormatter.parse( strTime2);
        long day=(date.getTime()-mydate.getTime())/(24*60*60*1000);
        System.out.println( "相差的日期: " + day);


        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date begin=dfs.parse("2018-09-29 19:20:00");
        java.util.Date end = dfs.parse("2018-09-29 19:28:00");
        long between=(end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒

        System.out.println( between);

    }

    /***
     * @comments 计算两个时间的时间差
     * @param strTime1
     * @param strTime2
     */
    private void getTimeDifference(String strTime1,String strTime2) {
        //格式日期格式，在此我用的是"2018-01-24 19:49:50"这种格式
        //可以更改为自己使用的格式，例如：yyyy/MM/dd HH:mm:ss 。。。
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date now = df.parse(strTime1);
            Date date=df.parse(strTime2);
            long l=now.getTime()-date.getTime();       //获取时间差
            long day=l/(24*60*60*1000);
            long hour=(l/(60*60*1000)-day*24);
            long min=((l/(60*1000))-day*24*60-hour*60);
            long s=(l/1000-day*24*60*60-hour*60*60-min*60);
            System.out.println(""+day+"天"+hour+"小时"+min+"分"+s+"秒");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}