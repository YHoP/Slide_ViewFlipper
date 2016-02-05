package com.ypeng.viewpager;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private int mCurrentPosition = 0;
    private Timer mTimer;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTimer();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        setUpViewPager();

        mViewPager.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                if(mCurrentPosition == 0 ) {
                    mCurrentPosition = mPagerAdapter.getCount();
                }
                mViewPager.setCurrentItem(mCurrentPosition, true);
            }

            public void onSwipeLeft() {
                if(mCurrentPosition < mPagerAdapter.getCount() - 1) {
                    mCurrentPosition++;
                } else {
                    mCurrentPosition = 0;
                }
                mViewPager.setCurrentItem(mCurrentPosition, true);
            }
        });
    }

    private void initTimer() {
        mTimer = new Timer();
        mHandler = new Handler();
    }

    private void setUpViewPager() {
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);

        final Runnable changeCurrentImage = new Runnable() {
            @Override
            public void run() {
                if(mCurrentPosition < mPagerAdapter.getCount() - 1) {
                    mCurrentPosition++;
                } else {
                    mCurrentPosition = 0;
                }
                mViewPager.setCurrentItem(mCurrentPosition, true);
            }
        };

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(changeCurrentImage);
            }
        }, 3000, 3000);
    }

//    @Override
//    public void onPageScrollStateChanged(int state) {
//        if (state == ViewPager.SCROLL_STATE_IDLE) {
//            mCurrentPosition = mViewPager.getCurrentItem();
//            initTimer();
//        }
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        mCurrentPosition = position;
//        initTimer();
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        mCurrentPosition = position;
//        initTimer();
//    }
}
