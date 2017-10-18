package com.example.a57vz.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 57VZ on 2017/4/25.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt")
        public String info;
    }
}
