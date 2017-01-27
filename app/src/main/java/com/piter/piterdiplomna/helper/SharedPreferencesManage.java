package com.piter.piterdiplomna.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.util.Locale.getDefault;


/**
 * Created by Piter on 16/10/2016.
 */

public class SharedPreferencesManage extends Application {
    //Getting tag it will be used for displaying log and it is optional
    public static final String TAG = "TAG ShrdPrefManage";
    public static OkHttpClient client;

    //Creatting class object
    private static SharedPreferencesManage mInstance;

    //Creating sharedpreferences object
    //We will store the user data in sharedpreferences
    private SharedPreferences sharedPreferences;

    //class instance will be initialized on app launch
    @Override
    public void onCreate() {
        this.client = new OkHttpClient();
        super.onCreate();
        mInstance = this;
    }

    public OkHttpClient getOkhttpClient() {
        return client;
    }

    //Public static showFragment to get the instance of this class
    public static synchronized SharedPreferencesManage getInstance() {
        return mInstance;
    }


    //Method to get shared preferences
    public SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    //This showFragment will clear the shared preference
    //It will be called on logout
    public void logout() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.apply();
    }

    //This showFragment will store the user data on shared preferences
    //It will be called on login
    public void loginUser(int id, String fname, String lname, String company, String position) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(Constants.USER_ID, id);
//        editor.putString(Constants.USER_ORGANIZATION, Organization);
        editor.putString(Constants.USER_FNAME, fname);
        editor.putString(Constants.USER_LNAME, lname);
        editor.putString(Constants.USER_COMPANY, company);
        editor.putString(Constants.USER_POSITION, position);
//        editor.putString(Constants.USER_TASK_LIST, "");
        editor.putBoolean(Constants.IS_LOGGED_IN, true);
        editor.apply();
        Log.d(TAG, "loginUser: login successfull id"+id);
        Log.d(TAG, "loginUser:fname= "+fname);
        Log.d(TAG, "loginUser:lname= "+lname);
    }

    //This showFragment will store the user data on shared preferences
    //It will be called on login
    public void storeToken( String token) {
        Log.d(TAG, "storeToken to sharedPref");
        SharedPreferences.Editor editor = getSharedPreferences().edit();
//        editor.putString(Constants.USER_ORGANIZATION, Organization);
        editor.putString(Constants.USER_TOKEN, token);
        editor.apply();
//        Log.d(TAG, "loginUser:lname= "+lname);
    }
    //This showFragment will return the user id of logged in user
    public String getToken() {
        return getSharedPreferences().getString(Constants.USER_TOKEN, "");
    }
    public String getTaskListAlarm() {
        return getSharedPreferences().getString(Constants.USER_TOKEN, "");
    }

    //This showFragment will check whether the user is logged in or not
    public boolean isLoggedIn() {
        return getSharedPreferences().getBoolean(Constants.IS_LOGGED_IN, false);
    }

    //This showFragment will return the user id of logged in user
    public int getUserId() {
        return getSharedPreferences().getInt(Constants.USER_ID, -1);
    }

    //This showFragment will return the username of logged in user
    public String getFullUserName() {
        return getSharedPreferences().getString(Constants.USER_FNAME,"No")+" "+getSharedPreferences().getString(Constants.USER_LNAME,"Name");
    }
    //This showFragment will return the username of logged in user
    public String getUserFirsName() {
        return getSharedPreferences().getString(Constants.USER_FNAME,"No");
    }
    //This showFragment will return the username of logged in user
    public String getUserLastName() {
        return getSharedPreferences().getString(Constants.USER_LNAME,"Name");
    }
    //Method to just set in motion events that will take place only on DB side deleteToken for now
    public void deleteToken(final String url) throws Exception{
        Log.d(TAG, "deleteToken: "+url);
            Request request = new Request.Builder()
                    .url(url)
                    .delete()
                    .build();
            SharedPreferencesManage.client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("TAG", "onFailure:async task url="+url);
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    response.body().close();
                    Log.d(TAG, "onResponse: successful");
                }
            });
        }
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
}
