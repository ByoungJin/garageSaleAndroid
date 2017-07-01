package com.garagesale.gapp.garagesale.response;

import com.garagesale.gapp.garagesale.entity.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by gimbyeongjin on 2017. 6. 17..
 * User 정보, Login Response 받는 객체
 */

public class UserResponse {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user")
    @Expose
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
