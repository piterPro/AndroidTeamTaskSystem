package com.piter.piterdiplomna.helper;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.piter.piterdiplomna.R;
import com.piter.piterdiplomna.activities.MainActivity;
import com.tyczj.extendedcalendarview.CalendarProvider;

import static com.piter.piterdiplomna.helper.Constants.NOTIFICATION_ID;

/**
 * Created by Piter on 28/11/2016.
 */

public class MyNewIntentService extends IntentService {
    private static final int NOTIFICATION_ID = 3;
    private String TAG="TAG IntentService";


    public MyNewIntentService() {
        super("MyNewIntentService");
        Log.d(TAG, "MyNewIntentService: constructor fired");
    }

    //alarm notification here
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: polu4ih alarma");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String task_id = intent.getStringExtra("id");
        Log.d(TAG, "onHandleIntent: title="+title+" desc= "+description+" task_id="+task_id);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(description);
        builder.setSmallIcon(R.drawable.common_google_signin_btn_icon_light);
        Intent notifyIntento = new Intent(this, MainActivity.class);
        notifyIntento.putExtra("id",intent.getStringExtra("id"));
        PendingIntent pendingIntento = PendingIntent.getActivity(this, 0, notifyIntento, 0);
//      to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntento);


        Intent notifyIntent = new Intent(this, AlarmDismissReceiver.class);
        notifyIntent.putExtra("title", title);
        notifyIntent.putExtra("description", description);
//        Log.d(TAG, "onHandleIntent: why cant i set it? id="+task_id);
        notifyIntent.putExtra("id", ""+task_id);//task_id
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getApplicationContext(), 0, notifyIntent, 0);
        builder.setDeleteIntent(pendingIntent2);

        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }
}