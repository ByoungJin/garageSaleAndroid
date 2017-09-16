package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;


public class PickCategoryFragment extends BaseFragment {


    GridView gridView = null;

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static PickCategoryFragment mInstance;
    public static PickCategoryFragment getInstance(){
        if(mInstance == null) mInstance = new PickCategoryFragment();
        return mInstance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pick_category, container, false);

        gridView = view.findViewById(R.id.gridview);
        gridView.setAdapter(new PickCategoryImageAdapter(getContext()));

        gridView.setOnItemClickListener((parent, view1, position, id) -> {
////                Toast.makeText(getContext(),""+(position+1),Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(getContext(), FullImageActivity.class);
//                // passing array index
//                i.putExtra("id", position);
//                startActivity(i);
        });

        return view;


    }

    @Override
    public String getTitle() {
        return "Pick";
    }

}
