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
 * 사용법 setPermssion.getInstance(Context) / 1싱글톤
 * 추가 권한은 TedPermssion.setPermssions(permesion,permesion,...);
 */

public class setPermission {


    @SuppressLint("StaticFieldLeak")
    private static setPermission mInstance;

    public static setPermission getmInstance(Context c){
        if (mInstance == null) mInstance = new setPermission(c);
        return mInstance;
    }

    Context mContext;

    public setPermission(Context context) {
        mContext = context;
        new TedPermission(mContext)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("권한을 거부하시면 사용자기반\n위치서비스를 사용수없습니다" +
                        ".\n\n                                     설정방법 [설정] > [권한]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(mContext, "권한획득", Toast.LENGTH_SHORT).show();
            //TODO: 권한 획득후
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(mContext, "권한거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            //TODO: 권한 획득 거부시
        }
    };
}
