package com.garagesale.gapp.garagesale.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentStoreBinding;
import com.garagesale.gapp.garagesale.entity.User;
import com.garagesale.gapp.garagesale.entity.listData;
import com.garagesale.gapp.garagesale.response.UserResponse;
import com.garagesale.gapp.garagesale.service.ImageService;
import com.garagesale.gapp.garagesale.util.Camera.*;
import com.garagesale.gapp.garagesale.util.DataContainer;
import com.garagesale.gapp.garagesale.util.mListAdapter;
import com.garagesale.gapp.garagesale.util.setPermission;
import com.google.android.gms.maps.model.LatLng;
import com.gun0912.tedpermission.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.R.attr.path;
import static android.app.Activity.RESULT_OK;
import static com.garagesale.gapp.garagesale.util.Camera.RealPathUtil.getRealPath;


/**
 * Created by juyeol on 2017-06-28.
 * 현재 skeleton 레이아웃
 */
public class StoreFragment extends BaseFragment implements GoogleMapFragment.FragmentInteractionListener,
        View.OnClickListener, View.OnTouchListener {

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
    // 여기부턴 카메라관련 인자
    private ImageService imageService;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_GALLERY = 2;
    private Uri outputFileUri;
    // 여기부턴 리스트 관련 인자들
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
        setPermission.getmInstance(getContext(), Manifest.permission.ACCESS_FINE_LOCATION, GoogleMapPermission); // 권한요청 및 권한에따른 구글맵 셋팅
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getNetworkComponent().inject(this); // retrofit 객체 주입
        imageService = retrofit.create(ImageService.class);
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
        binding.addItem.setOnClickListener(this);
        binding.addItem2.setOnClickListener(this);
        // Glide.with(getInstance()).load("http://192.168.42.180:3000/imeage/Screenshot_2017-07-04-17-56-47.png").into(binding.imageView5);


        // 유저 정보 출력
        if (user != null) {
            binding.editText11.setText(user.getName() + "의 행성");    // 유저 이름 출력
            binding.editText12.setText(user.getPlanet().getName()); // 행성 이름 출력
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                outputFileUri = data.getData();
            }

            if (outputFileUri != null) {
                drawFile();
                sendServerImage();
            }
        }
    }

    @Override
    public void sendMessageToParent(LatLng latLng, String address) {
        binding.editText13.setText("위도 : " + String.valueOf(latLng.longitude) + "\n경도 : " + String.valueOf(latLng.latitude));
        binding.editText14.setText(address);
    }

    /*****************카메라 처리 메소드 **********************/
    // 갤러리 호출 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RuntimeUtil.PERMISSION_CAMERA) {
            if (RuntimeUtil.verifyPermissions(getActivity(), grantResults)) {
                // onCamera(binding.addItem);
            }
        } else if (requestCode == RuntimeUtil.PERMISSION_ALBUM) {
            if (RuntimeUtil.verifyPermissions(getActivity(), getActivity().getWindow().getDecorView(), grantResults)) {
                // onGallery(binding.addItem);
            }
        }
    }

    private void drawFile() {
        Bitmap bitmapImage;
        try {
            bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), outputFileUri);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "IOException:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
        showImage(bitmapImage);
    }

    private void showImage(Bitmap bitmap) {
        Drawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        binding.imageView5.setImageDrawable(bitmapDrawable);
    }

    public void sendServerImage() {
        File file = new File(getRealPath(getContext(), outputFileUri));

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(
                MediaType.parse(getActivity().getContentResolver().getType(outputFileUri)),file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<UserResponse> repos = imageService.uploadImageFile(body);

        repos.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Log.e("model", "Success");
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable throwable) {
                Log.e("model", "Failed" + throwable.getMessage());
            }
        });
    }

    /*****************카메라 처리 메소드 끝**********************/
    /******************리스너 정의**********************/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.replyaccept:
                replyDataset.add(new listData("anonymous", "?? km", String.valueOf(binding.replytext.getText()), R.mipmap.ic_launcher));
                rAdapter.notifyDataSetChanged();
                binding.replyList.smoothScrollToPosition(rAdapter.getItemCount() - 1);
                binding.replytext.setText(null);
                break;
            case R.id.addItem:
                RuntimeUtil.checkPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, RuntimeUtil.PERMISSION_ALBUM, new OnPermssionCallBackListener() {
                    @Override
                    public void OnGrantPermission() {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_GALLERY);
                    }
                });
                break;
            case R.id.addItem2:
                Log.d("das", "dsads");
                RuntimeUtil.checkPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, RuntimeUtil.PERMISSION_ALBUM, new OnPermssionCallBackListener() {
                    @Override
                    public void OnGrantPermission() {
                        RuntimeUtil.checkPermission(getActivity(), getActivity().getWindow().getDecorView(), Manifest.permission.CAMERA, RuntimeUtil.PERMISSION_CAMERA, null, new OnPermssionCallBackListener() {
                            @Override
                            public void OnGrantPermission() {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                    outputFileUri = ProviderUtil.getOutputMediaFileUri(getActivity().getBaseContext());
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                                    startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                                }
                            }
                        });
                    }
                });
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
    /******************리스너 정의 끝**********************/

    /******************구글맵 메소드(+권한)**********************/
    public void setGoogleMap() {
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
    /******************구글맵 메소드(+권한) 끝**********************/

    /******************리스트 아이템 셋**********************/
    public void setTestItemData() {
        itemDataset = new ArrayList<>();
        iAdapter = new mListAdapter(itemDataset);
        binding.itemList.setAdapter(iAdapter);
        // 테스트셋
        for (int i = 0; i < 10; i++) {
            itemDataset.add(new listData("전자렌지", "1000원", "구매한지 1년 어쩌고", R.mipmap.ic_launcher));
        }
    }

    public void setTestreplyData() {
        replyDataset = new ArrayList<>();
        rAdapter = new mListAdapter(replyDataset);
        binding.replyList.setAdapter(rAdapter);

        // 테스트셋
        for (int i = 0; i < 10; i++) {
            replyDataset.add(new listData("병수", "0.05km", "깍아줘요", R.mipmap.ic_launcher));
        }
    }

    /******************리스트 아이템 셋 끝**********************/

    @Override
    public String getTitle() {
        return "Store";
    }


}
