package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintSet;
import android.util.Log;
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
        getMainActivity().pushOnBackKeyPressedListener(this);
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
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                UserListResponse userListResponse = response.body();

                if (userListResponse == null) {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {

            }

        });

        // 이미지 랜덤 배치
        randomArrangeImages();

        binding.imageViewCenter.setOnClickListener(view -> {
            randomArrangeImages();
        });

    }

    public void randomArrangeImages() {
        ConstraintSet set = new ConstraintSet();
        Random random = new Random();
        set.clone(binding.planetsContainer);

        int max = 68, min = 33, full = 100;

        // 2시
        
        set.setHorizontalBias(binding.imageView2h.getId(), getBiasFromMin(random, max, min, full)); // 33 ~ 100

        set.setVerticalBias(binding.imageView2h.getId(), getBiasToMax(random, max, full));   // 0 ~ 67

        // 5시
        set.setHorizontalBias(binding.imageView5h.getId(), getBiasFromMin(random, max, min, full));
        set.setVerticalBias(binding.imageView5h.getId(), getBiasFromMin(random, max, min, full));

        // 7시
        set.setHorizontalBias(binding.imageView7h.getId(), getBiasToMax(random, max, full));
        set.setVerticalBias(binding.imageView7h.getId(), getBiasFromMin(random, max, min, full));

        // 10시
        set.setHorizontalBias(binding.imageView10h.getId(), getBiasToMax(random, max, full));
        set.setVerticalBias(binding.imageView10h.getId(), getBiasToMax(random, max, full));


        // 3시
        set.setHorizontalBias(binding.imageView3h.getId(), getBiasFull(random, full));
        set.setVerticalBias(binding.imageView3h.getId(), (float)0.50);

        // 6시
        set.setHorizontalBias(binding.imageView6h.getId(), (float)0.50);
        set.setVerticalBias(binding.imageView6h.getId(), getBiasFull(random, full));

        // 9시
        set.setHorizontalBias(binding.imageView9h.getId(), getBiasFull(random, full));
        set.setVerticalBias(binding.imageView9h.getId(), (float)0.50);

        // 12시
        set.setHorizontalBias(binding.imageView12h.getId(), (float)0.50);
        set.setVerticalBias(binding.imageView12h.getId(), getBiasFull(random, full));

        set.applyTo(binding.planetsContainer);
    }

    public float getBiasFull(Random random, int full) {
        return ((float)random.nextInt(full+1)) / (float)full;
    }

    public float getBiasToMax(Random random, int max, float full) {
        return ((float)random.nextInt(max)) / full;
    }

    public float getBiasFromMin(Random random, int max, int min, float full) {
        return ((float)random.nextInt(max) + min) / full;
    }

    @Override
    public String getTitle() {
        return "Main";
    }

}
