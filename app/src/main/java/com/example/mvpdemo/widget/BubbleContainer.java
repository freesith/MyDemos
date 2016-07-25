package com.example.mvpdemo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchao on 16/7/22.
 */
public class BubbleContainer extends FrameLayout {

    private boolean isRun;

    private List<BubbleView> mViewList = new ArrayList<>();

    private static final int FORCE = 100;

    private static final float GRAVITY = (float) 0.01;


    public BubbleContainer(Context context) {
        super(context);
    }

    public BubbleContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BubbleContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            BubbleView bubbleView = new BubbleView(getContext());
            bubbleView.setLocationX(event.getX());
            bubbleView.setLocationY(event.getY());

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(200,200);

            addView(bubbleView,params);
            mViewList.add(bubbleView);
            layoutBubble();
        }
        return true;
    }

    private void layoutBubble() {

        for (BubbleView view: mViewList) {

            float dy = getMeasuredHeight() / 2 - view.getLocationY();
            float dx = getMeasuredWidth() / 2 - view.getLocationX();

            float l = (float) Math.pow(dx * dx + dy * dy, 0.5);

            view.setForceX((float) ((Math.max(GRAVITY * l /*- FORCE*/, 0)) * dx / l));
            view.setForceY((float) ((Math.max(GRAVITY * l/* - FORCE*/, 0)) * dy / l));

            view.move(SystemClock.elapsedRealtime());
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutBubble();
            }
        },20);
    }
}
