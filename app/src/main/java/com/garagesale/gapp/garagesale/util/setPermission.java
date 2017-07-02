package com.garagesale.gapp.garagesale.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.garagesale.gapp.garagesale.fragment.StoreFragment;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

/**
 * Created by juyeol on 2017. 6. 27
 * TedPermmsion을 이용한 권한 획득
 * 사용법 setPermission.getmInstance(context, 권한String , 권한 후 리스너(PermissionListener));
 * ex) setPermission.getmInstance(getContext(),Manifest.permission.ACCESS_FINE_LOCATION ,GoogleMapPermission);
 * 리스너는 각자 구현해줘야함.
 */

public class setPermission {


    @SuppressLint("StaticFieldLeak")
    private static setPermission mInstance;

    // PermissionListener 를 넘길수 있는 getmInstance 추가
    public static setPermission getmInstance(Context c,String s, PermissionListener permissionListener){
        if (mInstance == null) mInstance = new setPermission(c,s, permissionListener);
        return mInstance;
    }

    Context mContext;

    public setPermission(Context context,String permission, PermissionListener permissionListener){
        mContext = context;
        String message;
        // 퍼미션에 따른 에러메세지 설정
        switch (permission){
            case Manifest.permission.ACCESS_FINE_LOCATION:
                message ="위치서비스권한을 거부하시면 사용자기반 위치서비스를 사용수없습니다\n\n설정방법 [설정] > [권한]";
                break;
            default:
                message = "권한을 거부하시면 특정 서비스를 사용수없습니다\n\n설정방법 [설정] > [권한]";
                break;
        }


        new TedPermission(mContext)
                .setPermissionListener(permissionListener)
                .setDeniedMessage(message)
                .setPermissions(permission)
                .check();
    }
}
