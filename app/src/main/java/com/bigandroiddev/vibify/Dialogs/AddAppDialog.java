package com.bigandroiddev.vibify.Dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bigandroiddev.vibify.CustomViews.CustomProgressBar;
import com.bigandroiddev.vibify.Lists.AppListItem;
import com.bigandroiddev.vibify.Lists.SortByString;
import com.bigandroiddev.vibify.MainActivity;
import com.bigandroiddev.vibify.R;
import com.bigandroiddev.vibify.Vibify;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingRightInAnimationAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by spiros on 11/30/14.
 */
public class AddAppDialog extends Activity implements AdapterView.OnItemClickListener {

    private static final String TAG = AddAppDialog.class.getSimpleName();
    private static String ITEM_KEY = "key", IMAGE_KEY = "image", APP_NAME_KEY = "appname";
    private Context applicationCtx;
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private SwingRightInAnimationAdapter animationAdapter;
    private ArrayList<HashMap<String, AppListItem>> appList = new ArrayList<HashMap<String, AppListItem>>();
    private ProgressBar loadingApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_app_dialog);

        loadingApps = (ProgressBar) findViewById(R.id.loading_apps);

        this.listView = (ListView) findViewById(R.id.app_list_dialog);
        this.listView.setOnItemClickListener(this);
        simpleAdapter = new SimpleAdapter(this, appList, R.id.dialog_app_list_row, new String[]{ITEM_KEY,
                APP_NAME_KEY}, new int[]{R.id.dialog_app_icon, R.id.dialog_app_name}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Log.d(TAG, "getView");
                final HashMap<String, AppListItem> listItem = (HashMap<String, AppListItem>) getItem(position);
                final AppListItem appListItem = listItem.get(ITEM_KEY);
                View row;
                String packageName = appListItem.getPackageName();
                String appNameStr = appListItem.getName();
                Drawable logo = appListItem.getLogo();

                if (convertView == null) {
                    //Log.d(TAG, "getView");
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(R.layout.dialog_list_row, parent, false);
                } else {
                    row = convertView;
                }

                ImageView appLogo = (ImageView) row.findViewById(R.id.dialog_app_icon);
                appLogo.setTag(appNameStr);
                appLogo.setImageDrawable(logo);

                TextView appName = (TextView) row.findViewById(R.id.dialog_app_name);
                appName.setTag(packageName);
                appName.setText(appNameStr);

                return row;
            }
        };
        new AsyncTask() {

            @Override
            protected void onPreExecute() {
                listView.setVisibility(View.GONE);
                loadingApps.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected ArrayList<HashMap<String, AppListItem>> doInBackground(Object[] objects) {
                return getApps();
            }

            @Override
            protected void onPostExecute(Object apps) {
                super.onPostExecute(apps);
                loadingApps.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                loadToList((ArrayList<HashMap<String, AppListItem>>) apps);
            }

        }.execute();

    }

    public void loadToList(ArrayList<HashMap<String, AppListItem>> arrayList) {
        Log.d(TAG, "loadToList");
        appList.clear();
        appList.addAll(arrayList);

        animationAdapter = new SwingRightInAnimationAdapter(simpleAdapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);

        listView.setVisibility(View.VISIBLE);
        animationAdapter.notifyDataSetChanged();

    }

    public ArrayList<HashMap<String, AppListItem>> getApps() {
        ArrayList<HashMap<String, AppListItem>> arrayList = new ArrayList<HashMap<String, AppListItem>>();

        Intent localIntent = new Intent("android.intent.action.MAIN", null);
        localIntent.addCategory("android.intent.category.LAUNCHER");
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> rInfo = packageManager.queryIntentActivities(localIntent, 1);
        List<ApplicationInfo> packages = new ArrayList<ApplicationInfo>();
        List<String> packageNames = new ArrayList<String>();
        for (ResolveInfo info : rInfo) {
            packages.add(info.activityInfo.applicationInfo);
        }

        for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            //			if (names.contains(packageInfo.packageName)) {
            //				continue;
            //			}
            if (!packageNames.contains(packageInfo.packageName) && !Vibify.isSupported
                    (packageInfo.packageName)) {
//                HashMap<String, String> item = new HashMap<String, String>();
                AppListItem appListItem = new AppListItem();
                HashMap<String, AppListItem> listItem = new HashMap<String, AppListItem>();
                appListItem.setPackageName(packageInfo.packageName);
                appListItem.setName(packageManager.getApplicationLabel(packageInfo).toString());
                appListItem.setLogo(packageInfo.loadIcon(packageManager));
                listItem.put(ITEM_KEY, appListItem);
//                item.put(ITEM_KEY, packageInfo.packageName);

//                item.put(APP_NAME_KEY, packageManager.getApplicationLabel(packageInfo).toString());

//                appLogos.put(packageInfo.packageName, packageInfo.loadIcon(packageManager));
                arrayList.add(listItem);
                packageNames.add(packageInfo.packageName);
            }
        }
//        if (!packageNames.contains("com.android.phone")) {
////            HashMap<String, String> item = new HashMap<String, String>();
//            AppListItem appListItem = new AppListItem();
//            HashMap<String, AppListItem> listItem =new HashMap<String, AppListItem>();
////            item.put(ITEM_KEY, "com.android.phone");
//            appListItem.setPackageName("com.android.phone");
////            item.put(APP_NAME_KEY, applicationCtx.getResources().getString(R.string.phoneAppLabel));
//            appListItem.setName(applicationCtx.getResources().getString(R.string.phoneAppLabel));
////            appLogos.put("com.android.phone", applicationCtx.getResources().getDrawable(R.drawable.ic_phone));
//            appListItem.setLogo(applicationCtx.getResources().getDrawable(R.drawable.ic_phone));
//            listItem.put(ITEM_KEY,appListItem);
//            arrayList.add(listItem);
//            packageNames.add("com.android.phone");
//        }
        Collections.sort(arrayList, new SortByString());
        return arrayList;
    }

    public SwingRightInAnimationAdapter getSimpleAdapter() {
        return animationAdapter;
    }

    public void notifyAdapter() {
        animationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String packageName = view.findViewById(R.id.dialog_app_name).getTag().toString();
        Log.d(TAG, "itemClicked: " + packageName);
        Vibify.addPackage(packageName);
        MainActivity.reloadList = true;
        finish();
//        if (Vibify.isPackage(packageName))
//            appState.setBackgroundColor(applicationCtx.getResources().getColor(android.R.color.holo_green_light));
//        else
//            appState.setBackgroundColor(applicationCtx.getResources().getColor(android.R.color.holo_red_light));
        sendBroadcast(new Intent(getApplicationContext().getPackageName() + "REFRESH_NOTIFICATIONS"));
    }
}
