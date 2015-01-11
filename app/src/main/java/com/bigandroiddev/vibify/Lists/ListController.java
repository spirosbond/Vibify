package com.bigandroiddev.vibify.Lists;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bigandroiddev.vibify.R;
import com.bigandroiddev.vibify.Vibify;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by spiros on 11/30/14.
 */
public class ListController implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = ListController.class.getSimpleName();
    private static String ITEM_KEY = "key", IMAGE_KEY = "image", APP_NAME_KEY = "appname";
    private SwingBottomInAnimationAdapter animationAdapter;
    private Activity activity;
    private ListView listView;
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String, AppListItem>> appList = new ArrayList<HashMap<String, AppListItem>>();
//    private HashMap<String, Drawable> appLogos = new HashMap<String, Drawable>();

    public ListController(Activity ctx, ListView listView, int row_layout_id, int... item_id) {

        activity = ctx;
        this.listView = listView;
        this.listView.setOnItemClickListener(this);
        this.listView.setOnItemLongClickListener(this);
        simpleAdapter = new SimpleAdapter(activity, appList, row_layout_id, new String[]{ITEM_KEY, APP_NAME_KEY}, new int[]{item_id[0], item_id[1]}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
//				Log.d(TAG, "getView");
                final HashMap<String, AppListItem> listItem = (HashMap<String, AppListItem>) getItem(position);
                final AppListItem appListItem = listItem.get(ITEM_KEY);
                View row;
                String packageName = appListItem.getPackageName();
                String appNameStr = appListItem.getName();
                Drawable logo = appListItem.getLogo();

                if (convertView == null) {
                    //Log.d(TAG, "getView");
                    LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(R.layout.list_row, parent, false);
                } else {
                    row = convertView;
                }

                ImageView appLogo = (ImageView) row.findViewById(R.id.app_icon);
                appLogo.setTag(appNameStr);
                appLogo.setImageDrawable(logo);

                TextView appName = (TextView) row.findViewById(R.id.app_name);
                appName.setTag(packageName);
                appName.setText(appNameStr);

                ImageView appState = (ImageView) row.findViewById(R.id.app_state);
                appState.setTag(packageName);
                if (Vibify.isPackage(packageName))
                    appState.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_green_light));
                else
                    appState.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_red_light));

                return row;
            }
        };

    }

    public void loadToList(ArrayList<HashMap<String, AppListItem>> arrayList) {
        Log.d(TAG, "loadToList");
        appList.clear();
        appList.addAll(arrayList);

        animationAdapter = new SwingBottomInAnimationAdapter(simpleAdapter);
        animationAdapter.setAbsListView(listView);
        listView.setAdapter(animationAdapter);

        listView.setVisibility(View.VISIBLE);
        animationAdapter.notifyDataSetChanged();

    }

    public ArrayList<HashMap<String, AppListItem>> getApps() {
        ArrayList<HashMap<String, AppListItem>> arrayList = new ArrayList<HashMap<String, AppListItem>>();

        Intent localIntent = new Intent("android.intent.action.MAIN", null);
        localIntent.addCategory("android.intent.category.LAUNCHER");
        PackageManager packageManager = activity.getPackageManager();
        List<ResolveInfo> rInfo = packageManager.queryIntentActivities(localIntent, 1);
        List<ApplicationInfo> packages = new ArrayList<ApplicationInfo>();
        List<String> packageNames = new ArrayList<String>();
        for (ResolveInfo info : rInfo) {
            packages.add(info.activityInfo.applicationInfo);
        }

        for (ApplicationInfo packageInfo : packages) {
//			Log.d(TAG, "Installed package :" + packageInfo.packageName);
            //			if (names.contains(packageInfo.packageName)) {
            //				continue;
            //			}
            if (!packageNames.contains(packageInfo.packageName) && Vibify.isSupported(packageInfo.packageName)) {
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
        if (!packageNames.contains("com.android.phone")) {
//            HashMap<String, String> item = new HashMap<String, String>();
            AppListItem appListItem = new AppListItem();
            HashMap<String, AppListItem> listItem = new HashMap<String, AppListItem>();
//            item.put(ITEM_KEY, "com.android.phone");
            appListItem.setPackageName("com.android.phone");
//            item.put(APP_NAME_KEY, activity.getResources().getString(R.string.phoneAppLabel));
            appListItem.setName(activity.getResources().getString(R.string.phoneAppLabel));
//            appLogos.put("com.android.phone", activity.getResources().getDrawable(R.drawable.ic_phone));
            appListItem.setLogo(activity.getResources().getDrawable(R.drawable.ic_phone));
            listItem.put(ITEM_KEY, appListItem);
            arrayList.add(listItem);
            packageNames.add("com.android.phone");
        }
        Collections.sort(arrayList, new SortByString());
        return arrayList;
    }

    public SwingBottomInAnimationAdapter getSimpleAdapter() {
        return animationAdapter;
    }

    public void notifyAdapter() {
        getSimpleAdapter().notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        ImageView appState = (ImageView) view.findViewById(R.id.app_state);
        String packageName = view.findViewById(R.id.app_name).getTag().toString();
        Log.d(TAG, "itemClicked: " + packageName);

        Vibify.setPackage(packageName, !Vibify.isPackage(packageName));
        if (Vibify.isPackage(packageName)) {
            appState.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.half_round_inv));
            appState.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_green_light));
        } else {
            appState.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.half_round));
            appState.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_red_light));
        }

        activity.sendBroadcast(new Intent(activity.getApplicationContext().getPackageName() + "REFRESH_NOTIFICATIONS"));
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> adapterView, final View view, final int i, long l) {
        ((Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
        final String packageName = view.findViewById(R.id.app_name).getTag().toString();
        String appName = view.findViewById(R.id.app_icon).getTag().toString();

        Log.d(TAG, "itemLongClickedClicked: " + packageName + " " + i + "," + l);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // Add the buttons
        builder.setTitle(activity.getResources().getString(R.string.remove_app));
        builder.setMessage(activity.getResources().getString(R.string.remove_app_msg) + " " + appName + " from the list?");
        builder.setIcon(R.drawable.ic_warning);
        builder.setPositiveButton(R.string.do_it, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Vibify.removePackage(packageName);
                appList.remove(i);
                Animation anim = AnimationUtils.loadAnimation(activity, R.anim.zoom_out);
                anim.setAnimationListener(new MyListItemAnimationListener(view, getSimpleAdapter(), activity.getApplicationContext()));
                view.startAnimation(anim);
//                notifyAdapter();
//				loadToList(getApps());
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
        return true;
    }
}
