package com.example.a57vz.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 57VZ on 2017/4/25.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort{
        @SerializedName("txt")
        public String info;
    }
    public class CarWash{
        @SerializedName("txt")
        public String into;
    }
    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
