package com.example.a57vz.coolweather.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.example.a57vz.coolweather.WeatherActivity;

/**
 * Created by 57VZ on 2017/4/29.
 */

public class LBS implements BDLocationListener {
    public LocationClient locationClient;
    public Context context;
    public static String cityName;

    public LBS (Context context ,LocationClient locationClient){
        this.context = context;
        this.locationClient = locationClient;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        String city = bdLocation.getCity();
        if (!TextUtils.isEmpty(city)) {
            cityName = city.replace("市", "");
            LogUtil.d("定位成功", "当前城市为" + cityName);
            queryWeatherCode(cityName);
            Toast.makeText(context, "当前城市"+cityName, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "定位失败加载默认城市", Toast.LENGTH_SHORT).show();
             cityName = "厦门";
            queryWeatherCode(cityName);
        }
        locationClient.stop();
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    /***
     * 城市转化编码
     * */
    private void queryWeatherCode(String cityName) {

        String weatherId = Common.getCityIdByName(cityName);
        LogUtil.d("天气LBS", weatherId);
        if (weatherId != null) {
            Intent intent = new Intent(context, WeatherActivity.class);
            intent.putExtra("weather_LBSid", weatherId);
            context.startActivity(intent);
        }

    }
}
