package com.newthread.campusbroadcast.WebService;

import com.newthread.campusbroadcast.User.LoginGsonBean;
import com.newthread.campusbroadcast.User.SignupBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by HP on 2017/7/24.
 */

public interface LogInService {
   @POST("radio/login")
   Call<LoginGsonBean> getState(@Body RequestBody requestBody);
}
