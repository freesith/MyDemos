package com.example.mvpdemo.sticker;

import android.view.MotionEvent;

/**
 * Created by wangchao on 16/10/13.
 */
public interface Stickerable {

    void setEdit(boolean edit);

    void bringFront();

    void multi(MotionEvent event);


}
