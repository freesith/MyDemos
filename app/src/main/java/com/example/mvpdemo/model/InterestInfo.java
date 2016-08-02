package com.example.mvpdemo.model;

/**
 * Created by wangchao on 16/8/2.
 */
public class InterestInfo {

    public InterestInfo(int name, boolean isSelect) {
        this.name = name;
        this.isSelect = isSelect;
    }

    private int name;

    private boolean isSelect;


    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
