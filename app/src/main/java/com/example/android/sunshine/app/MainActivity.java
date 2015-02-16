package com.example.android.sunshine.app;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        if (savedInstanceState == null) {

            ForecastFragment forecastFragment = new ForecastFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, forecastFragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        else if (id == R.id.action_show_on_map){
           showPreferredLocationOnMap();
           return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showPreferredLocationOnMap() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String locationSetting = sharedPreferences.getString(getResources().getString(R.string.pref_location_key), getResources().getString(R.string.pref_location_default));
//        Uri geoLocationUri = Uri.parse("geo:0,0?").buildUpon()
//                .appendQueryParameter("q", locationSetting).build();
        Uri geoLocationUri = Uri.parse(locationSetting).buildUpon().build();
//
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.setData(geoLocationUri);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }



}
