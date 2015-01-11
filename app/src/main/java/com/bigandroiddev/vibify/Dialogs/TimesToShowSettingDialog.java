package com.bigandroiddev.vibify.Dialogs;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.bigandroiddev.vibify.R;
import com.bigandroiddev.vibify.Vibify;

/**
 * Created by spiros on 11/30/14.
 */
public class TimesToShowSettingDialog extends Activity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = TimesToShowSettingDialog.class.getSimpleName();
    private Spinner timesToShowValue;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.times_to_show_setting_dialog);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        timesToShowValue = (Spinner) findViewById(R.id.times_to_show_value);

        timesToShowValue.setTag(Vibify.SETTING_TIMES_TO_SHOW);
        timesToShowValue.setSelection(getTimesToShowSpinnerPosition(timesToShowValue.getTag().toString()));
        timesToShowValue.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Vibify.setTimesToShowPref(getTimesToShowSpinnerValue(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private String getTimesToShowSpinnerValue(int position) {
        String value;
        switch (position) {
            case 0:
                value = getResources().getString(R.string.times_to_show_value_1);
                break;
            case 1:
                value = getResources().getString(R.string.times_to_show_value_2);
                break;
            case 2:
                value = getResources().getString(R.string.times_to_show_value_3);
                break;
            case 3:
                value = getResources().getString(R.string.times_to_show_value_5);
                break;
            default:
                value = getResources().getString(R.string.times_to_show_value_3);
                break;
        }
        return value;
    }

    private int getTimesToShowSpinnerPosition(String key) {
        int position;
        switch (Vibify.getTimesToShowPref()) {
            case 1:
                position = 0;
                break;
            case 2:
                position = 1;
                break;
            case 3:
                position = 2;
                break;
            case 5:
                position = 3;
                break;
            default:
                position = 2;
        }
        return position;
    }
}
