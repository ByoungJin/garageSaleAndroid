package com.garagesale.gapp.garagesale.fragment.StoreTabsFragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentStoreProductTabBinding;
import com.garagesale.gapp.garagesale.entity.listData;
import com.garagesale.gapp.garagesale.util.Camera.LoadPicture;
import com.garagesale.gapp.garagesale.util.mListAdapter;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;

import static android.app.Activity.RESULT_OK;

/**
 * Created by juyeol on 2017-07-19.
 * 판매중인 상품에대한 Store 탭
 */

public class ProductTabFragment extends Fragment implements
        View.OnClickListener, View.OnTouchListener {

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static ProductTabFragment mInstance;

    public static ProductTabFragment getInstance() {
        if (mInstance == null) mInstance = new ProductTabFragment();
        return mInstance;
    }

    View view;

    private FragmentStoreProductTabBinding binding;
    private RecyclerView.Adapter pAdapter;
    private ArrayList<listData> productDataset;

    // 여기부턴 카메라관련 인자
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CODE_PROFILE_IMAGE_CROP = 3;
    private Uri outputFileUri;
    private LoadPicture loadPicture;
    private ProductTabInteractionListener mProductListener;

    /**
     * 부모 Fragment와 통신하기위한 리스너
     */
    public interface ProductTabInteractionListener {
        void ProductTabMessageToParent(Drawable d);

        void SendserverPicture(MultipartBody.Part body);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // check if parent Fragment implements listener
        if (getParentFragment() instanceof GoogleMapTabFragment.GoogleTapInteractionListener) {
            mProductListener = (ProductTabFragment.ProductTabInteractionListener) getParentFragment();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChildFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store_product_tab, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding = FragmentStoreProductTabBinding.bind(getView());
        loadPicture = new LoadPicture(this, getContext());

        //댓글, 아이템목록 맨 아래로
        view.post(() -> {
            binding.itemList.smoothScrollToPosition(pAdapter.getItemCount() - 1);
        });
        setTestItemData(); //아이템 리스트뷰 셋팅 (테스트셋)

        binding.addItem.setOnClickListener(this);
        binding.addItem2.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY) {
                outputFileUri = data.getData();

                if (outputFileUri != null) {
                    loadPicture.doCrop(outputFileUri);
                    //showImage(loadPicture.drawFile(outputFileUri));
                    //sendServerImage();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                if (outputFileUri != null) {
                    loadPicture.doCrop(outputFileUri);
                    //showImage(loadPicture.drawFile(outputFileUri));
                    //sendServerImage();
                }
            } else if (requestCode == REQUEST_CODE_PROFILE_IMAGE_CROP) {
                outputFileUri = data.getData();
                showImage(loadPicture.drawFile(outputFileUri));
                sendServerImage();
            }
        }
    }


    private void showImage(Bitmap bitmap) {
        Drawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        mProductListener.ProductTabMessageToParent(bitmapDrawable);
    }

    public void sendServerImage() {
        MultipartBody.Part body;
        body = loadPicture.createBody(outputFileUri);
        mProductListener.SendserverPicture(body);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addItem:
                loadPicture.onGallery();
                break;
            case R.id.addItem2:
                outputFileUri = loadPicture.onCamera();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    /******************리스트 아이템 셋**********************/
    public void setTestItemData() {
        productDataset = new ArrayList<>();
        pAdapter = new mListAdapter(productDataset);
        binding.itemList.setAdapter(pAdapter);
        // 테스트셋
        for (int i = 0; i < 10; i++) {
            productDataset.add(new listData("전자렌지", "1000원", "구매한지 1년 어쩌고", R.mipmap.ic_launcher));
        }
    }

}
