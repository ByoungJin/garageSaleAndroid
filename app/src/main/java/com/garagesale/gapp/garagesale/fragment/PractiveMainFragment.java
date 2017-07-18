package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
        renderImages();

        binding.ufoHome.setOnClickListener(view -> {
            renderImages();
            getMainActivity().getSoundPool().load(getMainActivity(), R.raw.fa, 1); // sound play
        });

        // Center 행성 회전 애니메이션
        Animation animation = loadAnimation(getContext(), R.anim.rotate); // 애니메이션xml 파일을 로드
        binding.layoutRotate.startAnimation(animation); // 애니메이션을 시작
        binding.layoutRotate.invalidate(); // 화면을 갱신

    }

    public void renderImages() {

        // 랜덤 위치,크기 점점 커지는 애니메이션, 클릭 이벤트 적용
        ConstraintSet set = new ConstraintSet();
        Random random = new Random();   // 난수
        set.clone(binding.planetsContainer);
        renderView(set, binding.imageView2h, random); // 2시
        renderView(set, binding.imageView5h, random); // 5시
        renderView(set, binding.imageView7h, random); // 7시
        renderView(set, binding.imageView10h, random); // 10시
        renderView(set, binding.imageView12h, random); // 12시
        set.applyTo(binding.planetsContainer);

    }

    private void moveViewAni(View targetView, View intoView) {
        TranslateAnimation anim = new TranslateAnimation(
                0.0f,
                (intoView.getLeft() - targetView.getX()) + (intoView.getWidth() - targetView.getWidth())/2,
                0.0f,
                (intoView.getTop() - targetView.getY()) + (intoView.getHeight() - targetView.getHeight())/2
        );
        anim.setFillAfter(true);
        anim.setDuration(2000); // 이동 시간 x/1000 초
        targetView.startAnimation(anim);
    }

    private void renderView(ConstraintSet set, ImageView view, Random random) {
        // setOnClickEvent : moveViewAni
        if(!view.hasOnClickListeners()) view.setOnClickListener(view1 -> {
            //** 복합적 애니메이션이 한꺼번에 발생해야함
            // TODO: 2017. 7. 19. 센터 작아져야함
            moveViewAni(view, binding.imageViewCenter); // 중심으로 이동
            // TODO: 2017. 7. 19. 센터 크기만큼 커지는 애니메이션
            // TODO: 2017. 7. 19. 타겟 구역에 새로운 View 랜덤 위치, 랜덤크기 가지고 점점 커져야함
        });

        // 랜덤위치 범위 : 0.0 ~ 1.0
        set.setHorizontalBias(view.getId(), getBiasFull(random));
        set.setVerticalBias(view.getId(), getBiasFull(random));

        // 랜덤크기 가져오기, 범위 : 35 ~ 85
        // returns the number of pixels for 123.4dip
        int imageWH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) random.nextInt(50 + 1) + 35, getResources().getDisplayMetrics());

        set.constrainWidth(view.getId(), imageWH);
        set.constrainHeight(view.getId(), imageWH);

        // Scale 애니메이션
        Animation animation = loadAnimation(getContext(), R.anim.scale); // 애니메이션xml 파일을 로드
        view.startAnimation(animation); // 애니메이션을 시작
        view.invalidate(); // 화면을 갱신
    }

    public float getBiasFull(Random random) {
        return ((float) random.nextInt(100 + 1)) / (float) 100;
    }

    @Override
    public String getTitle() {
        return "Main";
    }

}
