package com.garagesale.gapp.garagesale.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;

public class SettingFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_setting, container, false);

    }
}
