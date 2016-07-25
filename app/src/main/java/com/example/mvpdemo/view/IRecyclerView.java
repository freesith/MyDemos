package com.example.mvpdemo.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvpdemo.holder.BaseRecyclerViewHolder;

/**
 * Created by wangchao on 16/6/24.
 */
public interface IRecyclerView<T> extends ICollectionView<T>{

    RecyclerView.Adapter getAdapter();

    RecyclerView.LayoutManager getLayoutManager();

    RecyclerView getRecyclerView();

    BaseRecyclerViewHolder onCreateViewHolder(View v, ViewGroup parent, int type);

}
