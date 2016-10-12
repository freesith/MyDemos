package com.example.mvpdemo.activity;

import android.util.Log;
import android.widget.TextView;

import com.example.mvpdemo.R;
import com.example.mvpdemo.model.DoubanInfo;
import com.example.mvpdemo.utils.NativeHelper;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by wangchao on 16/9/20.
 */
public class KActivity extends BaseActivity {

    public static final String TAG = "KActivity";

    public static final String BASE_URL = "http://bubbler.labs.douban.com/";

    private Integer value = new Integer(500);

    private TextView mTextView;

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_k;
    }

    @Override
    protected void initActivityViews() {

        mTextView = (TextView) findViewById(R.id.text);
        mTextView.setText(new NativeHelper().getString());


//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(JacksonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//
//        DoubanService doubanService = retrofit.create(DoubanService.class);
//        doubanService.getDouban("ahbei")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<DoubanInfo>() {
//
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                    }
//
//
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(DoubanInfo doubanInfo) {
//                    }
//                });

    }

//    public interface DoubanService {
//
//        @GET("j/user/{uid}")
//        Observable<DoubanInfo> getDouban(@Path("uid") String user);
//
//    }

}
