package com.majy.tictactoe.vue;

import java.util.Locale;

import com.majy.tictactoe.R;

import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.os.Bundle;

@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener{
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateSettings();
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

	private void restartActivity() {
	    finish();
	    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub
		if(key.equals("pref_language")){
			updateSettings();
			restartActivity();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}
	
	private void updateSettings(){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();
        String pref_language = pref.getString("pref_language", "en");
        
        if (!config.locale.getLanguage().equals(pref_language)){
        	Locale locale = new Locale(pref_language);
        	Locale.setDefault(locale);
        	config.locale = locale;
        	getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
	}
}
