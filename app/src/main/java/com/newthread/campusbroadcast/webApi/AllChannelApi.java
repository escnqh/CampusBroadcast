package com.newthread.campusbroadcast.webApi;

import com.newthread.campusbroadcast.WebService.AllChannelService;

import retrofit2.Retrofit;

/**
 * Created by 张云帆 on 2017/8/1.
 */

public class AllChannelApi extends WebApi {
    String url = "http://123.207.221.213:8080/";
    Retrofit retrofit = getApi(url);

    @Override
    public <T> T getService() {
        return (T) retrofit.create(AllChannelService.class);
    }
}
