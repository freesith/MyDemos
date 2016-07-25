package com.example.mvpdemo.presenter;

import com.example.mvpdemo.ipresenter.IRecyclerViewPresenter;
import com.example.mvpdemo.view.IRecyclerView;

/**
 * Created by wangchao on 16/6/24.
 */
public class BaseRecyclerViewPresenter<T> implements IRecyclerViewPresenter {

    private IRecyclerView<T> mTIRecyclerView;

    public BaseRecyclerViewPresenter(IRecyclerView<T> TIRecyclerView) {
        mTIRecyclerView = TIRecyclerView;
    }

    @Override
    public void initRecyclerView() {
        mTIRecyclerView.getRecyclerView().setAdapter(mTIRecyclerView.getAdapter());
        mTIRecyclerView.getRecyclerView().setLayoutManager(mTIRecyclerView.getLayoutManager());
    }
}
