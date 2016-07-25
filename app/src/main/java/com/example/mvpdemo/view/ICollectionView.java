package com.example.mvpdemo.view;

import android.view.View;

import com.example.mvpdemo.ipresenter.ICollectionPresenter;

/**
 * Created by wangchao on 16/6/24.
 */
public interface ICollectionView<T> extends IView {

    void onItemClick(View v,T t,int position);

    void onItemLongClick(View v,T t, int position);

    int getItemLayoutResId(int viewType);
}
