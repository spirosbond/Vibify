package com.bigandroiddev.vibify.Lists;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bigandroiddev.vibify.AboutActivity;
import com.bigandroiddev.vibify.Dialogs.SafeTurnOffSettingDialog;
import com.bigandroiddev.vibify.Dialogs.SleepTimeSettingDialog;
import com.bigandroiddev.vibify.Dialogs.TimesToShowSettingDialog;
import com.bigandroiddev.vibify.MainActivity;
import com.bigandroiddev.vibify.R;
import com.bigandroiddev.vibify.Utils.Tutorial;
import com.bigandroiddev.vibify.Utils.Tutorial2;
import com.bigandroiddev.vibify.Utils.VerificationUtils;
import com.bigandroiddev.vibify.Vibify;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by spiros on 11/30/14.
 */
public class NavDrawerUtils implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = NavDrawerUtils.class.getSimpleName();
    private static String ITEM_KEY = "key", IMAGE_KEY = "image";
    private final ListView listView;
    private Activity activity;
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

    public NavDrawerUtils(Activity ctx, ListView listView, int row_layout_id,
                          int... item_id) {


        activity = ctx;
        this.listView = listView;
        this.listView.setOnItemClickListener(this);
        this.listView.setOnItemLongClickListener(this);
        simpleAdapter = new SimpleAdapter(activity, arrayList, row_layout_id, new String[]{ITEM_KEY, IMAGE_KEY}, new int[]{item_id[0], item_id[1]}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
//				Log.d(TAG, "getView");
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row = inflater.inflate(R.layout.nav_bar_list_row, parent, false);

                if (arrayList.get(position).get(ITEM_KEY).equals(String.valueOf(R.string.about_title)) ||
                        arrayList.get(position).get(ITEM_KEY).equals(String.valueOf(R.string.instructions_title)) ||
                        arrayList.get(position).get(ITEM_KEY).equals(String.valueOf(R.string.support))
                        ) {

                    String pageId = arrayList.get(position).get(ITEM_KEY);
                    String pageName = activity.getResources().getString(Integer.valueOf(pageId));

                    ImageView pageLogo = (ImageView) row.findViewById(R.id.nav_drawer_icon);
                    pageLogo.setTag(pageId);
                    pageLogo.setImageDrawable(activity.getResources().getDrawable(Integer.valueOf(arrayList.get(position).get(IMAGE_KEY))));

                    TextView pageNameTextView = (TextView) row.findViewById(R.id.nav_drawer_name);
                    pageNameTextView.setTag(pageId);
                    pageNameTextView.setText(pageName);

                    ImageView prefs = (ImageView) row.findViewById(R.id.nav_drawer_prefs);
                    prefs.setTag(pageId);
                    prefs.setVisibility(View.GONE);

//                    row.setBackgroundColor(activity.getResources().getColor(R.color.grass_light));
//                    pageNameTextView.setTextColor(activity.getResources().getColor(R.color.dark_dark));

                } else {


                    String settingId = arrayList.get(position).get(ITEM_KEY);
                    String settingName = activity.getResources().getString(Integer.valueOf(settingId));
                    //				View row = super.getView(position, convertView, parent);


                    ImageView settingLogo = (ImageView) row.findViewById(R.id.nav_drawer_icon);
                    settingLogo.setTag(settingId);
                    settingLogo.setImageDrawable(activity.getResources().getDrawable(Integer.valueOf(arrayList.get(position).get(IMAGE_KEY))));

                    TextView settingNameTextView = (TextView) row.findViewById(R.id.nav_drawer_name);
                    settingNameTextView.setTag(settingId);
                    if (settingId.equals(String.valueOf(R.string.times_to_show_title))) {
                        settingNameTextView.setText(activity.getResources().getString(R.string.notify) + " " + activity.getResources().getString(Vibify.getTimesToShowStringId()));
                    } else {
                        settingNameTextView.setText(settingName);
                    }

                    ImageView prefs = (ImageView) row.findViewById(R.id.nav_drawer_prefs);
                    prefs.setTag(settingId);

                    if (settingId.equals(String.valueOf(R.string.times_to_show_title))
                            || settingId.equals(String.valueOf(R.string.quiet_hours_title))
                            || settingId.equals(String.valueOf(R.string.safe_turnoff_title))) {
                        prefs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (view.getTag().equals(String.valueOf(R.string.times_to_show_title))) {
                                    activity.startActivity(new Intent(activity, TimesToShowSettingDialog.class));
                                } else if (view.getTag().equals(String.valueOf(R.string.quiet_hours_title))) {
                                    activity.startActivity(new Intent(activity, SleepTimeSettingDialog.class));
                                } else if (view.getTag().equals(String.valueOf(R.string.safe_turnoff_title))) {
                                    activity.startActivity(new Intent(activity, SafeTurnOffSettingDialog.class));
                                }
                            }
                        });
                    } else {
                        prefs.setVisibility(View.GONE);
                    }

