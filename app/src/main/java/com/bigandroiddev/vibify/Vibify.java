package com.bigandroiddev.vibify;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.format.Time;
import android.util.Log;

import com.bigandroiddev.vibify.Receivers.RestartMeAfterReceiver;
import com.bigandroiddev.vibify.Services.OrientationService;
import com.bigandroiddev.vibify.Services.ProximitySensorService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by spiros on 11/30/14.
 */
public class Vibify extends Application {

    public static final String SETTING_SCREEN = "setting_screen", SETTING_LOW_BAT = "setting_low_bat", SETTING_FIRST_RUN = "first_run", SETTING_SUPPORTED_PACK = "supported_pack", SETTING_VIBRATION_DUR = "vibration_duration_pref", SETTING_STAND_STILL_THRESHOLD = "stand_still_thresh", SETTING_MOVEMENT_STILL_DUR = "movement_sleep_duration", SETTING_TIMES_TO_SHOW = "times_to_show", SETTING_SLEEP_TIME_ON = "sleep_time_on", SETTING_SLEEP_TIME_OFF = "sleep_time_off", SETTING_PROXIMITY_SENSOR = "proximity_sensor_check", SETTING_PLEASE_RATE = "please_rate", SETTING_SAFE_TURNOFF_DELAY = "safe_turnoff_delay";
    private static final String TAG = Vibify.class.getSimpleName();
    private static final boolean OVERRIDE = true, DONT_OVERRIDE = false;
    private static final String supportPackages = ",com.android.phone,com.android.mms,com.viber.voip,com.skype.raider,com.google.android.talk,com.google.android.gm,com.facebook.katana,com.whatsapp,com.google.android.apps.plus,mikado.bizcalpro,netgenius.bizcal,com.ryosoftware.contactdatesnotifier,com.twitter.android,com.fsck.k9,com.onegravity.k10.pro2,de.gmx.mobile.android.mail,com.quoord.tapatalkHD,com.quoord.tapatalkpro.activity,com.facebook.orca,com.joelapenna.foursquared,com.snapchat.android,com.instagram.android,com.handcent.nextsms,kik.android,jp.naver.line.android,com.imo.android.imoim,de.shapeservices.impluslite,de.shapeservices.implusfull,com.bbm,com.gvoip,com.snrblabs.grooveip,com.google.android.apps.googlevoice,com.jb.gosms,com.moplus.gvphone,com.skymobius.vtok,com.google.android.calendar,com.android.calendar,";
    private static int timesShown = 0;
    private static String appPackageName;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    private static Intent orientationServiceIntent, proximityServiceIntent;
    private static RestartMeAfterReceiver restartMeAfterReceiver;
    private static Context ctx;
    private static boolean screenOff = false, screenBlocked = false;

    public static boolean isScreenOff() {
        Log.d(TAG, "isScreenOff" + ": " + screenOff);
        return screenOff;
    }

    public static void setScreenOff(boolean screenOff) {
        Log.d(TAG, "setScreenOff" + ": " + screenOff);
        Vibify.screenOff = screenOff;
    }

    public static boolean isEnabled() {
        boolean enabled = true;

        if (isSetting(String.valueOf(R.string.nav_draw_battery_setting)) && isLowBat()) {
            enabled = false;
        }

        if (isSetting(String.valueOf(R.string.times_to_show_title)) && getTimesToShowPref() != 0 && (getTimesToShowPref() - getTimesShown()) == 0) {
            enabled = false;
        }
        if (isSetting(String.valueOf(R.string.quiet_hours_title)) && isSleepTime(getSleepTime(SETTING_SLEEP_TIME_ON), getSleepTime(SETTING_SLEEP_TIME_OFF))) {
            enabled = false;
        }
        Log.d(TAG, "isEnabled" + ": " + enabled);
        return enabled;
    }

    public static boolean isScreenBlocked() {
        Log.d(TAG, "isScreenBlocked" + ": " + screenBlocked);
        return screenBlocked;
    }

    public static void setScreenBlocked(boolean screenBlocked) {
        Log.d(TAG, "setScreenBlocked" + ": " + screenBlocked);
        Vibify.screenBlocked = screenBlocked;
    }

    public static void setPackage(String packageName, boolean value) {
        Log.d(TAG, "package: " + packageName + " set: " + value);
        editor.putBoolean(packageName, value);
        editor.apply();
    }

    public static int countSelections() {
        String[] packages = getSupportedPackages().split(",");
        int count = 0;
        for (String p : packages) {
            if (isPackage(p)) count++;
        }
        Log.d(TAG, "countSelections" + ": " + count);
        return count;
    }

    public static String getSupportedPackages() {
        return prefs.getString(SETTING_SUPPORTED_PACK, supportPackages);
    }

    public static boolean isSupported(String packageName) {
        Log.d(TAG, "isSupported" + ": " + packageName + ", " + getSupportedPackages().contains("," + packageName + ","));
        return getSupportedPackages().contains("," + packageName + ",");
    }

