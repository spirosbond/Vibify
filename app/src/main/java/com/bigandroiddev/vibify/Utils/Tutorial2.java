package com.bigandroiddev.vibify.Utils;

import java.util.Locale;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.bigandroiddev.vibify.Lists.ListController;
import com.bigandroiddev.vibify.Lists.NavDrawerUtils;
import com.bigandroiddev.vibify.Lists.TutorialNavDrawerUtils;
import com.bigandroiddev.vibify.R;
import com.bigandroiddev.vibify.Services.NLService;
import com.bigandroiddev.vibify.Vibify;

public class Tutorial2 extends ActionBarActivity implements ViewPager.OnPageChangeListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    private static final String TAG = Tutorial2.class.getSimpleName();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    ImageView pageIndicator, page1, page2, page3, page4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
        pageIndicator = (ImageView) findViewById(R.id.tutorial_page_active);
        page1 = (ImageView) findViewById(R.id.tutorial_page1);
        page2 = (ImageView) findViewById(R.id.tutorial_page2);
        page3 = (ImageView) findViewById(R.id.tutorial_page3);
        page4 = (ImageView) findViewById(R.id.tutorial_page4);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutorial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "onPageScrolled: " + position + ", " + positionOffset + ", " + positionOffsetPixels);

//        pageIndicator.setTranslationX(positionOffset*pageIndicator.getWidth());
        switch (position) {
            case 0:
                pageIndicator.setX(page1.getX() + positionOffset * pageIndicator.getWidth());
                break;
            case 1:
                pageIndicator.setX(page2.getX() + positionOffset * pageIndicator.getWidth());
                break;
            case 2:
                pageIndicator.setX(page3.getX() + positionOffset * pageIndicator.getWidth());
                break;
            case 3:
                pageIndicator.setX(page4.getX() + positionOffset * pageIndicator.getWidth());
                break;
        }
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void tutorialOpenFacebookClick(View view) {
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/277665975739089")));
        } catch (Exception e) {
            openBrowser("https://www.facebook.com/vibify");
        }

    }

    public void tutorialOpenXdaClick(View view) {
        openBrowser("http://forum.xda-developers.com/showthread.php?t=2768875");
    }

    public void tutorialOpenTwitterClick(View view) {
        openBrowser("https://twitter.com/BigAndroidDev");
    }

    public void tutorialOpenGoogleplusClick(View view) {
        openBrowser("https://plus.google.com/109742763412505279388/posts");
    }

    public void tutorialOpenCrowdinClick(View view) {
        openBrowser("https://crowdin.net/project/vibify/invite");
    }

    private void openBrowser(String url) {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        /*@Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }*/
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                rootView = inflater.inflate(R.layout.fragment_tutorial1, container, false);
                Switch mSwitch = (Switch) rootView.findViewById(R.id.tutorial1_switch);
                mSwitch.setChecked(NLService.IS_RUNNING);
                mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    }
                });
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                rootView = inflater.inflate(R.layout.fragment_tutorial2, container, false);
                ListView listView = (ListView) rootView.findViewById(R.id.tutorial_app_list);
                ListController listController = new ListController(getActivity(), listView, R.layout.list_row, R.id.app_icon, R.id.app_name);
                listController.loadToList(listController.getApps());
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                rootView = inflater.inflate(R.layout.fragment_tutorial3, container, false);
                ListView mDrawerListView = (ListView) rootView.findViewById(R.id.tutorial_drawer_list_view);
                TutorialNavDrawerUtils navDrawerUtils = new TutorialNavDrawerUtils(getActivity(),
                        mDrawerListView,
                        R.layout.nav_bar_list_row, R.id.nav_drawer_icon, R.id.nav_drawer_name
                );

                mDrawerListView.setAdapter(navDrawerUtils.getSimpleAdapter());
                navDrawerUtils.loadList();
            } else {
                rootView = inflater.inflate(R.layout.fragment_tutorial4, container, false);
                Button testNotification = (Button) rootView.findViewById(R.id.test_notification_button);
                testNotification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Vibify.throwTestNotification(getActivity());
                    }
                });

                Button tutorialFinish = (Button) rootView.findViewById(R.id.tutorial_finish);
                tutorialFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });
            }
            return rootView;
        }
    }

}
