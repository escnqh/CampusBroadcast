package com.newthread.campusbroadcast.WebService;

import com.newthread.campusbroadcast.bean.isTrueBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by 倪启航 on 2017/7/24.
 */

public interface BeginBroadcastingService {
    @POST("startTime")
    Call<isTrueBean>getState(
            @Body RequestBody requestBody
            );
}
