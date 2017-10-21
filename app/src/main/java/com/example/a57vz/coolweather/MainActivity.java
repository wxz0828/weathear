package com.example.a57vz.coolweather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.baidu.location.LocationClientOption;
import com.example.a57vz.coolweather.util.BaseActivity;
import com.example.a57vz.coolweather.util.LBS;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {


  //  public LocationClient mLocationClient;
    private ProgressDialog progressDialog;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

      BaseActivity.mLocationClient.registerLocationListener(new LBS(this,BaseActivity.mLocationClient));
        //申请权限
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
        BaseActivity.mLocationClient.setLocOption(option);
        BaseActivity.mLocationClient.start();
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
