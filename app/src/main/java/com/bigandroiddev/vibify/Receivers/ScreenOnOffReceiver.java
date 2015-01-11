package com.bigandroiddev.vibify.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bigandroiddev.vibify.Services.NLService;
import com.bigandroiddev.vibify.Vibify;

/**
 * Created by spiros on 11/30/14.
 */
public class ScreenOnOffReceiver extends BroadcastReceiver {

    private static final String TAG = ScreenOnOffReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceived");
        if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
            Log.d(TAG, "SCREEN_OFF");
            Vibify.setScreenOff(true);
            if (NLService.isPendingNotification()) {
                Vibify.startOrientationService();
                Vibify.startProximityService();
                //				Vibify.registerRestartMeAfterReceiver();
            } else if (!NLService.isPendingNotification()) {
                Vibify.stopOrientationService();
                Vibify.stopProximityService();
                //				Vibify.unregisterRestartMeAfterReceiver();
            }

        } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
            Log.d(TAG, "SCREEN_ON");
            Vibify.setScreenOff(false);
            Vibify.stopOrientationService();
            Vibify.stopProximityService();
            //			Vibify.unregisterRestartMeAfterReceiver();
        }
    }
}
