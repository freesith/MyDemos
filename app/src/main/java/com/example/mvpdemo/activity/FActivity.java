package com.example.mvpdemo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mvpdemo.R;
import com.example.mvpdemo.model.InterestInfo;
import com.example.mvpdemo.widget.BubbleContainer;
import com.example.mvpdemo.widget.BubbleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchao on 16/8/2.
 */
public class FActivity extends BaseActivity {

    private EditText mEditText;
    private TextView mTextView;
    private ImageView mImageView;
    private View mLayout;

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_f;
    }

    @Override
    protected void initActivityViews() {
        mEditText = (EditText) findViewById(R.id.edit);
        mImageView = (ImageView) findViewById(R.id.image);
        mTextView = (TextView) findViewById(R.id.text);
        mLayout = findViewById(R.id.layout);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.buildDrawingCache();
                Bitmap drawingCache = mLayout.getDrawingCache();
                if (drawingCache != null) {
                    mImageView.setImageBitmap(drawingCache);
                }
            }
        });

        mLayout.setDrawingCacheEnabled(true);

    }
}
