package com.example.mvpdemo.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvpdemo.R;
import com.example.mvpdemo.adapter.BaseRecyclerAdapter;
import com.example.mvpdemo.holder.BHolder;
import com.example.mvpdemo.holder.BaseRecyclerViewHolder;
import com.example.mvpdemo.ipresenter.IRequestPresenter;
import com.example.mvpdemo.model.ListResponse;
import com.example.mvpdemo.model.UserInfo;
import com.example.mvpdemo.presenter.BaseRecyclerViewPresenter;
import com.example.mvpdemo.presenter.RequestUserListPresenter;
import com.example.mvpdemo.view.IRecyclerView;
import com.example.mvpdemo.view.IRequestView;

/**
 * Created by wangchao on 16/6/24.
 */
public class BActivity extends BaseActivity implements IRecyclerView<UserInfo>, IRequestView<ListResponse<UserInfo>>{

    private RecyclerView mRecyclerView;

    private BaseRecyclerAdapter<UserInfo> mAdapter;

    private View mProgress;

    private BaseRecyclerViewPresenter<UserInfo> mPresenter = new BaseRecyclerViewPresenter<>(this);

    private RequestUserListPresenter mRequestUserListPresenter = new RequestUserListPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestUserListPresenter.performRequest();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_b;
    }

    @Override
    protected void initActivityViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mProgress = findViewById(R.id.progress);
        mPresenter.initRecyclerView();
    }

    @Override
    public BaseRecyclerAdapter<UserInfo> getAdapter() {
        if (mAdapter == null) {
            mAdapter = new BaseRecyclerAdapter<>(this,this);
        }
        return mAdapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(View v, ViewGroup parent, int type) {
        return new BHolder(v);
    }


    @Override
    public void onItemClick(View v, UserInfo userInfo, int position) {
        Log.i("xx","======  onItemClick     user = " + userInfo.getName());
    }

    @Override
    public void onItemLongClick(View v, UserInfo userInfo, int position) {

    }

    @Override
    public int getItemLayoutResId(int viewType) {
        return R.layout.item_b_holder;
    }

    @Override
    public void onRequestStart() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestFinish() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onRequestSuccess(ListResponse<UserInfo> userInfoListResponse) {
        getAdapter().addItems(userInfoListResponse.getTList());
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onRequestFail(int meta) {

    }

}
