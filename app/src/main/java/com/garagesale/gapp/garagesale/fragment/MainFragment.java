package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentMainBinding;
import com.garagesale.gapp.garagesale.response.UserListResponse;
import com.garagesale.gapp.garagesale.service.LoginService;

import java.util.Random;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainFragment extends BaseFragment {
    private FragmentMainBinding binding;

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static MainFragment mInstance;

    public static MainFragment getInstance() {
        if (mInstance == null) mInstance = new MainFragment();
        return mInstance;
    }

    @Inject
    public Retrofit retrofit;  // retrofit

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getNetworkComponent().inject(this);
        binding = FragmentMainBinding.bind(getView());
        LoginService loginService = retrofit.create(LoginService.class);

        Call<UserListResponse> repos = loginService.getUserList("12", "12");
        repos.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                // Error Handle

                if (!response.isSuccessful()) {
                    if(getActivity() != null) Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                UserListResponse userListResponse = response.body();

                if (userListResponse == null) {
                    if(getActivity() != null) Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(getActivity() != null) Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {

            }

        });

    }

    public float getBiasFull(Random random, int full) {
        return ((float) random.nextInt(full + 1)) / (float) full;
    }

    public float getBiasToMax(Random random, int max, float full) {
        return ((float) random.nextInt(max)) / full;
    }

    public float getBiasFromMin(Random random, int max, int min, float full) {
        return ((float) random.nextInt(max) + min) / full;
    }

    @Override
    public String getTitle() {
        return "";
    }

}
