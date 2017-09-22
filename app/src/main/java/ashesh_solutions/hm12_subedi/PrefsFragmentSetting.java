/*Ashesh Subedi
  L20398950
  COSC 5340 Android Programming (Online)
  Summer 2016
  Homework #12
*/
package ashesh_solutions.hm12_subedi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class PrefsFragmentSetting extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public PrefsFragmentSetting() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preference from an XML resource
        addPreferencesFromResource(R.xml.prefs_fragment_settings);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener when a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        // Set up a click listener for company info
        Preference pref;
        pref = getPreferenceScreen().findPreference("my_high_score");
        String s = ""+Assets.HighScore;
        pref.setSummary(s);

    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


    }
}

