package com.bigandroiddev.vibify.Dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.bigandroiddev.vibify.R;
import com.bigandroiddev.vibify.Vibify;

/**
 * Created by spiros on 11/30/14.
 */
public class SleepTimeSettingDialog extends Activity implements TimePicker.OnTimeChangedListener {

    private static final String TAG = SleepTimeSettingDialog.class.getSimpleName();
    private TimePicker sleepStart, sleepStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sleep_time_setting_dialog);
        sleepStart = (TimePicker) findViewById(R.id.sleep_on_time);
        sleepStop = (TimePicker) findViewById(R.id.sleep_off_time);

        sleepStart.setIs24HourView(DateFormat.is24HourFormat(this));
        sleepStart.setTag(Vibify.SETTING_SLEEP_TIME_ON);
        sleepStop.setIs24HourView(DateFormat.is24HourFormat(this));
        sleepStop.setTag(Vibify.SETTING_SLEEP_TIME_OFF);

        String [] sleepStartTime = Vibify.getSleepTime(Vibify.SETTING_SLEEP_TIME_ON).split(":");
        String [] sleepStopTime = Vibify.getSleepTime(Vibify.SETTING_SLEEP_TIME_OFF).split(":");

        sleepStart.setCurrentHour(Integer.parseInt(sleepStartTime[0]));
        sleepStart.setCurrentMinute(Integer.parseInt(sleepStartTime[1]));

        sleepStop.setCurrentHour(Integer.parseInt(sleepStopTime[0]));
        sleepStop.setCurrentMinute(Integer.parseInt(sleepStopTime[1]));

        sleepStart.setOnTimeChangedListener(this);
        sleepStop.setOnTimeChangedListener(this);
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
        Vibify.setSleepTime(timePicker.getTag().toString(), hour + ":" + minute);
    }
}
