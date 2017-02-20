package com.piter.piterdiplomna.helper;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
        intent1.putExtra("title",intent.getStringExtra("title"));
        intent1.putExtra("description",intent.getStringExtra("description"));
        intent1.putExtra("id",intent.getStringExtra("id"));
        Log.d("TAG", "onReceive: MyAlarmReceiver");
        context.startService(intent1);

        //TODO:
        Intent Intent = new Intent(context, MyAlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (context, 9562094, Intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
//            alarmManager.set(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + 1000 * 5),
//                    pendingIntent);
//            Toast.makeText(this, "Alarm added ", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "onNavigationItemSelected: time" + (System.currentTimeMillis()));
//            if(alarmManager!=null){
//                alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        .... code to handle cancel
//    }
}