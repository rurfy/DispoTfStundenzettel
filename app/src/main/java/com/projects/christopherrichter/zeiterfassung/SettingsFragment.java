package com.projects.christopherrichter.zeiterfassung;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.preferences, s);

        bindPreferenceTitleToValue(findPreference(SettingsActivity.KEY_PREF_USER_NAME));
        bindPreferenceTitleToValue(findPreference(SettingsActivity.KEY_PREF_TARGET_MAIL));
    }

    private static Preference.OnPreferenceChangeListener onPreferenceChangeListener
            = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if(preference instanceof EditTextPreference) {
                // update the title
                preference.setTitle(stringValue);

            }
            return true;
        }
    };

    private void bindPreferenceTitleToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(onPreferenceChangeListener);
        onPreferenceChangeListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }
}
