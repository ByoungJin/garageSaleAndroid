package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentStoreBinding;

import com.garagesale.gapp.garagesale.util.GPSInfo;
import com.garagesale.gapp.garagesale.util.addrConvertor;
import com.garagesale.gapp.garagesale.util.setPermission;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;
import retrofit2.Retrofit;


public class StoreFragment extends BaseFragment
        implements OnMapReadyCallback, OnMapClickListener {

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

    private GoogleMap mGoogleMap;
    private GPSInfo mGPSInfo;
    private Location gLocation;
    private LatLng mLatLng;
    private SupportMapFragment supportMapFragment;
    ViewPager mViewParger;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_store, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getNetworkComponent().inject(this); // retrofit 객체 주입
        binding = FragmentStoreBinding.bind(getView()); // Store 프레그먼트 View

        setPermission.getmInstance(getContext());       // 권한 요청
        mGPSInfo = GPSInfo.getmInstance(getContext());  //GPS정보 객체
        supportMapFragment = (SupportMapFragment) this. // 지도 Fragment
                getChildFragmentManager().
                findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        // 현재 내위치로
        binding.GPSrenewal.setOnClickListener(view1 -> {
            gLocation = mGPSInfo.getGPSLocation();
            mLatLng = new LatLng(gLocation.getLatitude(),gLocation.getLongitude());
            createGoogleMap(mGoogleMap,mLatLng);
        });

        // fragment와 scrollview 간의 간섭 컨트롤
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

    /**
     * 초기 맵생성기 실행되는 구현메소드
     * @param map
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        mLatLng = mGPSInfo.getLatLng();
        createGoogleMap(map,mLatLng);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,16));
        mGoogleMap.setOnMapClickListener(this);
    }

    /**
     * 맵 클릭시 실행되는 클릭리스너
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        createGoogleMap(mGoogleMap,latLng);
        //화면 주소 -> 구글 지도 경도위도 변환
    }

    /**
     * GoogleMap 생성 메소드
     * @param gMap
     * @param latLng
     */
    public void createGoogleMap(GoogleMap gMap,LatLng latLng){
        mGoogleMap = gMap;

        this.mLatLng = latLng;
        float zlevel = mGoogleMap.getCameraPosition().zoom;
        String address =addrConvertor.getAddress(getContext(),mLatLng);

        mGoogleMap.clear();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,zlevel));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title(address)
        ).showInfoWindow();

        binding.editText13.setText("위도 : "+ String.valueOf(latLng.latitude+"\n경도 : "+String.valueOf(latLng.longitude)));
        binding.editText14.setText(address);
    }

    @Override
    public String getTitle() {
        return "Store";
    }

}
