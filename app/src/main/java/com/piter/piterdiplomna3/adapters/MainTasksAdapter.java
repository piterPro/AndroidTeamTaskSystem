package com.piter.piterdiplomna3.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.piter.piterdiplomna3.ObjectClasses.TaskClass;
import com.piter.piterdiplomna3.R;
import com.piter.piterdiplomna3.activities.MainActivity;
import com.piter.piterdiplomna3.fragments.CommentsFragment;
import com.piter.piterdiplomna3.fragments.MainFragment;
import com.piter.piterdiplomna3.helper.SharedPreferencesManage;
import com.piter.piterdiplomna3.helper.URLs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MainTasksAdapter extends RecyclerView.Adapter<MainTasksAdapter.myViewHolder> implements CommentsFragment.OnFragmentInteractionListener {
    private Context context;
    private ArrayList<TaskClass> taskList;
    String TAG="TAG MainTasksAdapter";

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        CardView task_card_view;
        public TextView titleTV;
        public TextView statusTV;
        public TextView descriptionTV;
        public TextView descriptionHintTV;
        public Button mSendBtn;
        public CommentsFragment mfragment;
        public Spinner spinnerStatus;

        public myViewHolder(View itemView) {

            super(itemView);
            task_card_view = (CardView) itemView.findViewById(R.id.task_card_view);
            titleTV= (TextView) itemView.findViewById(R.id.titleTV);
            statusTV= (TextView) itemView.findViewById(R.id.statusTV);
            descriptionTV= (TextView) itemView.findViewById(R.id.descriptionTV);
            descriptionHintTV= (TextView) itemView.findViewById(R.id.DescriptionHintTV);
            mSendBtn = (Button) itemView.findViewById(R.id.commentBtn);
            spinnerStatus = (Spinner) itemView.findViewById(R.id.statusChangeSpinner);
            List<String> listStatus = new ArrayList<String>(Arrays.asList(new String[]{"Pending", "Done"}));//TODO: change to use the current status as first element or at least use listStatus from strings file

            ArrayAdapter<String> dataAdapterStatus = new ArrayAdapter<>(((Activity)itemView.getContext()),
                    android.R.layout.simple_spinner_item, listStatus);
            dataAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerStatus.setAdapter(dataAdapterStatus);

            statusTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("TAG mainTaskAdapter", "onClick: q klikna me ");
                    if(spinnerStatus.getVisibility()==View.GONE) {
                        spinnerStatus.setVisibility(View.VISIBLE);
                        task_card_view.performClick();
//                        titleTV.performClick();
                    }else
                        spinnerStatus.setVisibility(View.GONE);
                }
            });
//            }
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
                holder.spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            if(currentSpinnerSatus.matches(holder.spinnerStatus.getSelectedItem().toString()))
//                                return;
                        try {
                            String newStatus = holder.spinnerStatus.getSelectedItem().toString();
//                                Log.d(TAG, "onItemClick: klikna spinnera");
                            Log.d(TAG, "onItemClick: URL=" + URLs.URL_UPDATE_STATUS + "?id=" + selectedTasks.getId() + "&status=" + newStatus+"&time=");
                            AsyncUpdateStatus(URLs.URL_UPDATE_STATUS + "?id=" + selectedTasks.getId() + "&status=" + newStatus, context);
                            selectedTasks.setStatus(newStatus);
                            holder.statusTV.setText(newStatus);

                        } catch (Exception e) {                                e.printStackTrace();                            }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {                        }
                });


                if (holder.descriptionTV.getVisibility() == View.GONE) {
                    holder.descriptionTV.setVisibility(View.VISIBLE);
                    holder.descriptionHintTV.setVisibility(View.VISIBLE);

                    holder.mfragment = CommentsFragment.newInstance(""+selectedTasks.getId(),"0");
//                    Log.d(TAG, "holder.itemView.onClick: holder.getItemId()="+selectedTasks.getId());
//                    final String currentSpinnerSatus = holder.spinnerStatus.getSelectedItem().toString();

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
//        holder.mIdTV.setText(selectedTasks.getId()+"");
        holder.titleTV.setText(selectedTasks.getTitle());
        holder.descriptionTV.setText(selectedTasks.getDescription());
        holder.statusTV.setText(selectedTasks.getStatus());

        //TODO: check if alarm needs to be set and set alarm here for the selected task
//        SharedPreferencesManage.getInstance().getToken()
        Log.d(TAG, "onBindViewHolder: zadade se alarma");
        selectedTasks.setAlarm();

//
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(taskList != null)
            return taskList.size();
        return 0;
    }
    public void AsyncUpdateStatus(String url,final Context context) throws Exception{
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Get a handler that can be used to post to the main thread
        SharedPreferencesManage.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.w(TAG, "run: Can't connect to the server" );
                        Toast.makeText(context, "Can't connect to the server", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
                return;
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.w(TAG, "status Changed" );
                        Toast.makeText(context, "status Changed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        });
    }
}
