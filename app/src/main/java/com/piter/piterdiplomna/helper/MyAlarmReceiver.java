package com.piter.piterdiplomna.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.piter.piterdiplomna.activities.ChatActivity;
import com.piter.piterdiplomna.activities.MainActivity;
import com.tyczj.extendedcalendarview.CalendarProvider;

import java.util.Calendar;

import static java.security.AccessController.getContext;

/**
 * Created by Piter on 28/11/2016.
 */

public class MyAlarmReceiver extends BroadcastReceiver {

    public MyAlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String title=intent.getStringExtra("title");
        String description=intent.getStringExtra("description");
        String id = intent.getStringExtra("id");
        Log.d("TAG", "onReceive: MyAlarmReceiver id ?="+id);

        Intent intent1 = new Intent(context, MyNewIntentService.class);
        intent1.putExtra("title",title);
        intent1.putExtra("description",description);
        intent1.putExtra("id",id);
        context.startService(intent1);

    }
}