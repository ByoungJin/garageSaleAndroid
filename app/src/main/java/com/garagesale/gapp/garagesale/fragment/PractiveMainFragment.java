package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.PractiveMainBinding;
import com.garagesale.gapp.garagesale.response.UserListResponse;
import com.garagesale.gapp.garagesale.service.LoginService;

import java.util.Random;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.view.animation.AnimationUtils.loadAnimation;

public class PractiveMainFragment extends BaseFragment {
    private PractiveMainBinding binding;

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static PractiveMainFragment mInstance;

    public static PractiveMainFragment getInstance() {
        if (mInstance == null) mInstance = new PractiveMainFragment();
        return mInstance;
    }

    @Inject
    public Retrofit retrofit;  // retrofit

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.practive_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getNetworkComponent().inject(this);
        binding = PractiveMainBinding.bind(getView());
        LoginService loginService = retrofit.create(LoginService.class);

        Call<UserListResponse> repos = loginService.getUserList("12", "12");
        repos.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserListResponse> call, Response<UserListResponse> response) {
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
            public void onFailure(@NonNull Call<UserListResponse> call, Throwable t) {

            }

        });

        // 이미지 랜덤 배치
        randomArrangeImages();

        binding.imageViewCenter.setOnClickListener(view -> {
            randomArrangeImages();
        });

        // 애니메이션
        // 애니메이션xml 파일을 로드
        Animation animation = loadAnimation(getContext(), R.anim.rotate);
        // 애니메이션을 시작
        binding.layoutRotate.startAnimation(animation);
        // 화면을 갱신
        binding.layoutRotate.invalidate();

    }

    public void randomArrangeImages() {
        ConstraintSet set = new ConstraintSet();
        Random random = new Random();
        set.clone(binding.planetsContainer);
        int full = 100;

        // 2시
        setNewPosition(set, binding.imageView2h, random);

        // 5시

        setNewPosition(set, binding.imageView5h, random);

        // 7시
        setNewPosition(set, binding.imageView7h, random);

        // 10시
        setNewPosition(set, binding.imageView10h, random);


        // 3시
        setNewPosition(set, binding.imageView3h, random);

        // 6시
        setNewPosition(set, binding.imageView6h, random);

        // 9시
        setNewPosition(set, binding.imageView9h, random);

        // 12시
        setNewPosition(set, binding.imageView12h, random);


        set.applyTo(binding.planetsContainer);
    }

    private void setNewPosition(ConstraintSet set, ImageView view, Random random) {
        set.setHorizontalBias(view.getId(), getBiasFull(random));
        set.setVerticalBias(view.getId(), getBiasFull(random));

        // 랜덤크기 가져오기, 35 ~ 85 : 50
        // returns the number of pixels for 123.4dip
        int imageWH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) random.nextInt(50 + 1) + 35, getResources().getDisplayMetrics());

        set.constrainWidth(view.getId(), imageWH);
        set.constrainHeight(view.getId(), imageWH);
    }

    public float getBiasFull(Random random) {
        return ((float) random.nextInt(100 + 1)) / (float) 100;
    }

    @Override
    public String getTitle() {
        return "Main";
    }

}
