package com.garagesale.gapp.garagesale.fragment.StoreTabsFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garagesale.gapp.garagesale.R;
import com.garagesale.gapp.garagesale.databinding.FragmentStoreCommentTabBinding;
import com.garagesale.gapp.garagesale.entity.listData;
import com.garagesale.gapp.garagesale.util.mListAdapter;

import java.util.ArrayList;

/**
 * Created by juyeol on 2017-07-19.
 * 방명록(댓글)에 대한 Store 탭
 */

public class CommentTabFragment extends Fragment implements View.OnClickListener {

    // 싱글톤 패턴
    @SuppressLint("StaticFieldLeak")
    private static CommentTabFragment mInstance;

    public static CommentTabFragment getInstance() {
        if (mInstance == null) mInstance = new CommentTabFragment();
        return mInstance;
    }

    View view;

    private RecyclerView.Adapter cAdapter;
    private ArrayList<listData> commentDataset;
    private FragmentStoreCommentTabBinding binding;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_store_comment_tab, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding = FragmentStoreCommentTabBinding.bind(getView());

        //댓글, 아이템목록 맨 아래로
        view.post(() -> {
            binding.commentList.smoothScrollToPosition(cAdapter.getItemCount() - 1);
        });
        setTestreplyData(); //댓글 리스트뷰 셋팅  (테스트셋)

        binding.commentaccept.setOnClickListener(this);        //댓글 작성 버튼
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.commentaccept:
                commentDataset.add(new listData("anonymous", "?? km", String.valueOf(binding.commenttext.getText()), R.mipmap.ic_launcher));
                cAdapter.notifyDataSetChanged();
                binding.commentList.smoothScrollToPosition(cAdapter.getItemCount() - 1);
                binding.commenttext.setText(null);
                break;
        }
    }


    public void setTestreplyData() {
        commentDataset = new ArrayList<>();
        cAdapter = new mListAdapter(commentDataset);
        binding.commentList.setAdapter(cAdapter);

        // 테스트셋
        for (int i = 0; i < 10; i++) {
            commentDataset.add(new listData("병수", "0.05km", "깍아줘요", R.mipmap.ic_launcher));
        }
    }


}
