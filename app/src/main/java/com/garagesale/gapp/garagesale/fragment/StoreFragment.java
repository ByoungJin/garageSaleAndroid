package com.garagesale.gapp.garagesale.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentStoreBinding;
import com.garagesale.gapp.garagesale.entity.User;
import com.garagesale.gapp.garagesale.entity.listData;
import com.garagesale.gapp.garagesale.util.DataContainer;
import com.garagesale.gapp.garagesale.util.mListAdapter;
import com.garagesale.gapp.garagesale.util.setPermission;
import com.google.android.gms.maps.model.LatLng;
import com.gun0912.tedpermission.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * Created by juyeol on 2017-06-28.
 * 현재 skeleton 레이아웃
 */
public class StoreFragment extends BaseFragment implements GoogleMapFragment.FragmentInteractionListener,
        View.OnClickListener, View.OnTouchListener{

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
    private final int CAMERA_CODE=1,GALLERY_CODE=2;
    private RecyclerView.Adapter iAdapter, rAdapter;
    private ArrayList<listData> itemDataset, replyDataset;
    User user = DataContainer.getInstance().getmUser(); // user 정보

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_store, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setPermission.getmInstance(getContext(),Manifest.permission.ACCESS_FINE_LOCATION ,GoogleMapPermission); // 권한요청 및 권한에따른 구글맵 셋팅
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getNetworkComponent().inject(this); // retrofit 객체 주입
        binding = FragmentStoreBinding.bind(getView()); // Store 프레그먼트 View

        setTestItemData(); //아이템 리스트뷰 셋팅 (테스트셋)
        setTestreplyData(); //댓글 리스트뷰 셋팅  (테스트셋)

        //댓글, 아이템목록 맨 아래로
        view.post(() -> {
            binding.itemList.smoothScrollToPosition(iAdapter.getItemCount() - 1);
            binding.replyList.smoothScrollToPosition(rAdapter.getItemCount() - 1);
        });

        binding.transparentImage.setOnTouchListener(this); // GoogleMapFragment와 scrollview 간의 간섭 컨트롤
        binding.replyaccept.setOnClickListener(this);        //댓글 작성 버튼
        binding.addItem.setOnClickListener(this);

        binding.editText11.setText(user.getName() + "의 행성");
        binding.editText12.setText(user.getPlanet().getName());
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case GALLERY_CODE:
                    SendPicture(data); //갤러리에서 가져오기
                    break;
                case CAMERA_CODE:
                    SendPicture(data); //카메라에서 가져오기
                    break;

                default:
                    break;
            }

        }
    }*/
    @Override
    public void sendMessageToParent(LatLng latLng, String address) {
        binding.editText13.setText("위도 : "+String.valueOf(latLng.longitude)+"\n경도 : "+String.valueOf(latLng.latitude));
        binding.editText14.setText(address);
    }

    /**
     * 이 아래부터는 리스너 정의
     */

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.replyaccept:
                replyDataset.add(new listData("anonymous", "?? km", String.valueOf(binding.replytext.getText()), R.mipmap.ic_launcher));
                rAdapter.notifyDataSetChanged();
                binding.replyList.smoothScrollToPosition(rAdapter.getItemCount() - 1);
                binding.replytext.setText(null);
                break;
            case R.id.addItem:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
                break;
        }
    }

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

    public void setGoogleMap(){
        Fragment childFragment = new GoogleMapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map, childFragment).commit();
    }

    PermissionListener GoogleMapPermission = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            setGoogleMap();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            setGoogleMap();
        }
    };

    public void setTestItemData() {
        itemDataset = new ArrayList<>();
        iAdapter = new mListAdapter(itemDataset);
        binding.itemList.setAdapter(iAdapter);
        // 테스트셋
        for (int i = 0; i < 10; i++) {
            itemDataset.add(new listData("전자렌지", "1000원", "구매한지 1년 어쩌고", R.mipmap.ic_launcher));
            itemDataset.add(new listData("벽걸이선풍기", "2000원", "구매한지 1년 어쩌고", R.mipmap.ic_launcher));
            itemDataset.add(new listData("전기난로", "3000원", "구매한지 1년 어쩌고", R.mipmap.ic_launcher));
            itemDataset.add(new listData("전기난로2", "35300원", "구매한지 1년 어쩌고", R.mipmap.ic_launcher));
            itemDataset.add(new listData("전기난로3", "63100원", "구매한지 1년 어쩌고", R.mipmap.ic_launcher));
        }
    }

    public void setTestreplyData() {
        replyDataset = new ArrayList<>();
        rAdapter = new mListAdapter(replyDataset);
        binding.replyList.setAdapter(rAdapter);

        // 테스트셋
        for (int i = 0; i < 10; i++) {
            replyDataset.add(new listData("병수", "0.05km", "깍아줘요", R.mipmap.ic_launcher));
            replyDataset.add(new listData("뱅수", "1.00km", "제발", R.mipmap.ic_launcher));
            replyDataset.add(new listData("뱅뱅", "1.50km", "한번만", R.mipmap.ic_launcher));
            replyDataset.add(new listData("뱅뱅2", "1.50km", "두번만", R.mipmap.ic_launcher));
            replyDataset.add(new listData("뱅뱅3", "1.50km", "세번만", R.mipmap.ic_launcher));
        }
    }

    @Override
    public String getTitle() {
        return "Store";
    }



}
