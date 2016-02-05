package com.ypeng.viewpager;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SwipeActivity extends AppCompatActivity {

    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private String [] mImageArray;
    private ImageView mImageView;
    private TextView mTextView;
    private int mCurrentPosition = 0;
    private Timer mTimer;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        Resources resources = this.getResources();
        mImageArray = resources.getStringArray(R.array.images);

        mImageView = (ImageView) findViewById(R.id.image_holder);
        mTextView = (TextView) findViewById(R.id.text_holder);

        setImageView(mCurrentPosition);
        initTimer();

        mImageView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                if(mCurrentPosition == 0 ) {
                    mCurrentPosition = mImageArray.length;
                }
                setImageView(mCurrentPosition);
            }

            public void onSwipeLeft() {
                if (mCurrentPosition < mImageArray.length - 1) {
                    mCurrentPosition++;
                } else {
                    mCurrentPosition = 0;
                }
                setImageView(mCurrentPosition);
            }
        });
    }

    private void initTimer() {
        mTimer = new Timer();
        mHandler = new Handler();

        final Runnable changeCurrentImage = new Runnable() {
            @Override
            public void run() {
                if(mCurrentPosition < mImageArray.length - 1) {
                    mCurrentPosition++;
                } else {
                    mCurrentPosition = 0;
                }
                setImageView(mCurrentPosition);
            }
        };

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(changeCurrentImage);
            }
        }, 3000, 3000);
    }

    private void setImageView(int position) {
        int imageId = getImageId(position);
        mImageView.setBackgroundResource(imageId);
        mTextView.setText("position : " + position);
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

}
