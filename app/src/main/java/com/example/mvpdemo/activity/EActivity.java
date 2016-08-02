package com.example.mvpdemo.activity;

import com.example.mvpdemo.R;
import com.example.mvpdemo.widget.BubbleContainer;
import com.example.mvpdemo.widget.FloatBubbleContainer;

/**
 * Created by wangchao on 16/7/25.
 */
public class EActivity extends BaseActivity {

    private FloatBubbleContainer mBubbleContainer;

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_e;
    }

    @Override
    protected void initActivityViews() {
        mBubbleContainer = (FloatBubbleContainer) findViewById(R.id.layout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBubbleContainer.setRun(false);
    }
}
