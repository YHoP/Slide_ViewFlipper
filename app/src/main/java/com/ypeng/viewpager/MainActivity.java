package com.ypeng.viewpager;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    PagerAdapter mPagerAdapter;
    ViewPager mViewPager;
    private int mCurrentImage = 0;
    private Timer mTimer;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimer = new Timer();
        mHandler = new Handler();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        setUpViewPager();
    }

    private void setUpViewPager() {
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);

        final Runnable changeCurrentImage = new Runnable() {
            @Override
            public void run() {
                mCurrentImage++;
                if(mCurrentImage >= mPagerAdapter.getCount()) {
                    mCurrentImage = 0;
                }
                mViewPager.setCurrentItem(mCurrentImage, true);
            }
        };

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(changeCurrentImage);
            }
        }, 7000, 7000);
    }
}
