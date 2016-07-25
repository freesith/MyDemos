package com.example.mvpdemo.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wangchao on 16/6/27.
 */
public class Toaster {

    public static void toast(Context context, String s) {
        Toast.makeText(context,s,Toast.LENGTH_SHORT);
    }

}
