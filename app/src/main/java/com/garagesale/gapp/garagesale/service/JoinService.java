package com.garagesale.gapp.garagesale.service;

import com.garagesale.gapp.garagesale.entity.Account;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by gimbyeongjin on 2017. 6. 17..
 * 로그인 관련 Restful request, response 정의
 * 한 화면 당 하나씩 있어야함
 */

public interface JoinService {
    @FormUrlEncoded
    @POST("/signup")
    Call<Account> signUp(@Field("name") String name,@Field("email") String email, @Field("password") String password);

}
