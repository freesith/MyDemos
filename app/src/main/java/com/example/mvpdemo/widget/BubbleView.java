package com.example.mvpdemo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;
import java.util.Random;

/**
 * Created by wangchao on 16/7/22.
 */
public class BubbleView extends View {

    private static final int NORMAL_RADIUS = 100;   //正常的半径

    private static final int SELECT_RADIUS = 200;   //选中半径

    private static final int FORCE = 100;

    private int r = NORMAL_RADIUS; // 当前半径

    private float locationX;  //在父控件中的横坐标

    private float locationY;  //在父控件中的纵坐标

    private float velocityX;  //在x轴的速度 单位 px/s

    private float velocityY;    //在Y轴的速度 单位 px/s

    private long lastTime;    //上次打点时间,用来计算新的位置

    private float forceX;

    private float forceY;

    private Paint mPaint;
    private Random mRandom;

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
    }

    public void setLocationX(float locationX) {
        this.locationX = locationX;
    }

    public void setLocationY(float locationY) {
        this.locationY = locationY;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
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

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setForceX(float forceX) {
        this.forceX = forceX;
    }

    public void setForceY(float forceY) {
        this.forceY = forceY;
    }

    public void move(long time) {

        if (lastTime == 0) {
            lastTime = time;
        } else {
            locationX += (time - lastTime) / 1000 * velocityX;
            locationY += (time - lastTime) / 1000 * velocityY;

            velocityX += (time - lastTime) / 1000 * forceX / 1;
            velocityY += (time - lastTime) / 1000 * forceY / 1;
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
