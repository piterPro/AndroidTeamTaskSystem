package com.piter.piterdiplomna3.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piter.piterdiplomna3.ObjectClasses.ChatMessageClass;
import com.piter.piterdiplomna3.R;
import com.piter.piterdiplomna3.activities.MainActivity;

import java.util.ArrayList;

import static android.view.View.LAYOUT_DIRECTION_LTR;
import static android.view.View.LAYOUT_DIRECTION_RTL;

public class ChatArrayAdapterRV extends RecyclerView.Adapter<ChatArrayAdapterRV.myViewHolder> {
    private Context context;
    private ArrayList<ChatMessageClass> chatMsgList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class myViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
//        CardView chat_card_view;
        public TextView mSingleMsg;
        public TextView timeSent;//data i 4as

        public myViewHolder(View itemView) {

            super(itemView);
//            chat_card_view = (CardView) itemView.findViewById(R.id.chat_card_view);
            mSingleMsg = (TextView) itemView.findViewById(R.id.SingleMessage);
            timeSent = (TextView) itemView.findViewById(R.id.timeTV);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatArrayAdapterRV(Context cont, ArrayList<ChatMessageClass> myDataset) {
        context = cont;
        chatMsgList = myDataset;
        Log.d("TAG ChatArrayAdapterRV","izvika se tupiq konstruktor za chat-a");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatArrayAdapterRV.myViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.d_chat_cardview_bubble_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ChatArrayAdapterRV.myViewHolder vh = new ChatArrayAdapterRV.myViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        boolean side;
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ChatMessageClass selectedMsg = chatMsgList.get(position);
        Log.d("TAG ChatArrayAdapterRV", "onBindViewHolder: position= "+position+": message= "+selectedMsg.getMessage()+" time= "+selectedMsg.getCreate_date_time());
        //LayoutParams for the gravity on message
        LinearLayout.LayoutParams paramsMsg = new LinearLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
//        if(selectedMsg.getUser_send_id()== MainActivity.user_id) {
//            side=true;
//        }else side=false;

        side=selectedMsg.getUser_send_id()== MainActivity.user_id?true:false;
//        Log.d("TAG", "onBindViewHolder: side2= "+side2);
        Log.d("TAG", "onBindViewHolder: side= "+side);

        if (side) {
            holder.itemView.setBackgroundResource( R.drawable.chatbubbleaa);
            paramsMsg.gravity = Gravity.RIGHT;
//            holder.itemView.setLayoutDirection(LAYOUT_DIRECTION_RTL);
            paramsMsg.setMarginStart(350);
//            paramsMsg.setLayoutDirection(LAYOUT_DIRECTION_RTL);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.itemView.setForegroundGravity(side ? Gravity.RIGHT : Gravity.LEFT);
//            }
        }else{
            holder.itemView.setBackgroundResource( R.drawable.chatbubblebb);
            paramsMsg.gravity = Gravity.LEFT;
            paramsMsg.setMarginEnd(350);
//            paramsMsg.setLayoutDirection(LAYOUT_DIRECTION_LTR);
            holder.itemView.setLayoutDirection(LAYOUT_DIRECTION_LTR);
        }
        holder.mSingleMsg.setText(selectedMsg.getMessage());
        holder.timeSent.setText(selectedMsg.getCreate_date_time());//.substring(        0,selectedMsg.getCreate_date_time().length()-2)
        holder.itemView.setLayoutParams(paramsMsg);//
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (chatMsgList != null)
            return chatMsgList.size();
        return 0;
    }
}
