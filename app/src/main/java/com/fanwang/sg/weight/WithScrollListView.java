package com.fanwang.sg.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class WithScrollListView extends ListView {

	public WithScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public WithScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public WithScrollListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 设置不滚动
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec); 

	}

}
