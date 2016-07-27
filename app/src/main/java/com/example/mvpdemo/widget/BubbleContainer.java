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

    private static final float LOSS = 1f;

    private static final float GRAVITY = (float) 30;

    private boolean knocked;


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
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(200, 200);

            addView(bubbleView, params);
            mViewList.add(bubbleView);
            layoutBubble();
        }
        return true;
    }

    private void layoutBubble() {


        if ( !knocked) {
            checkKnock();
        }

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
        }, 20);
    }


    private double getDistance(BubbleView a, BubbleView b) {

        float dx = a.getLocationX() - b.getLocationX();
        float dy = a.getLocationY() - b.getLocationY();

        return Math.sqrt(dx * dx + dy * dy);
    }

    private void checkKnock() {

        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            BubbleView a = (BubbleView) getChildAt(i);
//            float dy = getMeasuredHeight() / 2 - a.getLocationY();
//            float dx = getMeasuredWidth() / 2 - a.getLocationX();
//
//            float l = (float) Math.sqrt(dx * dx + dy * dy);
//
//            float velocityX = a.getVelocityX();
//            float velocityY = a.getVelocityY();
//
//
////            Log.i("BubbleContainer","layoutBubble vx = " + velocityX + "     vy = " + velocityY);
//
//            a.setForceX(GRAVITY * dx / l + (velocityX > 0 ? -FORCE * velocityX * velocityX : a.getVelocityX() == 0 ? 0 : FORCE * velocityX * velocityX));
//            a.setForceY(GRAVITY * dy / l + (velocityY > 0 ? -FORCE * velocityY * velocityY : a.getVelocityY() == 0 ? 0 : FORCE * velocityY * velocityY));
//
//            a.move(SystemClock.elapsedRealtime());

            for (int j = i + 1; j < count; j++) {

                BubbleView b = (BubbleView) getChildAt(j);

                if (Math.abs(a.getLocationX() - b.getLocationX()) > a.getR() + b.getR() || Math.abs(a.getLocationY() - b.getLocationY()) > b.getR() + a.getR()) {
                    continue;
                }

                if (getDistance(a, b) <= a.getR() + b.getR() + 2
                        && ((b.getLocationY() - a.getLocationY()) * (b.getVelocityY() - a.getVelocityY()) < 0 && Math.abs(a.getLocationX() - b.getLocationX()) <a.getR() + b.getR()
                            || b.getLocationX() - a.getLocationX() * (b.getVelocityX() - a.getVelocityX()) < 0 && Math.abs(a.getVelocityY() - b.getLocationY()) < a.getR() + b.getR())) {

                    knocked = true;
                    long time = SystemClock.elapsedRealtime();

                    Log.i("xx","碰撞前 a 的vx = " + a.getVelocityX() + "    vy = " + a.getVelocityY() + "   a 的lx = " + a.getLocationX() + "    ly = " + a.getLocationY());
                    Log.i("xx","碰撞前 b 的vx = " + b.getVelocityX() + "    vy = " + b.getVelocityY() + "   b 的lx = " + b.getLocationX() + "    ly = " + b.getLocationY());
                    double alpha = Math.atan(a.getVelocityY() / a.getVelocityX());
                    double beta = Math.atan(b.getVelocityY() / b.getVelocityX());

                    double cosAlpha = Math.cos(alpha);
                    double sinAlpha = Math.sin(alpha);
                    double cosBeta = Math.cos(beta);
                    double sinBeta = Math.sin(beta);


                    double va1 = a.getVelocityX() * cosAlpha + a.getVelocityY() * sinAlpha; //a 连线方向的速度
                    double va2 = a.getVelocityX() * sinAlpha + a.getVelocityY() * cosAlpha; //a 连线垂直方向的速度

                    double vb1 = b.getVelocityX() * cosBeta + b.getVelocityY() * sinBeta;
                    double vb2 = b.getVelocityX() * sinBeta + b.getVelocityY() * cosBeta;

                    double _va1 = va1 - b.getWeight() * (1 + LOSS) / (b.getWeight() + a.getWeight()) * (vb1 - va1); //碰撞后a连线方向的速度
                    _va1 = ((1 + LOSS) * b.getWeight() * vb1 + a.getWeight() * va1 - b.getWeight() * LOSS * va1) / (a.getWeight()  + b.getWeight());
                    double _va2 = va2;

                    double _vb1 = vb1 - a.getWeight() * (1 + LOSS) / (b.getWeight() + a.getWeight()) * (va1 - vb1);
                    _vb1 = ((1 + LOSS) * a.getWeight() * va1 + b.getWeight() * vb1 - a.getWeight() * LOSS * vb1) / (a.getWeight()  + b.getWeight());
                    double _vb2 = vb2;

                    float vax = (float) (_va1 * cosAlpha + _va2 * sinAlpha);
                    float vay = (float) (_va1 * sinAlpha + _va2 * cosAlpha);

                    float vbx = (float) (_vb1 * cosBeta + _vb2 * sinBeta);
                    float vby = (float) (_vb1 * sinAlpha + _vb2 * cosBeta);

                    a.setVelocityX(vax);
                    a.setVelocityY(vay);

                    b.setVelocityX(vbx);
                    b.setVelocityY(vby);



//                    float velocityX = a.getVelocityX();
//                    float velocityY = a.getVelocityY();
//
//                    a.setVelocityX(b.getVelocityX());
//                    a.setVelocityY(b.getVelocityY());
//
//                    b.setVelocityX(velocityX);
//                    b.setVelocityY(velocityY);

                    Log.i("xx", "检测到碰撞,计算时间为" + (SystemClock.elapsedRealtime() - time) + "   i = " + i + "   j = " + j);
                    Log.i("xx","碰撞后 a 的vx = " + a.getVelocityX() + "    vy = " + a.getVelocityY() + "   a 的lx = " + a.getLocationX() + "    ly = " + a.getLocationY());
                    Log.i("xx","碰撞后 b 的vx = " + b.getVelocityX() + "    vy = " + b.getVelocityY() + "   b 的lx = " + b.getLocationX() + "    ly = " + b.getLocationY());
                    Log.i("xx","================");
                    a.move(SystemClock.elapsedRealtime());
                    b.move(SystemClock.elapsedRealtime());
                }
            }
        }
    }

    private void checkKnock1() {
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
