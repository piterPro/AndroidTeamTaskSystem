package com.piter.piterdiplomna.gcm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.piter.piterdiplomna.activities.ChatActivity;
import com.piter.piterdiplomna.activities.MainActivity;
import com.piter.piterdiplomna.helper.Constants;
import com.piter.piterdiplomna.helper.NotificationHandler;

/**
 * Created by Piter on 16/10/2016.
 */

public class GCMPushReceiverService extends GcmListenerService {
    final String  TAG="TAG GCMPushService";
    
    //recives the GCM message and gets individual values and notifies 
    @Override
    public void onMessageReceived(String from, Bundle dataOG) {
        Log.d(TAG, "onMessageReceived: from="+from);
        Bundle data = dataOG.getBundle("notification");
        //from here to change for another push notification
        String type = data.getString("type");
        String message = data.getString("message");
        String create_date_time = data.getString("create_date_time");
        String user_send_id = data.getString("user_send_id");
        String user_to_id = data.getString("user_to_id");
        String title = data.getString("title");

        Log.d(TAG, "onMessageReceived:Bundle dataOG= "+dataOG.toString());
        Log.d(TAG, "onMessageReceived:Bundle data= "+data.toString());
        if(type.contains("chat"))
            sendNotificationToChat( user_send_id, user_to_id, message, create_date_time, title);
        else
            sendNotificationToTask( user_send_id, user_to_id, message, create_date_time, title);
    }

    //
    private void sendNotificationToChat(String user_send_id, String user_to_id, String message, String create_date_time, String title) {
        Log.d(TAG, "sendNotification: with all individual values");
        //Creating a broadcast intent
        Intent pushNotification = new Intent(getApplicationContext(),ChatActivity.class);
        pushNotification.setAction(Constants.CHAT_NOTIFICATION);
        //Adding notification data to the intent
//         Intent intent2 = new Intent(mContext, ChatActivity.class);
        pushNotification.putExtra("user_send_id", user_send_id);
        pushNotification.putExtra("user_to_id", user_to_id);
        pushNotification.putExtra("message", message);
        pushNotification.putExtra("create_date_time", create_date_time);//title
        pushNotification.putExtra("title", title);
//        if(user_send_id==user_to_id)//sam si pi6e6 brat
//            return;
        //We will create this class to handle notifications
        Log.d(TAG, "sendNotification: load all the data here");
        NotificationHandler notificationHandler = new NotificationHandler(getApplicationContext());

        //If the app is in foreground
        if (!NotificationHandler.isAppIsInBackground(getApplicationContext())) {
            //Sending a broadcast to the chat to add the new message
            Log.d(TAG, "sendNotification: AppIs in Foreground ");
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        } else {
            //If app is in foreground displaying push notification
            Log.d(TAG, "sendNotification: AppIs in InBackground ");
            notificationHandler.showNotificationMessage(user_send_id,title, message, pushNotification);
        }
    }
    private void sendNotificationToTask(String user_send_id, String user_to_id, String message, String create_date_time, String title) {
        Log.d(TAG, "sendNotificationToTask: with all individual values");
        //Creating a broadcast intent
        Intent pushNotification = new Intent(getApplicationContext(), MainActivity.class);
        pushNotification.setAction(Constants.TASK_NOTIFICATION);
        //Adding notification data to the intent
//         Intent intent2 = new Intent(mContext, ChatActivity.class);
        pushNotification.putExtra("user_send_id", user_send_id);
        pushNotification.putExtra("user_to_id", user_to_id);
        pushNotification.putExtra("message", message);
        pushNotification.putExtra("create_date_time", create_date_time);//title
        pushNotification.putExtra("title", title);
//        if(user_send_id==user_to_id)//sam si pi6e6 brat
//            return;
        //We will create this class to handle notifications
        Log.d(TAG, "sendNotification: load all the data here");
        NotificationHandler notificationHandler = new NotificationHandler(getApplicationContext());

        //If the app is in foreground
        if (!NotificationHandler.isAppIsInBackground(getApplicationContext())) {
            //Sending a broadcast to the chat to add the new message
            Log.d(TAG, "sendNotification: AppIs in Foreground ");
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            notificationHandler.showNotificationTask(user_send_id,title, message, pushNotification);
        } else {

            //If app is in foreground displaying push notification
            Log.d(TAG, "sendNotification: AppIs is InBackground ");
            notificationHandler.showNotificationTask(user_send_id,title, message, pushNotification);
        }
    }
}
