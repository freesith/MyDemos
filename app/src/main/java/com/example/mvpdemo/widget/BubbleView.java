package com.example.mvpdemo.widget;

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

    private static final int NORMAL_RADIUS = 100;   //正常的半径

    private static final int SELECT_RADIUS = 200;   //选中半径

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

    private Random mRandom;

    private Velocity velocity;

    public BubbleView(Context context) {
        super(context);
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
        mPaint.setColor(Color.HSVToColor(new float[]{(float) (mRandom.nextInt(360)), (float) (mRandom.nextInt(80) / 100f), (float) ((mRandom.nextInt(30) + 70) / 100f)}));
        velocity = new Velocity(0,0);
    }

    public void setLocationX(float locationX) {
        this.locationX = locationX;
    }

    public void setLocationY(float locationY) {
        this.locationY = locationY;
    }

    public void setVelocityX(float velocityX) {
        velocity.x = velocityX;
    }

    public void setVelocityY(float velocityY) {
        velocity.y = velocityY;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public float getForceX() {
        return forceX;
    }

    public float getForceY() {
        return forceY;
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

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
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

        setTranslationX(locationX - r);
        setTranslationY(locationY - r);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(r, r, r, mPaint);
    }
}
