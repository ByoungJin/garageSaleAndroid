package com.garagesale.gapp.garagesale.util;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.ListitemViewBinding;
import com.garagesale.gapp.garagesale.entity.listData;

import java.util.ArrayList;

/**
 * Created by juyeol on 2017-06-28.
 * Databinding을 이용한 RecyclerView Adapter 생성
 */

public class mListAdapter extends RecyclerView.Adapter<mListAdapter.BindingHolder> {
    private ArrayList<listData> listDatas;

    /**
     * 정보를 담기위한 BindingHolder 생성
     */
    public class BindingHolder extends RecyclerView.ViewHolder {
        private ListitemViewBinding binding;

        public BindingHolder(ListitemViewBinding listitemViewBinding) {
            super(listitemViewBinding.getRoot());
            this.binding = listitemViewBinding;
        }

        public void bindConnection(listData listdata) {
            binding.setList(listdata);
        }
    }

    public mListAdapter(ArrayList<listData> listDataset) {
        listDatas = listDataset;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListitemViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.listitem_view, parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        holder.bindConnection(listDatas.get(position));
    }

    // 이미지 바인딩용 BindingAdapter
    @BindingAdapter("imgload")
    public static void imageLoad(ImageView imageView, int img) {
        imageView.setImageResource(img);
    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

}

