package com.garagesale.gapp.garagesale.service;

import com.garagesale.gapp.garagesale.response.UserListResponse;
import com.garagesale.gapp.garagesale.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
/**
 * Created by gimbyeongjin on 2017. 6. 17..
 * 로그인 관련 Restful request, response 정의
 * 한 화면 당 하나씩 있어야함
 */

public interface LoginService {
    @FormUrlEncoded
    @POST("user/login")
    Call<UserResponse> loginPost(@Field("email") String email, @Field("password") String password);

    @POST("user/auth/token")
    Call<UserResponse> tokenLoginPost();

    @FormUrlEncoded
    @POST("user/list")
    Call<UserListResponse> getUserList(@Field("longitude") String longitude, @Field("latitude") String latitude);
}
