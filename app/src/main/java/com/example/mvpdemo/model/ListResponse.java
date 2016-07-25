package com.example.mvpdemo.model;

import java.util.List;

/**
 * Created by wangchao on 16/6/24.
 */
public class ListResponse<T> {

    private List<T> mTList;

    private boolean hasNext;

    private String nextCursor;


    public List<T> getTList() {
        return mTList;
    }

    public void setTList(List<T> TList) {
        mTList = TList;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }
}
