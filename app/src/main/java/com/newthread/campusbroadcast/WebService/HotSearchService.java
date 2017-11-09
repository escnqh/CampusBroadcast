package com.newthread.campusbroadcast.WebService;

import com.newthread.campusbroadcast.bean.ChannelBean.GHotSearch;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by 张云帆 on 2017/7/31.
 */

public interface HotSearchService {
        @GET("radio/hotSearch")
        Call<GHotSearch> getInfor();
}
