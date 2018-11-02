package com.fanwang.sg.weight;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * 解决ScrollView/listview嵌套GridView的情况，由于这两款控件都自带滚动条，当他们碰到一起的时候便会出问题，即GridView会显示不全
 *
 * @author lijin
 */
public class WithScrollGridView extends GridView {
    private static final String TAG = "AutoGridView";
    private int numColumnsID;
    private int previousFirstVisible;
    private int numColumns = 1;
    public WithScrollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public WithScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WithScrollGridView(Context context) {
        super(context);
    }

    /**
     * 设置不滚动
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

    OnTouchInvalidPositionListener mTouchInvalidPosListener;

    public interface OnTouchInvalidPositionListener {
        /**
         * motionEvent 可使用 MotionEvent.ACTION_DOWN 或者
         * MotionEvent.ACTION_UP等来按需要进行判断
         *
         * @return 是否要终止事件的路由
         */
        boolean onTouchInvalidPosition(int motionEvent);
    }

    /**
     * 点击空白区域时的响应和处理接口
     */
    public void setOnTouchInvalidPositionListener(
            OnTouchInvalidPositionListener listener) {
        mTouchInvalidPosListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTouchInvalidPosListener == null) {
            return super.onTouchEvent(event);
        }
        if (!isEnabled()) {
            // A disabled view that is clickable still consumes the touch
            // events, it just doesn't respond to them.
            return isClickable() || isLongClickable();
        }
        final int motionPosition = pointToPosition((int) event.getX(),
                (int) event.getY());
        if (motionPosition == INVALID_POSITION) {
            super.onTouchEvent(event);
            return mTouchInvalidPosListener.onTouchInvalidPosition(event
                    .getActionMasked());
        }
        return super.onTouchEvent(event);
    }

    /**
     * Sets the numColumns based on the attributeset
     */
    private void init(AttributeSet attrs) {
        // Read numColumns out of the AttributeSet
        int count = attrs.getAttributeCount();
        if(count > 0) {
            for(int i = 0; i < count; i++) {
                String name = attrs.getAttributeName(i);
                if(name != null && name.equals("numColumns")) {
                    // Update columns
                    this.numColumnsID = attrs.getAttributeResourceValue(i, 1);
                    updateColumns();
                    break;
                }
            }
        }
        Log.d(TAG, "numColumns set to: " + numColumns);
    }


    /**
     * Reads the amount of columns from the resource file and
     * updates the "numColumns" variable
     */
    private void updateColumns() {
        this.numColumns = getContext().getResources().getInteger(numColumnsID);
    }

    @Override
    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        super.setNumColumns(numColumns);

        Log.d(TAG, "setSelection --> " + previousFirstVisible);
        setSelection(previousFirstVisible);
    }

    @Override
    protected void onLayout(boolean changed, int leftPos, int topPos, int rightPos, int bottomPos) {
        super.onLayout(changed, leftPos, topPos, rightPos, bottomPos);
        setHeights();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        updateColumns();
        setNumColumns(this.numColumns);
    }

    @Override
    protected void onScrollChanged(int newHorizontal, int newVertical, int oldHorizontal, int oldVertical) {
        // Check if the first visible position has changed due to this scroll
        int firstVisible = getFirstVisiblePosition();
        if(previousFirstVisible != firstVisible) {
            // Update position, and update heights
            previousFirstVisible = firstVisible;
            setHeights();
        }

        super.onScrollChanged(newHorizontal, newVertical, oldHorizontal, oldVertical);
    }

    /**
     * Sets the height of each view in a row equal to the height of the tallest view in this row.
     * @param firstVisible The first visible position (adapter order)
     */
    private void setHeights() {
        ListAdapter adapter = getAdapter();

        if(adapter != null) {
            for(int i = 0; i < getChildCount(); i+=numColumns) {
                // Determine the maximum height for this row
                int maxHeight = 0;
                for(int j = i; j < i+numColumns; j++) {
                    View view = getChildAt(j);
                    if(view != null && view.getHeight() > maxHeight) {
                        maxHeight = view.getHeight();
                    }
                }
                //Log.d(TAG, "Max height for row #" + i/numColumns + ": " + maxHeight);

                // Set max height for each element in this row
                if(maxHeight > 0) {
                    for(int j = i; j < i+numColumns; j++) {
                        View view = getChildAt(j);
                        if(view != null && view.getHeight() != maxHeight) {
                            view.setMinimumHeight(maxHeight);
                        }
                    }
                }
            }
        }
    }

}