package com.newthread.campusbroadcast.webApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 张云帆 on 2017/7/11.
 */

public abstract class WebApi {
    public static String WEB_KEY="nt_nougat";

    Retrofit getApi(String url){
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public abstract <T> T getService();
}
