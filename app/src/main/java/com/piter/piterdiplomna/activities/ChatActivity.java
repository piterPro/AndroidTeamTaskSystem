package com.piter.piterdiplomna.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import com.android.volley.AuthFailureError;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.RetryPolicy;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piter.piterdiplomna.ObjectClasses.ChatMessageClass;
import com.piter.piterdiplomna.ObjectClasses.UserClass;
import com.piter.piterdiplomna.R;
import com.piter.piterdiplomna.adapters.ChatMessageAdapterRV;
import com.piter.piterdiplomna.gcm.GCMRegistrationIntentService;
import com.piter.piterdiplomna.helper.NotificationHandler;
import com.piter.piterdiplomna.helper.SharedPreferencesManage;
import com.piter.piterdiplomna.helper.Constants;

import com.piter.piterdiplomna.helper.URLs;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//import okhttp3.Response;

//import static com.piter.piterdiplomna.activities.MainActivity.urlIP;


public class ChatActivity extends AppCompatActivity {

    //Broadcast receiver to receive broadcasts
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAG = "TAG ChatActivity";
    //ArrayList of messages to store the thread messages
    public ArrayList<ChatMessageClass> messages;
    private EditText chatText;
    public static int receiver_id=0;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button send;
    ChatMessageClass msg;
    ArrayList<UserClass> receiverUser;
    public static Context con;


    //    private boolean side = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_chat);

        Intent intent = getIntent();
        try {
            receiver_id = Integer.parseInt(intent.getStringExtra("id"));
            Log.d(TAG, "onCreate: receiver_id ="+receiver_id);
        }catch (Exception e){
            if(receiver_id==0){
                Log.d(TAG, "onCreate:id ne moja da se zeme receiver_id="+receiver_id);
            }}

//        receiverNameTV
        chatText = (EditText) findViewById(R.id.chat_textET);
        send = (Button) findViewById(R.id.chatBtn);

        //Initializing recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.chatRV);
        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);

        con=getBaseContext();
        //seting the onClick listeners
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode ==
                        KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });


        //Initializing message arraylist
//        messages = new ArrayList<>();
//          get the information about the receiver
        try {
            AsyncGetNameOfReceiver(URLs.URL_FETCH_USERS+"?id=" + receiver_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        fetchMessages();//volley
//        AsyncGetMsgs();
        //Calling function to fetch the existing messages on the thread
        try {
            AsyncGetMsgs(URLs.URL_FETCH_MESSAGES +"?id=" + SharedPreferencesManage.getInstance().getUserId() + "&id2="+ receiver_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Creating broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {

                    //When gcm registration is success do something here if you need
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_TOKEN_SENT)) {

                    //When the registration token is sent to ther server displaying a toast
                    Log.d(TAG, "onReceive: the registration token is sent");
//                    Toast.makeText(getApplicationContext(), "Chatroom Ready...", Toast.LENGTH_SHORT).show();

                    //When we received a notification when the app is in foreground
                } else if (intent.getAction().equals(Constants.CHAT_NOTIFICATION)) {
                    //Getting message data
                    Log.d(TAG, "onReceive: push notification !!!");
//                    intent.get
//                    receiver_id = Integer.parseInt(intent.getStringExtra("user_to_id"));
//                    Log.d(TAG, "onReceive:receiver_id ot push-a "+receiver_id);
                    String user_send_id = intent.getStringExtra("user_send_id");
                    String user_to_id = intent.getStringExtra("user_to_id");
                    String message = intent.getStringExtra("message");
                    String create_date_time = intent.getStringExtra("create_date_time");
                    String title = intent.getStringExtra("title");
                    //processing the message to add it in current thread
                    Log.d(TAG, "onReceive to be send to Chat Activity:user_send_id= " + user_send_id + " ,user_to_id= " + user_to_id + " ,message= " + message + " ,create_date_time= " + create_date_time);
                    String collapse_key = intent.getStringExtra("collapse_key");
                    Log.d(TAG, "onReceive:collapse_key " + collapse_key);
                    Log.d(TAG, "onReceive: user_send_id = "+user_send_id+", receiver_id ="+ receiver_id);
                    if (Integer.parseInt(user_send_id) ==  + receiver_id) {//check if this activity is with the right person
                        Log.d(TAG, "onReceive: ednakvi sa gornite taka li?");
                        processMessage(user_send_id, user_to_id, message, create_date_time);
                    }
                    else//call new activity
                    {
                        Log.d(TAG, "onReceive: razli4ni sa gornite taka li?");
                        NotificationHandler notificationHandler = new NotificationHandler(getApplicationContext());
//                        Intent intent2 = new Intent(getApplicationContext(), ChatActivity.class);
//                        intent2.putExtra("user_send_id", intent.getStringExtra("user_send_id"));
                        notificationHandler.showNotificationMessage(user_send_id,title, message, intent);
                    }
                }
            }
        };

    }


    //Processing message to add on the thread message,create_date_time, user_send_id, user_to_id
    private void processMessage(String user_send_id, String user_to_id, String message, String create_date_time) {
        Log.d(TAG, "processMessage: processing single message");
        int userSend=-1,userTo=-1;
        try {
             userSend = Integer.parseInt(user_send_id);
             userTo = Integer.parseInt(user_to_id);
        }
        catch (Exception e){
            Log.d(TAG, "processMessage: cant parse data "+user_send_id+" and "+user_to_id);
            e.printStackTrace();
        }
        ChatMessageClass m = new ChatMessageClass(userSend, userTo , message, create_date_time);
//        if(messages.)
        messages.add(m);
        mAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    //This showFragment will send the new message to the thread
    private boolean sendChatMessage2() {
        final String message = chatText.getText().toString().trim();
        if (message.equalsIgnoreCase(""))
            return false;
        int userId = SharedPreferencesManage.getInstance().getUserId();
        String name = SharedPreferencesManage.getInstance().getFullUserName();
        String sentAt = getTimeStamp();

        ChatMessageClass m = new ChatMessageClass(userId, receiver_id, message, sentAt);
//        if(messages.isEmpty()){messages.add(0,m);}
//        else
            messages.add(m);
        mAdapter.notifyDataSetChanged();
//        scrollToBottom();

        chatText.setText("");
        return true;
    }

    //showFragment to scroll the recyclerview to bottom
    private void scrollToBottom() {
        mAdapter.notifyDataSetChanged();
        if (mAdapter.getItemCount() > 1)
            mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, mAdapter.getItemCount() - 1);
    }

    //This showFragment will return current timestamp
    public static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    //Registering broadcast receivers
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_TOKEN_SENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.CHAT_NOTIFICATION));
    }


    //Unregistering receivers
    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    //
    //function AsyncGetMsgs uses Get url and write that to main fragment
    //
    public void AsyncGetMsgs(String url) throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();
        SharedPreferencesManage.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "onFailure:async task  ");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString;
                responseString = response.body().string();
                response.body().close();
                Log.d(TAG, "AllMsgs are: " + responseString);
                Type listType = new TypeToken<List<ChatMessageClass>>() {
                }.getType();
