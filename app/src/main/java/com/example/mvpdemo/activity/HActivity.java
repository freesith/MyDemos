package com.example.mvpdemo.activity;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvpdemo.R;
import com.example.mvpdemo.widget.NarrowViewPager;

import java.util.Random;

/**
 * Created by wangchao on 16/8/10.
 */
public class HActivity extends BaseActivity {

    NarrowViewPager mViewPager;

    Random mRandom;




    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_h;
    }

    @Override
    protected void initActivityViews() {
        mViewPager = (NarrowViewPager) findViewById(R.id.pager);
        mRandom = new Random();
        mViewPager.setAdapter(new NarrowAdapter());
    }



    class NarrowAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = LayoutInflater.from(HActivity.this).inflate(R.layout.h_item,null);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
