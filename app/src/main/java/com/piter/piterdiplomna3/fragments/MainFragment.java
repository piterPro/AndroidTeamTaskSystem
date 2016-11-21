package com.piter.piterdiplomna3.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.piter.piterdiplomna3.ObjectClasses.*;
import com.piter.piterdiplomna3.R;
import com.piter.piterdiplomna3.activities.MainActivity;
import com.piter.piterdiplomna3.adapters.MainTasksAdapter;
import com.piter.piterdiplomna3.helper.SharedPreferencesManage;
import com.piter.piterdiplomna3.helper.URLs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.*;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment{
//    public int pidInput=-1;
//    String SpinnerChoice;
    String TAG="TAG MainFragment";
    View view;
    ArrayList<TaskClass> yourList;
    ArrayList<UserClass> UserClass;
//    ArrayList<String> UserName = new ArrayList();
    ArrayList<ChatMessageClass> ChatList = new ArrayList();
//    static final String urlGetTasks = MainActivity.urlIP+"/com.piter.jersey.first/api/v2/manage/tasks?id=";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
//MainTasksAdapter
    public static MainFragment mainFragment;
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.c_fragment_main, container, false);
        try {
            AsyncGetTasksAndPrint(URLs.URL_FETCH_TASKS+"?id="+ MainActivity.user_id,"AddRecyclerView");
        } catch (Exception e) {            e.printStackTrace();        }
        return view;
    }


    //
    //function AsyncGetNameOfReceiver uses Get url and write that to main fragment
    //
    public void AsyncGetTasksAndPrint(String url, final String S) throws Exception{
//        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
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
//                if (S == "AddRecyclerView") {
                    Type listType = new TypeToken<List<TaskClass>>() {
                    }.getType();
                    try {
                        yourList = new Gson().fromJson(responseString, listType);
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
                        }else{Log.d(TAG, "onResponse: ne sudurja DOCTYPE");}
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
        mAdapter = new MainTasksAdapter(getContext(), yourList, mainFragment);
        mRecyclerView.setAdapter(mAdapter);

        Log.d("TAG", "AddRecyclerView: adapter added!");
    }



    public void YouDaMen(){
        Toast.makeText(getContext(), "You are the man!", Toast.LENGTH_SHORT).show();
    }
// nz za kakvo mi e

}
