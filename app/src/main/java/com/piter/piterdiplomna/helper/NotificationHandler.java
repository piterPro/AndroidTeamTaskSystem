package com.piter.piterdiplomna.helper;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.piter.piterdiplomna.R;
import com.piter.piterdiplomna.activities.ChatActivity;
import com.piter.piterdiplomna.activities.MainActivity;

import java.util.List;

/**
 * Created by Piter on 16/10/2016.
 */

public class NotificationHandler {


    private Context mContext;
    public static String TAG="TAG NotificationHandler";

    public NotificationHandler(Context mContext) {
        this.mContext = mContext;
    }

    //This showNotificationMessage would display the notification
    public void showNotificationMessage(final String user_id,final String title, final String message, Intent intent) {
        Log.d(TAG, "showNotificationMessage: would display the notification");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Intent intent2 = new Intent(mContext, ChatActivity.class);
        intent2.putExtra("id", user_id);
        Log.d(TAG, "showNotificationMessage : message= "+message+" id="+intent.getStringExtra("id"));
        Log.d(TAG, "showNotificationMessage2 : message= "+message+" id="+user_id);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 9562094, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFICATION_ID, builder.build());
    }
    public void showNotificationTask(final String user_id,final String title, final String message, Intent intent) {
        Log.d(TAG, "showNotificationTask: would display the notification");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Intent intent2 = new Intent(mContext, MainActivity.class);
        intent2.putExtra("id", user_id);
//        Log.d(TAG, "showNotificationTask : message= "+message+" id="+intent.getStringExtra("id"));
//        Log.d(TAG, "showNotificationTask : message= "+message+" id="+user_id);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 9562094, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        notificationManager.notify(Constants.NOTIFICATION_ID, builder.build());
    }


    //This showFragment will check whether the app is in background or not
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
