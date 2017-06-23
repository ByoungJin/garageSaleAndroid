package com.garagesale.gapp.garagesale;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.garagesale.gapp.garagesale.network.NetworkComponent;

/**
 * Created by gimbyeongjin on 2017. 6. 22..
 * 모든 프레그먼트를 상속하는 공통 프레그먼트
 * 모든 화면이 공통적으로 해야할 것들을 구현할 때 사용
 */

public class BaseFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    // Get NetworkComponent
    protected NetworkComponent getNetworkComponent(){
        return ((MainActivity)getActivity()).getNetworkComponent();
    }

    // Get MainActivity Context
    protected MainActivity getMainActivity(){
        return (MainActivity)getActivity();
    }
}
