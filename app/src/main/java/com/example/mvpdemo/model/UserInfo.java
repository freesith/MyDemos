package com.example.mvpdemo.model;

/**
 * Created by wangchao on 16/6/24.
 */
public class UserInfo {

    public UserInfo(String name) {
        mName = name;
    }

    private String mName;

    private int mAge;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int age) {
        mAge = age;
    }
}
