package com.garagesale.gapp.garagesale.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Administrator on 2017-07-13.
 */

public class GoogleLogin  implements  GoogleApiClient.OnConnectionFailedListener{

    private Context context;
    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    private  BaseFragment fragment;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseUser user;

    public GoogleLogin(Context c, BaseFragment fragment){
        context= c;
        this.fragment = fragment;
        mAuth = FirebaseAuth.getInstance();
        GoogleSignIn();
        setClient();
    }

    public String getToken(){
        return user.getUid();
    }

    public String getUserEmail(){
        return user.getEmail();
    }

    public String getUserName(){
        return user.getDisplayName();
    }


    private void GoogleSignIn() {
        gso = new GoogleSignInOptions.Builder
                (GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    public void setClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(fragment.getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void requestLogin(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        fragment.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(context, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

}
