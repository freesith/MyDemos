package com.example.mvpdemo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

    private static final int FORCE = 0;

    private static final float GRAVITY = (float) 30;


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
            bubbleView.setVelocityX(80);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(200,200);

            addView(bubbleView,params);
            mViewList.add(bubbleView);
            layoutBubble();
        }
        return true;
    }

    private void layoutBubble() {

        checkKnock();

        for (BubbleView view: mViewList) {

            float dy = getMeasuredHeight() / 2 - view.getLocationY();
            float dx = getMeasuredWidth() / 2 - view.getLocationX();

            float l = (float) Math.pow(dx * dx + dy * dy, 0.5);

            float velocityX = view.getVelocityX();
            float velocityY = view.getVelocityY();


//            Log.i("BubbleContainer","layoutBubble vx = " + velocityX + "     vy = " + velocityY);

            view.setForceX( GRAVITY  * dx / l + (velocityX > 0 ? -FORCE * velocityX * velocityX  : view.getVelocityX() ==0 ? 0 : FORCE* velocityX * velocityX));
            view.setForceY( GRAVITY  * dy / l + (velocityY > 0 ? -FORCE * velocityY * velocityY  : view.getVelocityY() ==0 ? 0 : FORCE* velocityY * velocityY));

            view.move(SystemClock.elapsedRealtime());
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutBubble();
            }
        },20);
    }


    private double getDistance(BubbleView a, BubbleView b) {

        float dx = a.getLocationX() - b.getLocationX();
        float dy = a.getLocationY() - b.getLocationY();

        return Math.pow(dx * dx + dy * dy, 0.5);
    }

    private void checkKnock() {
        int count = getChildCount();
        for (int i = 0; i< count; i++) {
            for (int j = i +1; j < count; j ++) {
                BubbleView a = (BubbleView) getChildAt(i);
                BubbleView b = (BubbleView) getChildAt(j);

                if (getDistance(a,b) <= a.getR() + b.getR()) {

                    float velocityX = a.getVelocityX();
                    float velocityY = a.getVelocityY();

                    a.setVelocityX(b.getVelocityX());
                    a.setVelocityY(b.getVelocityY());

                    b.setVelocityX(velocityX);
                    b.setVelocityY(velocityY);

                    a.move(SystemClock.elapsedRealtime());
                    b.move(SystemClock.elapsedRealtime());
                }
            }
        }
    }
}
