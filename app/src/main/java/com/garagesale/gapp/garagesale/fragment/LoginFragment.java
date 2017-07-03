package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.BuildConfig;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentLoginBinding;
import com.garagesale.gapp.garagesale.entity.User;
import com.garagesale.gapp.garagesale.response.UserResponse;
import com.garagesale.gapp.garagesale.service.LoginService;
import com.garagesale.gapp.garagesale.util.DataContainer;
import com.garagesale.gapp.garagesale.util.SharedPreferenceManager;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginFragment extends BaseFragment {

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static LoginFragment mInstance;
    public static LoginFragment getInstance(){
        if(mInstance == null) mInstance = new LoginFragment();
        return mInstance;
    }

    private FragmentLoginBinding binding;
    SharedPreferenceManager preferenceManager;
    @Inject
    public Retrofit retrofit;  // retrofit

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentLoginBinding.bind(getView()); // Login 프레그먼트 View

        // Login (서버 주소는 build.gradle에 있음, 본인 로컬 서버 주소로 변경하여 테스트하세요)
        getNetworkComponent().inject(this); // retrofit 객체 주입 시점
        LoginService loginService = retrofit.create(LoginService.class);    // 로그인 서비스 객체 생성
        preferenceManager = SharedPreferenceManager.getInstance(getActivity());

        if(preferenceManager.getStringValue(BuildConfig.KEYTOKEN) != ""){
            // 토큰 로그인
            Call<UserResponse> repos = loginService.tokenLoginPost();
            repos.enqueue(getCallback());
            return;
        }

        // set Listener
        binding.loginButton.setOnClickListener(view1 -> {

            // 로그인
            // userId, Password를 넣고 Login Reqest 요청
            Call<UserResponse> repos = loginService.loginPost(
                    binding.userIdText.getText().toString(),
                    binding.passwordText.getText().toString()
            );

            repos.enqueue(getCallback());
        });

    }

    @NonNull
    public Callback<UserResponse> getCallback() {
        return new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()) {
                    try {

                        UserResponse userResponse = response.body();
                        preferenceManager.putStringValue(BuildConfig.KEYTOKEN, userResponse.getToken()); // 토큰을 로컬에 저장
                        DataContainer.getInstance().setmUser(userResponse.getUser()); // User DataContainer에 저장

                        Log.v("getToken : ", userResponse.getToken());
                        Log.v("getEmail : ", userResponse.getUser().getEmail());
                        Log.v("getName : ", userResponse.getUser().getName());

                        //Toast.makeText(getActivity(), "로그인 성공, Name : " + userResponse.getUser().getName(), Toast.LENGTH_SHORT).show();

                        Toast.makeText(getActivity(), "로그인 성공, 토큰 : " + preferenceManager.getStringValue(BuildConfig.KEYTOKEN), Toast.LENGTH_SHORT).show();



                        // 화면 전환
                        getMainActivity().changeFragment(MainFragment.getInstance());
                        mInstance = null;   // 재사용 불필요 시 프레그먼트 객체 제거

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    int statusCode  = response.code();
                    // handle request errors depending on status code
                    Toast.makeText(getActivity(), response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // 아무 응답도 못받았을 때
            }
        };
    }

    @Override
    public String getTitle() {
        return "Login";
    }

}
