package com.example.mvpdemo.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mvpdemo.view.IView;

/**
 * Created by wangchao on 16/6/24.
 */
public abstract class BaseActivity extends AppCompatActivity implements IView {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        initActivityViews();
    }

    protected abstract int getLayoutResource();

    protected abstract void initActivityViews();

}
