package com.newthread.campusbroadcast.WebService;

import com.newthread.campusbroadcast.User.SignupGsonBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by HP on 2017/7/30.
 */

public interface UserImgService {
    @Multipart
    @POST("radio/uploadUserImg")
    Call<SignupGsonBean> upload(@Part("userID") int userID,
                                @Part MultipartBody.Part file);
}
