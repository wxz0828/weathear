package com.example.a57vz.coolweather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.a57vz.coolweather.util.BaseActivity;
import com.example.a57vz.coolweather.util.Common;
import com.example.a57vz.coolweather.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {


    public LocationClient mLocationClient;
    private ProgressDialog progressDialog;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {

         initLocation();
        }
//
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        if (prefs.getString("weather", null) != null) {
//            Intent intent = new Intent(this, WeatherActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }

                initLocation();

                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }


    /**
     * 初始化百度地图
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //option.setScanSpan(5000);
        // option.setLocationMode();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            showWeatherProgressDialog();
            String city = bdLocation.getCity();
             if (!TextUtils.isEmpty(city)) {
                 closeWeatherProgressDialog();
            String cityName = city.replace("市", "");
            LogUtil.d("定位成功", "当前城市为" + cityName);
            queryWeatherCode(cityName);
            Toast.makeText(MainActivity.this, "当前城市"+cityName, Toast.LENGTH_SHORT).show();
           }else {
                 Toast.makeText(MainActivity.this, "定位失败加载默认城市", Toast.LENGTH_SHORT).show();
                 String cityName = "厦门";
                 closeWeatherProgressDialog();
                 queryWeatherCode(cityName);
             }
            mLocationClient.stop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
//    /**
//     * 转换城市编码
//     */
    private void queryWeatherCode(String cityName) {

      String weatherId = Common.getCityIdByName(cityName);
        LogUtil.d("天气",weatherId);
        if (weatherId != null) {
            Intent intent = new Intent(this,WeatherActivity.class);
            intent.putExtra("weather_LBSid",weatherId);
            startActivity(intent);
            finish();
       }
  }
    /**
     * 显示对话框
     */
    private void showWeatherProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在定位城市·····");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void closeWeatherProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
