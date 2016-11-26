package com.piter.piterdiplomna3.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piter.piterdiplomna3.ObjectClasses.CommentClass;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


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

    //This showFragment will check whether the user is logged in or not
    public boolean isLoggedIn() {
        return getSharedPreferences().getBoolean(Constants.IS_LOGGED_IN, false);
    }

    //This showFragment will return the user id of logged in user
    public int getUserId() {
        return getSharedPreferences().getInt(Constants.USER_ID, -1);
    }

    //This showFragment will return the username of logged in user
    public String getUserName() {
        return getSharedPreferences().getString(Constants.USER_FNAME,"No")+" "+getSharedPreferences().getString(Constants.USER_LNAME,"Name");
    }
    //Method to just set in motion events that will take place only on DB side
    public void updateURL(final String url) throws Exception{
            Request request = new Request.Builder()
                    .url(url)
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
                    Log.d(TAG, "onResponse: successfull");
                }
            });
        }
}
