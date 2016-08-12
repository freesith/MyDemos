package com.example.mvpdemo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by wangchao on 16/8/12.
 */
public class NarrowLayout extends FrameLayout {

    private ViewDragHelper mViewDragHelper;

    private NarrowViewPager mViewPager;

    public NarrowLayout(Context context) {
        super(context);
        init();
    }

    public NarrowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NarrowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NarrowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean handled = false;
        if (isEnabled()) {
            handled = mViewDragHelper.shouldInterceptTouchEvent(ev);
        } else {
            mViewDragHelper.cancel();
        }
        return !handled ? super.onInterceptTouchEvent(ev) : handled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this,10000f,new DragCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mViewPager = (NarrowViewPager) getChildAt(0);
    }

    class DragCallback extends ViewDragHelper.Callback{

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mViewPager == child;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.i("xx","clampVertical   top = " + top + "       dy = " + dy);
                mViewPager.childScrollUp(top);
            return 0;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return getHeight();
        }
    }

}
