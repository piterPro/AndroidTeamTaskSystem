package com.piter.piterdiplomna3.adapters;

import android.content.Context;
import android.content.Intent;
;
import com.piter.piterdiplomna3.activities.ChatActivity;
import java.util.ArrayList;
import android.view.ViewGroup;
import android.widget.TextView;

import com.piter.piterdiplomna3.ObjectClasses.UserClass;
import com.piter.piterdiplomna3.R;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.myViewHolder>{

        private Context context;
        public ArrayList<UserClass> usersList;//should be private?
        String TAG="TAG ChatUserAdapter";

         public Context getContext() {
        return context;
      }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class myViewHolder extends RecyclerView.ViewHolder {
//             each data item is just a string in this case
            CardView task_card_view;
            public TextView mChatUsernameTV;
            public TextView mChatLastMsgTV;

            public myViewHolder(View itemView) {

                super(itemView);
                task_card_view = (CardView) itemView.findViewById(R.id.chat_user_card_view);
                mChatUsernameTV = (TextView) itemView.findViewById(R.id.chatUserNameTV);
                mChatLastMsgTV = (TextView) itemView.findViewById(R.id.chatLastMsgTV);
            }

        }

        // Provide a suitable constructor (depends on the kind of dataset)

        public ChatUserAdapter(Context cont, ArrayList<UserClass> myDataset) {
            context=cont;
            usersList = myDataset;
        }
        public ChatUserAdapter(Context cont) {
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
        final UserClass selectedTask = usersList.get(position);

        Log.d(TAG, "onBindViewHolder:Users "+selectedTask.getId());
//        holder.mChatUsernameTV.setText(selectedTask.getFname()+" "+selectedTask.getLname());
        holder.mChatUsernameTV.setText(selectedTask.getFname()+" "+selectedTask.getLname());
        holder.mChatLastMsgTV.setText("last msg");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: klikna se ="+selectedTask.getId()+"i samo position ="+position);
                Intent intent = new Intent(context,ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",selectedTask.getId()+"");
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

    }

