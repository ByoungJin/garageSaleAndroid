package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.BuildConfig;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentJoinBinding;
import com.garagesale.gapp.garagesale.response.UserResponse;
import com.garagesale.gapp.garagesale.service.JoinService;
import com.garagesale.gapp.garagesale.util.SharedPreferenceManager;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JoinFragment extends BaseFragment {

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static JoinFragment mInstance;
    public static JoinFragment getInstance(){
        if(mInstance == null) mInstance = new JoinFragment();
        return mInstance;
    }

    private FragmentJoinBinding binding;
    View view;
    @Inject
    public Retrofit retrofit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_join, container, false);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getNetworkComponent().inject(this);
        JoinService joinService = retrofit.create(JoinService.class);

        binding = FragmentJoinBinding.bind(getView());
        binding.joinButton.setOnClickListener(view1 -> {

            // 패스워드가 일치하는지 확인
            if(!binding.passwordEditText.getText().toString().equals(binding.confirmEditText.getText().toString())){
                Toast.makeText(getActivity(),"패스워드가 일치하지 않습니다." , Toast.LENGTH_SHORT).show();
                return;
            }

            // HTTP Post
            Call<UserResponse> repos = joinService.signUp(
                    binding.nameEditText.getText().toString(),
                    binding.emailEditText.getText().toString(),
                    binding.passwordEditText.getText().toString()
            );

            repos.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    // 토큰을 로컬에 저장
                    UserResponse userResponse = response.body();
                    SharedPreferenceManager preferenceManager = SharedPreferenceManager.getInstance(getActivity());
                    preferenceManager.putStringValue(BuildConfig.KEYTOKEN, userResponse.getToken());

                    Log.v("getToken : ", userResponse.getToken());
                    Log.v("getEmail : ", userResponse.getUser().getEmail());
                    Log.v("getName : ", userResponse.getUser().getName());

                    //Toast.makeText(getActivity(), "로그인 성공, Name : " + userResponse.getUser().getName(), Toast.LENGTH_SHORT).show();

                    Toast.makeText(getActivity(), "Join 성공, 토큰 : " + preferenceManager.getStringValue(BuildConfig.KEYTOKEN), Toast.LENGTH_SHORT).show();


                    // 화면 전환
                    getMainActivity().changeFragment(MainFragment.getInstance());
                    mInstance = null;   // 재사용 불필요 시 프레그먼트 객체 제거

                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {

                }
            });
        });


    }

    @Override
    public String getTitle() {
        return "Join";
    }
}
