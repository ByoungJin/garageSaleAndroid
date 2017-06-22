package com.garagesale.gapp.garagesale.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

/**
 * Created by gimbyeongjin on 2017. 6. 21..
 * 토큰을 로컬에 저장하고 가져오는 클래스
 */

public class SharedPreferenceManager {

    // 싱클톤 패턴
    private static SharedPreferenceManager mInstance;
    public static SharedPreferenceManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new SharedPreferenceManager(context);
        }
        return mInstance;
    }

    private Context context;
    private SharedPreferences sharedPreferences;

    private SharedPreferenceManager(Context context){
        this.context = context;
        final String PREF_NAME = this.context.getPackageName();

        sharedPreferences = this.context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
    }

    public void putHashSet(String key, HashSet<String> set){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, set);
        editor.apply();
    }

    public HashSet<String> getHashSet(String key, HashSet<String> dftValue){
        try {
            return (HashSet<String>)sharedPreferences.getStringSet(key, dftValue);
        } catch (Exception e) {
            e.printStackTrace();
            return dftValue;
        }
    }

    public void putStringValue(String key, String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringValue(String key){
        try {
            return sharedPreferences.getString(key, "");
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

}
