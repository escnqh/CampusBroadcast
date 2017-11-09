package com.newthread.campusbroadcast.webApi;

import com.newthread.campusbroadcast.WebService.LogInService;

import retrofit2.Retrofit;

/**
 * Created by HP on 2017/7/24.
 */

public class LogInApi extends WebApi {
    String url="http://123.207.221.213:8080/";
    Retrofit retrofit=getApi(url);
    @Override
    public <T> T getService() {
        return(T) retrofit.create(LogInService.class);
    }
}
