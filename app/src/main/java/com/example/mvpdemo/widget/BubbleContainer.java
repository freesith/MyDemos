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
import java.util.Random;

/**
 * Created by wangchao on 16/7/22.
 */
public class BubbleContainer extends FrameLayout {

    private boolean isRun;

    private List<BubbleView> mViewList = new ArrayList<>();

    private static final int FORCE = 0;

    private static final float LOSS = 0.7f;

    private static final float GRAVITY = (float) 40;

    private Velocity va1 = new Velocity();
    private Velocity va2 = new Velocity();
    private Velocity vb1 = new Velocity();
    private Velocity vb2 = new Velocity();
    private Velocity _va1 = new Velocity();
    private Velocity _vb1 = new Velocity();

    private Random mRandom;


    public BubbleContainer(Context context) {
        super(context);
        init();
    }

    public BubbleContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BubbleContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void setRun(boolean run) {
        isRun = run;
    }

    private void init() {

        mRandom = new Random();

        for (int i = 0; i < 20; i ++) {
            BubbleView bubbleView = new BubbleView(getContext(), i);
            bubbleView.setLocationX(mRandom.nextInt(1440));
            bubbleView.setLocationY(mRandom.nextInt(2560));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(400, 400);

            addView(bubbleView, params);
            mViewList.add(bubbleView);
        }

        layoutBubble();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            BubbleView view = findBubble(event);
            if (view != null) {
                view.onSelect();
            }
        }
        return true;
    }


    private BubbleView findBubble(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        for (BubbleView view : mViewList) {
            if ((view.getLocationX() - x) * (view.getLocationX() - x) + (view.getLocationY() - y) * (view.getLocationY() - y) < view.getR() * view.getR()) {
                return view;
            }
        }

        return null;
    }

    private void layoutBubble() {

        checkKnock();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                layoutBubble();
            }
        },15);
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

            float yy = getMeasuredHeight() / 2 - a.getLocationY();
            float xx = getMeasuredWidth() / 2 - a.getLocationX();

            float l = (float) Math.sqrt(xx * xx + yy * yy);

            float velocityX = a.getVelocityX();
            float velocityY = a.getVelocityY();

            a.setForceX(GRAVITY * xx / l + (velocityX > 0 ? -FORCE * velocityX * velocityX : a.getVelocityX() == 0 ? 0 : FORCE * velocityX * velocityX));
            a.setForceY(GRAVITY * yy / l + (velocityY > 0 ? -FORCE * velocityY * velocityY : a.getVelocityY() == 0 ? 0 : FORCE * velocityY * velocityY));

            a.move(SystemClock.elapsedRealtime());

            long startTime = SystemClock.elapsedRealtime();

            for (int j = i + 1; j < count; j++) {

                BubbleView b = (BubbleView) getChildAt(j);

                if (Math.abs(a.getLocationX() - b.getLocationX()) > a.getR() + b.getR() || Math.abs(a.getLocationY() - b.getLocationY()) > b.getR() + a.getR()) {
                    continue;
                }


                double distance = getDistance(a, b);

                if (distance <= a.getR() + b.getR() + 2
                        /*&& ((b.getLocationY() - a.getLocationY()) * (b.getVelocityY() - a.getVelocityY()) < 0 && Math.abs(a.getLocationX() - b.getLocationX()) <a.getR() + b.getR()
                            || b.getLocationX() - a.getLocationX() * (b.getVelocityX() - a.getVelocityX()) < 0 && Math.abs(a.getVelocityY() - b.getLocationY()) < a.getR() + b.getR())*/) {

//                    Log.i("xx","检测到碰撞时的距离为 = " + distance);

                    float dd = (float) (a.getR() + b.getR() + 2 - distance);

                    if (b.getLocationY() >= a.getLocationY()) {
                        b.setLocationY(b.getLocationY() + dd / 2);
                        a.setLocationY(a.getLocationY() - dd / 2);
                    } else {
                        b.setLocationY(b.getLocationY() - dd / 2);
                        a.setLocationY(a.getLocationY() + dd / 2);
                    }

                    if (b.getLocationX() >= a.getLocationX()) {
                        b.setLocationX(b.getLocationX() + dd / 2);
                        a.setLocationX(a.getLocationX() - dd / 2);
                    } else {
                        b.setLocationX(b.getLocationX() - dd / 2);
                        a.setLocationX(a.getLocationX() + dd / 2);
                    }


                    long time = SystemClock.elapsedRealtime();

                    float dy = Math.abs(a.getLocationY() - b.getLocationY());
                    float dx = Math.abs(a.getLocationX() - b.getLocationX());

                    float dl = (float) Math.sqrt(dx * dx + dy * dy);

//                    Log.i("xx","碰撞前 a xy方向动能 = " + (a.getVelocityX() * a.getVelocityX() + a.getVelocityY() * a.getVelocityY()));
//                    Log.i("xx","碰撞前 b xy方向动能  = " + (b.getVelocityX() * b.getVelocityX() + b.getVelocityY() * b.getVelocityY()));
//                    Log.i("xx","转换前a速度为" + a.getVelocity());
//                    Log.i("xx", "转换前b速度为" + b.getVelocity());

                    float cosAlpha = /*Math.cos(alpha)*/ dx / dl;
                    float sinAlpha =/* Math.sin(alpha)*/ dy / dl;

                    va1.x = (a.getVelocity().x * cosAlpha - a.getVelocity().y * sinAlpha) * cosAlpha;
                    va1.y = -(a.getVelocity().x * cosAlpha - a.getVelocity().y * sinAlpha) * sinAlpha;

                    va2.x = (a.getVelocity().x * sinAlpha + a.getVelocity().y * cosAlpha) * sinAlpha;
                    va2.y = (a.getVelocity().x * sinAlpha + a.getVelocity().y * cosAlpha) * cosAlpha;

                    vb1.x = (b.getVelocity().x * cosAlpha - b.getVelocity().y * sinAlpha) * cosAlpha;
                    vb1.y =  -(b.getVelocity().x * cosAlpha - b.getVelocity().y * sinAlpha) * sinAlpha;

                    vb2.x = (b.getVelocity().x * sinAlpha + b.getVelocity().y * cosAlpha) * sinAlpha;
                    vb2.y = (b.getVelocity().x * sinAlpha + b.getVelocity().y * cosAlpha) * cosAlpha;

//                    Log.i("xx","转换后a速度为 va1 = " + va1 + "   va2 = " + va2);
//                    Log.i("xx","转换后b速度为 vb1 = " + vb1 + "   vb2 = " + vb2);
//                    Log.i("xx","碰撞前动能和为" + (a.getVelocity().getValue() * a.getVelocity().getValue() + b.getVelocity().getValue() * b.getVelocity().getValue()));
//                    Log.i("xx","va1 = " + va1 + "   va2 = " + va2 + "   vb1 = " + vb1 + "   vb2 = " + vb2);

                    _va1 = va1.aMerge(va1.aMerge(vb1).multi((float)b.getWeight() / (a.getWeight() + b.getWeight()) * (1 + LOSS)));
                    _vb1 = vb1.merge(va1.aMerge(vb1).multi((float)a.getWeight() / (a.getWeight() + b.getWeight()) * (1 + LOSS)));

//                    Log.i("xx","_va1 = " + _va1 + "     _va2 = " + va2 + "     _vb1 = " + _vb1 + "     _vb2 = " + vb2);

                    Velocity.merge(_va1,va2,a.getVelocity());
                    Velocity.merge(_vb1, vb2, b.getVelocity());

//                    Log.i("xx","碰撞后动能和为" + (a.getVelocity().getValue() * a.getVelocity().getValue() + b.getVelocity().getValue() * b.getVelocity().getValue()));

//                    Log.i("xx", "检测到碰撞,计算时间为" + (SystemClock.elapsedRealtime() - time) + "   i = " + i + "   j = " + j);
//                    Log.i("xx","碰撞后 a 的vx = " + a.getVelocityX() + "    vy = " + a.getVelocityY() + "   a 的lx = " + a.getLocationX() + "    ly = " + a.getLocationY());
//                    Log.i("xx","碰撞后 b 的vx = " + b.getVelocityX() + "    vy = " + b.getVelocityY() + "   b 的lx = " + b.getLocationX() + "    ly = " + b.getLocationY());
//                    Log.i("xx","================");
                    a.move(SystemClock.elapsedRealtime());
                    b.move(SystemClock.elapsedRealtime());
                }
            }


//            Log.i("xx","球的数量为 " + count +  "    遍历需要的时间为 " + (SystemClock.elapsedRealtime()  - startTime));

        }
    }


}
