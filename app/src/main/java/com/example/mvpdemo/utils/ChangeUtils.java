package com.example.mvpdemo.utils;

import android.os.SystemClock;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by wangchao on 16/5/18.
 * 主要用于UI随时间变化
 */
public class ChangeUtils implements Runnable {

    protected long mDuration;
    protected boolean mIsFinished = true;
    protected long mStartTime;
    protected Interpolator mInterpolator;
    protected float mFrom;
    protected float mTo;
    protected float mDistance;
    protected View mView;

    public ChangeUtils() {
    }

    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    public void abortAnimation() {
        mIsFinished = true;
    }

    public boolean isFinished() {
        return mIsFinished;
    }

    public void run() {
        if (mIsFinished) {
            return;
        }
        float f2;
        if ((!mIsFinished) && (mDistance != 0)) {

            // 时间过去了多少
            float percent = ((float) SystemClock.currentThreadTimeMillis() - (float) mStartTime) / (float) mDuration;
            if (percent > 1) {  //时间超了,直接变化
                onChange(mTo);
                onFinish();
                return;
            }
            // 已经变化了多少
            f2 = mDistance * mInterpolator.getInterpolation(percent);
            if (mTo > mFrom && mFrom + f2 < mTo || mTo < mFrom && mFrom + f2 > mTo) {   //当前的取值在变化范围内

                onChange(mFrom + f2);
                onVary(f2);
                mFrom += f2;
                mView.postDelayed(this, 10);
                return;
            }
            onChange(mTo);
            onFinish();
            mIsFinished = true;
        }
    }


    public void onChange(float current) {

    }

    public void onVary(float vary) {

    }

    public void onFinish() {

    }

    public void startChange(View view, float from,float to, long duration) {
        mStartTime = SystemClock.currentThreadTimeMillis();
        mInterpolator = new DecelerateInterpolator();
        mDuration = duration;
        mIsFinished = false;
        mFrom = from;
        mTo = to;
        mDistance = to - from;
        mView = view;
        if(mDistance != 0) {
            mView.post(this);
        }
    }
}


