package com.fanwang.sg.utils;

/**
 * Created by yc on 2017/12/25.
 */

import android.app.Activity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by Administrator on 2016/11/10.
 * 要注意包不要导错了
 */

public class HtmlFormat {

    public static String getNewContent(String htmltext) {
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();
    }

    /**
     *  可以获取HTML宽高
     * @param act
     * @param htmltext
     * @return
     */
    private static int mWidth;
    public static String getNewContent(final Activity act, String htmltext) {
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (final Element element : elements) {

        }
        return doc.toString();
    }
}

