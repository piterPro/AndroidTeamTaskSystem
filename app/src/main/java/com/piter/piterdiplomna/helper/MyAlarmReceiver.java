package com.piter.piterdiplomna.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
        Log.d("TAG", "onReceive: MyAlarmReceiver");
        context.startService(intent1);
    }
}