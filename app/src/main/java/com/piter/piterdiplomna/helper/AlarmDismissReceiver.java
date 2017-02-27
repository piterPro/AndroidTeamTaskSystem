package com.piter.piterdiplomna.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.piter.piterdiplomna.activities.ChatActivity;
import com.piter.piterdiplomna.activities.MainActivity;
import com.tyczj.extendedcalendarview.CalendarProvider;

import java.util.Calendar;

import static com.piter.piterdiplomna.helper.Constants.NOTIFICATION_ID;
import static java.security.AccessController.getContext;

/**
 * Manages dismiss of any notification
 */

public class AlarmDismissReceiver extends BroadcastReceiver {

    public AlarmDismissReceiver() {
        Log.d("TAG", "AlarmDismissReceiver: constructor fired");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TAG", "onReceive: AlarmDismissReceiver");

        //Create intent just to cancel it
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String task_id = intent.getStringExtra("id");
        Log.d("TAG AlarmDismissR", "onReceive: title="+title+" descr="+description+" task_id="+task_id);
        Intent notifyIntent = new Intent(context, MyAlarmReceiver.class);
        notifyIntent.putExtra("title","End of task "+title);
        notifyIntent.putExtra("description", description);
        notifyIntent.putExtra("id",""+task_id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 9562094, notifyIntent, 0);
        Log.d("TAG", "onReceive: did i canceled it?");
//        pendingIntent.cancel();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        //flag to cancel the notification when clickenotificationCompat.flags = Notification.FLAG_AUTO_CANCEL;

        ContentValues values = new ContentValues();
        values.put(CalendarProvider.TASK_ID, task_id);
        context.getContentResolver().insert(CalendarProvider.CONTENTNOTIF_URI,values);

    }
}