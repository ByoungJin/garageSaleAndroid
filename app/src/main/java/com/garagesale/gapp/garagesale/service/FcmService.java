package com.garagesale.gapp.garagesale.service;

import com.garagesale.gapp.garagesale.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by juyeol on 2017-06-25.
 * FCM 기본 테스트 요청들
 */

public interface FcmService {
    @FormUrlEncoded
    @POST("fcm/saveFcmUser")
    Call<UserResponse>  fcmSavePost(@Field("userId") String userId, @Field("regId") String regId);

    @POST("fcm/sendFcmNotification")
    Call<UserResponse> reqFcmNotificaitioPost();
}
