package com.garagesale.gapp.garagesale.util;

import com.garagesale.gapp.garagesale.entity.User;

import java.util.List;

public class DataContainer {

    // 싱클톤 패턴
    private static DataContainer mInstance;
    public static DataContainer getInstance(){
        if(mInstance == null){
            mInstance = new DataContainer();
        }
        return mInstance;
    }

    User mUser;

    List<User> userList;

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
