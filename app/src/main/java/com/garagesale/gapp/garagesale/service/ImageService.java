package com.garagesale.gapp.garagesale.service;

/**
 * Created by juyeol on 2017-07-05.
 */

import com.garagesale.gapp.garagesale.response.UserResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageService {
    @Multipart
    @POST("product/picture")
    Call<UserResponse> uploadImageFile(@Part MultipartBody.Part file);
}

