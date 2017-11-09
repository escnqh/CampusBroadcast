package com.newthread.campusbroadcast.WebService;

import com.newthread.campusbroadcast.User.SignupGsonBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by HP on 2017/7/20.
 */

public interface SignUpService {
    @POST("radio/register")
    Call<SignupGsonBean> getState(@Body RequestBody requestBody);
}
