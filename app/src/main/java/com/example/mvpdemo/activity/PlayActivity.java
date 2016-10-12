package com.example.mvpdemo.activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.example.mvpdemo.R;

import java.io.IOException;


/**
 * Created by wangchao on 16/9/12.
 */
public class PlayActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener {

    public static final String EXTRA_PATH = "extra_path";

    private String mPath;

    private SurfaceView mSurfaceView;

    MediaPlayer mPlayer;

    public int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            mPath = getIntent().getStringExtra(EXTRA_PATH);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_play;
    }

    @Override
    protected void initActivityViews() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(this);

        mSurfaceView.setOnClickListener(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
//        mPlayer = MediaPlayer.create(this, Uri.parse(mPath),holder);
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(this, Uri.parse(mPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.setDisplay(holder);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setLooping(true);
        play();
    }

    private void play() {
//        mPlayer.reset();
        if (!mPlayer.isPlaying()) {
            try {
                mPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.start();
        }

//        try {
//            mPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        }
//        mPlayer.start();

        if (position > 0) {
            mPlayer.seekTo(position);
            position = 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.release();
    }

    @Override
    public void onClick(View v) {
        if (mPlayer.isPlaying()) {
            position = mPlayer.getCurrentPosition();
            mPlayer.stop();
        } else {
            play();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


}
