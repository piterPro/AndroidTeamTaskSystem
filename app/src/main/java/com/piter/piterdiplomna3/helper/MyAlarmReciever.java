package com.piter.piterdiplomna3.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Piter on 28/11/2016.
 */

public class MyAlarmReciever extends BroadcastReceiver {
    public MyAlarmReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        Intent intent1 = new Intent(context, MyNewIntentService.class);
        context.startService(intent1);
    }
}