package com.bigandroiddev.vibify.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bigandroiddev.vibify.Vibify;

/**
 * Created by spiros on 11/30/14.
 */
public class BatteryLevelReceiver extends BroadcastReceiver {
    private static final String TAG = BatteryLevelReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "intent action: " + intent.getAction());

        if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) Vibify.setLowBat(true);
        else Vibify.setLowBat(false);

    }
}
