package com.example.mvpdemo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by wangchao on 16/10/13.
 */
public class ColorEditText extends EditText {

    private Paint mPaint;

    private int RADIUS = 20;

    public ColorEditText(Context context) {
        super(context);
        init();
    }

    public ColorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        setPadding(2 * RADIUS, 2 * RADIUS,2 * RADIUS,2 * RADIUS);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    public void setTextColor(int color) {
        super.setTextColor(color);
        mPaint.setColor(color);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("COLOREDIT","onTouchEvent     event = " + MotionEvent.actionToString(event.getAction()) + "   location = " + event.getX() + ", " + event.getY());
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(RADIUS,RADIUS,getMeasuredWidth() - RADIUS,RADIUS,mPaint);
        canvas.drawLine(RADIUS,RADIUS,RADIUS,getMeasuredHeight() - RADIUS,mPaint);
        canvas.drawLine(RADIUS,getMeasuredHeight() - RADIUS,getMeasuredWidth() - RADIUS,getMeasuredHeight() - RADIUS,mPaint);
        canvas.drawLine(getMeasuredWidth() - RADIUS,RADIUS,getMeasuredWidth() - RADIUS,getMeasuredHeight() - RADIUS,mPaint);

        canvas.drawCircle(RADIUS,RADIUS,RADIUS,mPaint);
        canvas.drawCircle(getMeasuredWidth() - RADIUS,getMeasuredHeight() - RADIUS,RADIUS,mPaint);

        super.onDraw(canvas);
    }
}
