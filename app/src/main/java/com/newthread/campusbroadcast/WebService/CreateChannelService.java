package com.newthread.campusbroadcast.WebService;

import com.newthread.campusbroadcast.bean.ChannelBean.GChannelResult;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by 张云帆 on 2017/7/30.
 */

public interface CreateChannelService {
    @POST("radio/createChannel")
    Call<GChannelResult> getCreatResult(@Body RequestBody requestBody);
}
