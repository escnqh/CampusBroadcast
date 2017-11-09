package com.newthread.campusbroadcast.webApi;

import com.newthread.campusbroadcast.WebService.SignUpService;
import com.newthread.campusbroadcast.WebService.UserImgService;

import retrofit2.Retrofit;

/**
 * Created by HP on 2017/7/30.
 */

public class UserImgApi extends WebApi {
    String url="http://123.207.221.213:8080/";
    Retrofit retrofit=getApi(url);
    @Override
    public <T> T getService() {
        return (T)retrofit.create(UserImgService.class);
    }
}
