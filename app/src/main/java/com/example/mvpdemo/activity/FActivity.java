package com.example.mvpdemo.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

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

    private ViewPager mViewPager;

    private List<InterestInfo> mInterestList = new ArrayList<>();

    private static final int TOTAL_COUNT = 100;

    private static final int PAGE_COUNT = 20;

    private List<BubbleContainer> mBubbleContainerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < TOTAL_COUNT; i ++) {
            mInterestList.add(new InterestInfo(i,false));
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_f;
    }

    @Override
    protected void initActivityViews() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new MyAdapter());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(BubbleContainer container : mBubbleContainerList) {
                    container.setRun(false);
                }
                mBubbleContainerList.get(position).setRun(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    class MyAdapter extends PagerAdapter{


        @Override
        public int getCount() {
            return TOTAL_COUNT / PAGE_COUNT + (TOTAL_COUNT % PAGE_COUNT == 0 ? 0 : 1);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BubbleContainer bubbleContainer = new BubbleContainer(FActivity.this);

            bubbleContainer.addBuubles(mInterestList.subList(position * PAGE_COUNT, Math.min((position + 1) * PAGE_COUNT, mInterestList.size())));
            container.addView(bubbleContainer);
            mBubbleContainerList.add(bubbleContainer);
            return bubbleContainer;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            ((BubbleContainer)object).setRun(false);
            ((BubbleContainer)object).removeAllViews();
            container.removeView((View)object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

}
