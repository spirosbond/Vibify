package com.bigandroiddev.vibify;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.bigandroiddev.vibify.Dialogs.AddAppDialog;
import com.bigandroiddev.vibify.Lists.ListController;
import com.bigandroiddev.vibify.Lists.NavDrawerUtils;
import com.bigandroiddev.vibify.Services.NLService;
import com.bigandroiddev.vibify.Utils.Tutorial;
import com.bigandroiddev.vibify.Utils.Tutorial2;
import com.bigandroiddev.vibify.Utils.VerificationUtils;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ListController listController;
    private ListView appList;
    private TextView warning;
    private NavDrawerUtils navDrawerUtils;
    private ListView mDrawerListView;
    public static boolean reloadList = false;
    public static boolean isDrawerOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = getTitle();
        mDrawerTitle = getString(R.string.app_name);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                isDrawerOpen = false;
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(mTitle);
                syncState();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                isDrawerOpen = true;
                invalidateOptionsMenu();
                getSupportActionBar().setTitle(mDrawerTitle);
                syncState();
            }

        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionBarDrawerToggle.syncState();

        VerificationUtils.verifyThroughVersionCode(this);

        warning = (TextView) findViewById(R.id.is_disabled_warning);
        appList = (ListView) findViewById(R.id.app_list);
        listController = new ListController(this, appList, R.layout.list_row, R.id.app_icon, R.id.app_name);
        listController.loadToList(listController.getApps());

        mDrawerListView = (ListView) findViewById(R.id.drawer_list_view);
        navDrawerUtils = new NavDrawerUtils(this,
                mDrawerListView,
                R.layout.nav_bar_list_row, R.id.nav_drawer_icon, R.id.nav_drawer_name
        );

        mDrawerListView.setAdapter(navDrawerUtils.getSimpleAdapter());
        navDrawerUtils.loadList();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
        VerificationUtils.showPleaseRateDialog(this);
        if (Vibify.isFirstRun()) {
//            new Tutorial(this);
            startActivity(new Intent(this, Tutorial2.class));
            Vibify.setFirstRun(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isDrawerOpen = false;
        if (reloadList) {
            listController.loadToList(listController.getApps());
            reloadList = false;
        }
        if (!NLService.IS_RUNNING) {
            Log.d(TAG, "Show Warning msg");
            if (warning.getVisibility() != View.VISIBLE) {
                warning.setVisibility(View.VISIBLE);
                warning.startAnimation(AnimationUtils.loadAnimation(this, R.anim.push_down_in));
            }
        } else {
            Log.d(TAG, "Hide Warning msg");
            if (warning.getVisibility() != View.GONE) {
                warning.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
                warning.setVisibility(View.GONE);
            }
        }
        listController.notifyAdapter();
        navDrawerUtils.loadList();
        invalidateOptionsMenu();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(!isDrawerOpen)   getMenuInflater().inflate(R.menu.main, menu);
        else if(isDrawerOpen)   getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (id) {
            case R.id.enable_service_button:
                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                break;
            case R.id.add_app:
                startActivity(new Intent(this, AddAppDialog.class));
                break;
            case R.id.preferences:
                startActivity(new Intent(this, PreferencesActivity.class));
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view

        if (!NLService.IS_RUNNING)
            menu.findItem(R.id.enable_service_button).setTitle(getResources().getString(R.string.enable));
        else
            menu.findItem(R.id.enable_service_button).setTitle(getResources().getString(R.string.disable));
        return super.onPrepareOptionsMenu(menu);
    }


    public void toggleDrawer(boolean toggle) {
        if (toggle) drawerLayout.openDrawer(Gravity.LEFT);
        else drawerLayout.closeDrawer(Gravity.LEFT);
    }

    public void showPlayStoreDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setTitle("Big Android Dev");
        builder.setMessage(R.string.landing_msg);
        builder.setIcon(R.drawable.ic_play_store);
        builder.setPositiveButton(R.string.playStore, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://search?q=pub:Big Android Dev"));
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Set other dialog properties

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        //		AppSpecificOrientation.ALREADY_SHOWED = true;
        //		AppSpecificOrientation.RETURN_FROM_ABOUT = false;
        dialog.show();
    }

}
