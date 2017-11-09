package com.newthread.campusbroadcast.WebService;

import com.newthread.campusbroadcast.User.SignupGsonBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by HP on 2017/7/30.
 */

public interface ApplyService {
    @POST("radio/applyChannel")
    Call<SignupGsonBean> getState(@Body RequestBody requestBody);
}
