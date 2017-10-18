package com.example.a57vz.coolweather.util;

import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.a57vz.coolweather.MyApplication;

/**
 * Created by 57VZ on 2017/4/29.
 */

public class LBS {
    public LocationClient mLocationClient;
    public String WeatherLBSId;
    public String Id;

    //  BDLocationListener myListener = new MyLocationListener();

    //  public static String weid = "CN101230101";


    public String getWeatherLBSId() {
        return WeatherLBSId;
    }

    public void setWeatherLBSId(String weatherLBSId) {
        this.WeatherLBSId = weatherLBSId;
    }

    public void bdCommon() {
        mLocationClient = new LocationClient(MyApplication.getContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String city = bdLocation.getCity();
            //  if (!TextUtils.isEmpty(city)) {
            if (city.length() > 0) {
                //closeWeatherProgressDialog();
                String cityName = city.replace("市", "");
                LogUtil.d("WeatherActivity", "当前城市为" + cityName);
                //  WeatherActivity weatherActivity = new WeatherActivity();
                // weatherActivity.queryWeatherCode(cityName);
                queryWeatherCode(cityName);
                Toast.makeText(MyApplication.getContext(), "定位成功,你当前城市为：" + city, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

    }


    /**
     * 初始化百度地图
     */
    public void initLocation() {
        LocationClientOption option = new LocationClientOption();
        //  option.setScanSpan(5000);
        // option.setLocationMode();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 转换城市编码
     */

    public void queryWeatherCode(String cityName) {

       Id = Common.getCityIdByName(cityName);
        // weid = SweatherId;
       // WeatherLBSId = SweatherId;
        this.setWeatherLBSId(Id);
        //   mWeatherId = SweatherId;
        //  requestWeather(SweatherId);
//        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
//        editor.putString("weather",SweatherId);
//        editor.apply();

        LogUtil.d("天气", Id);
        //return SweatherId;
//        if (weatherId != null) {
//            Intent intent = new Intent(this,WeatherActivity.class);
//            intent.putExtra("weather_id",weatherId);
//            startActivity(intent);
//            finish();
////            WeatherFragment weatherFragment = new WeatherFragment();
////            Bundle bundle = new Bundle();
////            bundle.putString("weather_id", weatherId);
////            weatherFragment.setArguments(bundle);
////            manager = getSupportFragmentManager();
////            FragmentTransaction transaction = manager.beginTransaction();
////            transaction.add(R.id.myCoor, weatherFragment).commit();
//////            }
//        }
    }

//    public static String WeatherLBSId(){
//        final String[] wid = new String[1];
//        lbs = new LBSreturn() {
//            @Override
//            public void onFinish(String response) {
//                wid[0] = response;
//            }
//        };
//        return wid[0];
//    }


}
