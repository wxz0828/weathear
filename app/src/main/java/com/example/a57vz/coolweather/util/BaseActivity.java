package com.example.a57vz.coolweather.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.baidu.location.LocationClient;

/**
 * Created by 57VZ on 2017/5/1.
 */

public class BaseActivity extends AppCompatActivity {
    public static LocationClient mLocationClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        mLocationClient = new LocationClient(getApplicationContext());
      //  mLocationClient.registerLocationListener(new LBS(this));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
