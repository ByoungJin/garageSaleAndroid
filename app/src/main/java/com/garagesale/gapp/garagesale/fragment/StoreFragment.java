package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentStoreBinding;

import com.garagesale.gapp.garagesale.util.addrConvertor;
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
    // GoogleMap
    GoogleMap mGoogleMap;
    LatLng mLatLng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_store, container, false);
        // 구글맵 Fragment로 가져오기
        SupportMapFragment supportMapFragment;
        supportMapFragment = (SupportMapFragment) this.
                getChildFragmentManager().
                findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
//        mGoogleMap.setOnMapClickListener(this);         // 구글 클릭 리스너 등록

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getNetworkComponent().inject(this); // retrofit 객체 주입
        binding = FragmentStoreBinding.bind(getView()); // Store 프레그먼트 View
    }

    /**
     *
     * 초기 맵생성기 실행되는 구현메소드
     * @param map
     */
    @Override
    public void onMapReady(final GoogleMap map) {
        mGoogleMap = map;
        mLatLng = new LatLng(37.56, 126.97);
        mGoogleMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title(addrConvertor.getAddress(getMainActivity(),mLatLng))
        ).showInfoWindow();
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        mGoogleMap.setOnMapClickListener(this);
    }

    /**
     * 맵 클릭시 실행되는 클릭리스너
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        // 좌표로 주소변환
        mLatLng = latLng;
        String address =addrConvertor.getAddress(getMainActivity(),mLatLng);

        //화면 주소 -> 구글 지도 경도위도 변환
        Point screenPt = mGoogleMap.getProjection().toScreenLocation(latLng);
        latLng = mGoogleMap.getProjection().fromScreenLocation(screenPt);

        // 마커 옵션 설정 (추후 이미지 설정등 가능)
        // 맵 좌표 clear 후, 해당 위치로 Forcusing
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title(addrConvertor.getAddress(getMainActivity(),mLatLng))
        ).showInfoWindow();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
        binding.editText13.setText("위도 : "+ String.valueOf(latLng.latitude+"\n경도 : "+String.valueOf(latLng.longitude)));
        binding.editText14.setText(address);
    }


    @Override
    public String getTitle() {
        return "Store";
    }

}
