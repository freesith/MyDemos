package com.example.mvpdemo.activity;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvpdemo.R;
import com.example.mvpdemo.fragment.NarrowFragment;
import com.example.mvpdemo.widget.NarrowParentViewPager;
import com.example.mvpdemo.widget.NarrowViewPager;

import java.util.Random;

/**
 * Created by wangchao on 16/8/10.
 */
public class HActivity extends BaseActivity {

    NarrowParentViewPager mViewPager;

    Random mRandom;




    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_h;
    }

    @Override
    protected void initActivityViews() {
        mViewPager = (NarrowParentViewPager) findViewById(R.id.pager);
        mRandom = new Random();
        mViewPager.setAdapter(new NarrowFragmentAdapter(getSupportFragmentManager()));


        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiManager.getWifiState();
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.i("WIFI","wifiInfo.getSSID() = " + wifiInfo.getSSID() + "\n" +
                    "wifiInfo.getBSSID() = " + wifiInfo.getBSSID() +"\n" +
                    "wifiInfo.getIpAddress() = " + wifiInfo.getIpAddress());


    }


    class NarrowFragmentAdapter extends FragmentPagerAdapter{

        public NarrowFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new NarrowFragment();
        }

        @Override
        public int getCount() {
            return 20;
        }
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
