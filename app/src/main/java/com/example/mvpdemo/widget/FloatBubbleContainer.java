package com.example.mvpdemo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wangchao on 16/8/2.
 */
public class FloatBubbleContainer extends FrameLayout {

    private boolean isRun;

    private List<BubbleView> mViewList = new ArrayList<>();

    private List<Integer> mValueList = new ArrayList<>();

    private static final int FORCE = 30;

    private static final float LOSS = 0;

    private static final float GRAVITY = (float) 80;

    private static final int TIME = 500;

    private long mLastAddTime;

    private long mLastEventTime;

    private MotionEvent mLastEvent;

    private float mLastEventX;

    private float mLastEventY;

    private Paint mPaint;

    private boolean canSelect = true;

    private Velocity va1 = new Velocity();
    private Velocity va2 = new Velocity();
    private Velocity vb1 = new Velocity();
    private Velocity vb2 = new Velocity();
    private Velocity _va1 = new Velocity();
    private Velocity _vb1 = new Velocity();

    private Random mRandom;

    private boolean hasArrive;


    public FloatBubbleContainer(Context context) {
        super(context);
        init();
    }

    public FloatBubbleContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FloatBubbleContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setRun(boolean run) {
        isRun = run;
    }

    private void init() {

        mRandom = new Random();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);

        for (int i = 0; i < 20; i ++) {
            BubbleView bubbleView = new BubbleView(getContext(), i);
            bubbleView.setLocationX(mRandom.nextInt(1440));
            bubbleView.setLocationY(mRandom.nextInt(2560));
//            bubbleView.setVelocityX(mRandom.nextInt(120) - 60);
//            bubbleView.setVelocityY(mRandom.nextInt(120) - 60);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(400, 400);
            addView(bubbleView, params);

            mViewList.add(bubbleView);
        }

