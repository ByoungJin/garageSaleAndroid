package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;

public class ProfileFragment extends BaseFragment {

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static ProfileFragment mInstance;
    public static ProfileFragment getInstance(){
        if(mInstance == null) mInstance = new ProfileFragment();
        return mInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public String getTitle() {
        return "Profile";
    }

}
