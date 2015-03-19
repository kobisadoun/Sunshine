package com.example.android.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sunshine.app.data.WeatherContract;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

        private static final String LOG_TAG = DetailFragment.class.getSimpleName();
        private static final String FORECAST_SHARE_HASHING = "#SunshineApp";

        private ShareActionProvider mShareActionProvider;

        TextView mDayTextView;
        TextView mDateTextView;
        TextView mMinTempTextView;
        TextView mMaxTempTextView;
        TextView mHumidityTextView;
        TextView mWindTextView;
        TextView mPressureTextView;
        TextView mWeatherDescriptionTextView;
        ImageView mIcon;

        private static final int DETAIL_LOADER = 0;

        //private String mForecastStr;
        private String mForecast;

        private static final String[] DETAILS_COLUMNS = {
                WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
                WeatherContract.WeatherEntry.COLUMN_DATE,
                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
                WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
                WeatherContract.WeatherEntry.COLUMN_PRESSURE,
                WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
                WeatherContract.WeatherEntry.COLUMN_DEGREES,
                WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
        };

        private static final int COL_WEATHER_ID = 0;
        private static final int COL_WEATHER_DATE = 1;
        private static final int COL_WEATHER_SHORT_DESC = 2;
        private static final int COL_WEATHER_MAX_TEMP = 3;
        private static final int COL_WEATHER_MIN_TEMP = 4;
        private static final int COL_WEATHER_HUMIDITY = 5;
        private static final int COL_WEATHER_PRESSURE = 6;
        private static final int COL_WEATHER_WIND_SPEED = 7;
        private static final int COL_WEATHER_DEGREES = 8;
        private static final int COL_WEATHER_CONDITION_ID = 9;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }


        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            // This is called when a new Loader needs to be created.  This
            // sample only has one Loader, so we don't care about the ID.
            // First, pick the base URI to use depending on whether we are
            // currently filtering.
            Log.v(LOG_TAG, "In onCreateLoader");
            Intent intent = getActivity().getIntent();
            if(intent == null){
                return null;
            }

            return new CursorLoader(getActivity(),intent.getData(), DETAILS_COLUMNS, null, null, null);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.detailfragment, menu);

            // Set up ShareActionProvider's default share intent
            MenuItem shareItem = menu.findItem(R.id.action_share);
            mShareActionProvider = new ShareActionProvider(this.getActivity());


            if(mForecast != null) {
                mShareActionProvider.setShareIntent(getShareForecastIntent());
            }
            MenuItemCompat.setActionProvider(shareItem, mShareActionProvider);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            mDayTextView = (TextView)rootView.findViewById(R.id.details_day_textview);
            mDateTextView = (TextView)rootView.findViewById(R.id.details_date_textview);
            mMinTempTextView = (TextView)rootView.findViewById(R.id.details_low_textview);
            mMaxTempTextView = (TextView)rootView.findViewById(R.id.details_high_textview);
            mHumidityTextView = (TextView)rootView.findViewById(R.id.details_humidity_textview);
            mWindTextView = (TextView)rootView.findViewById(R.id.details_wind_textview);
            mPressureTextView = (TextView)rootView.findViewById(R.id.details_pressure_textview);
            mWeatherDescriptionTextView = (TextView)rootView.findViewById(R.id.details_forecast_textview);
            mIcon = (ImageView)rootView.findViewById(R.id.details_icon);

            return rootView;

//            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
//            TextView detailTextView = (TextView)rootView.findViewById(R.id.detail_text);
//
//            Intent intent = getActivity().getIntent();
//            if (intent != null) {
//                mForecastStr = intent.getDataString();
//                detailTextView.setText(mForecastStr);
//            }


//            if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
//                mForecastStr = intent.getStringExtra(Intent.EXTRA_TEXT);
//                detailTextView.setText(mForecastStr);
//            }


            //return inflater.inflate(R.layout.fragment_detail, container, false);
        }

        private Intent getShareForecastIntent() {

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
            shareIntent.setType("text/plain");
            //shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastStr+"#SunshineApp");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHING);
            return shareIntent;
        }

        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            // Swap the new cursor in.  (The framework will take care of closing the
            // old cursor once we return.)
            Log.v(LOG_TAG, "In onLoadFinished");
            if(!data.moveToFirst()){
                return;
            }

//            String dateString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));
//
//            String weatherDescription = data.getString(COL_WEATHER_SHORT_DESC);
//
//            boolean isMetric = Utility.isMetric(getActivity());
//
//            String high = Utility.formatTemperature(getActivity(), data.getInt(COL_WEATHER_MAX_TEMP), isMetric);
//            String low = Utility.formatTemperature(getActivity(), data.getInt(COL_WEATHER_MIN_TEMP), isMetric);
//
//            mForecast = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);
//            TextView detailTextView = (TextView)getView().findViewById(R.id.detail_text);
//            detailTextView.setText(mForecast);


            String dayString = Utility.getDayName(getActivity(), data.getLong(COL_WEATHER_DATE));
            mDayTextView.setText(dayString);

            String dateString = Utility.getFormattedMonthDay(getActivity(), data.getLong(COL_WEATHER_DATE));
            mDateTextView.setText(dateString);

            String minTempString = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MIN_TEMP), Utility.isMetric(getActivity()));
            mMinTempTextView.setText(minTempString);

            String maxTempString = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MAX_TEMP), Utility.isMetric(getActivity()));
            mMaxTempTextView.setText(maxTempString);

            String humidityString = Utility.getFormattedHumidity(getActivity(), data.getFloat(COL_WEATHER_HUMIDITY));
            mHumidityTextView.setText(humidityString);

            String windString = Utility.getFormattedWind(getActivity(), data.getFloat(COL_WEATHER_WIND_SPEED), data.getFloat(COL_WEATHER_DEGREES));
            mWindTextView.setText(windString);

            String pressureString = Utility.getFormattedPressure(getActivity(), data.getFloat(COL_WEATHER_PRESSURE));
            mPressureTextView.setText(pressureString);

            String weatherDescription = data.getString(COL_WEATHER_SHORT_DESC);
            mWeatherDescriptionTextView.setText(weatherDescription);

            int weatherId = data.getInt(COL_WEATHER_CONDITION_ID);
            mIcon.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));


            //if onCreateIoptionMenu has already happened we need to update the share intent
            if(mShareActionProvider != null){
                mShareActionProvider.setShareIntent(getShareForecastIntent());
            }

        }

        public void onLoaderReset(Loader<Cursor> loader) {

        }

    }