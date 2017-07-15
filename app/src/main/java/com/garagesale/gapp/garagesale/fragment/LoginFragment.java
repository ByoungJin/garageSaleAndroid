package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.BuildConfig;
import com.garagesale.gapp.garagesale.MainActivity;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.ActivityMainBinding;
import com.garagesale.gapp.garagesale.databinding.FragmentLoginBinding;
import com.garagesale.gapp.garagesale.databinding.MenuLayoutBinding;
import com.garagesale.gapp.garagesale.response.UserResponse;
import com.garagesale.gapp.garagesale.service.LoginService;
import com.garagesale.gapp.garagesale.util.DataContainer;
import com.garagesale.gapp.garagesale.util.GoogleLogin;
import com.garagesale.gapp.garagesale.util.SharedPreferenceManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginFragment extends BaseFragment implements MainActivity.OnLoginSuccessListener {

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static LoginFragment mInstance;

    public static LoginFragment getInstance() {
        if (mInstance == null) mInstance = new LoginFragment();
        return mInstance;
    }

    private FragmentLoginBinding binding;
    private LoginService loginService;
    SharedPreferenceManager preferenceManager;
    @Inject
    public Retrofit retrofit;  // retrofit
    private static final int RC_SIGN_IN = 9001;
    private GoogleLogin googleLogin;

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
        loginService = retrofit.create(LoginService.class);    // 로그인 서비스 객체 생성
        preferenceManager = SharedPreferenceManager.getInstance(getActivity());

        binding.signInButton.setSize(SignInButton.SIZE_STANDARD);

        binding.signInButton.setOnClickListener(view -> {
            googleLogin = new GoogleLogin(getContext(), this);
            googleLogin.requestLogin();
        });

        if (preferenceManager.getStringValue(BuildConfig.KEYTOKEN) != "") {
            // 토큰 로그인
            Call<UserResponse> repos = loginService.tokenLoginPost();
            repos.enqueue(getCallback());
            return;
        }

        // set Listener
        binding.loginButton.setOnClickListener(view -> {
            // 로그인
            Call<UserResponse> repos = loginService.loginPost(
                    binding.userIdText.getText().toString(),
                    binding.passwordText.getText().toString()
            ); // userId, Password를 넣고 Login Reqest 요청
            repos.enqueue(getCallback());
        });

        binding.joinButton.setOnClickListener(view -> {
            getMainActivity().changeFragment(JoinFragment.getInstance());   // Change to Join Fragment
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                Call<UserResponse> repos = loginService.GoogleLoginPost(
                        acct.getEmail(), acct.getIdToken(), acct.getDisplayName()
                );
                repos.enqueue(getCallback());
            } else {
                Toast.makeText(getContext(), "로그인 연동 실패", Toast.LENGTH_SHORT);
            }
        }
    }

    @NonNull
    public Callback<UserResponse> getCallback() {
        return new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    try {
                        if(googleLogin != null) {
                            googleLogin.getmGoogleApiClient().stopAutoManage(getActivity());
                            googleLogin.getmGoogleApiClient().disconnect();

                        }

                        UserResponse userResponse = response.body();
                        if(userResponse.getUser() == null) {
                            throw new Exception("User Empty");
                        }

                        preferenceManager.putStringValue(BuildConfig.KEYTOKEN, userResponse.getToken()); // 토큰을 로컬에 저장
                        DataContainer.getInstance().setmUser(userResponse.getUser()); // User DataContainer에 저장

                        Log.v("getToken : ", userResponse.getToken());
                        Log.v("getEmail : ", userResponse.getUser().getEmail());
                        Log.v("getName : ", userResponse.getUser().getName());

                        //Toast.makeText(getActivity(), "로그인 성공, Name : " + userResponse.getUser().getName(), Toast.LENGTH_SHORT).show();

                        Toast.makeText(getActivity(), "로그인 성공, 토큰 : " + preferenceManager.getStringValue(BuildConfig.KEYTOKEN), Toast.LENGTH_SHORT).show();

                        // 로그인, 조인 버튼 없애고, 로그아웃 보임.
                        ActivityMainBinding activityMainBinding = getMainActivity().getBinding();
                        MenuLayoutBinding menuLayoutBinding = activityMainBinding.contentMain.menuLayout;
                        menuLayoutBinding.loginButton.setVisibility(Button.GONE); // login button
                        menuLayoutBinding.joinButton.setVisibility(Button.GONE); // join button
                        menuLayoutBinding.logoutButton.setVisibility(Button.VISIBLE); // logout button
                        // 화면 전환

                        getMainActivity().changeFragment(MainFragment.getInstance());
                        getMainActivity().setOnLoginSuccessListener();
                        mInstance = null;   // 재사용 불필요 시 프레그먼트 객체 제거

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // 아무 응답도 못받았을 때
                Log.v("t.getMessage()", t.getMessage());
            }
        };
    }

    @Override
    public String getTitle() {
        return "Login";
    }

}
