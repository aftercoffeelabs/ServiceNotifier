package com.example.aram.servicenotifier.settings;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aram.servicenotifier.R;
import com.example.aram.servicenotifier.infrastructure.MyApp;
import com.example.aram.servicenotifier.notifier.model.AlertCriteria;
import com.example.aram.servicenotifier.notifier.model.Notifier;

/**
 * Class SettingsFragment
 */
public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundColor(getResources().getColor(R.color.grey_background));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(MyApp.getRes().getString(R.string.sharedPrefKey_persistence_time))) {

            String duration = sharedPreferences.getString(key, "1");
            AlertCriteria.setPersistenceDuration(Integer.parseInt(duration));
        } else if (key.equals(MyApp.getRes().getString(R.string.sharedPrefKey_vibrate))) {

            Boolean enabled = sharedPreferences.getBoolean(key, true);
            Notifier.doVibrate(enabled);
        }

    }
}