//                    if (Vibify.isSetting(String.valueOf(settingId)))
//                        settingState.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_green_light));
//                    else
//                        settingState.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_red_light));
                }
                return row;
            }
        };

    }

    public SimpleAdapter getSimpleAdapter() {
        return simpleAdapter;
    }

    public void loadList() {

        ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> item = new HashMap<String, String>();

        item.put(ITEM_KEY, String.valueOf(R.string.nav_draw_screen_setting));
        if (Vibify.isSetting(item.get(ITEM_KEY)))
            item.put(IMAGE_KEY, String.valueOf(R.drawable.ic_turn_screen_on));
        else
            item.put(IMAGE_KEY, String.valueOf(R.drawable.ic_turn_screen_on_off));
        mArrayList.add(item);

        item = new HashMap<String, String>();
        item.put(ITEM_KEY, String.valueOf(R.string.nav_draw_battery_setting));
        if (Vibify.isSetting(item.get(ITEM_KEY)))
            item.put(IMAGE_KEY, String.valueOf(R.drawable.ic_low_bat));
        else
            item.put(IMAGE_KEY, String.valueOf(R.drawable.ic_low_bat_off));
        mArrayList.add(item);

        item = new HashMap<String, String>();
        item.put(ITEM_KEY, String.valueOf(R.string.times_to_show_title));
        if (Vibify.isSetting(item.get(ITEM_KEY)))
            item.put(IMAGE_KEY, String.valueOf(R.drawable.ic_notify_times));
        else
            item.put(IMAGE_KEY, String.valueOf(R.drawable.ic_notify_times_off));
        mArrayList.add(item);

        item = new HashMap<String, String>();
        item.put(ITEM_KEY, String.valueOf(R.string.quiet_hours_title));
        if (Vibify.isSetting(item.get(ITEM_KEY)))
            item.put(IMAGE_KEY, String.valueOf(R.drawable.ic_quiet_hours));
        else
            item.put(IMAGE_KEY, String.valueOf(R.drawable.ic_quiet_hours_off));
        mArrayList.add(item);

        item = new HashMap<String, String>();
        item.put(ITEM_KEY, String.valueOf(R.string.safe_turnoff_title));
        if (Vibify.isSetting(item.get(ITEM_KEY)))
            item.put(IMAGE_KEY, String.valueOf(R.drawable.ic_safe_turnoff));
        else
            item.put(IMAGE_KEY, String.valueOf(R.drawable.ic_safe_turnoff_off));
        mArrayList.add(item);

        item = new HashMap<String, String>();
        item.put(ITEM_KEY, String.valueOf(R.string.support));
        item.put(IMAGE_KEY, String.valueOf(R.drawable.ic_support));
        mArrayList.add(item);

        item = new HashMap<String, String>();
        item.put(ITEM_KEY, String.valueOf(R.string.instructions_title));
        item.put(IMAGE_KEY, String.valueOf(R.drawable.ic_instructions));
        mArrayList.add(item);

        item = new HashMap<String, String>();
        item.put(ITEM_KEY, String.valueOf(R.string.about_title));
        item.put(IMAGE_KEY, String.valueOf(R.drawable.ic_about));
        mArrayList.add(item);

        this.arrayList.clear();
        this.arrayList.addAll(mArrayList);

        //		SwingBottomInAnimationAdapter animationAdapter;
        //		animationAdapter = new SwingBottomInAnimationAdapter(simpleAdapter);
        //		animationAdapter.setAbsListView(listView);
        listView.setAdapter(simpleAdapter);

        listView.setVisibility(View.VISIBLE);
        simpleAdapter.notifyDataSetChanged();

    }

    public void notifyAdapter() {
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        ImageView state = (ImageView) view.findViewById(R.id.nav_drawer_state);
        ImageView icon = (ImageView) view.findViewById(R.id.nav_drawer_icon);
        String id = view.findViewById(R.id.nav_drawer_name).getTag().toString();
        Log.d(TAG, "itemClicked: " + id);
        if (id.equals(String.valueOf(R.string.about_title))) {
            activity.startActivity(new Intent(activity, AboutActivity.class));
        } else if (id.equals(String.valueOf(R.string.instructions_title))) {
//            new Tutorial((MainActivity) activity);
            activity.startActivity(new Intent(activity, Tutorial2.class));
        } else if (id.equals(String.valueOf(R.string.support))) {
            VerificationUtils.showBuyPremiumVersionDialog(activity);
        } else {

            Vibify.setSetting(id, !Vibify.isSetting(id));
            if (Vibify.isSetting(id)) {
//                state.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_green_light));
                if (i == 0)
                    icon.setImageDrawable(activity.getResources().getDrawable(R.drawable
                            .ic_turn_screen_on));
                else if (i == 1)
                    icon.setImageDrawable(activity.getResources().getDrawable(R.drawable
                            .ic_low_bat));
                else if (i == 2)
                    icon.setImageDrawable(activity.getResources().getDrawable(R.drawable
                            .ic_notify_times));
                else if (i == 3)
                    icon.setImageDrawable(activity.getResources().getDrawable(R.drawable
                            .ic_quiet_hours));
                else if (i == 4)
                    icon.setImageDrawable(activity.getResources().getDrawable(R.drawable
                            .ic_safe_turnoff));
                Toast.makeText(activity, activity.getResources().getString(Integer.parseInt(id)) + ": " + activity.getResources().getString(R.string.on), Toast.LENGTH_SHORT).show();
            } else {
//                state.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_red_light));
                if (i == 0)
                    icon.setImageDrawable(activity.getResources().getDrawable(R.drawable
                            .ic_turn_screen_on_off));
                else if (i == 1)
                    icon.setImageDrawable(activity.getResources().getDrawable(R.drawable
                            .ic_low_bat_off));
                else if (i == 2)
                    icon.setImageDrawable(activity.getResources().getDrawable(R.drawable
                            .ic_notify_times_off));
                else if (i == 3)
                    icon.setImageDrawable(activity.getResources().getDrawable(R.drawable
                            .ic_quiet_hours_off));
                else if (i == 4)
                    icon.setImageDrawable(activity.getResources().getDrawable(R.drawable
                            .ic_safe_turnoff_off));
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        String id = view.findViewById(R.id.nav_drawer_name).getTag().toString();
        if (id.equals(String.valueOf(R.string.nav_draw_screen_setting))) {
           // activity.startActivity(new Intent(activity, LockscreenActivity.class));

        }
        if (id.equals(String.valueOf(R.string.about_title))) {
            Vibify.throwTestNotification(activity);
        }
        return true;
    }
}