    public static void addPackage(String packageName) {
        Log.d(TAG, "addPackage" + ": " + packageName);
        editor.putString(SETTING_SUPPORTED_PACK, getSupportedPackages().concat(packageName + ","));
        editor.apply();
    }

    public static void removePackage(String packageName) {
        Log.d(TAG, "removePackage" + ": " + packageName);
        setPackage(packageName, false);
        String newSupportedPackages = getSupportedPackages().replace("," + packageName + ",", ",");
        editor.putString(SETTING_SUPPORTED_PACK, newSupportedPackages);
        editor.apply();
    }

    public static boolean isPackage(String packageName) {
        Log.d(TAG, "isPackage" + ": " + packageName + ", " + (prefs.getBoolean(packageName, false) | packageName.contains(appPackageName)));
        return prefs.getBoolean(packageName, false) | packageName.contains(appPackageName);
    }

    public static void setSetting(String settingID, boolean value) {
        Log.d(TAG, "setSetting: " + settingID + " set: " + value);
        editor.putBoolean(settingID, value);
        editor.apply();
    }

    public static boolean isSetting(String settingID) {
        Log.d(TAG, "isSetting" + ": " + settingID + ", " + prefs.getBoolean(settingID, false));
        return prefs.getBoolean(settingID, false);
    }

    public static void registerRestartMeAfterReceiver() {
        ctx.registerReceiver(restartMeAfterReceiver, new IntentFilter(ctx.getPackageName() + "RESTART_ME"));
    }

    public static void unregisterRestartMeAfterReceiver() {
        try {
            ctx.unregisterReceiver(restartMeAfterReceiver);
        } catch (Exception e) {
            Log.w(TAG, "Couldn't unregister receiver. Not registered?");
        }
    }

    public static void startOrientationService() {
        if (!OrientationService.IS_RUNNING) ctx.startService(orientationServiceIntent);
    }

    public static void stopOrientationService() {
        ctx.stopService(orientationServiceIntent);
    }

    public static boolean getProximitySetting() {
        Log.d(TAG, "getProximitySetting" + ": " + SETTING_PROXIMITY_SENSOR + ", " + prefs.getBoolean(SETTING_PROXIMITY_SENSOR, false));
        return prefs.getBoolean(SETTING_PROXIMITY_SENSOR, false);
    }

    public static void startProximityService() {
        if (getProximitySetting() && !ProximitySensorService.IS_RUNNING)
            ctx.startService(proximityServiceIntent);
    }

    public static void stopProximityService() {
        ctx.stopService(proximityServiceIntent);
    }

    public static boolean isLowBat() {
        Log.d(TAG, "isLowBat" + ": " + prefs.getBoolean("low_bat", false));
        return prefs.getBoolean("low_bat", false);
    }

    public static void setLowBat(boolean lowBat) {
        Log.d(TAG, "setLowBat" + ": " + lowBat);
        editor.putBoolean("low_bat", lowBat);
        editor.apply();
    }

    public static boolean isFirstRun() {
        Log.d(TAG, "isFirstRun" + ": " + prefs.getBoolean(SETTING_FIRST_RUN, true));
        return prefs.getBoolean(SETTING_FIRST_RUN, true);
    }

    public static void setFirstRun(boolean b) {
        Log.d(TAG, "setFirstRun" + ": " + b);
        editor.putBoolean(SETTING_FIRST_RUN, b);
        editor.apply();

    }

    public static int getTimesShown() {
        Log.d(TAG, "getTimesShown" + ": " + timesShown);
        return timesShown;
    }

    public static void setTimesShown(int timesShown) {
        Log.d(TAG, "setTimesShown" + ": " + timesShown);
        Vibify.timesShown = timesShown;
    }

    public static int incTimesShown() {
        timesShown = timesShown + 1;
        Log.d(TAG, "incTimesShown" + ": " + timesShown);
        return timesShown;

    }

    public static int getVibrationDurationPref() {
        Log.d(TAG, "getVibrationDurationPref" + ": " + Integer.parseInt(prefs.getString(SETTING_VIBRATION_DUR, "150")));
        return Integer.parseInt(prefs.getString(SETTING_VIBRATION_DUR, "150"));
    }

    public static int getStandStillThresholdPref() {
        Log.d(TAG, "getStandStillThresholdPref" + ": " + Integer.parseInt(prefs.getString(SETTING_STAND_STILL_THRESHOLD, "3")));
        return Integer.parseInt(prefs.getString(SETTING_STAND_STILL_THRESHOLD, "3"));
    }

    public static int getMovementStillDurationPref() {
        Log.d(TAG, "getMovementStillDurationPref" + ": " + Integer.parseInt(prefs.getString(SETTING_MOVEMENT_STILL_DUR, "7")));
        return Integer.parseInt(prefs.getString(SETTING_MOVEMENT_STILL_DUR, "7"));
    }

    public static int getSafeTurnoffDelayPref() {
        Log.d(TAG, "getSafeTurnoffDelayPref" + ": " + Integer.parseInt(prefs.getString(SETTING_SAFE_TURNOFF_DELAY, "3600")));
        return Integer.parseInt(prefs.getString(SETTING_SAFE_TURNOFF_DELAY, "3600"));
    }

