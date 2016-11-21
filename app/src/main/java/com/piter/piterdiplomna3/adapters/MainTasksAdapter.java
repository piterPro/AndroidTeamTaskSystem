package com.piter.piterdiplomna3.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.piter.piterdiplomna3.ObjectClasses.TaskClass;
import com.piter.piterdiplomna3.R;
import com.piter.piterdiplomna3.activities.MainActivity;
import com.piter.piterdiplomna3.fragments.CommentsFragment;
import com.piter.piterdiplomna3.fragments.MainFragment;

import java.util.ArrayList;

public class MainTasksAdapter extends RecyclerView.Adapter<MainTasksAdapter.myViewHolder> implements CommentsFragment.OnFragmentInteractionListener {
    private Context context;
    private ArrayList<TaskClass> taskList;
    String TAG="TAG MainTasksAdapter";
//    public View CurrentElementView;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        CardView task_card_view;
        public TextView titleTV;
        public TextView statusTV;
        public TextView descriptionTV;
        public TextView descriptionHintTV;
        public Button msendBtn;
        public CommentsFragment mfragment;

        public myViewHolder(View itemView) {

            super(itemView);
            task_card_view = (CardView) itemView.findViewById(R.id.task_card_view);
            titleTV= (TextView) itemView.findViewById(R.id.titleTV);
            statusTV= (TextView) itemView.findViewById(R.id.statusTV);
            descriptionTV= (TextView) itemView.findViewById(R.id.descriptionTV);
            descriptionHintTV= (TextView) itemView.findViewById(R.id.DescriptionHintTV);
            msendBtn = (Button) itemView.findViewById(R.id.commentBtn);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainTasksAdapter(Context cont, ArrayList<TaskClass> myDataset, MainFragment fragment) {
        context = cont;
        taskList = myDataset;
        MainActivity.fragment = fragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainTasksAdapter.myViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.c_task_cardview_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        myViewHolder vh = new myViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final myViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final TaskClass selectedTasks = taskList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                RelativeLayout item = (RelativeLayout)holder.itemView.findViewById(R.id.comment_fragment_container);
                View child = View.inflate(context, R.layout.cc_fragment_comments_list, null);
//                View child = View.inflate(context, R.layout.g_suggest, null);

                if (holder.descriptionTV.getVisibility() == View.GONE) {
                    holder.descriptionTV.setVisibility(View.VISIBLE);//DescriptionHintTV
                    holder.descriptionHintTV.setVisibility(View.VISIBLE);//DescriptionHintTV

                    holder.mfragment = CommentsFragment.newInstance(""+selectedTasks.getId(),"0");
                    Log.d(TAG, "holder.itemView.onClick: holder.getItemId()="+selectedTasks.getId());
                    if(holder.mfragment.ready=="false"){
                        //whait!
                            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show();
                    }


                    item.addView(child);
                    int cdi = child.getId();
                    if(MainActivity.fragment.isAdded())
                        MainActivity.fragment
                            .getChildFragmentManager()
                            .beginTransaction()//.add(R.id.comment_fragment_container,holder.mfragment)//replace
                            .add(cdi, holder.mfragment)
                            .commit();
                } else {
                    holder.descriptionTV.setVisibility(View.GONE);
                    holder.descriptionHintTV.setVisibility(View.GONE);
                    item.removeAllViews();
                    MainActivity.fragment
                            .getChildFragmentManager()
                            .beginTransaction()
                            .remove(holder.mfragment)
                            .commit();
                }
                
            }
        });
//            holder.mIdTV.setText(selectedTasks.getId()+"");
            holder.titleTV.setText(selectedTasks.getTitle());
            holder.descriptionTV.setText(selectedTasks.getDescription());
            holder.statusTV.setText(selectedTasks.getStatus());

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(taskList != null)
            return taskList.size();
        return 0;
    }
}
