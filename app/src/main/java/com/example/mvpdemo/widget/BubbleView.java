package com.example.mvpdemo.widget;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.Random;

/**
 * Created by wangchao on 16/7/22.
 */
public class BubbleView extends View {

    public static final int NORMAL_RADIUS = 100;   //正常的半径

    public static final int SELECT_RADIUS = 200;   //选中半径

    private static final int FORCE = 20;   //固定的摩擦力

    private int r = NORMAL_RADIUS; // 当前半径

    private float locationX;  //在父控件中的横坐标

    private float locationY;  //在父控件中的纵坐标

//    private float velocityX;  //在x轴的速度 单位 px/s
//
//    private float velocityY;    //在Y轴的速度 单位 px/s

    private long lastTime;    //上次打点时间,用来计算新的位置

    private float forceX;

    private float forceY;

    private Paint mPaint;

    private Paint mTextPaint;

    private Random mRandom;

    private Velocity velocity;

    private int index;

    private boolean isSelect;

    public BubbleView(Context context) {
        super(context);
        init();
    }
    public BubbleView(Context context, int i) {
        super(context);
        this.index = i;
        init();
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        mRandom = new Random();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        mPaint.setColor(Color.HSVToColor(new float[]{(float) (mRandom.nextInt(360)), (float) (mRandom.nextInt(80) / 100f), (float) ((mRandom.nextInt(30) + 70) / 100f)}));
        velocity = new Velocity(0,0);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setLocationX(float locationX) {
        this.locationX = locationX;
    }

    public void setLocationY(float locationY) {
        this.locationY = locationY;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setVelocityX(float velocityX) {
        velocity.x = velocityX;
    }

    public void setVelocityY(float velocityY) {
        velocity.y = velocityY;
    }


    public float getLocationX() {
        return locationX;
    }

    public float getLocationY() {
        return locationY;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getR() {
        return r;
    }

    public int getWeight() {
        return r / 100 * r / 100;
    }

    public float getVelocityX() {
        return velocity.x;
    }

    public float getVelocityY() {
        return velocity.y;
    }

    public void setForceX(float forceX) {
        this.forceX = forceX;
    }

    public void setForceY(float forceY) {
        this.forceY = forceY;
    }

    public Velocity getVelocity() {
        return velocity;
    }


    public void move(long time) {

        float dt = (float)(time - lastTime) / 1000;
//        Log.i("BubbleView","move   dt = " + dt + "   forceX = " + forceX + "     forceY = " + forceY + "       vx = " + velocityX + "       vy = " + velocityY);

        if (lastTime == 0) {
            lastTime = time;
        } else {

//            double velocity = Math.sqrt(velocityX * velocityX + velocityY * velocityY); //运动放心的合速度

//            double fx = FORCE * Math.abs(velocityX) / velocity; //横向摩擦力绝对值
//            double fy = FORCE * Math.abs(velocityY) / velocity; //纵向摩擦力绝对值

            float weight = r / 100 * r / 100;

            float dvx = (float) dt * forceX /*/ weight*/;
            float dvy = (float) dt * forceY /*/ weight*/;

            //v0t + 1/2at^2
            float dlx = (float)(dt * velocity.x /* + 0.5 * forceX / weight * dt * dt*/);
            float dly = (float)(dt * velocity.y/* + 0.5 * forceY / weight * dt * dt*/);


//            Log.i("BubbleView","move    dlx = " + dlx + "   dly = " + dly + "   dvx = " + dvx + "   dvy = " + dvy);
            locationX += dlx;
            locationY += dly;

            velocity.x += dvx;
            velocity.y += dvy;

            lastTime = time;

        }

        setTranslationX(locationX - getWidth() / 2);
        setTranslationY(locationY - getHeight() / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, r, mPaint);

        float v = mTextPaint.measureText("" + index);
        mTextPaint.setTextSize(20);
        canvas.drawText("" + index, (getWidth() - v) / 2, getHeight() / 2, mTextPaint);
    }

    public void onSelect() {
        r = SELECT_RADIUS;
        isSelect = true;
        ObjectAnimator.ofFloat(this,"scaleX",1,2).setDuration(200).start();
        ObjectAnimator.ofFloat(this,"scaleY",1,2).setDuration(200).start();
    }

    public void onDisSelect() {
        isSelect = false;
        r = NORMAL_RADIUS;
        ObjectAnimator.ofFloat(this,"scaleX",2,1).setDuration(200).start();
        ObjectAnimator.ofFloat(this,"scaleY",2,1).setDuration(200).start();
    }

}
