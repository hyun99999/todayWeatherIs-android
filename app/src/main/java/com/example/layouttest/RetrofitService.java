package com.example.layouttest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RetrofitService {
    // 베이스 Url
    @GET("data/2.5/weather")
    Call<WeatherTest> getWeather(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String appid);
}