        layoutBubble();

    }

    private void addBubble(int x,Velocity v,int value) {
        BubbleView bubbleView = new BubbleView(getContext(), value);
        bubbleView.setLocationX(1440 + BubbleView.SELECT_RADIUS);
        bubbleView.setLocationY(mRandom.nextInt(2560));
//        bubbleView.setVelocityX(-150);
//        bubbleView.setVelocityY(mRandom.nextInt(200) - 100);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(400, 400);

        addView(bubbleView,params);
        bubbleView.setTranslationX(bubbleView.getLocationX());
        mViewList.add(bubbleView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            BubbleView view = findBubble(event);
            if (view != null) {
                if (canSelect) {
                    if (!view.isSelect()) {
                        view.onSelect();
                    } else {
                        view.onDisSelect();
                    }
                }
            } else {
                Log.i("xx","onActionUp      lastX = " + mLastEventX + "     eventX = " + event.getX() + "   lastTime = " + mLastEventTime + "       current = " + SystemClock.elapsedRealtime());
                if (mLastEventX != 0 || mLastEventY != 0) {
                    float x = (float)(event.getX() - mLastEventX) / (SystemClock.elapsedRealtime() - mLastEventTime);
                    float y = (float)(event.getY() - mLastEventY) / (SystemClock.elapsedRealtime() - mLastEventTime);
                    speedUp(10 * x, 10 * y);
                }
            }
            mLastEventX = 0;
            mLastEventY = 0;

            canSelect = true;

        } else if (event.getAction() == MotionEvent.ACTION_DOWN){


        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if(mLastEventX != 0 || mLastEventY != 0) {
                onMove(event.getX() - mLastEventX, event.getY() - mLastEventY);

                float x = (float)(event.getX() - mLastEventX) / (SystemClock.elapsedRealtime() - mLastEventTime);
                float y = (float)(event.getY() - mLastEventY) / (SystemClock.elapsedRealtime() - mLastEventTime);
                speedUp(10 * x, 10 * y);
            }
            mLastEvent = event;
        }

        mLastEventX = event.getX();
        mLastEventY = event.getY();
        mLastEventTime = SystemClock.elapsedRealtime();
        return true;
    }


    private void onMove(float x,float y) {

        if (x == 0 && y == 0) {
            return;
        }
        canSelect = false;
        Log.i("xx","==== onMove  x = " + x + "      y = " + y);
        for (BubbleView bubbleView : mViewList) {
            bubbleView.setLocationX(bubbleView.getLocationX() + x);
            bubbleView.setLocationY(bubbleView.getLocationY() + y);
//            bubbleView.move(SystemClock.elapsedRealtime());
        }

    }

    private void speedUp(float x, float y) {
        if (x == 0 && y == 0) {
            return;
        }
        Log.i("xx","===== SpeedUp     x = " + x + "     y = " + y);
        for (BubbleView bubbleView : mViewList) {
            bubbleView.getVelocity().mergeUp(x, y);
//            bubbleView.move(SystemClock.elapsedRealtime());
        }
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


    private void checkKnockWall(BubbleView bubbleView) {
        if (bubbleView.getLocationY() - bubbleView.getR() < 1/* && bubbleView.getVelocityY() < 0*/) {
            bubbleView.setLocationY(1 + bubbleView.getR());
            bubbleView.setVelocityY(-bubbleView.getVelocityY());
            bubbleView.move(SystemClock.elapsedRealtime());
        } else if (bubbleView.getLocationY() + bubbleView.getR() > 2560 - 1 /*&& bubbleView.getVelocityY() > 0*/) {
            bubbleView.setLocationY(2400 - 1 - bubbleView.getR());
            bubbleView.setVelocityY(-bubbleView.getVelocityY());
            bubbleView.move(SystemClock.elapsedRealtime());
        }
    }

    private void setForce(BubbleView bubbleView) {
        float yy = getMeasuredHeight() / 2 - bubbleView.getLocationY();
        float xx = getMeasuredWidth() / 2 - bubbleView.getLocationX();

        double l = Math.sqrt(xx * xx + yy * yy);

        float velocityX = bubbleView.getVelocityX();
        float velocityY = bubbleView.getVelocityY();

        /**
         * 静摩擦 & 动摩擦
         */

//        Log.i("xx","======GRAVITY = " + (GRAVITY * (l / 500)) + "       l = " + l + "   xx = " + xx + "    yy = " + yy);

        if (GRAVITY * (l / 500) < FORCE && bubbleView.getVelocity().getValue() < 2) {
            bubbleView.setVelocityX(0);
            bubbleView.setVelocityY(0);
            bubbleView.setForceX(0);
            bubbleView.setForceY(0);
        } else {
            bubbleView.setForceX((float) (GRAVITY * xx / l * (l / 500) + (velocityX > 0 ? -FORCE : velocityX < 0 ? FORCE : 0) * Math.abs(xx) / l));
            bubbleView.setForceY((float) (GRAVITY * yy / l * (l / 500) + (velocityY > 0 ? -FORCE : velocityY < 0 ? FORCE : 0) * Math.abs(yy) / l));
        }

//        Log.i("xx","==== ForceX = " + bubbleView.getForceX() + "        ForceY = "  + bubbleView.getForceY());

    }

    private void checkKnock() {

//        if (!hasArrive && SystemClock.elapsedRealtime() - mLastAddTime > TIME) {
//            addBubble(1440,new Velocity(-60,0),mValueList.remove(0));
//            mLastAddTime = SystemClock.elapsedRealtime();
//        }

        int count = mViewList.size();

        for (int i = 0; i < count; i++) {

            BubbleView a = mViewList.get(i);
            checkKnockWall(a);
            setForce(a);
            a.move(SystemClock.elapsedRealtime());

            for (int j = i + 1; j < count; j++) {

                BubbleView b = mViewList.get(j);
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

                    float dy = Math.abs(a.getLocationY() - b.getLocationY());
                    float dx = Math.abs(a.getLocationX() - b.getLocationX());

                    float dl = (float) Math.sqrt(dx * dx + dy * dy);

//                    Log.i("xx","碰撞前 a xy方向动能 = " + (a.getVelocityX() * a.getVelocityX() + a.getVelocityY() * a.getVelocityY()));
//                    Log.i("xx","碰撞前 b xy方向动能  = " + (b.getVelocityX() * b.getVelocityX() + b.getVelocityY() * b.getVelocityY()));
//                    Log.i("xx","转换前a速度为" + a.getVelocity());
//                    Log.i("xx", "转换前b速度为" + b.getVelocity());

                    float cosAlpha = dx / dl;
                    float sinAlpha = dy / dl;

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

                    Velocity.merge(_va1, va2, a.getVelocity());
                    Velocity.merge(_vb1, vb2, b.getVelocity());

//                    Log.i("xx","碰撞后动能和为" + (a.getVelocity().getValue() * a.getVelocity().getValue() + b.getVelocity().getValue() * b.getVelocity().getValue()));

//                    Log.i("xx", "检测到碰撞,计算时间为" + (SystemClock.elapsedRealtime() - time) + "   i = " + i + "   j = " + j);
//                    Log.i("xx","碰撞后 a 的vx = " + a.getVelocityX() + "    vy = " + a.getVelocityY() + "   a 的lx = " + a.getLocationX() + "    ly = " + a.getLocationY());
//                    Log.i("xx","碰撞后 b 的vx = " + b.getVelocityX() + "    vy = " + b.getVelocityY() + "   b 的lx = " + b.getLocationX() + "    ly = " + b.getLocationY());
//                    Log.i("xx","================");
                    setForce(a);
                    setForce(b);
                    a.move(SystemClock.elapsedRealtime());
                    b.move(SystemClock.elapsedRealtime());
                }
            }

//            Log.i("xx","球的数量为 " + count +  "    遍历需要的时间为 " + (SystemClock.elapsedRealtime()  - startTime));

        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, 4, mPaint);

    }
}
