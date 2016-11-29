package com.piter.piterdiplomna3.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.piter.piterdiplomna3.fragments.CommentsFragment;
import com.piter.piterdiplomna3.fragments.EditInfoFragment;
import com.piter.piterdiplomna3.fragments.TaskAddFragment;
import com.piter.piterdiplomna3.fragments.CalendarFragment;
import com.piter.piterdiplomna3.fragments.ChatUserListFragment;
import com.piter.piterdiplomna3.fragments.MainFragment;
import com.piter.piterdiplomna3.R;
import com.piter.piterdiplomna3.gcm.GCMRegistrationIntentService;
import com.piter.piterdiplomna3.helper.Constants;
import com.piter.piterdiplomna3.helper.NotificationHandler;
import com.piter.piterdiplomna3.helper.SharedPreferencesManage;
import com.piter.piterdiplomna3.helper.URLs;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static public Context con;
    Toolbar toolbar;
    NavigationView navigationView;
    static public int activityPosition=-1;
    static public long BeginDate=0;
    static public long EndDate=0;
    static public int WhichDate=0;//1= begin 2=end
    static public int user_id;
//    static public TextView drawerUserName;
    static public TextView drawerPosition;
//    ArrayList<UserClass> UserClass;
    static public String TAG = "TAG MainActivity";
    public static MainFragment fragment = new MainFragment();
    //Broadcast receiver to receive broadcasts
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    DrawerLayout drawer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_main);
        if(!SharedPreferencesManage.getInstance().isLoggedIn()){
            continueUserLog();//exit this class and start other activity
            return;
        }
        Intent intent1;
        if(SharedPreferencesManage.getInstance().getToken().isEmpty()) {
            intent1 = new Intent(this, GCMRegistrationIntentService.class);
            startService(intent1);
            Log.d(TAG, "user to be registered start service");
        }else
            Log.d(TAG, "already registered user with token: "+SharedPreferencesManage.getInstance().getToken());
        user_id = SharedPreferencesManage.getInstance().getUserId();
        Log.d(TAG, "onCreate: user id ="+user_id);

        //set the today date as default
        if(BeginDate==0)
            SetDateToday();
        con=getBaseContext();
        //set the initial MainFragment
        MainFragment fragment = new MainFragment();
        activityPosition=0;
        android.support.v4.app.FragmentTransaction fragmentTransaction=
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragment.mainFragment = fragment;
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerPosition = (TextView) findViewById(R.id.drawerUserPositionTV);
        drawer.getOverlay();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Log.d(TAG, "onCreate: link="+ URLs.URL_FETCH_USERS+"?id="+user_id );
        try {            drawerUserDetails();        }
        catch (Exception e) {            e.printStackTrace();        }
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkGplayServices();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.PUSH_NOTIFICATION)) {
                    //Getting message data
                    Log.d(TAG, "onReceive: push notification da se predade na Chat activity !!!");
                    String user_send_id = intent.getStringExtra("user_send_id");
                    String user_to_id = intent.getStringExtra("user_to_id");
                    String message = intent.getStringExtra("message");
                    String create_date_time = intent.getStringExtra("create_date_time");
                    String title = intent.getStringExtra("title");
                    //processing the message to add it in current thread
                    Log.d(TAG, "onReceive to be send to Chat Activity:user_send_id= "+user_send_id+" ,user_to_id= "+user_to_id+ " ,message= "+message+ " ,create_date_time= "+create_date_time);
//                    String collapse_key = intent.getStringExtra("collapse_key");
//                    Log.d(TAG, "onReceive:collapse_key "+collapse_key);
                    NotificationHandler notificationHandler = new NotificationHandler(getApplicationContext());
                    Intent intent2 = new Intent(getApplicationContext(), ChatActivity.class);
                    intent2.putExtra("id", intent.getStringExtra("user_send_id"));
                    notificationHandler.showNotificationMessage(title, message, intent2);
                }
                else
                {

                    Log.d(TAG, "onReceive: doide alarmata !!!");
                }
            }
        };
    }//end of onCreate

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            if(this is the main fragment ) then close the app
            if(activityPosition>0){
                activityPosition=0;
                MainFragment fragment=new MainFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction=
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }else {
                super.finishAffinity();
//                unregisterReceiver(mBroadcastReceiver);
//                unregisterReceiver(mBroadcastStopReceiver);
            }
        }
    }


    private boolean continueUserLog() {
        Log.d(TAG, "continueUserLog: from shrdPref");
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("id",SharedPreferencesManage.getInstance().getUserId()+"");
        startActivity(intent);
        return true;
    }


    //
    //function drawerUserDetails that populates the user name
    //
    public void drawerUserDetails() throws IOException {
        final String O=SharedPreferencesManage.getInstance().getUserName();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);
        final TextView drawerUserName = (TextView ) v.findViewById(R.id.drawerUserNameTV);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                drawerUserName.setText(O);
                Log.d(TAG, "run drawerUserDetails:O= "+O);
            }
        });
    }



    public void SetDateToday() {
        BeginDate=EndDate=System.currentTimeMillis();
        Log.d(TAG, "SetDateToday: in millis"+BeginDate );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else
        if (id == R.id.logout_menu) {
            SharedPreferencesManage.getInstance().logout();
            try {
                SharedPreferencesManage.getInstance().updateURL(URLs.URL_DELETE_TOKEN+"?id="+user_id);
            } catch (Exception e) {
                Log.d(TAG, "onOptionsItemSelected: URL_DELETE_TOKEN fail");        e.printStackTrace();            }
            if(!SharedPreferencesManage.getInstance().isLoggedIn()){
                continueUserLog();//exit this class and start other activity
                
            }
            return true;
        }else
        if (id == R.id.Edit_info) {
            activityPosition++;
            EditInfoFragment fragment=new EditInfoFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction=
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.support.v4.app.FragmentTransaction fragmentTransaction=
                getSupportFragmentManager().beginTransaction();

        if (id == R.id.nav_home) {
            activityPosition=0;
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        } else if (id == R.id.nav_add_task) {
            activityPosition++;
            TaskAddFragment fragment=new TaskAddFragment();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
        } else if (id == R.id.nav_calendar) {
            activityPosition++;
            CalendarFragment fragment=new CalendarFragment();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
        } else if (id == R.id.nav_manage) {
//            Calendar cl = Calendar.getInstance();cl.getTimeInMillis()+50000
//            Intent notifyIntent = new Intent(this, MyAlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast
//                (getBaseContext(), 9562090, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
////            alarmManager.set();
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  (System.currentTimeMillis() + 1000*5),
//                1000 * 30 , pendingIntent);
//
//        Toast.makeText(this, "Alarm added ", Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "onNavigationItemSelected: time"+(System.currentTimeMillis()));
//        if(alarmManager!=null)
//            alarmManager.cancel(pendingIntent);

//                intentArray.add(pi);

//            }
        }else if (id == R.id.nav_chat) {
            ChatUserListFragment fragment = new ChatUserListFragment();
            activityPosition++;
            fragmentTransaction.replace(R.id.fragment_container,fragment);
        } else if (id == R.id.nav_send) {
            CommentsFragment fragment=new CommentsFragment().newInstance("5","");
            activityPosition++;
            fragmentTransaction.replace(R.id.fragment_container, fragment);
        }

        fragmentTransaction.commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Unregistering receivers
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//        unregisterReceiver(mBroadcastReceiver);
//        unregisterReceiver(mBroadcastStopReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.PUSH_NOTIFICATION));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mBroadcastReceiver);
//        unregisterReceiver(mBroadcastStopReceiver);
    }
    public void checkGplayServices(){
        //if the google play service is not in the device app won't work
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            Intent intent1;
            if(SharedPreferencesManage.getInstance().getToken().isEmpty()) {
                intent1 = new Intent(this, GCMRegistrationIntentService.class);
                startService(intent1);
                Log.d(TAG, "user to be registered start service");
            }else
                Log.d(TAG, "already registered user with token: "+SharedPreferencesManage.getInstance().getToken());

        }
    }

    public void updateURL(final String url) throws Exception{
        Log.d(TAG, "updateURL: "+url);
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
/////
////
///
//
