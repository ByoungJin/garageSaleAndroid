package com.garagesale.gapp.garagesale.fragment;

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
import com.garagesale.gapp.garagesale.databinding.FragmentLoginBinding;
import com.garagesale.gapp.garagesale.entity.Account;
import com.garagesale.gapp.garagesale.service.LoginService;
import com.garagesale.gapp.garagesale.util.SharedPreferenceManager;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginFragment extends BaseFragment {
    private FragmentLoginBinding binding;
    View view;
    @Inject
    public Retrofit retrofit ;  // retrofit

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_login, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getNetworkComponent().inject(this); // retrofit 객체 주입 시점

        binding = FragmentLoginBinding.bind(getView()); // Login 프레그먼트 View

        // button
        binding.loginButton.setOnClickListener(view1 -> {

            // Login (서버 주소는 build.gradle에 있음, 본인 로컬 서버 주소로 변경하여 테스트하세요)
            LoginService loginService = retrofit.create(LoginService.class);

            // userId, Password를 넣고 Login Reqest 요청
            Call<Account> repos = loginService.loginPost(
                    binding.userIdText.getText().toString(),
                    binding.passwordText.getText().toString()
            );

            // Login Response 콜백
            repos.enqueue(new Callback<Account>() {
                @Override
                public void onResponse(Call<Account> call, Response<Account> response) {
                    if(response.isSuccessful()) {
                        try {
                            // 토큰을 로컬에 저장
                            Account account = response.body();
                            SharedPreferenceManager preferenceManager = SharedPreferenceManager.getInstance(getActivity());
                            preferenceManager.putStringValue(BuildConfig.KEYTOKEN,account.getToken());

                            Log.v("getToken : ", account.getToken());
                            Log.v("getEmail : ", account.getUser().getEmail());
                            Log.v("getName : ", account.getUser().getName());

                            Toast.makeText(getActivity(), "로그인 성공, Name : " + account.getUser().getName(),Toast.LENGTH_SHORT).show();

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }else {
                        int statusCode  = response.code();
                        // handle request errors depending on status code
                        Toast.makeText(getActivity(), response.message(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Account> call, Throwable t) {
                    // 아무 응답도 못받았을 때
                }
            });


        });

    }

}
