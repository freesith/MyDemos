package com.example.mvpdemo.presenter;

import com.example.mvpdemo.model.UserInfo;
import com.example.mvpdemo.view.IRequestView;

import java.util.Random;

/**
 * Created by wangchao on 16/6/24.
 */
public class RequestUserPresenter extends BaseRequestPresenter<UserInfo> {

    public RequestUserPresenter(IRequestView iRequestView) {
        super(iRequestView);
    }

    @Override
    UserInfo processInBackground() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String[] names = new String[]{"张三", "李四", "王五"};

        return new UserInfo(names[new Random().nextInt(names.length)]);
    }
}
