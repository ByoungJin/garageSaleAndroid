package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentLoginBinding;
import com.garagesale.gapp.garagesale.databinding.FragmentPickPlanetBinding;
import com.garagesale.gapp.garagesale.databinding.PractiveMainBinding;
import com.garagesale.gapp.garagesale.entity.listData;
import com.garagesale.gapp.garagesale.entity.listPlanet;
import com.garagesale.gapp.garagesale.service.LoginService;
import com.garagesale.gapp.garagesale.util.SharedPreferenceManager;
import com.garagesale.gapp.garagesale.util.mListAdapter;
import com.garagesale.gapp.garagesale.util.pListAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Retrofit;

public class PickPlanetFragment extends BaseFragment {

    private FragmentPickPlanetBinding binding;

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static PickPlanetFragment mInstance;
    public static PickPlanetFragment getInstance(){
        if(mInstance == null) mInstance = new PickPlanetFragment();
        return mInstance;
    }


    @Inject
    public Retrofit retrofit;  // retrofit
    private RecyclerView.Adapter adapter;
    private ArrayList<listPlanet> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pick_planet, container, false);

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = FragmentPickPlanetBinding.bind(getView()); // Login 프레그먼트 View

        getNetworkComponent().inject(this); // retrofit 객체 주입 시점

        setTestData(); //댓글 리스트뷰 셋팅  (테스트셋)

        binding.planetIconList.setOnScrollChangeListener(new RecyclerView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                int visilbeCompFirstPosition = ((LinearLayoutManager)binding.planetIconList.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                int visiblieCompLastPosition = ((LinearLayoutManager)binding.planetIconList.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                Toast.makeText(getContext(),String.valueOf(visilbeCompFirstPosition),Toast.LENGTH_LONG).show();
                binding.test.setText(String.valueOf(visilbeCompFirstPosition));
            }
        });
    }



    public void setTestData() {
        list = new ArrayList<>();
        adapter = new pListAdapter(list);
        binding.planetIconList.setAdapter(adapter);

        // 테스트셋
        for (int i = 0; i < 10; i++) {
            list.add(new listPlanet(R.mipmap.ic_launcher));
        }
    }

        @Override
    public String getTitle() {
        return "PickPlanet";
    }



}
