package com.bigandroiddev.vibify.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Vibrator;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.bigandroiddev.vibify.R;
import com.bigandroiddev.vibify.Receivers.ScreenOnOffReceiver;
import com.bigandroiddev.vibify.Vibify;

import java.util.ArrayList;

/**
 * Created by spiros on 11/30/14.
 */
public class NLService extends NotificationListenerService {
    private static final String TAG = NLService.class.getSimpleName();
    public static boolean IS_RUNNING = false;
    private static int numOfNotifications;
    private static ArrayList<StatusBarNotification> notificationArrayList;
    private Vibify vibify;
    private MovementReceiver movementReceiver;
    private ScreenOnOffReceiver screenOnOffReceiver;
    private RefreshNumberOfNotificationsReceiver refreshNumberOfNotificationsReceiver;

    public static boolean isPendingNotification() {
        return numOfNotifications > 0;
    }

    public static void incNumberOfNotifications() {
        numOfNotifications += 1;
        Log.d(TAG, "numOfNotifications inc: " + numOfNotifications);
    }

    public static void decNumberOfNotifications() {
        numOfNotifications -= 1;
        if (numOfNotifications < 0) numOfNotifications = 0;
        Log.d(TAG, "numOfNotifications dec: " + numOfNotifications);
    }

    public static int getNumOfNotifications() {
        return numOfNotifications;
    }

    public static void setNumOfNotifications(int value) {
        numOfNotifications = value;
        Log.d(TAG, "numOfNotifications set: " + numOfNotifications);
    }

    public static ArrayList<StatusBarNotification> getNotificationArrayList() {
        return notificationArrayList;
    }

    public static void setNotificationArrayList(ArrayList<StatusBarNotification> notificationArrayList) {
        NLService.notificationArrayList = notificationArrayList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IS_RUNNING = false;
        Log.d(TAG, "onDestroy");
        Vibify.stopOrientationService();
        Vibify.startProximityService();
        unregisterReceiver(movementReceiver);
        unregisterReceiver(screenOnOffReceiver);
        unregisterReceiver(refreshNumberOfNotificationsReceiver);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        vibify = (Vibify) getApplication();
        IS_RUNNING = true;
        movementReceiver = new MovementReceiver();
        registerReceiver(movementReceiver, new IntentFilter(getApplicationContext().getPackageName() + "MOVEMENT_DETECTED"));

        screenOnOffReceiver = new ScreenOnOffReceiver();
        registerReceiver(screenOnOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(screenOnOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));

        refreshNumberOfNotificationsReceiver = new RefreshNumberOfNotificationsReceiver();
        registerReceiver(refreshNumberOfNotificationsReceiver, new IntentFilter(getApplicationContext().getPackageName() + "REFRESH_NOTIFICATIONS"));

        refreshNumberOfActiveNotifications();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        IS_RUNNING = true;
        return START_STICKY;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        Log.d(TAG, "onNotificationPosted: getPackage: " + packageName + " ongoing: " + sbn.isOngoing());
        Vibify.setTimesShown(0);
        refreshNumberOfActiveNotifications();
        if (NLService.isPendingNotification() && Vibify.isScreenOff()) {
            Vibify.startOrientationService();
            Vibify.startProximityService();
            //			Vibify.registerRestartMeAfterReceiver();
        } else {
            Vibify.stopOrientationService();
            Vibify.stopProximityService();
            //			Vibify.unregisterRestartMeAfterReceiver();
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        Log.d(TAG, "onNotificationRemoved: getPackage: " + packageName + " ongoing: " + sbn.isOngoing());

        refreshNumberOfActiveNotifications();
        //		if (NLService.isPendingNotification() && Vibify.isScreenOff() && !OrientationService.IS_RUNNING){
        //			Vibify.startOrientationService();
        //			Vibify.registerRestartMeAfterReceiver();
        //		}
        //		else{
        //			Vibify.stopOrientationService();
        //			Vibify.unregisterRestartMeAfterReceiver();
        //		}
        if (!NLService.isPendingNotification()) {
            Vibify.stopOrientationService();
            Vibify.stopProximityService();
        }
    }

    public int getNumOfActiveNotifications() {
        int activeNotifications = 0;

        ArrayList<StatusBarNotification> notificationList = new ArrayList<StatusBarNotification>();
        try {
            for (StatusBarNotification sbn :  NLService.this.getActiveNotifications()) {
                Log.d(TAG, "packageName: " + sbn.getPackageName() + " ongoing: " + sbn.isOngoing());
                if (Vibify.isPackage(sbn.getPackageName()) && !sbn.isOngoing()) {
                    activeNotifications++;
                    notificationList.add(sbn);
                }
            }
            setNotificationArrayList(notificationList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "number of active notifications:" + activeNotifications);
        return activeNotifications;
    }

    public void refreshNumberOfActiveNotifications() {
        setNumOfNotifications(getNumOfActiveNotifications());
    }

    //	@Override
    //	public IBinder onBind(Intent intent) {
    //		Log.d(TAG,"onBind");
    //		return null;
    //	}

    public class MovementReceiver extends BroadcastReceiver {
        public Vibrator vibrator;

        public MovementReceiver() {
            Log.d(TAG, "MovementReceiver created");
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Movement received");

            try {
                if (intent.getAction().equals(getApplicationContext().getPackageName() + "MOVEMENT_DETECTED") && isPendingNotification() /*Trivial*/ && Vibify.isEnabled()) {
                    if (Vibify.isSetting(String.valueOf(R.string.nav_draw_screen_setting))) vibify.turnScreenOn();
                    Vibify.incTimesShown();
                    vibrator.vibrate(Vibify.getVibrationDurationPref());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class RefreshNumberOfNotificationsReceiver extends BroadcastReceiver {

        public RefreshNumberOfNotificationsReceiver() {
            Log.d(TAG, "RefreshNumberOfNotificationsReceiver created");
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Refreshing notifications");
            if (intent.getAction().equals(getApplicationContext().getPackageName() + "REFRESH_NOTIFICATIONS")) {
                refreshNumberOfActiveNotifications();
            }
        }
    }

}

