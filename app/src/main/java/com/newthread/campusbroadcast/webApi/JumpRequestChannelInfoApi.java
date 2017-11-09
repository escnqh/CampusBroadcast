package com.newthread.campusbroadcast.webApi;

import com.newthread.campusbroadcast.WebService.JumpRequstChannelInfoService;

import retrofit2.Retrofit;

/**
 * Created by 倪启航 on 2017/7/30.
 */

public class JumpRequestChannelInfoApi extends WebApi {

    String url="http://123.207.221.213:8080/radio/";
    Retrofit retrofit=getApi(url);

    @Override
    public <T> T getService() {
        return (T) retrofit.create(JumpRequstChannelInfoService.class);
    }
}
