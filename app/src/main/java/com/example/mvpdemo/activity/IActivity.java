package com.example.mvpdemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mvpdemo.R;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * Created by wangchao on 16/9/7.
 */
public class IActivity extends BaseActivity implements SurfaceHolder.Callback, MediaRecorder.OnErrorListener, Camera.AutoFocusCallback {

    Camera mCamera;
    private Button mButton;
    private Button mChange;
    private Button mWaterMark;
    int camWidth = 320;
    int camHeight = 240;

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private boolean isRecording;
    private MediaRecorder mMediaRecorder;
    private EditText mEditText;
    private TextView mTextView;


    private String mPath;

    private int mCurrentCamera = Camera.CameraInfo.CAMERA_FACING_BACK;

    private File mOutPut;

    /**
     * -i /storage/emulated/0/mvpDemo/1476255125765.mp4 -i /storage/emulated/0/mvpDemo/water_mark_temp.png -filter_complex overlay=5:5 -codec:a copy /storage/emulated/0/mvpDemo/out.mp4
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            FFmpeg.getInstance(this).loadBinary(new FFmpegLoadBinaryResponseHandler() {
                @Override
                public void onFailure() {

                }

                @Override
                public void onSuccess() {

                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {

                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_i;
    }

    @Override
    protected void initActivityViews() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
//        mSurfaceView.setScaleY(640 * (float) 640 / 360 / 360);
//        mSurfaceView.setmat
        mButton = (Button) findViewById(R.id.button);
        mChange = (Button) findViewById(R.id.change);
        mTextView = (TextView) findViewById(R.id.text);
        mEditText = (EditText) findViewById(R.id.edit);
        mWaterMark = (Button) findViewById(R.id.add);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTextView.setText(s);
            }
        });

        mWaterMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPlayPage();
            }
        });

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

        mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCamera();
            }
        });

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }

    private void changeCamera() {
        mCurrentCamera = mCurrentCamera ^ Camera.CameraInfo.CAMERA_FACING_FRONT;
        openCamera(mCurrentCamera);
        startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        openCamera(mCurrentCamera);
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void startRecord() {

        Log.i("FFmpeg", "CUP-ABI = " + Build.CPU_ABI);

        isRecording = true;
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setOnErrorListener(IActivity.this);
        } else {
            mMediaRecorder.reset();
        }

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
//        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        // Step 2: Set sources
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//before setOutputFormat()
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//before setOutputFormat()
        //设置视频输出的格式和编码
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_TIME_LAPSE_CIF);
        //after setVideoSource(),after setOutFormat()
        mMediaRecorder.setVideoSize(camWidth, camHeight);
        mMediaRecorder.setAudioEncodingBitRate(44100);
//        if (mProfile.videoBitRate > 2 * 1024 * 1024)
//            mMediaRecorder.setVideoEncodingBitRate(2 * 1024 * 1024);
//        else
            mMediaRecorder.setVideoEncodingBitRate(1024 * 512);
        //after setVideoSource(),after setOutFormat();
        mMediaRecorder.setVideoFrameRate(24);
        mMediaRecorder.setOrientationHint(mCurrentCamera == 0 ? 90 : 270);
        //after setOutputFormat()
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //after setOutputFormat()
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // Step 3: Set output file
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + "mvpDemo/");
        File file = new File(dir, "" + System.currentTimeMillis() + ".mp4");

        mOutPut = new File(dir, "output" + System.currentTimeMillis() + ".mp4");
//        if (!mOutPut.exists()) {
//            try {
//                mOutPut.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        try {

            if (!dir.exists()) {
                dir.mkdirs();
            }

            file.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }
        mPath = file.getAbsolutePath();
        mMediaRecorder.setOutputFile(file.getAbsolutePath());
        // Step 4: start and return
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void stopRecord() {
        isRecording = false;
        mMediaRecorder.setOnErrorListener(null);
        mMediaRecorder.setPreviewDisplay(null);
        mMediaRecorder.stop();
        mCamera.stopPreview();
        mCamera.lock();
        mMediaRecorder.release();
        mMediaRecorder = null;

    }

    private File saveBitmap(Bitmap mBitmap, File file, boolean isNeedRecycle) {
        if (mBitmap == null) {
            return null;
        }

        if (!file.exists()) {
            File parentFile = file.getParentFile();
            parentFile.mkdirs();
            try {
                file.createNewFile();
            } catch (Exception e) {
                return null;
            }

        } else {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            if (isNeedRecycle) {
                mBitmap.recycle();
            }
            fos.flush();
            fos.close();
            return file;
        } catch (FileNotFoundException e) {
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void wormUp(final String pic) {
        Log.i("FFmpeg","wormUp");
        if (TextUtils.isEmpty(mPath)) {
            return;
        }

        String[] cmd = new String[]{"-i",mPath,"-i",pic};
////        String[] cmd = new String[]{"-i",mPath,"-vf","movie="+file.getAbsolutePath()+" [logo]; [in][logo] overlay=5:5 [out]",mOutPut.getAbsolutePath()};
        try {
            final long time = SystemClock.elapsedRealtime();
            FFmpeg.getInstance(getApplicationContext())
                    .execute(cmd, new FFmpegExecuteResponseHandler() {
                        @Override
                        public void onSuccess(String message) {
                            Log.i("FFmpeg", "onSuccess       message = " + message + "   cost = " + (SystemClock.elapsedRealtime() - time));
//                            Intent intent = new Intent(IActivity.this,PlayActivity.class);
//                            intent.putExtra(PlayActivity.EXTRA_PATH,mOutPut.getAbsolutePath());
//                            startActivity(intent);
                        }

                        @Override
                        public void onProgress(String message) {
                            Log.i("FFmpeg","onProgress       message = " + message);
                        }

                        @Override
                        public void onFailure(String message) {
                            Log.i("FFmpeg","onFailure       message = " + message);
                        }

                        @Override
                        public void onStart() {
                            Log.i("FFmpeg","onStart");
                        }

                        @Override
                        public void onFinish() {
                            overlay(pic);
                            Log.i("FFmpeg","onFinish" + "   cost = " + (SystemClock.elapsedRealtime() - time));
                        }
                    });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
            Log.i("FFmpeg", "onSuccess       message = " + e.getMessage());
        }

    }

    private void overlay(final String pic ) {


        Log.i("FFmpeg","overlay");

//        String[] cmd = new String[]{"-i",mPath,"-i",pic,"-filter_complex","overlay=5:5","-codec:a","copy","-y",mOutPut.getAbsolutePath()};
        String[] cmd = new String[]{"-i",mPath,"-vf","movie="+pic+" [logo]; [in][logo] overlay=0:0 [out]",mOutPut.getAbsolutePath()};
        try {
            final long time = SystemClock.elapsedRealtime();
            FFmpeg.getInstance(getApplicationContext())
                    .execute(cmd, new FFmpegExecuteResponseHandler() {
                        @Override
                        public void onSuccess(String message) {

                            Log.i("FFmpeg", "onSuccess       message = " + message + "   cost = " + (SystemClock.elapsedRealtime() - time));
//                            overlay(pic);
                            Intent intent = new Intent(IActivity.this,PlayActivity.class);
                            intent.putExtra(PlayActivity.EXTRA_PATH,mOutPut.getAbsolutePath());
                            startActivity(intent);
                        }

                        @Override
                        public void onProgress(String message) {
                            Log.i("FFmpeg","onProgress       message = " + message);
                        }

                        @Override
                        public void onFailure(String message) {
                            Log.i("FFmpeg","onFailure       message = " + message);
                        }

                        @Override
                        public void onStart() {
                            Log.i("FFmpeg","onStart");
                        }

                        @Override
                        public void onFinish() {
                            Log.i("FFmpeg","onFinish" + "   cost = " + (SystemClock.elapsedRealtime() - time));
                        }
                    });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
            Log.i("FFmpeg", "onSuccess       message = " + e.getMessage());
        }

    }

    private void toPlayPage() {
        if (TextUtils.isEmpty(mPath)) {
            return;
        }

        mTextView.setDrawingCacheEnabled(true);
        Bitmap drawingCache = mTextView.getDrawingCache();
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + "mvpDemo/");
        final File file = new File(dir, "water_mark_temp.png");
        if (drawingCache != null) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(drawingCache, 240, 320, false);
            saveBitmap(scaledBitmap, file, true);
        }

//        wormUp(file.getAbsolutePath());

        overlay(file.getAbsolutePath());
//
//        mTextView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                overlay(file.getAbsolutePath());
//            }
//        },1000);


//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                final long time = SystemClock.elapsedRealtime();
//                CustomUtil.addBitmapOverlayOnVideo(IActivity.this, mPath, file.getAbsolutePath(), mOutPut.getAbsolutePath());
//                Log.i("FFmpeg", "onFinish" + "   cost = " + (SystemClock.elapsedRealtime() - time));
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                Intent intent = new Intent(IActivity.this, PlayActivity.class);
//                intent.putExtra(PlayActivity.EXTRA_PATH, mOutPut.getAbsolutePath());
////        intent.putExtra(PlayActivity.EXTRA_PATH, mPath);
//                startActivity(intent);
//
//            }
//        }.execute();
////
//
//        Intent intent = new Intent(IActivity.this, PlayActivity.class);
////        intent.putExtra(PlayActivity.EXTRA_PATH, mOutPut.getAbsolutePath());
//        intent.putExtra(PlayActivity.EXTRA_PATH, mPath);
//        startActivity(intent);

//        String vp = "/storage/emulated/0/DCIM/Camera/VCameraDemo/1475917698370/0.v";
////        String[] comm = new String[]{"-f","rawvideo","-s","480x480","-i",vp,"-vf","movie="+file.getAbsolutePath()+" [watermark];[in][watermark] overlay=5:5 [out]","-c:v","libx264",mOutPut.getAbsolutePath()};
//        String[] comm = new String[]{"-f","rawvideo","-s","480x480","-i",vp,"-c:v","libx264",mOutPut.getAbsolutePath()};
//          -i /storage/emulated/0/mvpDemo/1476100096712.mp4 -i /storage/emulated/0/mvpDemo/water_mark_temp.png -filter_complex overlay=5:5 -codec:a copy /storage/emulated/0/mvpDemo/out.mp4

    }

    private void openCamera(int CameraFacing) {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraFacing) {
                try {
                    mCamera = Camera.open(i);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    mCamera = null;
                    continue;
                }
                break;
            }
        }
        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            if (null != mCamera) {
                mCamera.release();
                mCamera = null;
            }
        }
    }

    private void startPreview() {
        Camera.Parameters parameters = mCamera.getParameters();

        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        parameters.setFlashMode("off"); // 无闪光灯
        parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        parameters.setPreviewFormat(ImageFormat.YV12);
        parameters.set("orientation", "portrait");
//        parameters.set("orientation", "landscape");
        parameters.setPictureSize(320, 240);
        parameters.setPreviewSize(camWidth, camHeight);
        parameters.setRotation(90);
        //这两个属性 如果这两个属性设置的和真实手机的不一样时，就会报错
        mCamera.setParameters(parameters);
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
}
