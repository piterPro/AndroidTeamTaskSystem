package com.piter.piterdiplomna3.helper;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Locale.getDefault;

/**
 * Created by Piter on 29/11/2016.
 */

public class MyDateHelper {
    final String TAG="TAG MyDateHelper";
    public Calendar convertFromString(String data) {
        Calendar cl = Calendar.getInstance();
        String DateTimeArray[] = data.split(" ");
        String DateArray[] = DateTimeArray[0].split("-");
        String TimeArray[] = DateTimeArray[1].split(":");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date mDate = null;
        try {
            mDate = sdf.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cl.clear();
        cl.set(
                Integer.parseInt(DateArray[0]),//year
                (Integer.parseInt(DateArray[1])-1),//month should be -1 if it comes from real date
                Integer.parseInt(DateArray[2]),//day
                Integer.parseInt(TimeArray[0]),//hours
                Integer.parseInt(TimeArray[1]), //minutes
                Integer.parseInt(TimeArray[2]));//seconds
        cl.setTimeInMillis(mDate.getTime());//TODO:serious workaround fix it
        return cl;
    }
    //convert milisecs to real date
    public String convertFromMilisec(Long data) {
        String retutnString=null;
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(data);
        retutnString=cl.get(Calendar.YEAR)+"-"+(cl.get(Calendar.MONTH)+1)+"-"+cl.get(Calendar.DAY_OF_MONTH)+" "+cl.get(Calendar.HOUR_OF_DAY)+":"+cl.get(Calendar.MINUTE)+":"+cl.get(Calendar.SECOND);
        return retutnString;
    }
    //TODO: compared to today return string Date with correct format
    //hour if today, day name if this week else full date to REAL date
    public String returnCorrectFormatDate(Calendar cl){
        String returnString=null;

        Calendar Now = Calendar.getInstance();
        Log.d(TAG, "cl koeto doide: "+cl.getTime());

        Now.clear(Calendar.HOUR_OF_DAY);
        Now.clear(Calendar.HOUR);
        Now.clear(Calendar.MINUTE);
        Now.clear(Calendar.SECOND);
        Now.clear(Calendar.MILLISECOND);
        int compareNumber = cl.compareTo(Now);
        if(compareNumber==1){
            Log.d(TAG, "returnCorrectFormatDate: the date is from today! ="+cl.getTime());
            returnString = cl.get(Calendar.HOUR_OF_DAY)+":"+String.format("%02d",cl.get(Calendar.MINUTE));
            Log.d(TAG, "returnCorrectFormatDate: returnString="+returnString );
            return returnString;
        }
        else
        if(compareNumber==-1){
            Log.d(TAG, "returnCorrectFormatDate: compareNumber=-1");
        }

        Now.add(Calendar.WEEK_OF_YEAR,-1);
        Log.d(TAG, "   now-1week="+Now.getTime());

        if(cl.compareTo(Now)==1){
            Log.d(TAG, "returnCorrectFormatDate: the date is from this week! = "+cl.getTime());
            cl.setFirstDayOfWeek(Calendar.MONDAY);
            returnString = ""+cl.getDisplayName((Calendar.DAY_OF_WEEK),Calendar.LONG, getDefault());
//                    DayOfWeek dow = cl.get();
            Log.d(TAG, "returnCorrectFormatDate: returnString="+returnString );
            return returnString;
        }
        returnString=cl.get(Calendar.YEAR)+"-"+(cl.get(Calendar.MONTH)+1)+"-"+cl.get(Calendar.DAY_OF_MONTH);
        Log.d(TAG, "returnCorrectFormatDate: returnString="+returnString );
        return returnString;
    }

    public String returnCorrectFormatDate(long long_cl){
        String returnString=null;

        Calendar Now = Calendar.getInstance();
        Calendar cl=Calendar.getInstance();
        cl.setTimeInMillis(long_cl);
        Log.d(TAG, "cl koeto doide: "+cl.getTime());

        Now.clear(Calendar.HOUR_OF_DAY);
        Now.clear(Calendar.HOUR);
        Now.clear(Calendar.MINUTE);
        Now.clear(Calendar.SECOND);
        Now.clear(Calendar.MILLISECOND);
        int compareNumber = cl.compareTo(Now);
        if(compareNumber==1){
            Log.d(TAG, "returnCorrectFormatDate: the date is from today! ="+cl.getTime());
//            String curTime = String.format("%02d:%02d", hrs, mnts);
            returnString = cl.get(Calendar.HOUR_OF_DAY)+":"+String.format("%02d",cl.get(Calendar.MINUTE));
            Log.d(TAG, "returnCorrectFormatDate: returnString="+returnString );
            return returnString;
        }
        else
            if(compareNumber==-1){
                Log.d(TAG, "returnCorrectFormatDate: compareNumber=-1");
            }

        Now.add(Calendar.WEEK_OF_YEAR,-1);
//        Log.d(TAG, "returnCorrectFormatDate: now-1week="+Now.getTime());

        if(cl.compareTo(Now)==1){
            Log.d(TAG, "returnCorrectFormatDate: the date is from this week! = "+cl.getTime());
            cl.setFirstDayOfWeek(Calendar.MONDAY);
            returnString = ""+cl.getDisplayName((Calendar.DAY_OF_WEEK),Calendar.LONG, getDefault());
//                    DayOfWeek dow = cl.get();
            Log.d(TAG, "returnCorrectFormatDate: returnString="+returnString );
            return returnString;
        }
        returnString=cl.get(Calendar.YEAR)+"-"+(cl.get(Calendar.MONTH)+1)+"-"+cl.get(Calendar.DAY_OF_MONTH);
        Log.d(TAG, "returnCorrectFormatDate: returnString="+returnString );
        return returnString;
    }
}
