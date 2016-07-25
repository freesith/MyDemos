package com.example.mvpdemo.presenter;

import com.example.mvpdemo.model.ListResponse;
import com.example.mvpdemo.model.UserInfo;
import com.example.mvpdemo.view.IRequestView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchao on 16/7/6.
 */
public class RequestUserListPresenter extends BaseRequestPresenter<ListResponse<UserInfo>> {

    public RequestUserListPresenter(IRequestView iRequestView) {
        super(iRequestView);
    }

    @Override
    ListResponse<UserInfo> processInBackground() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ListResponse<UserInfo> listResponse = new ListResponse<>();
        List<UserInfo> list = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            UserInfo info = new UserInfo("张三" + i);
            list.add(info);
        }
        listResponse.setTList(list);
        return listResponse;
    }
}
