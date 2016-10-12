package com.example.mvpdemo.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.example.mvpdemo.R;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by wangchao on 16/9/7.
 */
public class JActivity extends BaseActivity implements SurfaceHolder.Callback, MediaRecorder.OnErrorListener, Camera.AutoFocusCallback, Camera.PreviewCallback {

    private static final String TAG = "JActivity";
    private static final int FRAME_RATE = 15;
    Camera mCamera;
    private Button mButton;
    int camWidth = 320;
    int camHeight = 240;
    private byte[] buff = new byte[camWidth * camHeight * 3 / 2];

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private boolean isRecording;

    private String mPath;
    private MediaCodec mediaCodec;

    private int mCount;
    private FrameListener frameListener;


    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_i;
    }

    @Override
    protected void initActivityViews() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isRecording) {
                    stopRecord();
                } else {
                    startRecord();
                }

            }
        });

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startRecord() {

        try {
            mediaCodec = MediaCodec.createEncoderByType("Video/AVC");
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaFormat mediaFormat = MediaFormat.createVideoFormat("Video/AVC", camWidth, camHeight);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 125000);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);
        mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mediaCodec.start();

        isRecording = true;

        File dir = new File(Environment.getExternalStorageDirectory() + "/" + "mvpDemo/");
        File file = new File(dir, "" + System.currentTimeMillis() + ".mp4");
        try {


            if (!dir.exists()) {
                dir.mkdirs();
            }

            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        mPath = file.getAbsolutePath();
        frameListener = new FrameListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onFrame(byte[] data, int offset, int length, int flag) {
                ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();
                int inputBufferIndex = mediaCodec.dequeueInputBuffer(-1);
                if (inputBufferIndex >= 0) {
                    ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                    inputBuffer.clear();
                    inputBuffer.put(buff, offset, length);
                    mediaCodec.queueInputBuffer(inputBufferIndex, 0, length, mCount * 1000000 / FRAME_RATE, 0);
                    mCount++;
                }

                MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
                while (outputBufferIndex >= 0) {
                    mediaCodec.releaseOutputBuffer(outputBufferIndex, true);
                    outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
                }
            }
        };


    }

    private void stopRecord() {

        frameListener = null;
        mCamera.stopPreview();
        mCamera.lock();

        toPlayPage();

    }

    private void toPlayPage() {
        if (TextUtils.isEmpty(mPath)) {
            return;
        }

        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra(PlayActivity.EXTRA_PATH, mPath);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startPreview() {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode("off"); // 无闪光灯
        parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        parameters.setPreviewFormat(ImageFormat.YV12);
//        parameters.set("orientation", "portrait");
        parameters.setPictureSize(camWidth, camHeight);
        parameters.setPreviewSize(camWidth, camHeight);
        parameters.setRotation(90);
        //这两个属性 如果这两个属性设置的和真实手机的不一样时，就会报错
        mCamera.setParameters(parameters);
        mCamera.addCallbackBuffer(buff);
        mCamera.setPreviewCallbackWithBuffer(this);
//        mCamera.setDisplayOrientation(90);
        mCamera.autoFocus(this);
        mCamera.startPreview();


    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        Log.i("IActivity", " onError     what = " + what + "     extra = " + extra);
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.i(TAG, "onPreviewFrame  date.length = " + data.length);

        if (frameListener != null) {
            frameListener.onFrame(data, 0, data.length, 0);
        }

        camera.addCallbackBuffer(buff);
    }
}


interface FrameListener {
    void onFrame(byte[] data, int offset, int length, int flag);
}
