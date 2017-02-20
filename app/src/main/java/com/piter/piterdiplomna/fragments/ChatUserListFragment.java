package com.piter.piterdiplomna.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piter.piterdiplomna.ObjectClasses.UserClass;
import com.piter.piterdiplomna.R;
import com.piter.piterdiplomna.adapters.ListUserAdapter;
import com.piter.piterdiplomna.helper.SharedPreferencesManage;
import com.piter.piterdiplomna.helper.URLs;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatUserListFragment extends Fragment {
//    static final String urlGetTasks = MainActivity.urlIP + "/com.piter.jersey.first/api/v2/manage/users";
    String TAG = "TAG ChatUserListFr";
    View view;
    ArrayList<UserClass> UserList = new ArrayList();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ChatUserListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.d_fragment_chat_user_list, container, false);


        Initialize(view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.chatUserRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        return view;
    }

    public void Initialize(View view) {
        try {
            AsyncGetAndPrint(URLs.URL_FETCH_USERS+"?id=all&key="+SharedPreferencesManage.getInstance().getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    //function AsyncGetNameOfReceiver uses Get url and write that to main fragment
    //
    public void AsyncGetAndPrint(String url) throws Exception {
//        OkHttpClient client = new OkHttpClient();
        Log.i("TAG chatUserList", ":url="+url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        SharedPreferencesManage.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "chatUserList:async task  ");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.w(TAG, "run: Can't connect to the server" );
                        Toast.makeText(getContext(), "Can't connect to the server", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString;
                responseString = response.body().string();
                Log.d(TAG, "onResponse: responseString=" + responseString);
                response.body().close();
                Type listType = new TypeToken<List<UserClass>>() {
                }.getType();
                UserList = new Gson().fromJson(responseString, listType);
                //za da se izpulni vinagi ot glavnata ni6ka
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                        try {
                            setAdapterUsers(UserList);
                        } catch (Exception e) {
                            Log.i("TAG", "run: userCardViewList se precaka");
                            e.printStackTrace();
                        }
//                    }
//                });
            }
        });
    }

    public void setAdapterUsers(ArrayList<UserClass> yourList) {
        //recycler view


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // specify an adapter (see also next example)
        mAdapter = new ListUserAdapter(getContext(),yourList);

        // use a linear layout manager
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }
}
