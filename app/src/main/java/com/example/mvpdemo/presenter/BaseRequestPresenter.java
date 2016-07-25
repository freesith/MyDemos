package com.example.mvpdemo.presenter;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.example.mvpdemo.ipresenter.IRequestPresenter;
import com.example.mvpdemo.model.UserInfo;
import com.example.mvpdemo.view.IRequestView;

import java.util.Random;

/**
 * Created by wangchao on 16/6/24.
 */
public abstract class BaseRequestPresenter<T> implements IRequestPresenter {

    private IRequestView<T> mRequestView;

    public BaseRequestPresenter(IRequestView iRequestView) {
        this.mRequestView = iRequestView;
    }

    @Override
    public void performRequest() {
        mRequestView.onRequestStart();

        new AsyncTask<Void, Void, T>() {
            @Override
            protected T doInBackground(Void... params) {
                return processInBackground();
            }

            @Override
            protected void onPostExecute(T t) {
                mRequestView.onRequestFinish();
                if (t == null) {
                    mRequestView.onRequestFail(0);
                } else {
                    mRequestView.onRequestSuccess(t);
                }

            }
        }.execute();

    }

    abstract T processInBackground();

}
