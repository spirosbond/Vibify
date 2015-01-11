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
public class SafeTurnOffSettingDialog extends Activity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = SafeTurnOffSettingDialog.class.getSimpleName();
    private Spinner safeTurnOffValue;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_turnoff_setting_dialog);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        safeTurnOffValue = (Spinner) findViewById(R.id.safe_turnoff_value);

        safeTurnOffValue.setTag(Vibify.SETTING_SAFE_TURNOFF_DELAY);
        safeTurnOffValue.setSelection(getSafeTurnOffSpinnerPosition(safeTurnOffValue.getTag().toString()));
        safeTurnOffValue.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Vibify.setSafeTurnoffDelayPref(getSafeTurnOffSpinnerValue(i));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private String getSafeTurnOffSpinnerValue(int position) {
        String value;
        switch (position) {
            case 0:
                value = getResources().getString(R.string.safe_turnoff_delay_value_05);
                break;
            case 1:
                value = getResources().getString(R.string.safe_turnoff_delay_value_1);
                break;
            case 2:
                value = getResources().getString(R.string.safe_turnoff_delay_value_2);
                break;
            default:
                value = getResources().getString(R.string.times_to_show_value_1);
                break;
        }
        return value;
    }

    private int getSafeTurnOffSpinnerPosition(String key) {
        int position;
        switch (Vibify.getSafeTurnoffDelayPref()) {
            case 1800:
                position = 0;
                break;
            case 3600:
                position = 1;
                break;
            case 7200:
                position = 2;
                break;
            default:
                position = 1;
        }
        return position;
    }
}

