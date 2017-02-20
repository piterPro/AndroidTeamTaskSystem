package com.piter.piterdiplomna.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import com.piter.piterdiplomna.activities.MainActivity;

import java.util.Calendar;

/**
 * Created by Piter on 29/11/2016.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (!view.isShown()) {
            return;
        }
        // Do something with the time chosen by the user
        if(MainActivity.WhichDate==1) {
            Calendar cl=Calendar.getInstance();
            cl.setTimeInMillis(MainActivity.BeginDate);
            cl.clear(Calendar.HOUR_OF_DAY);
            cl.clear(Calendar.MINUTE);
            cl.clear(Calendar.SECOND);
            cl.clear(Calendar.HOUR);//clear the hour too f calendar
            cl.add(Calendar.HOUR_OF_DAY,hourOfDay);
            cl.add(Calendar.MINUTE,minute);
            MainActivity.BeginDate=cl.getTimeInMillis();
            Log.d("TAG", "onTimeSet: BeginDate set="+cl.getTime());

        }
        else
        if(MainActivity.WhichDate==2) {
            Calendar cl=Calendar.getInstance();
            cl.setTimeInMillis(MainActivity.EndDate);
            cl.clear(Calendar.HOUR_OF_DAY);
            cl.clear(Calendar.MINUTE);
            cl.clear(Calendar.SECOND);
            cl.clear(Calendar.HOUR);//clear the hour too f calendar
            cl.add(Calendar.HOUR_OF_DAY,hourOfDay);
            cl.add(Calendar.MINUTE,minute);

            MainActivity.EndDate=cl.getTimeInMillis();
            Log.d("TAG", "onTimeSet: EndDate set="+cl.getTime());
        }

        Log.d("TAG TimePicker", "onTimeSet: hourOfDay="+hourOfDay+" minute"+minute);
    }
}
