package com.garagesale.gapp.garagesale.service;

import com.garagesale.gapp.garagesale.entity.Account;

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
    @POST("/saveFcmUser")
    Call<Account>  fcmSavePost(@Field("userId") String userId, @Field("regId") String regId);

    @POST("/sendFcmNotification")
    Call<Account> reqFcmNotificaitioPost();
}
