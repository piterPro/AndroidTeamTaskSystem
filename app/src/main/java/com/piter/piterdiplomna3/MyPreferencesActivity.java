package com.piter.piterdiplomna3;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.piter.piterdiplomna3.activities.MainActivity;
import com.piter.piterdiplomna3.fragments.MainFragment;
import com.piter.piterdiplomna3.helper.SharedPreferencesManage;

public class MyPreferencesActivity extends PreferenceActivity {
    String TAG = "TAG MyPreferencesActvty";

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
