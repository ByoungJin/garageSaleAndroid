package com.garagesale.gapp.garagesale.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentStoreBinding;
import com.garagesale.gapp.garagesale.entity.listData;
import com.garagesale.gapp.garagesale.util.mListAdapter;
import com.garagesale.gapp.garagesale.util.setPermission;
import com.gun0912.tedpermission.PermissionListener;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by juyeol on 2017-06-28.
 * 현재 skeleton 레이아웃
 */
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

    private RecyclerView.Adapter iAdapter, rAdapter;
    private ArrayList<listData> itemDataset, replyDataset;

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

        binding.transparentImage.setOnTouchListener(interceptListener); // GoogleMapFragment와 scrollview 간의 간섭 컨트롤
        binding.replyaccept.setOnClickListener(addItemListener);        //댓글 작성 버튼
    }

    /**
     * 이 아래부터는 리스너 정의
     */
    View.OnClickListener addItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            replyDataset.add(new listData("anonymous", "?? km", String.valueOf(binding.replytext.getText()), R.mipmap.ic_launcher));
            rAdapter.notifyDataSetChanged();
            binding.replyList.smoothScrollToPosition(rAdapter.getItemCount() - 1);
            binding.replytext.setText(null);
        }
    };

    View.OnTouchListener interceptListener = new View.OnTouchListener() {
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
    };

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
