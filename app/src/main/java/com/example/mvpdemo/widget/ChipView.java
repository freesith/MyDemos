package com.example.mvpdemo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by wangchao on 16/7/11.
 */
public class ChipView extends View {

    private Random mRandom;

    private Paint mPaint;

    public ChipView(Context context) {
        super(context);
        init();
    }

    public ChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChipView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mRandom = new Random();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaint.setColor(Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
        mPaint.setColor(Color.HSVToColor(new float[]{(float)(mRandom.nextInt(360)),(float)(mRandom.nextInt(80)/100f),(float)((mRandom.nextInt(30) + 70 )/100f)}));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int random = mRandom.nextInt(5);
        if (random >= 4) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth()/2,mPaint);
        } else if (random >= 3 ) {
            canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);
        }else {
            Path path = new Path();
            path.moveTo(mRandom.nextInt(getMeasuredWidth()),0);
            path.lineTo(getMeasuredWidth(), mRandom.nextInt(getMeasuredHeight()));
            path.lineTo(mRandom.nextInt(getMeasuredWidth()), getMeasuredHeight());
            path.lineTo(0, mRandom.nextInt(getMeasuredHeight()));
            canvas.drawPath(path,mPaint);
        }

    }
}
