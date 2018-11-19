package com.fanwang.sg.weight;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.TextView;

import com.fanwang.sg.R;

/**
 * 作者：yc on 2018/8/30.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class StrikethroughTextView extends android.support.v7.widget.AppCompatTextView{

    private Context mContext;

    public StrikethroughTextView(Context context) {
        super(context);
        init(context);
    }

    public StrikethroughTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StrikethroughTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        this.mContext = context;

        getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);// 设置中划线并加清晰
        setTextColor(ContextCompat.getColor(context,R.color.black_3E3A39));
        setTextSize(10);
    }


    public void setTextClor(int color){
        setTextColor(mContext.getColor(color));
    }

}
