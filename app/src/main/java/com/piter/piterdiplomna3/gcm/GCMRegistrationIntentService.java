package com.piter.piterdiplomna3.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.piter.piterdiplomna3.R;
import com.piter.piterdiplomna3.helper.Constants;
import com.piter.piterdiplomna3.helper.SharedPreferencesManage;
import com.piter.piterdiplomna3.helper.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


/**
 * Created by Piter on 16/10/2016.
 */

public class GCMRegistrationIntentService  extends IntentService {
        public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
        public static final String REGISTRATION_ERROR = "RegistrationError";
        public static final String REGISTRATION_TOKEN_SENT = "RegistrationTokenSent";
        public final String TAG = "TAG GCMRegIntentService";
        public GCMRegistrationIntentService() {
            super("");
        }


        @Override
        protected void onHandleIntent(Intent intent) {
            registerGCM();
        }

    private void registerGCM() {
        Intent registrationComplete = null;
        String token = null;
        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.d("GCMRegIntentService", "token:" + token);
//if device is already registered do not regit again
            sendRegistrationTokenToServer(token);
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token", token);
            SharedPreferencesManage.getInstance().storeToken(token);
        } catch (Exception e) {
            Log.d("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }


    private void sendRegistrationTokenToServer(final String token) {
        //Getting the user id from shared preferences
        //We are storing gcm token for the user in our mysql database
        JSONObject json = new JSONObject();
        try {
            json.put("token", token);
            json.put("userId",""+ SharedPreferencesManage.getInstance().getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        OkHttpClient client = new OkHttpClient();
        Log.d(TAG, "sendRegistrationTokenToServer: JSON="+json.toString());
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(URLs.URL_STORE_TOKEN)
                .post(RequestBody.create(Constants.JSON, json.toString()))
                .build();
        SharedPreferencesManage.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "onFailure: sendRegistrationTokenToServer async task  ");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                Intent registrationComplete = new Intent(REGISTRATION_TOKEN_SENT);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
            }
        });
    }
}
