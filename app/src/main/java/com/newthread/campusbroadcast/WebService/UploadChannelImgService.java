package com.newthread.campusbroadcast.WebService;

import com.newthread.campusbroadcast.bean.ChannelBean.GChannelResult;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by 张云帆 on 2017/7/30.
 */

public interface UploadChannelImgService {
    @Multipart
    @POST("radio/uploadChannelImg")
    Call<GChannelResult>  UploadChannelImg(@Query("channelID") int channelID,
                                @Part MultipartBody.Part file);
}
