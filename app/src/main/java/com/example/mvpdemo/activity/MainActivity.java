package com.example.mvpdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mvpdemo.R;
import com.example.mvpdemo.widget.Velocity;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


//        Velocity a = new Velocity(3,5);
//        Velocity b = new Velocity(10,4);
//        Velocity b1 = b.merge(a.aMerge(b).multi((float) 1 / 2 * 2));
//        Velocity a1 = a.aMerge(a.aMerge(b).multi( (float)1 / 2 * 2));
//        Log.i("xx","a1 = " + a1 + "   b1 = " + b1);
//
//        Velocity aaMergeb = a.aMerge(b);
//        Log.i("xx","aamerge = " + aaMergeb);
//        b1 = b.merge(aaMergeb);
//        Log.i("xx","b1 = " + b1);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.a:
                startActivity(AActivity.class);
                break;

            case R.id.b:
                startActivity(BActivity.class);
                break;

            case R.id.c:
                startActivity(CActivity.class);
                break;
            case R.id.d:
                startActivity(DActivity.class);
                break;
            case R.id.e:
                startActivity(EActivity.class);
                break;
            case R.id.f:
                startActivity(FActivity.class);
                break;
            case R.id.g:
                startActivity(GActivity.class);
                break;
            case R.id.h:
                startActivity(HActivity.class);
                break;
        }
    }

    private void startActivity(Class clazz) {
        Intent intent = new Intent();
        intent.setClass(this,clazz);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
