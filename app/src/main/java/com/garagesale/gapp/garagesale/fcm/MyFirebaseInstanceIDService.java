package com.garagesale.gapp.garagesale.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import android.content.Context;
/**
 * Created by juyeol on 2017-06-19.
 * Firebase 토큰 최초생성, 재생성
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    // 최초 토큰이 생성,재생성될시에 동작
    @Override
    public void onTokenRefresh() {
        Context context = getApplicationContext();
        Log.d("MyFCM", "FCM token: " + FirebaseInstanceId.getInstance().getToken());
    }
}