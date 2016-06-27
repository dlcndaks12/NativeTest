package com.almond.nativetest;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 2016-06-24.
 */
public class HeightViewPager extends ViewPager {
    public HeightViewPager(Context context) {
        super(context);
    }

    public HeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        boolean wrapHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST;
        /*if (wrapHeight) {
            Log.e("온미쥬어 이프안 : ", "도착");

            int width = getMeasuredWidth();
            int height = getMeasuredHeight();

            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

            if(getChildCount() > 0) {
                View firstChild = getChildAt(0);

                firstChild.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));

                height = firstChild.getMeasuredHeight();
            }

            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

            Log.e("동작하니?? : ", ""+height);

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }*/

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

        if (getChildCount() > 0) {
            View firstChild = getChildAt(0);

            firstChild.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));

            height = firstChild.getMeasuredHeight();
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
