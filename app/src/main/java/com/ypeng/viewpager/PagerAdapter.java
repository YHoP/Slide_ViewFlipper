package com.ypeng.viewpager;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class PagerAdapter extends FragmentPagerAdapter{

    private String [] imagesArray;
    private int imagesArrayLength = 0;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        Resources resources = context.getResources();
        imagesArray = resources.getStringArray(R.array.images);
        imagesArrayLength = imagesArray.length;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("Current image position", String.valueOf(position));
        PagerFragment pagerFragment = new PagerFragment();
        Bundle args = new Bundle();
        int virtualPosition = position % imagesArrayLength;
        args.putInt(pagerFragment.ImageIDKey, getImageId(virtualPosition));
        pagerFragment.setArguments(args);
        return pagerFragment;
    }

    private int getImageId(int position){
        int id = 0;
        switch (position){
            case 0:
                id = R.drawable.image01;
                break;
            case 1:
                id = R.drawable.image02;
                break;
            case 2:
                id = R.drawable.image03;
                break;
        }

        return id;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