    public static boolean isSafeTurnoffDelayPref() {
        Log.d(TAG, "isSafeTurnoffDelayPref" + ": " + isSetting(String.valueOf(R.string.safe_turnoff_title)));
        return isSetting(String.valueOf(R.string.safe_turnoff_title));
    }

    public static void setSafeTurnoffDelayPref(String value) {
        Log.d(TAG, "setSafeTurnoffDelayPref" + ": " + value);
        editor.putString(SETTING_SAFE_TURNOFF_DELAY, value);
        editor.apply();
    }

    public static int getTimesToShowPref() {
        Log.d(TAG, "getTimesToShowPref: " + Integer.parseInt(prefs.getString(SETTING_TIMES_TO_SHOW, "3")));
        return Integer.parseInt(prefs.getString(SETTING_TIMES_TO_SHOW, "3"));
    }

    public static void setTimesToShowPref(String times) {
        Log.d(TAG, "setTimesToShowPref" + ": " + times);
        editor.putString(SETTING_TIMES_TO_SHOW, times);
        editor.apply();
    }

    public static int getTimesToShowStringId() {
        int timesToNotifyString;
        switch (getTimesToShowPref()) {
            case 1:
                timesToNotifyString = R.string.times_to_show_1;
                break;
            case 2:
                timesToNotifyString = R.string.times_to_show_2;
                break;
            case 3:
                timesToNotifyString = R.string.times_to_show_3;
                break;
            case 5:
                timesToNotifyString = R.string.times_to_show_5;
                break;
            default:
                timesToNotifyString = R.string.times_to_show_3;
        }
        Log.d(TAG, "getTimesToShowStringId" + ": " + timesToNotifyString);
        return timesToNotifyString;
    }

    public static void setSleepTime(String key, String time) {
        Log.d(TAG, "setSleepTime" + ": " + key + ", " + time);
        editor.putString(key, time);
        editor.apply();
    }

    public static String getSleepTime(String key) {
        Log.d(TAG, "getSleepTime" + ": " + key + ", " + prefs.getString(key, "00:00"));
        return prefs.getString(key, "00:00");
    }

    private static boolean isSleepTime(String sleepTimeOn, String sleepTimeOff) {

        Time nowTime = new Time();
        nowTime.setToNow();

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
        try {
            Date sleepOn = parser.parse(sleepTimeOn);
            Date sleepOff = parser.parse(sleepTimeOff);
            Date now = parser.parse(nowTime.hour + ":" + nowTime.minute);
//			Date now = parser.parse("00:35");
            if (sleepOff.before(sleepOn)) {
                sleepOff.setDate(2);
                if (now.before(sleepOff)) now.setDate(2);
            }
            Log.d("isSleepTime", sleepOn + " " + sleepOff + " " + now + " " + (now.after(sleepOn) && now.before(sleepOff)));
            return now.after(sleepOn) && now.before(sleepOff);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }

    public static void initTheme(Activity activity) {
    //    FlatUI.initDefaultValues(activity);
      //  FlatUI.setDefaultTheme(FlatUI.GRASS);
        //activity.getActionBar().setBackgroundDrawable(FlatUI.getActionBarDrawable(activity, FlatUI.GRASS, false));
    }

    public static void throwTestNotification(Activity activity) {
        NotificationManager nManager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncomp = new NotificationCompat.Builder(activity);
        ncomp.setContentTitle("My Notification");
        ncomp.setContentText("Notification Listener Service Example");
        ncomp.setTicker("Notification Listener Service Example");
        ncomp.setSmallIcon(R.drawable.ic_launcher);
        ncomp.setAutoCancel(true);
        nManager.notify((int) System.currentTimeMillis(), ncomp.build());
    }

    public static boolean isPleaseRateClicked() {
        Log.d(TAG, "isPleaseRateClicked" + ": " + prefs.getBoolean(SETTING_PLEASE_RATE, false));
        return prefs.getBoolean(SETTING_PLEASE_RATE, false);
    }

    public static void setPleaseRateClicked(boolean v) {
        Log.d(TAG, "setPleaseRateClicked" + ": " + v);
        editor.putBoolean(SETTING_PLEASE_RATE, v);
        editor.apply();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        orientationServiceIntent = new Intent(this, OrientationService.class);
        proximityServiceIntent = new Intent(this, ProximitySensorService.class);
        //		restartMeAfterReceiver = new RestartMeAfterReceiver();
        ctx = this;
        appPackageName = this.getPackageName();
//		VerificationUtils.createTrialStart(getApplicationContext(), DONT_OVERRIDE);
//		VerificationUtils.createTrialEnd(getApplicationContext(), 7, OVERRIDE);
//		VerificationUtils.checkIfTrialEnded(getApplicationContext());

    }

    public boolean turnScreenOn() {
        Log.d(TAG, "turnScreenOn");
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        		pm.wakeUp(0);

        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, TAG);
        wl.acquire(1);
        //		wl.release();

        return true;

    }
}
