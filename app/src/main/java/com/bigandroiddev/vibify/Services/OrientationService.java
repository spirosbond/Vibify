package com.bigandroiddev.vibify.Services;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import com.bigandroiddev.vibify.Vibify;

/**
 * Created by spiros on 11/30/14.
 */
public class OrientationService extends Service implements SensorEventListener {

    public static boolean IS_RUNNING = false;
    private static boolean still = false;
    private static PowerManager.WakeLock wl;
    private static PowerManager pm;
    private final String TAG = this.getClass().getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private long now = 0;
    private long timeDiff = 0;
    private long previous = 0;
    private float forceThreshold = 3.0f;
    private int timeThreshold = Vibify.getStandStillThresholdPref() * 1000;
    private int sleepTime = Vibify.getMovementStillDurationPref() * 1000;
    private float x = 0;
    private float y = 0;
    private float z = 0;
    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;
    private float force = 0;

    public static boolean isStill() {
        return still;
    }

    public static void setStill(boolean still) {
        OrientationService.still = still;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (Vibify.isScreenBlocked()) {
            Log.d(TAG, "Screen Blocked");
            startAfter(sleepTime);
            stopSelf();
        }

        x = sensorEvent.values[0];
        y = sensorEvent.values[1];
        z = sensorEvent.values[2];
        now = System.currentTimeMillis();

        if (previous == 0) {
            previous = now;
            lastX = x;
            lastY = y;
            lastZ = z;

        } else {
            Log.d(TAG, "timeDiff: " + timeDiff);
            timeDiff = now - previous;
            stopIfSafeTurnOffDelay(timeDiff);
            //			timeDiff = timeDiff + Long.MAX_VALUE - 10000;
            force = Math.abs(x + y + z - lastX - lastY - lastZ);

            if (Float.compare(force, forceThreshold) > 0) {
                Log.d(TAG, "Movement Detected: (" + x + ", " + y + ", " + z + ")" + " force: " + force + /*" at: " + now +*/ " after: " + timeDiff);
                previous = now;
                lastX = x;
                lastY = y;
                lastZ = z;
                if (isStill()) {
                    sendBroadcast(new Intent(getApplicationContext().getPackageName() + "MOVEMENT_DETECTED"));


                }
                setStill(false);
                //								if (wl.isHeld()) {
                //									Log.d(TAG, "Wakelock released: " + timeDiff);
                //									wl.release();
                //								}
                //				sendBroadcast(new Intent("com.spirosbond.RESTART_ME"));
                sendBroadcast(new Intent(getApplicationContext().getPackageName() + "SLEEP_PROXIMITY_SENSOR"));
                startAfter(sleepTime);
                stopSelf();
            } else if (timeDiff > timeThreshold) {
                //				Log.d(TAG, "Device is still: " + timeDiff);
                //				if (!wl.isHeld()) {
                //					Log.d(TAG, "Wakelock acquired: " + timeDiff);
                //					wl.acquire();
                //				}
                setStill(true);
            }

        }
    }

    private void stopIfSafeTurnOffDelay(long waitingForMillis) {
        if (!Vibify.isSafeTurnoffDelayPref()) return;
        int safeTurnOffDelaySec = Vibify.getSafeTurnoffDelayPref();
        Log.d(TAG, "waitingForSec: " + waitingForMillis / 1000 + " safeTurnOffPrefSec: " + safeTurnOffDelaySec);
        if ((waitingForMillis / 1000) > safeTurnOffDelaySec) stopSelf();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        if (!wl.isHeld()) wl.acquire();

        IS_RUNNING = true;
        if (!Vibify.isScreenOff() || !Vibify.isEnabled()) stopSelf();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        timeThreshold = Vibify.getStandStillThresholdPref() * 1000;
        sleepTime = Vibify.getMovementStillDurationPref() * 1000;
        setStill(false);
        IS_RUNNING = true;
        previous = 0;

        Log.d(TAG, "onStartCommand timeThreshold: " + timeThreshold + " sleepTime: " + sleepTime);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IS_RUNNING = false;
        Log.d(TAG, "onDestroy");
        mSensorManager.unregisterListener(this);
        IS_RUNNING = false;
        if (wl.isHeld()) wl.release();
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "onTaskRemoved");
        IS_RUNNING = false;
        mSensorManager.unregisterListener(this);
        startAfter(2000);
    }

    public void startAfter(long millis) {
        Intent restartService = new Intent(getApplicationContext(), this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(getApplicationContext(), 1, restartService, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + millis, restartServicePI);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
