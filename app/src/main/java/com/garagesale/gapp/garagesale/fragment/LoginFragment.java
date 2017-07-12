package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
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
import com.garagesale.gapp.garagesale.entity.User;
import com.garagesale.gapp.garagesale.response.UserResponse;
import com.garagesale.gapp.garagesale.service.LoginService;
import com.garagesale.gapp.garagesale.util.DataContainer;
import com.garagesale.gapp.garagesale.util.SharedPreferenceManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.google.android.gms.internal.zzs.TAG;


public class LoginFragment extends BaseFragment implements  GoogleApiClient.OnConnectionFailedListener{

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

    private FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

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

        GoogleSignIn();
        mAuth = FirebaseAuth.getInstance();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        binding.signInButton.setSize(SignInButton.SIZE_STANDARD);

        binding.signInButton.setOnClickListener(view -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        if(preferenceManager.getStringValue(BuildConfig.KEYTOKEN) != ""){
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

                // 로그인 성공 했을때
                GoogleSignInAccount acct = result.getSignInAccount();
                Log.d(TAG, "표시되는 전체 이름 =" + acct.getDisplayName());
                Log.d(TAG, "표시되는 이름=" + acct.getGivenName());
                Log.d(TAG, "이메일=" + acct.getEmail());
                Log.d(TAG, "표시되는 성=" + acct.getFamilyName());

                firebaseAuthWithGoogle(acct);

            } else {
                Log.d(TAG,"실패");
                // 로그인 실패 했을때
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getMainActivity(), task -> {
                    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithCredential", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                    // ...
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

                        // 로그인, 조인 버튼 없애고, 로그아웃 보임.
                        ActivityMainBinding activityMainBinding = getMainActivity().getBinding();
                        MenuLayoutBinding menuLayoutBinding = activityMainBinding.contentMain.menuLayout;
                        menuLayoutBinding.loginButton.setVisibility(Button.GONE); // login button
                        menuLayoutBinding.joinButton.setVisibility(Button.GONE); // join button
                        menuLayoutBinding.logoutButton.setVisibility(Button.VISIBLE); // logout button

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
                Log.v("t.getMessage()",t.getMessage());
            }
        };
    }

    private void GoogleSignIn() {
        gso = new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(getContext(), "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getTitle() {
        return "Login";
    }


}
