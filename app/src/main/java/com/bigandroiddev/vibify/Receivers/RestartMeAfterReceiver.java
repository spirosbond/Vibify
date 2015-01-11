package com.bigandroiddev.vibify.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bigandroiddev.vibify.Vibify;

/**
 * Created by spiros on 11/30/14.
 */
public class RestartMeAfterReceiver extends BroadcastReceiver {

    private static String TAG = RestartMeAfterReceiver.class.getSimpleName();
    private long timeThreshold = 7000;

    public RestartMeAfterReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        if (intent.getAction().equals(context.getPackageName() + "RESTART_ME")) {
            long now = System.currentTimeMillis();
            while (System.currentTimeMillis() - now < timeThreshold) {
                continue;
            }
            Log.d(TAG, "timeThreshold passed: " + timeThreshold);
            Vibify.startOrientationService();
            Vibify.startProximityService();

        }

    }

}

