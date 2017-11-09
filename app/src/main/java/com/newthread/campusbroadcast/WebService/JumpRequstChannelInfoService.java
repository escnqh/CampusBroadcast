package com.newthread.campusbroadcast.WebService;

import com.newthread.campusbroadcast.bean.ChannelJumpResultBean;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by 倪启航 on 2017/7/30.
 */

public interface JumpRequstChannelInfoService {

    @POST("searchByID")
    Call<ChannelJumpResultBean> getState(
            @Body RequestBody requestBody
    );
}
