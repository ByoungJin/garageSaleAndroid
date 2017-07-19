package com.garagesale.gapp.garagesale.fragment.StoreTabsFragment;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentStoreGooglemapTabBinding;
import com.garagesale.gapp.garagesale.util.GPSInfo;
import com.garagesale.gapp.garagesale.util.addrConvertor;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by juyeol on 2017-06-28.
 * GoogleMap 단독으로 분리된 Fragment
 * GoogleMap 이벤트, ParentFragment View로 리스너 컨트롤
 */

public class GoogleMapTabFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener,PlaceSelectionListener{

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static GoogleMapTabFragment mInstance;

    public static GoogleMapTabFragment getInstance() {
        if (mInstance == null) mInstance = new GoogleMapTabFragment();
        return mInstance;
    }

    private GoogleMap mGoogleMap;
    private GPSInfo mGPSInfo;
    private Location gLocation;
    private LatLng mLatLng;
    private SupportMapFragment supportMapFragment;
    private FragmentStoreGooglemapTabBinding binding;
    private FragmentInteractionListener mParentListener;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private AutocompleteFilter typeFilter;

    /**
     * 부모 Fragment와 통신하기위한 리스너
     */
    public interface FragmentInteractionListener {
        void sendMessageToParent(LatLng latLng,String address);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // check if parent Fragment implements listener
        if (getParentFragment() instanceof FragmentInteractionListener) {
            mParentListener = (FragmentInteractionListener) getParentFragment();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChildFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_store_googlemap_tab, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mGPSInfo = GPSInfo.getmInstance(getContext());  //GPS정보 객체
        // 지도 Fragment
        supportMapFragment = (SupportMapFragment) this.
                getChildFragmentManager().
                findFragmentById(R.id.googleMap);
        supportMapFragment.getMapAsync(this);
        binding = FragmentStoreGooglemapTabBinding.bind(getView());

        createAutoComplete();

        binding.MyLocation.setOnClickListener(view1 -> {
            gLocation = mGPSInfo.getGPSLocation();
            mLatLng = new LatLng(gLocation.getLatitude(), gLocation.getLongitude());
            createGoogleMap(mGoogleMap, mLatLng);
            mParentListener.sendMessageToParent(mLatLng, addrConvertor.getAddress(getContext(), mLatLng));
        });
    }
    public void createAutoComplete(){
        // 자동완성
        autocompleteFragment = (SupportPlaceAutocompleteFragment) this.
                getChildFragmentManager().
                findFragmentById(R.id.autoComplete);
        autocompleteFragment.setOnPlaceSelectedListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        autocompleteFragment.onActivityResult(requestCode,resultCode,data);
    }

    /**
     * 초기 맵생성기 실행되는 구현메소드
     *
     * @param map 구글맵
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mGoogleMap = map;
        mLatLng = mGPSInfo.getLatLng();
        createGoogleMap(map, mLatLng);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 16));
        mParentListener.sendMessageToParent(mLatLng, addrConvertor.getAddress(getContext(), mLatLng));
        mGoogleMap.setOnMapClickListener(this);
    }

    /**
     * 맵 클릭시 실행되는 클릭리스너
     *
     * @param latLng 좌표값
     */
    @Override
    public void onMapClick(LatLng latLng) {
        createGoogleMap(mGoogleMap, latLng);
        mParentListener.sendMessageToParent(mLatLng, addrConvertor.getAddress(getContext(), mLatLng));
    }

    /**
     * GoogleMap 생성 메소드
     *
     * @param gMap   구글맵
     * @param latLng 리턴 위치값
     */
    public void createGoogleMap(GoogleMap gMap, LatLng latLng) {
        mGoogleMap = gMap;

        this.mLatLng = latLng;
        float zlevel = mGoogleMap.getCameraPosition().zoom;
        String address = addrConvertor.getAddress(getContext(), mLatLng);

        mGoogleMap.clear();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, zlevel));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title(address)
        ).showInfoWindow();
    }

    //자동완성 리스너
    @Override
    public void onPlaceSelected(Place place) {
        mLatLng = place.getLatLng();
        createGoogleMap(mGoogleMap, mLatLng);
        mParentListener.sendMessageToParent(mLatLng, addrConvertor.getAddress(getContext(), mLatLng));
    }
    //자동완성 리스너
    @Override
    public void onError(Status status) {
        Toast.makeText(getContext(),"다시 검색해주세요",Toast.LENGTH_SHORT);
    }

    /*  터치간섭 삭제

        @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                binding.scrollView.requestDisallowInterceptTouchEvent(true);
                return false;

            case MotionEvent.ACTION_UP:
                binding.scrollView.requestDisallowInterceptTouchEvent(false);
                return true;

            case MotionEvent.ACTION_MOVE:
                binding.scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            default:
                return true;
        }
    }
     */
}
