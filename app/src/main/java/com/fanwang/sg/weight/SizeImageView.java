package com.fanwang.sg.weight;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/9/11.
 */

public class SizeImageView extends android.support.v7.widget.AppCompatImageView {
    public SizeImageView(Context context) {
        super(context);
    }

    public SizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SizeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int mWidth;
    private int mHeight;
    private boolean mIsScale = false;

    /**
     * 注意：宽高相等时 width：height = 1:1 isScale = true
     * @param width 宽度
     * @param height 高度
     * @param isScale 是否改变
     */
    public void setWH(int width, int height, boolean isScale){
        this.mWidth = width;
        this.mHeight = height;
        this.mIsScale = isScale;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取真实的图片
        if (!mIsScale) {
            Drawable drawable = getDrawable();
            if (drawable != null) {
                //获取真实的宽
                int width = drawable.getMinimumWidth();
                //获取真实的高
                int height = drawable.getMinimumHeight();
                //计算宽和高的比例
                float scale = (float) width / height;
                //获取测量宽的规则
                int withsize = MeasureSpec.getSize(widthMeasureSpec);
                //按照比例计算高的测量规则
                int heightsize = (int) (withsize / scale);
                //设置高的测量规则 第一个值是按照比例计算的高 第二个参数是测量模式 精确
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightsize, MeasureSpec.EXACTLY);
                System.out.println("hello image:" + width + ", " + height + ", " + scale);

            }
        }else {
            if (mWidth == mHeight){
                heightMeasureSpec = widthMeasureSpec;
            }else {
                //获取真实的图片
                if (mWidth != 0) {
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
                }
                if (mHeight != 0) {
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
