package com.example.customnavigationdrawer.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitConfig {
    Gson gson = new GsonBuilder().setLenient().create();
    RetrofitConfig retrofit = new Retrofit.Builder()
            .baseUrl("https://smarthomektmdt.000webhostapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(RetrofitConfig.class);
    @FormUrlEncoded
    @POST("/request_fcm.php")
    Call<String> request_fcm(@Field("token_device") String token,
                              @Field("humi") String humi,
                                  @Field("temp") String temp);
}
