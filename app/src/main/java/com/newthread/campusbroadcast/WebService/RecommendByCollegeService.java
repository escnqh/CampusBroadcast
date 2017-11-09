package com.newthread.campusbroadcast.WebService;

import com.newthread.campusbroadcast.bean.ChannelBean.GChannelInfor;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by 张云帆 on 2017/8/1.
 */

public interface RecommendByCollegeService {
    @POST("radio/recommendByCollege")
    Call<GChannelInfor> getRecommendChannelInfor(@Body RequestBody requestBody);
}
