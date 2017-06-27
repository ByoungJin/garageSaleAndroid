package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentStoreBinding;

import com.garagesale.gapp.garagesale.util.setPermission;

import javax.inject.Inject;
import retrofit2.Retrofit;


public class StoreFragment extends BaseFragment {

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static StoreFragment mInstance;

    public static StoreFragment getInstance() {
        if (mInstance == null) mInstance = new StoreFragment();
        return mInstance;
    }

    private FragmentStoreBinding binding;
    View view;
    @Inject
    public Retrofit retrofit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_store, container, false);
        setPermission.getmInstance(getContext());       // 권한 요청
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Fragment childFragment = new GoogleMapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map, childFragment).commit();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getNetworkComponent().inject(this); // retrofit 객체 주입
        binding = FragmentStoreBinding.bind(getView()); // Store 프레그먼트 View

        // GoogleMapFragment와 scrollview 간의 간섭 컨트롤
        binding.transparentImage.setOnTouchListener((view12, motionEvent) -> {
            int action = motionEvent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    // Disallow ScrollView to intercept touch events.
                    binding.scrollView.requestDisallowInterceptTouchEvent(true);
                    // Disable touch on transparent view
                    return false;

                case MotionEvent.ACTION_UP:
                    // Allow ScrollView to intercept touch events.
                    binding.scrollView.requestDisallowInterceptTouchEvent(false);
                    return true;

                case MotionEvent.ACTION_MOVE:
                    binding.scrollView.requestDisallowInterceptTouchEvent(true);
                    return false;

                default:
                    return true;
            }
        });

    }


    @Override
    public String getTitle() {
        return "Store";
    }

}
