package com.example.mvpdemo.activity;

import android.opengl.GLES10;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mvpdemo.R;
import com.example.mvpdemo.model.UserInfo;
import com.example.mvpdemo.presenter.RequestUserPresenter;
import com.example.mvpdemo.view.IRequestView;
import com.example.mvpdemo.widget.ColourChipView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func0;

/**
 * Created by wangchao on 16/6/24.
 */
public class AActivity extends BaseActivity implements IRequestView<UserInfo>, View.OnClickListener {

    private TextView mTextView;

    private View mProgress;

    private RequestUserPresenter mPresenter = new RequestUserPresenter(this);

    private ColourChipView mColourChipView;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
//        mPresenter.performRequest();
    }

    @Override
    public void onRequestStart() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestFinish() {
        mProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onRequestSuccess(UserInfo userInfo) {
        mTextView.setText(userInfo.getName());
    }

    @Override
    public void onRequestFail(int meta) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_a;
    }

    @Override
    protected void initActivityViews() {
        mTextView = (TextView) findViewById(R.id.text);
        mProgress = findViewById(R.id.progress);
        mColourChipView = (ColourChipView) findViewById(R.id.color);

        findViewById(R.id.button).setOnClickListener(this);


//        ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
//
//        View view1 = layout.getChildAt(0);
//        View view2 = layout.getChildAt(1);
//        View view3 = layout.getChildAt(2);
//        View view4 = layout.getChildAt(3);
//        View view5 = layout.getChildAt(4);
//        View view6 = layout.getChildAt(5);
//
//
//        Random random = new Random();
//
//
//
//
//        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view1,"rotationX",0,360).setDuration(500);
//        animator1.setRepeatCount(ValueAnimator.INFINITE);
//        animator1.setInterpolator(new LinearInterpolator());
//        animator1.startSpill();
//
//        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view2,"rotationX",0,360).setDuration(400);
//        animator2.setRepeatCount(ValueAnimator.INFINITE);
//        animator2.setInterpolator(new LinearInterpolator());
//        animator2.startSpill();
//
//        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view3,"rotationX",0,360).setDuration(300);
//        animator3.setRepeatCount(ValueAnimator.INFINITE);
//        animator3.setInterpolator(new LinearInterpolator());
//        animator3.startSpill();
//
//        ObjectAnimator animator4 = ObjectAnimator.ofFloat(view4,"rotationX",0,360).setDuration(600);
//        animator4.setRepeatCount(ValueAnimator.INFINITE);
//        animator4.setInterpolator(new LinearInterpolator());
//        animator4.startSpill();
//
//        ObjectAnimator animator5 = ObjectAnimator.ofFloat(view5,"rotationX",0,360).setDuration(530);
//        animator5.setRepeatCount(ValueAnimator.INFINITE);
//        animator5.setInterpolator(new LinearInterpolator());
//        animator5.startSpill();
//
//        ObjectAnimator animator6 = ObjectAnimator.ofFloat(view6,"rotationX",0,360).setDuration(480);
//        animator6.setRepeatCount(ValueAnimator.INFINITE);
//        animator6.setInterpolator(new LinearInterpolator());
//        animator6.startSpill();
//
//
//        view1.setRotation(random.nextInt(360));
//        view2.setRotation(random.nextInt(360));
//        view3.setRotation(random.nextInt(360));
//        view4.setRotation(random.nextInt(360));
//        view5.setRotation(random.nextInt(360));
//        view6.setRotation(random.nextInt(360));



    }

    @Override
    public void onClick(View v) {
//        mPresenter.performRequest();
//        onRunSchedulerExampleButtonClicked();

        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);

        if (mColourChipView.isStart()) {
            mColourChipView.stop();
        } else {
            mColourChipView.startSpill();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mColourChipView.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mColourChipView.stop();
    }

    private static final String TAG = "RxAndroidSamples";

    private Looper backgroundLooper;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        BackgroundThread backgroundThread = new BackgroundThread();
        backgroundThread.start();
        backgroundLooper = backgroundThread.getLooper();
    }

    void onRunSchedulerExampleButtonClicked() {
        sampleObservable()
                // Run on a background thread
                .subscribeOn(AndroidSchedulers.from(backgroundLooper))
                        // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError()", e);
                    }

                    @Override
                    public void onNext(String string) {
                        Log.d(TAG, "onNext(" + string + ")");
                    }
                });
    }

    static Observable<String> sampleObservable() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override public Observable<String> call() {
                try {
                    // Do some long running operation
                    Thread.sleep(TimeUnit.SECONDS.toMillis(5));
                } catch (InterruptedException e) {
                    throw OnErrorThrowable.from(e);
                }
                return Observable.just("one", "two", "three", "four", "five");
            }
        });
    }

    static class BackgroundThread extends HandlerThread {
        BackgroundThread() {
            super("SchedulerSample-BackgroundThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        }
    }

}