//                if(responseString.length()>2) {
                    messages = new Gson().fromJson(responseString, listType);
//                    Log.d(TAG, "messages.get(0).getMessage()= " + messages.get(0).getMessage());
//                    Log.d(TAG, "messages.get(0).getCreate_date_time()= " + messages.get(0).getCreate_date_time());
                    AddRecyclerView(messages, messages.size());
//                }
            }
        });
    }


    //
    //function AddRecyclerView
    //
    public void AddRecyclerView(final ArrayList<ChatMessageClass> yourList, int m) throws IOException {

        final LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager.setStackFromEnd(true);
//        LinearLayoutManager.setReverseLayout(true);
//        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager = LinearLayoutManager;
        final Context c= this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setLayoutManager(mLayoutManager);
                // specify an adapter (see also next example)
                mAdapter = new ChatMessageAdapterRV(c, yourList);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
        Log.d(TAG, "AddRecyclerView: adapter Added!");
    }

    private boolean sendChatMessage() {
        if (chatText.getText().toString().length() == 0 || chatText.getText().toString().trim().length()==0) {
            return false;
        }
//            Calendar cl=Calendar.getInstance();
//            String date = "" +cl.get(Calendar.YEAR)+"-" + cl.get(Calendar.MONTH) + "-" + cl.get(Calendar.DAY_OF_MONTH)+"-"+cl.get(Calendar.HOUR_OF_DAY)+"-"+cl.get(Calendar.MINUTE)+"-"+cl.get(Calendar.SECOND);
        String date = getTimeStamp();
        msg = new ChatMessageClass(SharedPreferencesManage.getInstance().getUserId(), receiver_id, chatText.getText().toString(), date);
//        Log.d(TAG, "sendChatMessage: receiver_id ="+receiver_id);
        Gson gson = new Gson();
        String json = gson.toJson(msg);
        Log.d(TAG, "sendChatMessage:ChatMessageClass json= "+json);
//        final OkHttpClient client = new OkHttpClient();
        Log.d(TAG, "sendChatMessage: URL="+ URLs.URL_SEND_MESSAGE);
        Request request = new Request.Builder()
                .url(URLs.URL_SEND_MESSAGE)
                .post(RequestBody.create(Constants.JSON, json))
                .build();

        // Get a handler that can be used to post to the main thread
        SharedPreferencesManage.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                response.body().close();
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
            }
        });
        if(receiver_id!=SharedPreferencesManage.getInstance().getUserId())
            messages.add(msg);
        chatText.setText("");
        try {
            AddRecyclerView(messages, messages.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        scrollToBottom();
        return true;
    }

    //
    //function   uses Get user details
    //
    public void AsyncGetNameOfReceiver(String url) throws Exception {
//        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        SharedPreferencesManage.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "onFailure:async task  ");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                String responseString;
                responseString = response.body().string();
                Log.d(TAG, "onResponse: AsyncGetNameOfReceiver=" + responseString);
                response.body().close();
                Type listType = new TypeToken<List<UserClass>>() {
                }.getType();
                receiverUser = new Gson().fromJson(responseString, listType);
                try {
//                                Log.d(TAG, "onResponse: "+receiverUser.get(0).getFname()+" "+receiverUser.get(0).getLname());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (receiverUser.size() > 0)
                                setTitle("Chat " + receiverUser.get(0).getFname() + " " + receiverUser.get(0).getLname());
//                                            receiverNameTV.setText(receiverUser.get(0).getFname()+" "+receiverUser.get(0).getLname());
                            else
                                Log.d(TAG, "onResponse: didn't get user details from server");
                        }
                    });


                } catch (Exception e) {
                    Log.i(TAG, "run: receiverUser se precaka");
                    e.printStackTrace();
                }
            }
        });
    }
}
