package com.piter.piterdiplomna3.adapters;

import android.content.Context;
import android.content.Intent;
;
import com.piter.piterdiplomna3.activities.ChatActivity;

import java.io.IOException;
import java.util.ArrayList;

import android.view.ViewGroup;
import android.widget.TextView;

import com.piter.piterdiplomna3.ObjectClasses.UserClass;
import com.piter.piterdiplomna3.R;
import com.piter.piterdiplomna3.helper.SharedPreferencesManage;
import com.piter.piterdiplomna3.helper.URLs;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;


public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.myViewHolder>{

        private Context context;
        public ArrayList<UserClass> usersList;
        String TAG="TAG ListUserAdapter";
        private boolean wait = false;
        private String lastMsgTime="";

         public Context getContext() {
        return context;
      }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class myViewHolder extends RecyclerView.ViewHolder {
//             each data item is just a string in this case
            CardView task_card_view;
            public TextView mUsernameTV;
            public TextView mLastMsgTV;

            public myViewHolder(View itemView) {

                super(itemView);
                task_card_view = (CardView) itemView.findViewById(R.id.chat_user_card_view);
                mUsernameTV = (TextView) itemView.findViewById(R.id.chatUserNameTV);
                mLastMsgTV = (TextView) itemView.findViewById(R.id.chatLastMsgTV);
            }

        }

        // Provide a suitable constructor (depends on the kind of dataset)

        public ListUserAdapter(Context cont, ArrayList<UserClass> myDataset) {
            context=cont;
            usersList = myDataset;
        }
        public ListUserAdapter(Context cont) {
            context=cont;
        }
    public void InsertData(ArrayList<UserClass> myDataset){usersList = myDataset;}

        // Create new views (invoked by the layout manager)
        @Override
        public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.d_user_chat_cardview_entry_layout, parent, false);
            // set the view's size, margins, paddings and layout parameters
            myViewHolder vh = new myViewHolder(view);
            return vh;
        }


    @Override
    public void onBindViewHolder(final myViewHolder holder, final int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final UserClass selectedUser = usersList.get(position);

        Log.d(TAG, "onBindViewHolder:Users "+selectedUser.getId());
//        holder.mUsernameTV.setText(selectedTask.getFname()+" "+selectedTask.getLname());
        holder.mUsernameTV.setText(selectedUser.getFname()+" "+selectedUser.getLname());
        try {
            Log.d(TAG, "onBindViewHolder: URL="+URLs.URL_FETCH_LASTMESSAGE+"?id="+ SharedPreferencesManage.getInstance().getUserId() +"&&id2="+selectedUser.getId());
            AsyncGetLastMsg(URLs.URL_FETCH_LASTMESSAGE+"?id="+ SharedPreferencesManage.getInstance().getUserId() +"&&id2="+selectedUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        wait= true;
        while(wait==true){}

        holder.mLastMsgTV.setText(lastMsgTime);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: klikna se ="+selectedUser.getId()+"i samo position ="+position);
                Intent intent = new Intent(context,ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",selectedUser.getId()+"");
                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            if(usersList != null)
                return usersList.size();
            return 0;
        }
    //
    //function AsyncGetLastMsg uses Get last message between two users
    //
    public void AsyncGetLastMsg(String url) throws Exception{
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
                String responseString = response.body().string();
                response.body().close();
                //za da se izpulni vinagi ot glavnata ni6ka
                            lastMsgTime = responseString;
                            wait=false;
                        }
        });
    }

}

