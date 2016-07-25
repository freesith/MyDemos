package com.example.mvpdemo.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.mvpdemo.view.ICollectionView;


/**
 * Created by wangchao on 16/6/24.
 */
public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder{

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        initHolderViews(itemView);

    }

    public abstract void initHolderViews(View v);

    public abstract void bindViewHolder(View v,T t, ICollectionView iCollectionView);

}
