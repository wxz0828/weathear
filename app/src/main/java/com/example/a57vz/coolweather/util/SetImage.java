package com.example.a57vz.coolweather.util;

import com.example.a57vz.coolweather.R;

/**
 * Created by 57VZ on 2017/5/2.
 */

public class SetImage {
    public static int getWeatherTypeBackgroundID(String type){
        if (type == null){
            return R.drawable.night;
        }
        int ImageId = 0;
        switch (type){
            case "多云":
                ImageId = R.drawable.bg_13;
                break;
            default:
                ImageId = R.drawable.night;
                break;
        }
        return ImageId;
    }
}
