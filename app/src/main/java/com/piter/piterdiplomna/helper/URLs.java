package com.piter.piterdiplomna.helper;

/**
 * Created by Piter on 16/10/2016.
 */

public class URLs {
    public static final String ROOT_URL = "http://10.0.3.2:8080/com.piter.rest.jersey/api/v2/manage/";

    public static final String URL_TRY_LOGIN = ROOT_URL + "login";
    public static final String URL_REGISTER_USER = ROOT_URL + "registerUser";
    public static final String URL_STORE_TOKEN = ROOT_URL + "storegcmtoken";
    public static final String URL_DELETE_TOKEN = ROOT_URL + "deleteToken";

    public static final String URL_REGISTER_COMPANY = ROOT_URL + "registerCompany";
    public static final String URL_FETCH_COMPANY = ROOT_URL + "company";
    public static final String URL_FETCH_POSITIONS = ROOT_URL + "positions";
    public static final String URL_SEND_REQUEST = ROOT_URL + "addRequest";

    public static final String URL_FETCH_USERS = ROOT_URL + "users";
    public static final String URL_FETCH_TASKS = ROOT_URL + "tasks";
    public static final String URL_ADD_TASK = ROOT_URL + "addTask";

    public static final String URL_FETCH_COMMENTS = ROOT_URL + "comments";
    public static final String URL_SEND_COMMENT = ROOT_URL + "addComment";
    public static final String URL_UPDATE_STATUS = ROOT_URL + "updateStatus";
    public static final String URL_UPDATE_TASK = ROOT_URL + "updateTask";

    public static final String URL_FETCH_MESSAGES = ROOT_URL + "messages";
    public static final String URL_FETCH_LASTMESSAGE = ROOT_URL + "lastmsg";
    public static final String URL_SEND_MESSAGE = ROOT_URL + "sendMsg";

//    public static final String URL_FETCH_STATUS = ROOT_URL + "checkStatus";//do i need this?

}
