package com.newthread.campusbroadcast.WebService;

import com.newthread.campusbroadcast.bean.ChannelBean.GSearchResult;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by 张云帆 on 2017/7/31.
 */

public interface SearchChannelService {
    @POST("radio/searchByID")
    Call<GSearchResult> getSearchResult(@Body RequestBody requestBody);

}
