package com.bigandroiddev.vibify;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * Created by spiros on 11/30/14.
 */
public class PreferencesActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private static final String TAG = PreferencesActivity.class.getSimpleName();
    private ListPreference vibrationDurationListPref;
    private Context applicationCtx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Vibify.initTheme(this);

        addPreferencesFromResource(R.xml.preferences);
        applicationCtx = getApplicationContext();
        vibrationDurationListPref = (ListPreference) findPreference(Vibify.SETTING_VIBRATION_DUR);
        vibrationDurationListPref.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(Integer.parseInt(newValue.toString()));
        return true;
    }
}

