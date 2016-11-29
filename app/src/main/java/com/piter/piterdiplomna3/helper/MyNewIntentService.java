package com.piter.piterdiplomna3.helper;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.piter.piterdiplomna3.R;
import com.piter.piterdiplomna3.activities.MainActivity;

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

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: polu4ih alarma");
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(intent.getStringExtra("title"));
        builder.setContentText(intent.getStringExtra("description"));
        builder.setSmallIcon(R.drawable.common_google_signin_btn_icon_light);
        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }
}