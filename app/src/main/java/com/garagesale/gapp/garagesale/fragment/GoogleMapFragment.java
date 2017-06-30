package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.Intent;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentGooglemapBinding;
import com.garagesale.gapp.garagesale.util.GPSInfo;
import com.garagesale.gapp.garagesale.util.addrConvertor;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by juyeol on 2017-06-28.
 * GoogleMap 단독으로 분리된 Fragment
 * GoogleMap 이벤트, ParentFragment View로 리스너 컨트롤
 */

public class GoogleMapFragment extends Fragment
        implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static GoogleMapFragment mInstance;

    public static GoogleMapFragment getInstance() {
        if (mInstance == null) mInstance = new GoogleMapFragment();
        return mInstance;
    }

    View view;

    private GoogleMap mGoogleMap;
    private GPSInfo mGPSInfo;
    private Location gLocation;
    private LatLng mLatLng;
    private SupportMapFragment supportMapFragment;
    private FragmentGooglemapBinding binding;
    private FragmentInteractionListener mParentListener;
    private SupportPlaceAutocompleteFragment autocompleteFragment;

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
        view = inflater.inflate(R.layout.fragment_googlemap, container, false);
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

        binding = FragmentGooglemapBinding.bind(getView()); // Store 프레그먼트 Vie

        autocompleteFragment = (SupportPlaceAutocompleteFragment) this.
                getChildFragmentManager().
                findFragmentById(R.id.autoComplete);
        if(autocompleteFragment==null)
            Log.d("부모","왜널이야");

        binding.MyLocation.setOnClickListener(view1 -> {
            gLocation = mGPSInfo.getGPSLocation();
            mLatLng = new LatLng(gLocation.getLatitude(), gLocation.getLongitude());
            createGoogleMap(mGoogleMap, mLatLng);
            mParentListener.sendMessageToParent(mLatLng,addrConvertor.getAddress(getContext(),mLatLng));
        });
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
        mParentListener.sendMessageToParent(mLatLng,addrConvertor.getAddress(getContext(),mLatLng));
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
        mParentListener.sendMessageToParent(mLatLng,addrConvertor.getAddress(getContext(),mLatLng));
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

}
