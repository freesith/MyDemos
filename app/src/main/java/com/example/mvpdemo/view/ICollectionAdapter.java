package com.example.mvpdemo.view;

import java.util.List;

/**
 * Created by wangchao on 16/6/27.
 */
public interface ICollectionAdapter<T> {

    void addItems(List<T> list);

    void addItem(T t);

    void removeItem(T t);

    boolean isEmpty();
}
