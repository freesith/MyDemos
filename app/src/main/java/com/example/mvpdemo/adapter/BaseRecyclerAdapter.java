package com.example.mvpdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvpdemo.holder.BaseRecyclerViewHolder;
import com.example.mvpdemo.view.ICollectionAdapter;
import com.example.mvpdemo.view.IRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchao on 16/6/24.
 */
public class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> implements ICollectionAdapter<T>{

    protected Context mContext;

    protected List<T> mList;

    private IRecyclerView<T> mRecyclerView;

    public BaseRecyclerAdapter(Context context, IRecyclerView<T> recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
        mList = new ArrayList<>();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(mRecyclerView.getItemLayoutResId(viewType),null);
        return mRecyclerView.onCreateViewHolder(v, parent,viewType);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.bindViewHolder(null,mList.get(position), mRecyclerView);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void addItems(List<T> list) {
        mList.addAll(list);
    }

    @Override
    public void addItem(T t) {
        mList.add(t);
    }

    @Override
    public void removeItem(T t) {
        mList.remove(t);
    }

    @Override
    public boolean isEmpty() {
        return mList == null || mList.size() == 0;
    }
}
