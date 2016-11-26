package com.piter.piterdiplomna3.helper;

import okhttp3.MediaType;

/**
 * Created by Piter on 16/10/2016.
 */

public class Constants {
    public static final String SHARED_PREF_NAME = "piterdiplomna";
    public static final String USER_ID = "userid";
    public static final String USER_FNAME = "fname";
    public static final String USER_LNAME = "lname";
    public static final String USER_COMPANY= "company";
    public static final String USER_POSITION= "position";
    public static final String IS_LOGGED_IN= "is_logged_in";

    public static final String PUSH_NOTIFICATION = "pushnotification";
    public static final String USER_TOKEN = "token";
    public static final int NOTIFICATION_ID = 235;//92748725348 maybe
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public static final String USER_TASK_LIST = "usertasklist";

}
