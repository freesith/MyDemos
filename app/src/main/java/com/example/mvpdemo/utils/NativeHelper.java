package com.example.mvpdemo.utils;

/**
 * Created by wangchao on 16/10/11.
 */
public class NativeHelper {

    static {
        System.loadLibrary("mylibrary");
    }

    public static native String getString();
}
