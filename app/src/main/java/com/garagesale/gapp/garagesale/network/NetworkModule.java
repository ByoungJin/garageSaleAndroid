package com.garagesale.gapp.garagesale.network;

import android.content.Context;
import android.text.TextUtils;

import com.garagesale.gapp.garagesale.BuildConfig;
import com.garagesale.gapp.garagesale.util.SharedPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gimbyeongjin on 2017. 6. 21
 * 네트워크 담당하는 공통 모듈
 * 보유 토큰이 있으면 토큰을 담아보낸다
 */

@Module
public class NetworkModule {

    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 15;
    private static final int READ_TIMEOUT = 15;
    private static final String baseUrl = BuildConfig.BASEURL; // your base url;

    private Context context;

    public NetworkModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache() {
        final int cacheSize = 10 * 1024 * 1024; // 10MB
        return new Cache(context.getCacheDir(), cacheSize);
    }


    @Provides
    @Singleton
    SharedPreferenceManager provideSharedPreferenceManager(){
        return SharedPreferenceManager.getInstance(context);
    }

    @Provides
    @Singleton
    Interceptor provideInterceptor(SharedPreferenceManager prefs) {
        return chain -> {
            if (!TextUtils.isEmpty(prefs.getStringValue(BuildConfig.KEYTOKEN))) {
                final String bearer = BuildConfig.KEYTOKEN+" " + prefs.getStringValue(BuildConfig.KEYTOKEN);
                final Request.Builder builder = chain.request().newBuilder()
                        .header("Authorization", bearer)
                        .header("Accept", "application/json");

                return chain.proceed(builder.build());
            } else {
                return chain.proceed(chain.request());
            }
        };
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, Interceptor interceptor) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    @Singleton
    Gson provideGson() {

        return new GsonBuilder()
                //.registerTypeAdapterFactory(AutoValueGsonFactory.create())
                .create();

    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

}