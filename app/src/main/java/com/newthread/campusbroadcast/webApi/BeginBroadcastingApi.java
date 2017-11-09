package com.newthread.campusbroadcast.webApi;

import com.newthread.campusbroadcast.WebService.BeginBroadcastingService;

import retrofit2.Retrofit;

/**
 * Created by 倪启航 on 2017/7/24.
 */

public class BeginBroadcastingApi extends WebApi {
    String url="http://123.207.221.213:8080/radio/";
    Retrofit retrofit=getApi(url);
    @Override
    public <T> T getService() {
        return (T) retrofit.create(BeginBroadcastingService.class);
    }
}
