package com.newthread.campusbroadcast.WebService;

import com.newthread.campusbroadcast.User.CurrentChannelBean;
import com.newthread.campusbroadcast.User.SignupGsonBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by HP on 2017/8/1.
 */

public interface CurrentChannelService {
    @POST("radio/currentChannel")
    Call<CurrentChannelBean> getState(@Body RequestBody requestBody);
}
