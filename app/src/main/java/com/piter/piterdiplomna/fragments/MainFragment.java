package com.piter.piterdiplomna.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.piter.piterdiplomna.MyPreferencesActivity;
import com.piter.piterdiplomna.ObjectClasses.*;
import com.piter.piterdiplomna.R;
import com.piter.piterdiplomna.activities.MainActivity;
import com.piter.piterdiplomna.adapters.MainTasksAdapter;
import com.piter.piterdiplomna.helper.MyAlarmReceiver;
import com.piter.piterdiplomna.helper.MyDateHelper;
import com.piter.piterdiplomna.helper.SharedPreferencesManage;
import com.piter.piterdiplomna.helper.URLs;
import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;
//import com.tyczj.extendedcalendarview.CalendarProvider;
//import com.tyczj.extendedcalendarview.Event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment{
    String TAG="TAG MainFragment";
    View view;
    ArrayList<TaskClass> yourList;
//    ArrayList<UserClass> UserClass;
//    ArrayList<ChatMessageClass> ChatList = new ArrayList();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static MainFragment mainFragment;
    public static int ColorFlag=1;
    private Spinner MainMenuSpinner;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.c_fragment_main, container, false);
        MainMenuSpinner = (Spinner) view.findViewById(R.id.MainMenuspinner);

        List<String> listStatus = new ArrayList<String>(Arrays.asList(new String[]{"All", "Daily task", "I created tasks"}));//TODO: change to use the current status as first element or at least use listStatus from strings file

        ArrayAdapter<String> dataAdapterStatus = new ArrayAdapter<>(((Activity)view.getContext()),
                android.R.layout.simple_spinner_item, listStatus);
        dataAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        MainMenuSpinner.setAdapter(dataAdapterStatus);
        SpinnerMenuMake();

