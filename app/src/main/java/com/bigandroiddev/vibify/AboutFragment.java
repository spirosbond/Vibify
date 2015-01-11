package com.bigandroiddev.vibify;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

/**
 * Created by spiros on 11/30/14.
 */
public class AboutFragment extends Fragment {
    private static final String TAG = AboutFragment.class.getSimpleName();
    private final BaseSpringSystem mSpringSystem = SpringSystem.create();
    private final ExampleSpringListener mSpringListener = new ExampleSpringListener();
    ImageView logo;
    private Spring mScaleSpring;
    private double tension = 350, friction = 8;

    public AboutFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
//			Bundle args = new Bundle();
//			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//			fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_about, container, false);

        TextView footer = (TextView) rootView.findViewById(R.id.aboutFooterTextView);
        try {
            footer.setText(footer.getText() + " " + getActivity().getPackageManager().getPackageInfo
                    (getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
//        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        logo = (ImageView) rootView.findViewById(R.id.aboutImageView);
//	    logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.round));
        SpringConfig sc = new SpringConfig(tension, friction);
        mScaleSpring = mSpringSystem.createSpring();
        mScaleSpring.setSpringConfig(sc);
        logo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_SCROLL:
                        Log.d(TAG, "ACTION_SCROLL");
                        break;
                    case MotionEvent.ACTION_DOWN:
                        Log.d(TAG, "ACTION_DOWN");
                        mScaleSpring.setEndValue(1);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(TAG, "ACTION_UP");
                    case MotionEvent.ACTION_CANCEL:
                        Log.d(TAG, "ACTION_CANCEL");
                        mScaleSpring.setEndValue(0);
                        break;
                }
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScaleSpring.addListener(mSpringListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScaleSpring.removeListener(mSpringListener);
    }

    private class ExampleSpringListener extends SimpleSpringListener {
        @Override
        public void onSpringUpdate(Spring spring) {
            float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);
            logo.setScaleX(mappedValue);
            logo.setScaleY(mappedValue);
        }
    }
}




