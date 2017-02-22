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

        Intent intent1 = new Intent(context, MyNewIntentService.class);
        String title=intent.getStringExtra("title");

        String description=intent.getStringExtra("description");
        intent1.putExtra("title",title);
        intent1.putExtra("description",description);
        intent1.putExtra("id",intent.getStringExtra("id"));
        Log.d("TAG", "onReceive: MyAlarmReceiver");
        context.startService(intent1);

        //TODO:
//        Intent Intent = new Intent(context, MyAlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast                (context, 9562094, Intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent notifyIntent = new Intent(context, MyAlarmReceiver.class);

        notifyIntent.putExtra("title","End of task"+title);
        notifyIntent.putExtra("description",description);
        notifyIntent.putExtra("id",intent.getStringExtra("id"));
        MyDateHelper converter = new MyDateHelper();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 9562094, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.d("TAG", "onReceive: did i canceled it?");
        pendingIntent.cancel();
        Intent pushNotification = new Intent(context,ChatActivity.class);
        pushNotification.setAction(Constants.CHAT_NOTIFICATION);
        //Adding notification data to the intent
//         Intent intent2 = new Intent(mContext, ChatActivity.class);
        pushNotification.putExtra("title",title);
        pushNotification.putExtra("description",description);
//        if(user_send_id==user_to_id)//sam si pi6e6 brat
//            return;
        //We will create this class to handle notifications
        Log.d("TAG", "sendNotification: load all the data here");
        NotificationHandler notificationHandler = new NotificationHandler(context);

        LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotification);//Log.d(TAG, "sendNotification: AppIs not in Background ");
        if (!NotificationHandler.isAppIsInBackground(context)) {
            //Sending a broadcast to the chat to add the new message
//            Log.d(TAG, "sendNotification: AppIs not in Background ");
            LocalBroadcastManager.getInstance(context).sendBroadcast(pushNotification);
        } else {
            //If app is in foreground displaying push notification
//            Log.d(TAG, "sendNotification: AppIs is InBackground ");
            notificationHandler.showNotificationMessage("End of task"+title, description, pushNotification);
        }
    }
}