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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.ViewGroup.LayoutParams;

public class MainActivity extends Activity {

    private Animation mInFromRight;
    private Animation mOutToLeft;
    private Animation mInFromLeft;
    private Animation mOutToRight;
    private ViewFlipper mViewFlipper;
    private Timer mTimer;
    private Handler mHandler;
    private GestureDetector gestureDetector;
    private int mId;
    private List<Team> mTeam = new ArrayList<Team>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
//        initTeamInfo();
        getFirebaseData();

        mTimer = new Timer();
        mHandler = new Handler();

        initAnimations();
        setAutoPlay();

        setContentView(R.layout.activity_main);
        gestureDetector = new GestureDetector(new MyGestureDetector());
    }

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

    private void getFirebaseData(){
        Firebase firebaseRef = new Firebase("https://view-flipper-ad.firebaseio.com/team");
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot contentSnapshot : dataSnapshot.getChildren()) {
                    Team team = contentSnapshot.getValue(Team.class);
                    mTeam.add(team);
                }

                for (int i = 0; i < mTeam.size(); i++) {
                    ImageView imageView = new ImageView(MainActivity.this);
                    imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 600));
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    Picasso.with(MainActivity.this).load(mTeam.get(i).getImage()).into(imageView);
                    imageView.setId(i);
                    imageView.setOnTouchListener(gestureListener);
                    mViewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
                    mViewFlipper.addView(imageView);
                }
                mViewFlipper.setDisplayedChild(0);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("The read failed: ", firebaseError.getMessage());
            }
        });
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
    }

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public boolean onSingleTapUp(MotionEvent event) {
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
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mTeam.get(mId).getLink()));
        startActivity(browserIntent);

    }

    /*  Save team image and link info to Firebase  */
    /*
    private void initTeamInfo(){
        Firebase myFirebaseRef = new Firebase("https://view-flipper-ad.firebaseio.com/");

        Team blazers = new Team();
        blazers.setImage("http://covermyfb.com/media/covers/thumb/6335-portland-trail-blazers.jpg");
        blazers.setLink("http://www.nba.com/blazers/");
        Firebase firebase1 = myFirebaseRef.child("team").child("blazers");
        firebase1.setValue(blazers);

        Team timbers = new Team();
        timbers.setImage("https://www.flagline.com/sites/default/files/styles/product_main_page/public/images/products/Portland_Timbers_Bumper_Sticker_4648.jpg");
        timbers.setLink("http://www.timbers.com/");
        Firebase firebase2 = myFirebaseRef.child("team").child("timbers");
        firebase2.setValue(timbers);

        Team winterhawks = new Team();
        winterhawks.setImage("http://www.vbconline.org/wp-content/uploads/2014/11/Winterhawks-web-banner.jpg");
        winterhawks.setLink("http://www.winterhawks.com/");
        Firebase firebase3 = myFirebaseRef.child("team").child("winterhawks");
        firebase3.setValue(winterhawks);
    }
    */
}