//        try {
//            AsyncGetTasksAndPrint(URLs.URL_FETCH_TASKS+"?id="+ MainActivity.user_id,"AddRecyclerView");
//        } catch (Exception e) {            e.printStackTrace();        }
        return view;
    }


    public void SpinnerMenuMake(){
        MainMenuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            if(currentSpinnerSatus.matches(holder.spinnerStatus.getSelectedItem().toString()))
//                                return;
                try {
                    String newStatus = MainMenuSpinner.getSelectedItem().toString();
//                                Log.d(TAG, "onItemClick: klikna spinnera");
                    try {
                        switch (newStatus){
                            case "Daily task": AsyncGetTasksAndPrint(URLs.URL_FETCH_TASKS+"?id="+ MainActivity.user_id+"&key=0","AddRecyclerView"); break;
//                            case "All": AsyncGetTasksAndPrint(URLs.URL_FETCH_TASKS+"?id="+ MainActivity.user_id+"&key=1","AddRecyclerView"); break;
                            case "I created tasks": AsyncGetTasksAndPrint(URLs.URL_FETCH_TASKS+"?id="+ MainActivity.user_id+"&key=2","AddRecyclerView");break;
                            default: AsyncGetTasksAndPrint(URLs.URL_FETCH_TASKS+"?id="+ MainActivity.user_id+"&key=1","AddRecyclerView"); break;

                        }

                    } catch (Exception e) {            e.printStackTrace();        }

                } catch (Exception e) {                                e.printStackTrace();                            }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {                        }
        });
    }

    //
    //function AsyncGetNameOfReceiver uses Get url and write that to main fragment
    //
    public void AsyncGetTasksAndPrint(final String url, final String S) throws Exception{
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d(TAG, "AsyncGetTasksAndPrint: URL="+url);
        SharedPreferencesManage.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "onFailure:async task  ");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.w(TAG, "run: Can't connect to server" );
                        Toast.makeText(getContext(), "Can't connect to server", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString;
                responseString = response.body().string();

                Log.d(TAG, "onResponse:FETCH_TASKS= "+responseString);
                response.body().close();
                    Type listType = new TypeToken<List<TaskClass>>() {
                    }.getType();
                    try {
                        yourList = new Gson().fromJson(responseString, listType);
                        if(url.contains("&key=1")){
                            AddNewDatabase();//
                            //redownload all the db and save it to local temp db
//                            CalendarProvider asdasd = new CalendarProvider();
//                            asdasd.deleteDB();
                            // OR this wich one actually delete the DB

//                            CalendarProvider tozi = new CalendarProvider();//CalendarProvider.CONTENT_URI
//                            if(getContext().databaseList().length>0) {
//                                Log.d(TAG, "onResponse: getContext().databaseList().length="+getContext().databaseList().length+" databaselist="+getContext().databaseList()[0].toString());
//                                getContext().deleteDatabase("Calendar");//TRIE KAKTO TRQBVA
//                                Log.d(TAG, "onResponse: iztri bazata");

                            
//                                getActivity().getContentResolver().delete(CalendarProvider.CONTENT_URI, CalendarProvider.ID+">0",null);
//                                Log.d(TAG, "onResponse: dropnaha li se vsi4ki?");
//
                        }

                    }catch(Exception e) {
                        if (responseString.contains("!DOCTYPE html")) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.w(TAG, "run: Error from the server" );
                                    Toast.makeText(getContext(), "Error from the server", Toast.LENGTH_SHORT).show();
                                }
                            });
                            return;
                        }else{Log.d(TAG, "onResponse: ne sudurja DOCTYPE ama ima gre6ka");
                            e.printStackTrace();}
                    }
                    //za da se izpulni vinagi ot glavnata ni6ka
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                AddRecyclerView(yourList, yourList.size());
                            } catch (IOException e) {
                                Log.i(TAG, "run: yourList se precaka");
                                e.printStackTrace();
                            }
                        }
                    });
            }
        });
    }

    //
    //function AddRecyclerView
    //
    public void AddRecyclerView(final ArrayList<TaskClass> yourList, int m) throws IOException {
        //recycler view
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
//        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                if(rv.getChildCount() > 0) {
//                    View childView = rv.findChildViewUnder(e.getX(), e.getY());
//                    if(rv.getChildPosition(childView) == [listview position]) {
//                        int action = e.getAction();
//                        switch (action) {
//                            case MotionEvent.ACTION_DOWN:
//                                rv.requestDisallowInterceptTouchEvent(true);
//                        }
//                    }
//                }
//                return false;
//            }

//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            }
//        });
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MainTasksAdapter(getContext(), yourList, mainFragment,getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        Log.d("TAG", "AddRecyclerView: adapter added!");
    }
    //
    //function AddNewDatabase
    //
    public void AddNewDatabase(){
        getActivity().getContentResolver().delete(CalendarProvider.CONTENT_URI, CalendarProvider.ID+">0",null);
        Log.d(TAG, "onResponse: dropnaha li se vsi4ki?");

        int sizeOfList = yourList.size();
        Log.d(TAG, "onResponse: sizeOfList="+sizeOfList);
        for(int i=0;i<sizeOfList;i++) {
            TaskClass temp = yourList.get(i);
            ContentValues values = new ContentValues();
            values.put(CalendarProvider.COLOR, ColorFlag++);//Event.COLOR_RED);
            if(ColorFlag==5)    ColorFlag=1;
            values.put(CalendarProvider.ID, temp.getId());
            values.put(CalendarProvider.DESCRIPTION, temp.getDescription());
            values.put(CalendarProvider.begin_date, temp.getBegin_date());
            values.put(CalendarProvider.end_date, temp.getEnd_date());
            values.put(CalendarProvider.STATUS, temp.getStatus());
            values.put(CalendarProvider.TITLE, temp.getTitle());
            values.put(CalendarProvider.user_made_by_id, temp.getUser_made_by_id());
            values.put(CalendarProvider.user_made_for_id, temp.getUser_made_for_id());
            values.put(CalendarProvider.EVENT, "Test Event №"+i);
//
            Calendar cal = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();

            String begin_date = temp.getBegin_date();
            cal=SharedPreferencesManage.getInstance().convertFromString(begin_date);
//                                Log.d(TAG, "initializeCalendar:is it correct? day1" + cal.getTime().toString());
            int julianDay = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
//                                Log.d(TAG, "initializeCalendar: julianDay=" + julianDay);

            values.put(CalendarProvider.START, cal.getTimeInMillis());
            values.put(CalendarProvider.START_DAY, julianDay);


            String end_date = temp.getEnd_date();
            cal=SharedPreferencesManage.getInstance().convertFromString(end_date);
//                                Log.d(TAG, "initializeCalendar: day2" + cal.getTime().toString());
            int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));

            values.put(CalendarProvider.END, cal.getTimeInMillis());
            values.put(CalendarProvider.END_DAY, endDayJulian);//CalendarProvider.CONTENT_URI
            getActivity().getContentResolver().insert(CalendarProvider.CONTENT_URI, values);//(ArrayList<ContentValues>)(ArrayList<TaskClass>) yourList
            String[] params = {""+temp.getId()};//task_id
            String[] select = {"task_id"};//
            Cursor cur = getActivity().getContentResolver().query(CalendarProvider.CONTENTNOTIF_URI,select,"id=?",params,null);
            if(!cur.isBeforeFirst())//this notification has been dismised
            {
                CreateAlarm(temp.getEnd_date(), temp);
                Log.d(TAG, "AddNewDatabase:  title="+temp.getTitle());
            }
            else Log.d(TAG, "AddNewDatabase: This notification has been dismissed title="+temp.getTitle());
        }
    }
    public void CreateAlarm(String endDate,TaskClass temp) {
        if(temp.getStatus().contains("Done"))
            return;
        Intent notifyIntent = new Intent(getContext(), MyAlarmReceiver.class);
        notifyIntent.putExtra("title","End of task"+temp.getTitle());
        notifyIntent.putExtra("description",temp.getDescription());
        notifyIntent.putExtra("id",temp.getId());
        MyDateHelper converter = new MyDateHelper();
        Calendar cl = converter.convertFromString(endDate);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (getContext(), 9562094, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getContext());
        String strUserName = SP.getString("oppositeOfDelay", "3600000");
        int oppositeOfDelay = Integer.parseInt(strUserName);
        alarmManager.set(AlarmManager.RTC_WAKEUP, (cl.getTimeInMillis() - 3600000),
                pendingIntent);

//        Toast.makeText(getContext(), "Alarm added ", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onNavigationItemSelected: time" + (System.currentTimeMillis()));
    }
    public void RemoveAlarm(){
        Intent notifyIntent = new Intent(getContext(), MyAlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (getContext(), 9562094, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
//            alarmManager.set(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + 1000 * 5),
//                    pendingIntent);
//            Toast.makeText(this, "Alarm added ", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "onNavigationItemSelected: time" + (System.currentTimeMillis()));
//            if(alarmManager!=null){
//                alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }

}
