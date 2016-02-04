package com.ypeng.viewpager;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    public static String [] images;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);

        Resources resources = context.getResources();
        images = resources.getStringArray(R.array.images);
    }

    @Override
    public Fragment getItem(int position) {
        PagerFragment pagerFragment = new PagerFragment();
        Bundle args = new Bundle();
        args.putInt(pagerFragment.ImageIDKey, getImageId(position));
        pagerFragment.setArguments(args);
        return pagerFragment;
    }

    public static int getImageId(int position){
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
        return images.length;
    }

    public static int getImagePosition(int imageId){
        int imagePosition = 0;
        switch (imageId){
            case R.drawable.image01:
                imagePosition = 0;
                break;
            case R.drawable.image02:
                imagePosition = 1;
                break;
            case R.drawable.image03:
                imagePosition = 2;
                break;
        }
        return imagePosition;
    }
}
