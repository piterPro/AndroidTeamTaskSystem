package com.piter.piterdiplomna3.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piter.piterdiplomna3.ObjectClasses.CommentClass;
import com.piter.piterdiplomna3.ObjectClasses.UserClass;
import com.piter.piterdiplomna3.R;
import com.piter.piterdiplomna3.activities.MainActivity;
import com.piter.piterdiplomna3.fragments.CommentsFragment;
import com.piter.piterdiplomna3.helper.SharedPreferencesManage;
import com.piter.piterdiplomna3.helper.URLs;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Piter on 21/11/2016.
 */

public class CommentAdapter extends ArrayAdapter<CommentClass>{
    private Context context;
    private boolean wait;
    private ArrayList<CommentClass> commentList;
    String TAG="TAG MainTasksAdapter";
    private ArrayList<UserClass> mUserDetails;

    public TextView nameOfPersonTV;
    public TextView dataTV;
    public TextView textTV;

    public CommentAdapter(Context context1, int resource, ArrayList<CommentClass> objects) {
        super(context1, resource, objects);
//        if (objects.isEmpty()){
//            commentList = //empty list
//        }
        context = context1;
        commentList = objects;
    }

    public CommentAdapter(Context context, int resource, ArrayList<CommentClass> commentList, Context context1) {
        super(context, resource);
        this.commentList = commentList;
        this.context = context1;
        context=context1;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommentClass holder = commentList.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cc_comment_fragment, parent, false);
        }
        if (convertView != null) {
                nameOfPersonTV = (TextView) convertView.findViewById(R.id.CommentPersonNameTV);
            dataTV= (TextView) convertView.findViewById(R.id.CommentDateTV);
            textTV= (TextView) convertView.findViewById(R.id.CommentTextTV);

            try {
                getUserDetails(URLs.URL_FETCH_USERS + "?id=" + holder.getUser_id());
            } catch (Exception e) {
                e.printStackTrace();
            }
            wait=true;
            while(wait==true){//wait
                         }

            Log.d(TAG, "getStringValue: izlezna ot try bloka getUserDetails");
        }

        ((Activity) getContext()).runOnUiThread(new Runnable() {//yess it works! (thunder)
            @Override
            public void run() {
                nameOfPersonTV.setText(""+mUserDetails.get(0).getFname()+" "+mUserDetails.get(0).getLname());
                textTV.setText(holder.getText());
                dataTV.setText(holder.getCreate_date_time());
            }
        });

        return convertView;
    }
    //
    //function getUserDetails uses Get url and write that to main fragment
    //
    public void getUserDetails(String url) throws Exception{
//        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Log.d(TAG, "getUserDetails: url="+url);
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
                Log.d("TAG", "onResponse: getUserDetails"+responseString);

                Type listType = new TypeToken<List<UserClass>>() {
                }.getType();
                mUserDetails = new Gson().fromJson(responseString, listType);
                wait=false;
            }
        });
    }
}