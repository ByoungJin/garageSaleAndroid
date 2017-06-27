package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.util.GPSInfo;
import com.garagesale.gapp.garagesale.util.addrConvertor;
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
    private View pView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_googlemap, container, false);
        pView = getParentFragment().getView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mGPSInfo = GPSInfo.getmInstance(getContext());  //GPS정보 객체
        supportMapFragment = (SupportMapFragment) this. // 지도 Fragment
                getChildFragmentManager().
                findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        // ParentFragment().getView() 로 Binding하면 런타임 에러.. 당연한건가.. 그럼이건왜 되지..?
        // ParaentFragment 와 ChildFragment의 통신수단을 못찾아서 임시.. 괴랄;
        pView.findViewById(R.id.GPSrenewal).setOnClickListener(view1 -> {
           gLocation = mGPSInfo.getGPSLocation();
           mLatLng = new LatLng(gLocation.getLatitude(),gLocation.getLongitude());
           createGoogleMap(mGoogleMap,mLatLng);
            EditText ed = pView.findViewById(R.id.editText13);
            ed.setText(String.valueOf(mLatLng.latitude)+"\n/"+String.valueOf(mLatLng.longitude));
            ed = pView.findViewById(R.id.editText14);
            ed.setText(addrConvertor.getAddress(getContext(),mLatLng));
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
        String address = addrConvertor.getAddress(getContext(),mLatLng);

        mGoogleMap.clear();
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng,zlevel));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(mLatLng)
                .title(address)
        ).showInfoWindow();
    }
}
