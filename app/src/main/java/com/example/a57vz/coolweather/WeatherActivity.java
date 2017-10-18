package com.example.a57vz.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.bumptech.glide.Glide;
import com.example.a57vz.coolweather.gson.Forecast;
import com.example.a57vz.coolweather.gson.Weather;
import com.example.a57vz.coolweather.service.AutoUpdateService;
import com.example.a57vz.coolweather.util.ActivityCollector;
import com.example.a57vz.coolweather.util.BaseActivity;
import com.example.a57vz.coolweather.util.HttpUtil;
import com.example.a57vz.coolweather.util.LogUtil;
import com.example.a57vz.coolweather.util.SetImage;
import com.example.a57vz.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity {
    public String SweatherId;
    public String tempWeather;
    public DrawerLayout drawerLayout;

    private Button navButton;

    private ScrollView weatherLayout;

    private TextView titleCity, titleUpdateTime, degreeText, weatherInfoText;

    private TextView aqiText, pm25Text, comfortText, carWashText, sportText;

    private ImageView bingPicImg;

    private LinearLayout forecastLayout;
    private static final String TAG = "WeatherActivity";

    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;

    public LocationClient mLocationClient;


    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);


        //初始化控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);

       // bingPicImg.setImageResource(R.drawable.bg_13);
//        int ImageId = SetImage.getWeatherTypeBackgroundID("多云");
//        bingPicImg.setImageResource(ImageId);

        //得到定位的城市
        Intent intent = getIntent();
        String weatherLBSId = intent.getStringExtra("weather_LBSid");
        if (!TextUtils.isEmpty(weatherLBSId)) {
            requestWeather(weatherLBSId);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        //下拉刷新初始化
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

//         SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//                 String weatherString = prefs.getString("weather", null);
//            if (weatherString != null) {
//                //有缓存时直接解析天气数据
//                Weather weather = Utility.handleWeatherResponse(weatherString);
//                mWeatherId = weather.basic.weatherId;
//                showWeatherInfo(weather);
//
//            } else {
        //无缓存时去服务器查询
       mWeatherId = getIntent().getStringExtra("weather_id");//通过ChooseAreaFragment中当县级别时传递过来的weatherId
       // String weatherId = getIntent().getStringExtra("weather_id");
        if (!TextUtils.isEmpty(mWeatherId)) {
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
   //     }
 }
        /**
         * 下拉刷新
         * */
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
                // loadBingPic();
            }
        });
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String bingPic = prefs.getString("bing_pic", null);
//      //  int i = R.drawable.night;
//        if (bingPic != null) {
//            Glide.with(this).load(bingPic).into(bingPicImg);
//        } else {
//           // Glide.with(this).load(R.drawable.night).into(bingPicImg);
//           loadBingPic();
//        }

        /**
         * 滑动菜单
         * */
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add_city:
                                Intent intent = new Intent(WeatherActivity.this, ChooseCity.class);
                                startActivity(intent);
                                break;
                            case R.id.multi_city:
                                Toast.makeText(WeatherActivity.this,"开发中",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.setting:
                                Toast.makeText(WeatherActivity.this,"开发中",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.about:
                                Toast.makeText(WeatherActivity.this,"谢谢支持",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.exit:
                                ActivityCollector.finishAll();
                                Process.killProcess(Process.myPid());
                                break;
                        }

                        return false;
                    }
                });
    }


    /**
     * 根据天气id请求城市天气信息
     */
    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +

                "&key=fccf3cd293c441ffbed0b04ab26514ae";

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                LogUtil.d("Weather", responseText);
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
//                            SharedPreferences.Editor editor = PreferenceManager.
//                                    getDefaultSharedPreferences(WeatherActivity.this).edit();
//                            editor.putString("weather", responseText);
//                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败!!!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败!",
                                Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

        });
        // loadBingPic();
        //  Glide.with(this).load(R.drawable.night).into(bingPicImg);
    }

    /**
     * 加载必应每日一图
     */
//    private void loadBingPic() {
//        String requestBingPic = "http://guolin.tech/api/bing_pic";
//        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                final String bingPic = response.body().string();
//                SharedPreferences.Editor editor = PreferenceManager.
//                        getDefaultSharedPreferences(WeatherActivity.this).edit();
//                editor.putString("bing_pic", bingPic);
//                editor.apply();
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Glide.with(WeatherActivity.this).load(R.drawable.night).into(bingPicImg);
//            }
//        });
//    }

//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//        });
    //   }

    /**
     * 处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;
        LogUtil.d("多云",weatherInfo);
        //设置相对应的天气图片
        int imageId = SetImage.getWeatherTypeBackgroundID(weatherInfo);
       // bingPicImg.setImageResource(imageId);
        Glide.with(WeatherActivity.this).load(imageId).into(bingPicImg);  //加载图片

        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime+"更新");
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();

        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.into_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);

            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null) {
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.into;
        String sport = "运动指数：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    /*
    * 点击返回键两次退出程序
    **/
    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            ActivityCollector.finishAll();
            finish();
            System.exit(0);
            Process.killProcess(Process.myPid());
        }
    }


}
