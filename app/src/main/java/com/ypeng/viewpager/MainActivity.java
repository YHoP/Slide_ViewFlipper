package com.ypeng.viewpager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private Animation mInFromRight;
    private Animation mOutToLeft;
    private Animation mInFromLeft;
    private Animation mOutToRight;
    private ViewFlipper mViewFlipper;
    private ImageView image01, image02, image03;
    private Timer mTimer;
    private Handler mHandler;
    private GestureDetector gestureDetector;
    private int mId;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTimer = new Timer();
        mHandler = new Handler();

        mViewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        image01 = (ImageView) findViewById(R.id.image_01);
        image02 = (ImageView) findViewById(R.id.image_02);
        image03 = (ImageView) findViewById(R.id.image_03);
        mViewFlipper.setDisplayedChild(0);
        initAnimations();
        setAutoPlay();
    }

    private void setAutoPlay(){
        final Runnable changeCurrentImage = new Runnable() {
            @Override
            public void run() {
                mViewFlipper.setInAnimation(mInFromRight);
                mViewFlipper.setOutAnimation(mOutToLeft);
                mViewFlipper.showNext();
            }
        };
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(changeCurrentImage);
            }
        }, 5000, 5000);
    }

    private void initAnimations() {
        mInFromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        mInFromRight.setDuration(500);
        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
        mInFromRight.setInterpolator(accelerateInterpolator);

        mInFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        mInFromLeft.setDuration(500);
        mInFromLeft.setInterpolator(accelerateInterpolator);

        mOutToRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,
                0.0f, Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        mOutToRight.setDuration(500);
        mOutToRight.setInterpolator(accelerateInterpolator);

        mOutToLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        mOutToLeft.setDuration(500);
        mOutToLeft.setInterpolator(accelerateInterpolator);


        gestureDetector = new GestureDetector(new MyGestureDetector());

        View.OnTouchListener gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                mId = view.getId();
                if (gestureDetector.onTouchEvent(event)) {
                    return false;
                } else {
                    return true;
                }
            }
        };

//        mViewFlipper.setOnTouchListener(gestureListener);
        image01.setOnTouchListener(gestureListener);
        image02.setOnTouchListener(gestureListener);
        image03.setOnTouchListener(gestureListener);
    }

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public boolean onSingleTapUp(MotionEvent event) {
            Log.i("id : ", String.valueOf(mId));
            loadIntent();
            return super.onSingleTapUp(event);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mViewFlipper.setInAnimation(mInFromRight);
                mViewFlipper.setOutAnimation(mOutToLeft);
                mViewFlipper.showNext();
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                mViewFlipper.setInAnimation(mInFromLeft);
                mViewFlipper.setOutAnimation(mOutToRight);
                mViewFlipper.showPrevious();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    private void loadIntent() {
            Intent browserIntent = null;
            switch (mId){
                case R.id.image_01:
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.cnn.com"));
                    break;
                case R.id.image_02:
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                    break;
                case R.id.image_03:
                    browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.bbc.com"));
                    break;
            }
            if(browserIntent != null) {
                startActivity(browserIntent);
            }
    }
}
