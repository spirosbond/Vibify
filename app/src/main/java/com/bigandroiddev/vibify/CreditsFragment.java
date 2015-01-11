package com.bigandroiddev.vibify;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.bigandroiddev.vibify.Utils.StorageUtils;

import java.io.IOException;

/**
 * Created by spiros on 11/30/14.
 */
public class CreditsFragment extends Fragment {

    private static final String TAG = CreditsFragment.class.getSimpleName();

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CreditsFragment newInstance() {
        CreditsFragment fragment = new CreditsFragment();
//			Bundle args = new Bundle();
//			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//			fragment.setArguments(args);
        return fragment;
    }

    public CreditsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_credits, container, false);
        WebView creditsWebView = (WebView) rootView.findViewById(R.id.about_thirdsparty_credits);
        try {
            creditsWebView.loadData(StorageUtils.loadRawData(getActivity().getBaseContext(), "credits_thirdparty"), "text/html", "UTF-8");
        } catch (IOException ioe) {
            Log.e(TAG, "Error reading changelog file!", ioe);
        }
        return rootView;
    }

}

