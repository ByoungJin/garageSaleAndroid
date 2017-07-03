package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentPlanetListBinding;
import com.garagesale.gapp.garagesale.entity.User;
import com.garagesale.gapp.garagesale.entity.listData;
import com.garagesale.gapp.garagesale.response.UserListResponse;
import com.garagesale.gapp.garagesale.service.LoginService;
import com.garagesale.gapp.garagesale.util.GPSInfo;
import com.garagesale.gapp.garagesale.util.mListAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlanetListFragment extends BaseFragment {

    private RecyclerView.Adapter adapter;
    private ArrayList<listData> itemDataset;
    private FragmentPlanetListBinding binding;

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static PlanetListFragment mInstance;
    public static PlanetListFragment getInstance(){
        if(mInstance == null) mInstance = new PlanetListFragment();
        return mInstance;
    }

    @Inject
    public Retrofit retrofit;  // retrofit

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_planet_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getNetworkComponent().inject(this);
        binding = FragmentPlanetListBinding.bind(getView());
        LoginService loginService = retrofit.create(LoginService.class);

        Location gLocation = GPSInfo.getmInstance(getContext()).getGPSLocation();  //GPS정보 객체

        Call<UserListResponse> repos = loginService.getUserList(String.valueOf(gLocation.getLongitude()), String.valueOf(gLocation.getLatitude()));

        repos.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                UserListResponse userListResponse = response.body();
                setTestItemData(userListResponse);
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public String getTitle() {
        return "Planet List";
    }

    public void setTestItemData(UserListResponse userListResponse) {
        itemDataset = new ArrayList<>();
        adapter = new mListAdapter(itemDataset);
        binding.testList.setAdapter(adapter);

        // 테스트셋
        for (User user : userListResponse.getUsers()) {
            //if(user.getPlanet().getName() == null ) continue;
            itemDataset.add(new listData(user.getEmail(), user.getPlanet().getName(),
                    user.getPlanet().getDescription(),R.mipmap.ic_launcher
            ));
        }

    }

}
