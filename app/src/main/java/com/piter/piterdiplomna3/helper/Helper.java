package com.piter.piterdiplomna3.helper;

import android.annotation.TargetApi;
import android.icu.text.DateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Piter on 29/11/2016.
 */

public class Helper {
    public Calendar convertFromString(String data) {
        Calendar cl = Calendar.getInstance();
        String DateTimeArray[] = data.split(" ");
        String DateArray[] = DateTimeArray[0].split("-");
        String TimeArray[] = DateTimeArray[1].split(":");
        cl.set(
                Integer.parseInt(DateArray[0]),//year
                Integer.parseInt(DateArray[1]),//month
                (Integer.parseInt(DateArray[2]) - 1),//day
                (Integer.parseInt(TimeArray[0])),//hours
                Integer.parseInt(TimeArray[1]), //minutes
                Integer.parseInt(TimeArray[2]));//seconds
        return cl;
    }
    //TODO: compared to today return string Date with correct format
    //hour if today, day name if this week else full date
    public String returnCorrectFormatDate(Calendar cl){
        String returnString=null;
        Calendar clNow = Calendar.getInstance();
        Date data1 = Calendar.getInstance().getTime();
//        data1.getDay();


        if(clNow.compareTo(cl)!=-1){
            Log.d("TAG taskClass", "date past =cl: "+cl.getTime().toString());
            return returnString;
        }


        return returnString;
    }
}
