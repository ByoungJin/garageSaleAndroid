package com.garagesale.gapp.garagesale.util;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.ListplaentViewBinding;
import com.garagesale.gapp.garagesale.entity.listPlanet;

import java.util.ArrayList;

/**
 * Created by juyeol on 2017-06-28.
 * Databinding을 이용한 RecyclerView Adapter 생성
 */

public class pListAdapter extends RecyclerView.Adapter<pListAdapter.BindingHolder> {
    private ArrayList<listPlanet> listplanets;


    /**
     * 정보를 담기위한 BindingHolder 생성
     */
    public class BindingHolder extends RecyclerView.ViewHolder {
        private ListplaentViewBinding binding;

        public BindingHolder(ListplaentViewBinding listplaentViewBinding) {
            super(listplaentViewBinding.getRoot());
            this.binding = listplaentViewBinding;
        }

        public void bindConnection(listPlanet listplanet) {
            binding.setList(listplanet);
        }
    }

    public pListAdapter(ArrayList<listPlanet> listplanetset) {
        listplanets = listplanetset;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListplaentViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.listplaent_view, parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        holder.bindConnection(listplanets.get(position));
    }

    // 이미지 바인딩용 BindingAdapter
    @BindingAdapter("imgload")
    public static void imageLoad(ImageView imageView, int img) {
        imageView.setImageResource(img);
    }

    @Override
    public int getItemCount() {
        return listplanets.size();
    }

}

