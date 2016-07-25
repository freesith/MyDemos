package com.example.mvpdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvpdemo.holder.BaseViewHolder;
import com.example.mvpdemo.view.ICollectionAdapter;
import com.example.mvpdemo.view.ICollectionView;
import com.example.mvpdemo.view.IListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchao on 16/6/24.
 */
public class BaseListAdapter<T> extends android.widget.BaseAdapter implements ICollectionAdapter<T> {

    protected List<T> mTList;

    protected Context mContext;

    protected IListView mIListView;

    public BaseListAdapter(Context context,ICollectionView collectionView) {
        this.mContext = context;
        mTList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mTList == null ? 0 : mTList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(mIListView.getItemLayoutResId(type),null);
            BaseViewHolder holder = mIListView.onCreateViewHolder(convertView,parent,type);
            convertView.setTag(holder);

        } else {

            BaseViewHolder holder = (BaseViewHolder) convertView.getTag();
            holder.bindViewHolder(convertView, mTList.get(position), mIListView);
        }
        return convertView;
    }

    @Override
    public void addItems(List<T> list) {
        mTList.addAll(list);
    }

    @Override
    public void addItem(T t) {
        mTList.add(t);
    }

    @Override
    public void removeItem(T t) {
        mTList.remove(t);
    }



}
