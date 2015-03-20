package com.majy.tictactoe.vue;

import java.util.Locale;
import com.majy.tictactoe.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
	
	private static final int SETTINGS_RETURN = 1;
	private SharedPreferences pref;
	private Configuration config;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateSettings();
        setContentView(R.layout.activity_main);
    }

	public void singlePlayerGame(View view){
		Intent intent = new Intent(this, GameActivity.class);
		
		Bundle b = new Bundle();
		b.putInt("mode", 1);
		intent.putExtras(b);
		
		startActivity(intent);
	}
	
	public void multiPlayerGame(View view){
		Intent intent = new Intent(this, GameActivity.class);
		
		Bundle b = new Bundle();
		b.putInt("mode", 2);
		intent.putExtras(b);
		
		startActivity(intent);
	}
	
	public void bluetoothGame(View view){
		Intent intent = new Intent(this, GameActivity.class);
		
		Bundle b = new Bundle();
		b.putInt("mode", 3);
		intent.putExtras(b);
		
		startActivity(intent);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
    	super.onOptionsItemSelected(item);
    	
        switch(item.getItemId()){
        	case(R.id.action_settings):
        		Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        		startActivityForResult(intent, SETTINGS_RETURN);
        		return true;
        }

        return false;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SETTINGS_RETURN){
            updateSettings();
        }
    }

	private void updateSettings(){
		pref = PreferenceManager.getDefaultSharedPreferences(this);
        config = getBaseContext().getResources().getConfiguration();
        //TODO: Check if we should be using phone locale instead of 'en'
        String pref_language = pref.getString("pref_language", "en");
        
        if (!config.locale.getLanguage().equals(pref_language)){
        	Locale locale = new Locale(pref_language);
        	Locale.setDefault(locale);
        	config.locale = locale;
        	getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }
	}
}
