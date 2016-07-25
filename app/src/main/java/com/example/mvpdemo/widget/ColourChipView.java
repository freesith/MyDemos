package com.example.mvpdemo.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import java.util.Random;

/**
 * Created by wangchao on 16/7/11.
 */
public class ColourChipView extends FrameLayout {

    private Random mRandom;

    private boolean start;

    LinearInterpolator mLinearInterpolator;

    private Spill mSpill;

    AccelerateInterpolator mAccelerateInterpolator;

    public ColourChipView(Context context) {
        super(context);
        init();
    }

    public ColourChipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColourChipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColourChipView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mRandom = new Random();
        mLinearInterpolator = new LinearInterpolator();
        mAccelerateInterpolator = new AccelerateInterpolator(0.6f);
        mSpill = new Spill();
    }

    public void startSpill() {
        start = true;
        post(mSpill);
    }

    public void stop() {
        start = false;

    }

    public boolean isStart() {
        return start;
    }


    private void addChip(float scale) {

        final View v = new ChipView(getContext());

//        v.setBackgroundColor(Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
        v.setRotation(mRandom.nextInt(360));

        scale = mRandom.nextInt(100);
        if (scale >= 95) {
            scale = 3;
        } else if (scale >= 80) {
            scale = 2;
        } else {
            scale = 1 + scale / 10;
            scale = (float) Math.pow(scale,0.3);
        }

//        int random = mRandom.nextInt(100);
//        if (random >= 90) {
//            scale = 3;
//        } else if (scale >= 75) {
//            scale = 2;
//        }


        long duration = (long) ((6000 + mRandom.nextInt(300))/scale);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = (int) ((mRandom.nextInt(10) + 20) * scale);
        params.width = /*(int) ((mRandom.nextInt(20) + 20) * scale)*/ params.height;
        params.leftMargin = mRandom.nextInt(getMeasuredWidth());
        addView(v, params);

        ObjectAnimator rotationX = ObjectAnimator.ofFloat(v, "rotationX", 0, 360).setDuration(mRandom.nextInt(500) + 300);
        rotationX.setRepeatCount((int) (duration / rotationX.getDuration()) + 1);
//        rotationX.setInterpolator(mLinearInterpolator);
        rotationX.start();

        ObjectAnimator rotationY = ObjectAnimator.ofFloat(v,"rotationY",0,mRandom.nextInt(360)).setDuration(mRandom.nextInt(500) + 300);
        rotationY.setRepeatCount((int) (duration / rotationY.getDuration()) + 1);
//        rotationY.setInterpolator(mLinearInterpolator);
        rotationY.start();

        ObjectAnimator rotation = ObjectAnimator.ofFloat(v,"rotation",v.getRotation(),mRandom.nextInt(360)).setDuration(mRandom.nextInt(500) + 300);
        rotation.setRepeatCount((int) (duration / rotation.getDuration()) + 1);
        rotation.start();

        ObjectAnimator translationY = ObjectAnimator.ofFloat(v, "translationY", 0, (float) getMeasuredHeight()).setDuration(duration);
        translationY.setInterpolator(mAccelerateInterpolator);
        translationY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(v);
            }
        });

        translationY.start();
    }



    class Spill implements Runnable {
        @Override
        public void run() {
            Log.i("xx", "childCount = " + ColourChipView.this.getChildCount());
            if (start) {
                addChip(1);
//                addChip(1);
//                addChip(1);
//                addChip(1);
                postDelayed(this,20);
            }

        }
    }

}
