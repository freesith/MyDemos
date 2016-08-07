package com.example.mvpdemo.activity;

import android.graphics.PointF;
import android.os.SystemClock;
import android.util.Log;

import com.example.mvpdemo.R;
import com.example.mvpdemo.largeimage.ImageSource;
import com.example.mvpdemo.largeimage.ImageViewState;
import com.example.mvpdemo.largeimage.SubsamplingScaleImageView;
import com.example.mvpdemo.utils.Vars;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Tish on 2016/8/7.
 */
public class GActivity extends BaseActivity {

    private SubsamplingScaleImageView mImage;


    @Override
    protected int getLayoutResource() {
        return R.layout.layout_activity_g;
    }

    @Override
    protected void initActivityViews() {
        mImage = (SubsamplingScaleImageView) findViewById(R.id.sub_image);

        Vars.startTime = SystemClock.elapsedRealtime();

        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL("http://ww1.sinaimg.cn/bmiddle/a083fa30gw1evex3psnfij20c8cq17wi.jpg");
                    URLConnection conn = url.openConnection();
                    final InputStream inputStream = conn.getInputStream();

                    mImage.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("xx","======== download success  cost = " + (SystemClock.elapsedRealtime() - Vars.startTime));
                            mImage.setImage(ImageSource.stream(inputStream),new ImageViewState(10,new PointF(0,0),0));
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();


    }
}
