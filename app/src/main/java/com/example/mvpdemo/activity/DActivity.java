package com.example.mvpdemo.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.graphics.Path;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Property;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.mvpdemo.R;

/**
 * Created by wangchao on 16/7/8.
 */
public class DActivity extends BaseActivity {

    private ImageView mImage;

    private ImageView mAdd;

    private ImageView mPlay;


    private boolean isPause;

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_d;

    }

    @Override
    protected void initActivityViews() {
        mImage = (ImageView) findViewById(R.id.image);
        mImage.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Drawable drawable = mImage.getDrawable();
                if (drawable != null && drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }

//                mImage.setImageDrawable(getDrawable(R.drawable.trans_back));
            }
        });

        mAdd = (ImageView) findViewById(R.id.add);
        mAdd.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Drawable drawable = mAdd.getDrawable();
                if (drawable != null && drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }
            }
        });

        mPlay = (ImageView) findViewById(R.id.play);

        mPlay.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                mPlay.setImageDrawable(getResources().getDrawable(isPause ? R.drawable.pause_to_play : R.drawable.play_to_pause));
                Drawable drawable = mPlay.getDrawable();
                if (drawable != null && drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                }

                mPlay.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isPause = !isPause;
//                        Drawable drawable = mPlay.getDrawable();
//                        if (drawable != null && drawable instanceof Animatable) {
//                            ((Animatable) drawable).stop();
//                        }

                    }
                }, 500);

            }
        });

    }
}
