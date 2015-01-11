package com.bigandroiddev.vibify.Services;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.bigandroiddev.vibify.Vibify;

/**
 * Created by spiros on 11/30/14.
 */
public class ProximitySensorService extends Service implements SensorEventListener {

    private static final String TAG = ProximitySensorService.class.getSimpleName();
    private SensorManager mSensorManager;
    private Sensor mProximity;
    private MovementReceiver movementReceiver;
    public static boolean IS_RUNNING = false;
    private int sleepTime = Vibify.getMovementStillDurationPref();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Proximity Sensor Service started");
        IS_RUNNING = true;
        Vibify.setScreenBlocked(false);
        if (!Vibify.isScreenOff() || !Vibify.isEnabled()) stopSelf();
        sleepTime = Vibify.getMovementStillDurationPref() * 1000 ;
        movementReceiver = new MovementReceiver();
        registerReceiver(movementReceiver, new IntentFilter(getApplicationContext().getPackageName() + "SLEEP_PROXIMITY_SENSOR"));
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IS_RUNNING = false;
        Vibify.setScreenBlocked(false);
        Log.d(TAG, "onDestroy");
        mSensorManager.unregisterListener(this);
        unregisterReceiver(movementReceiver);

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "onTaskRemoved");
        IS_RUNNING = false;
        Vibify.setScreenBlocked(false);
        mSensorManager.unregisterListener(this);
        unregisterReceiver(movementReceiver);
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
    public void onSensorChanged(SensorEvent sensorEvent) {

        float distance = sensorEvent.values[0];
        if(distance<1 && !OrientationService.isStill()) Vibify.setScreenBlocked(true);
        else Vibify.setScreenBlocked(false);
        Log.d(TAG, "onSensorChanged: " + distance);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public class MovementReceiver extends BroadcastReceiver {

        public MovementReceiver() {
            Log.d(TAG, "MovementReceiver created");

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Movement received");
            if (intent.getAction().equals(getApplicationContext().getPackageName() + "SLEEP_PROXIMITY_SENSOR")) {
                startAfter(sleepTime);
                stopSelf();
            }
        }
    }
}

