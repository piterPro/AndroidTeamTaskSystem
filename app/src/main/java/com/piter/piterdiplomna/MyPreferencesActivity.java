package com.piter.piterdiplomna;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;


/**
 * A simple {@link Fragment} subclass.
 */
import android.preference.PreferenceActivity;

public class MyPreferencesActivity extends PreferenceActivity {
    String TAG = "TAG MyPreferencesActvty";
//   Settings can be manipulated here
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String strUserName = SP.getString("username", "NA");
        Log.d(TAG, "onCreate: strUserName="+strUserName);
        boolean bAppUpdates = SP.getBoolean("applicationUpdates",false);
        Log.d(TAG, "onCreate: bAppUpdates="+bAppUpdates);
        String downloadType = SP.getString("downloadType","1");

    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }



}
