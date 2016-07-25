package com.example.mvpdemo.view;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;

import com.example.mvpdemo.holder.BaseViewHolder;

/**
 * Created by wangchao on 16/6/24.
 */
public interface IListView<T> extends ICollectionView<T> {

    BaseAdapter getAdapter();

    BaseViewHolder<T> onCreateViewHolder(View v, View parent, int type);

}
