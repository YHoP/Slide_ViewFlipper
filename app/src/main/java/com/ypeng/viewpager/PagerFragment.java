package com.ypeng.viewpager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PagerFragment extends Fragment {

    public static final String ImageIDKey = "ImageIDKey";
    private Bundle mBundle;
    ImageView mImage;
    private int imageId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.fragment_main, container, false);
        if(mBundle != null){
            imageId = mBundle.getInt(ImageIDKey);
            mImage = (ImageView)view.findViewById(R.id.image);
            setImage(imageId);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }
        });

        return view;
    }


    private void setImage(int imageId) {
        mImage.setBackgroundResource(imageId);
    }

}
