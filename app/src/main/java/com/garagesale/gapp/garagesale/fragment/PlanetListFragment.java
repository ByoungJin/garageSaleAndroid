package com.garagesale.gapp.garagesale.fragment;

import android.annotation.SuppressLint;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.garagesale.gapp.garagesale.BaseFragment;
import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentPlanetListBinding;
import com.garagesale.gapp.garagesale.databinding.FragmentPlanetListItemBinding;
import com.garagesale.gapp.garagesale.entity.User;
import com.garagesale.gapp.garagesale.response.UserListResponse;
import com.garagesale.gapp.garagesale.service.LoginService;
import com.garagesale.gapp.garagesale.util.GPSInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlanetListFragment extends BaseFragment {

    private RecyclerView.Adapter adapter;
    private ArrayList<PlanetListData> itemDataset;
    private FragmentPlanetListBinding binding;

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static PlanetListFragment mInstance;
    public static PlanetListFragment getInstance(){
        if(mInstance == null) mInstance = new PlanetListFragment();
        return mInstance;
    }

    @Inject
    public Retrofit retrofit;  // retrofit

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_planet_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getNetworkComponent().inject(this);
        binding = FragmentPlanetListBinding.bind(getView());
        LoginService loginService = retrofit.create(LoginService.class);

        // Get GPS정보
        Location gLocation = GPSInfo.getmInstance(getContext()).getGPSLocation();
        Toast.makeText(getActivity(), "gLocation : " + String.valueOf(gLocation.getLongitude()) + ", " + String.valueOf(gLocation.getLatitude()) , Toast.LENGTH_SHORT).show();

        // Get 근거리 User 정보
        Call<UserListResponse> repos = loginService.getUserList(String.valueOf(gLocation.getLongitude()), String.valueOf(gLocation.getLatitude()));
        repos.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                UserListResponse userListResponse = response.body();
                setTestItemData(userListResponse.getUsers());
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public String getTitle() {
        return "Planet List";
    }

    public void setTestItemData(List<User> userList) {
        itemDataset = new ArrayList<>();
        adapter = new mListAdapter(itemDataset);
        binding.testList.setAdapter(adapter);

        for (User user : userList) {
            itemDataset.add(new PlanetListData(user.getEmail(), user.getPlanet().getName(),
                    user.getPlanet().getDescription(),R.mipmap.ic_launcher
            ));
        }

    }


    public class PlanetListData {
        public String header;
        public String option;
        public String body;
        public int img;

        public PlanetListData(String header, String option, String body, int img) {
            this.header = header;
            this.option = option;
            this.body = body;
            this.img = img;
        }
    }

    public class mListAdapter extends RecyclerView.Adapter {
        private ArrayList<PlanetListData> listDatas;

        /**
         * 정보를 담기위한 BindingHolder 생성
         */
        public class BindingHolder extends RecyclerView.ViewHolder {
            private FragmentPlanetListItemBinding binding;

            public BindingHolder(FragmentPlanetListItemBinding fragmentPlanetListItemBinding) {
                super(fragmentPlanetListItemBinding.getRoot());
                this.binding = fragmentPlanetListItemBinding;
            }

            public void bindConnection(PlanetListData listdata) {
                binding.setList(listdata);
            }
        }

        public mListAdapter(ArrayList<PlanetListData> listDataset) {
            listDatas = listDataset;
        }


        @Override
        public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            FragmentPlanetListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.fragment_planet_list_item, parent, false);
            return new BindingHolder(binding);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((BindingHolder)holder).bindConnection(listDatas.get(position));
        }


        // 이미지 바인딩용 BindingAdapter
        @BindingAdapter("imgload")
        public void imageLoad(ImageView imageView, int img) {
            imageView.setImageResource(img);
        }

        @Override
        public int getItemCount() {
            return listDatas.size();
        }

    }

}