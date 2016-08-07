package com.example.mvpdemo.activity;

import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.mvpdemo.R;
import com.example.mvpdemo.adapter.BaseListAdapter;
import com.example.mvpdemo.holder.BaseViewHolder;
import com.example.mvpdemo.holder.CHolder;
import com.example.mvpdemo.ipresenter.IPresenter;
import com.example.mvpdemo.ipresenter.IRequestPresenter;
import com.example.mvpdemo.model.ListResponse;
import com.example.mvpdemo.model.UserInfo;
import com.example.mvpdemo.view.IListView;
import com.example.mvpdemo.view.IRequestView;

/**
 * Created by wangchao on 16/7/6.
 */
public class CActivity extends BaseActivity implements IListView<UserInfo>, IRequestView<ListResponse<UserInfo>> {

    private ListView mListView;

    private BaseAdapter mAdapter;


    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_c;
    }

    @Override
    protected void initActivityViews() {
        mListView = (ListView) findViewById(R.id.list);

        mListView.setAdapter(getAdapter());

    }

    @Override
    public BaseAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new BaseListAdapter<>(this,this);
        }
        return mAdapter;
    }

    @Override
    public BaseViewHolder<UserInfo> onCreateViewHolder(View v, View parent, int type) {
        return new CHolder(v);
    }

    @Override
    public void onItemClick(View v, UserInfo userInfo, int position) {
        Log.i("xx"," name = " + userInfo.getName());
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

    }

    @Override
    public void onRequestFinish() {

    }

    @Override
    public void onRequestSuccess(ListResponse<UserInfo> userInfoListResponse) {

    }

    @Override
    public void onRequestFail(int meta) {

    }

}
