package com.garagesale.gapp.garagesale.service;

import com.garagesale.gapp.garagesale.response.ProductResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by gimbyeongjin on 2017. 6. 17..
 * 로그인 관련 Restful request, response 정의
 * 한 화면 당 하나씩 있어야함
 */

public interface ProductService {
    @FormUrlEncoded
    @POST("product/create")
    Call<ProductResponse> createProduct(@Field("name") String name, @Field("description") String description, @Field("price") Integer price);

}
