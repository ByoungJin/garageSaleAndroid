package com.garagesale.gapp.garagesale.fragment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.garagesale.gapp.garagesale.R;

/**
 * Created by BaekByoungSoo on 2017. 9. 16..
 */

class PickCategoryImageAdapter extends BaseAdapter {


    private Context context = null;
    ImageView imageView = null;

    public Integer[] imageIDs = {
            R.drawable.icon02, R.drawable.icon03, R.drawable.icon04, R.drawable.icon05, R.drawable.icon06, R.drawable.icon07,
            R.drawable.icon08, R.drawable.icon09, R.drawable.icon10, R.drawable.icon11, R.drawable.icon12, R.drawable.icon13,
            R.drawable.icon14, R.drawable.icon15, R.drawable.icon16, R.drawable.icon17, R.drawable.icon18, R.drawable.icon19,
            R.drawable.icon20, R.drawable.icon21, R.drawable.icon22, R.drawable.icon23, R.drawable.icon24, R.drawable.icon25,
            R.drawable.icon26
    };

    public PickCategoryImageAdapter(Context context){
        this.context = context;
    }

    //view에 보여지는 grid 개수
    public int getCount() {
        return imageIDs.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {

        if (convertView == null) {

//            Log.d("DEBUG","positon: "+position);
            imageView = new ImageView(context);

            imageView.setLayoutParams(new GridView.LayoutParams(150,150));
            imageView.setAdjustViewBounds(false);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else {
            imageView = (ImageView) convertView;
        }

//        if(position < chacheImgList.size())
//            imageView.setImageBitmap(chacheImgList.get(position));

        imageView.setImageResource(imageIDs[position]);

        return imageView;
    }
}
