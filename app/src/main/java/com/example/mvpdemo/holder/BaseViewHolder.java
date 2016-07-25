package com.example.mvpdemo.holder;

import android.content.Context;
import android.view.View;

import com.example.mvpdemo.view.ICollectionView;
import com.example.mvpdemo.view.IListView;

/**
 * Created by wangchao on 16/6/24.
 */
public abstract class BaseViewHolder<T> {

    public BaseViewHolder(View v) {
        initHolderViews(v);
    }

    public abstract void initHolderViews(View v);

    public abstract void bindViewHolder(View v,T t, ICollectionView iCollectionView);
}
