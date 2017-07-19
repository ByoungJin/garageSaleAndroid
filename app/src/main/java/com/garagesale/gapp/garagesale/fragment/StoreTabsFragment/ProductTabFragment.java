package com.garagesale.gapp.garagesale.fragment.StoreTabsFragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentStoreCommentTabBinding;
import com.garagesale.gapp.garagesale.databinding.FragmentStoreProductTabBinding;
import com.garagesale.gapp.garagesale.entity.listData;
import com.garagesale.gapp.garagesale.util.mListAdapter;

import java.util.ArrayList;

import static com.garagesale.gapp.garagesale.R.id.view;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store_product_tab, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding = FragmentStoreProductTabBinding.bind(getView());

        //댓글, 아이템목록 맨 아래로

        view.post(() -> {
            binding.itemList.smoothScrollToPosition(pAdapter.getItemCount() - 1);
        });
        setTestItemData(); //아이템 리스트뷰 셋팅 (테스트셋)

        binding.addItem.setOnClickListener(this);
        binding.addItem2.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

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
