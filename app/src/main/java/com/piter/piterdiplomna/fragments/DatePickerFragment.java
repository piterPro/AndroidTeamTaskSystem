package com.piter.piterdiplomna.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import com.piter.piterdiplomna.activities.MainActivity;

import java.util.Calendar;


/**
 * Created by Piter on 21/06/2016.
 */

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        // Use the current date as the default date in the picker
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //DO SOMETHING with the data
        if (!view.isShown()) {
            return;
        }
        Calendar cl=Calendar.getInstance();
        cl.set(year,monthOfYear,dayOfMonth);

        if(MainActivity.WhichDate==1) {
            MainActivity.BeginDate=cl.getTimeInMillis();
            cl.setTimeInMillis(MainActivity.BeginDate);
            String date1 = "" + cl.get(Calendar.DAY_OF_MONTH) + ":" + (cl.get(Calendar.MONTH)+1) + ":" + cl.get(Calendar.YEAR);
            Toast.makeText(getContext(),"added BeginDate="+date1,Toast.LENGTH_LONG).show();
            Log.d("TAG DatePicker", "onDateSet1: "+date1);
            MainActivity.WhichDate = 0;
//            Intent a = new Intent(MainActivity.con, TimePickerFragment.class);//it is not a activity
//            startActivity(a);

            DialogFragment newFragment = new TimePickerFragment();
            MainActivity.WhichDate = 1;//to know witch date are we setting begin or end date
            newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");

        }else
            if(MainActivity.WhichDate==2){
                MainActivity.EndDate = cl.getTimeInMillis();
                cl.setTimeInMillis(MainActivity.EndDate);
                String date2 = "" + cl.get(Calendar.DAY_OF_MONTH) + ":" + (cl.get(Calendar.MONTH)+1) + ":" + cl.get(Calendar.YEAR);
                Toast.makeText(getContext(),"added EndDate="+date2,Toast.LENGTH_LONG).show();
                Log.d("TAG DatePicker", "onDateSet2: "+date2);
                MainActivity.WhichDate = 0;

                DialogFragment newFragment = new TimePickerFragment();
                MainActivity.WhichDate = 2;//to know witch date are we setting begin or end date
                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    //            return;
            }

    }
}
