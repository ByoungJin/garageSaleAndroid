package com.garagesale.gapp.garagesale.util;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.garagesale.gapp.garagesale.fragment.StoreTabsFragment.CommentTabFragment;
import com.garagesale.gapp.garagesale.fragment.StoreTabsFragment.GoogleMapTabFragment;
import com.garagesale.gapp.garagesale.fragment.StoreTabsFragment.ProductTabFragment;

/**
 * Created by Administrator on 2017-07-19.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {


    private static final int PAGE_NUMBER = 3;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ProductTabFragment.getInstance();
            case 1:
                return GoogleMapTabFragment.getInstance();
            case 2:
                return CommentTabFragment.getInstance();
            default:
                return null;
        }
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "판매중";
            case 1:
                return "행성 위치";
            case 2:
                return "방명록";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_NUMBER;
    }
}
