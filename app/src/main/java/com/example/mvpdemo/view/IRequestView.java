package com.example.mvpdemo.view;

/**
 * Created by wangchao on 16/6/24.
 */
public interface IRequestView<T> extends IView {

    void onRequestStart();

    void onRequestFinish();

    void onRequestSuccess(T t);

    void onRequestFail(int meta);
}