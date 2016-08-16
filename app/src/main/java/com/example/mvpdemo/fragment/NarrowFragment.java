package com.example.mvpdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mvpdemo.R;
import com.example.mvpdemo.adapter.BaseRecyclerAdapter;
import com.example.mvpdemo.holder.BaseRecyclerViewHolder;
import com.example.mvpdemo.view.ICollectionView;
import com.example.mvpdemo.view.IRecyclerView;

/**
 * Created by wangchao on 16/8/16.
 */
public class NarrowFragment extends Fragment implements IRecyclerView<String>{

    private MyAdapter mAdapter;

    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_narrow,null);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(getAdapter());
        for (int i = 0; i < 20; i ++) {
            getAdapter().addItem("" + i);
        }
        return v;
    }

    @Override
    public BaseRecyclerAdapter<String> getAdapter() {
        if (mAdapter == null) {
            mAdapter = new MyAdapter(getActivity(),this);
        }
        return mAdapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(View v, ViewGroup parent, int type) {
        return new MyHolder(v);
    }

    @Override
    public void onItemClick(View v, String s, int position) {

    }

    @Override
    public void onItemLongClick(View v, String s, int position) {

    }

    @Override
    public int getItemLayoutResId(int viewType) {
        return R.layout.item_narrow_fragment;
    }

    class MyAdapter extends BaseRecyclerAdapter<String>{

        public MyAdapter(Context context, IRecyclerView recyclerView) {
            super(context, recyclerView);
        }
    }

    class MyHolder extends BaseRecyclerViewHolder<String> {

        private TextView mTextView;

        public MyHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void initHolderViews(View v) {
            mTextView = (TextView) v.findViewById(R.id.text);
        }

        @Override
        public void bindViewHolder(View v, String s, ICollectionView iCollectionView) {
            Log.i("xx","bind View ");
            mTextView.setText(s);
        }
    }
}
