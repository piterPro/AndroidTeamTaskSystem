package com.piter.piterdiplomna.fragments;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.piter.piterdiplomna.ObjectClasses.TaskClass;
import com.piter.piterdiplomna.R;
import com.piter.piterdiplomna.activities.MainActivity;
import com.piter.piterdiplomna.helper.Constants;
import com.piter.piterdiplomna.helper.MyDateHelper;
import com.piter.piterdiplomna.helper.SharedPreferencesManage;
import com.piter.piterdiplomna.helper.URLs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaskAddFragment extends Fragment {// implements DatePickerFragment.OnDataPass{
    private Spinner SpinnerStatus;
    private EditText title_of_task;
    private EditText description_of_task;
    private Button AddBtn;
    private Button BeginDateBtn;
    private Button EndDateBtn;
    private ImageButton selectUserBtn;
    Dialog addEditDialog;
    final String TAG = "TAG TaskAddFragment";
    public static String jsonData="";
    private TextView BeginDateDateTV,BeginDateTimeTV,EndDateDateTV,EndDateTimeTV;

    //Broadcast receiver to receive broadcasts
    private BroadcastReceiver mRegistrationBroadcastReceiver;
//    final String urlAddTask = MainActivity.urlIP + "/com.piter.jersey.first/api/v2/manage/updateTask";

//    public static final MediaType JSON
//            = MediaType.parse("application/json; charset=utf-8");


    public TaskAddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.e_fragment_add_task, container, false);
        //
        addSpinnerItems(view);
        addListenerOnButtons(view);
        InisializeTimeTV(view);
        //
//        if (MainActivity.WhichDate == 1 || MainActivity.WhichDate == 2) {
//            long l = Long.parseLong(getArguments().getString("message"));
//            onDataPassNew(l);
//        }
        //Creating broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.USER_TASK_LIST)) {
                    //Getting message data
                    jsonData = intent.getStringExtra("user_list");
                    Log.d(TAG, "onReceive: data from user task list = "+jsonData);

                }
                else{
                    Log.d(TAG, "onReceive: polu4i se call-a ama ne e ot tam jsonData= "+jsonData);
                }
            }
        };
        return view;
    }

    public void AddBeginDate(long time) {
        MainActivity.BeginDate = time;
    }

    public void AddEndDate(long time) {
        MainActivity.EndDate = time;
    }

    // add items into spinner dynamically
    public void addSpinnerItems(View view) {
        SpinnerStatus = (Spinner) view.findViewById(R.id.statusSpnr);
        List<String> listStatus = new ArrayList<String>(Arrays.asList(new String[]{"Pending", "Done"}));//to be change to use listStatus from strings file

        ArrayAdapter<String> dataAdapterStatus = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, listStatus);
        dataAdapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerStatus.setAdapter(dataAdapterStatus);
    }


    public void addListenerOnButtons(View view) {
        AddBtn = (Button) view.findViewById(R.id.addTaskBtn);
        selectUserBtn = (ImageButton) view.findViewById(R.id.userSelectBtn);

        BeginDateBtn = (Button) view.findViewById(R.id.beginDateBtn);
        EndDateBtn = (Button) view.findViewById(R.id.endBtn);
        Button btnCheck = (Button) view.findViewById(R.id.viewBtn);
        BeginDateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                MainActivity.WhichDate = 1;//to know witch date are we setting begin or end date
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

            }
        });

        EndDateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                MainActivity.WhichDate = 2;
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
//                MainActivity.WhichDate=0;
            }
        });
        selectUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserListFragment fragment = UserListFragment.newInstance("TaskAddFragment", "");
                fragment.show(getFragmentManager(),"user_list_task");
            }
        });
        title_of_task = (EditText) view.findViewById(R.id.titleET);
        description_of_task = (EditText) view.findViewById(R.id.decsriptionET);
        AddBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addTask();
                    }
                }
        );

        btnCheck.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Calendar cl = Calendar.getInstance();
                MyDateHelper halp = new MyDateHelper();
                cl.setTimeInMillis(MainActivity.BeginDate);
                Log.d(TAG, "onClick: BeginDate="+MainActivity.BeginDate+" whaaaaat????????????");
                String date1 = "" + cl.get(Calendar.YEAR) + "-" +( cl.get(Calendar.MONTH) + 1 )+ "-" + cl.get(Calendar.DAY_OF_MONTH) + " " + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);
                cl.setTimeInMillis(MainActivity.EndDate);
                String date2 = "" + cl.get(Calendar.YEAR) + "-" +( cl.get(Calendar.MONTH) + 1) + "-" + cl.get(Calendar.DAY_OF_MONTH) + " " + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);
                Toast.makeText(getActivity(),
                        "OnClickListener : " +
                                "\nTitle : " + title_of_task.getText().toString() +
                                "\nDescrip : " + description_of_task.getText() +
                                "\nSpinnerStatus : " + SpinnerStatus.getSelectedItem().toString() +
                                "\nSpinnerTID : " + date1 +
                                "\nSpinnerTID : " + date2 +
                                "\n formatiranata BeginDate:"+ halp.returnCorrectFormatDate(MainActivity.BeginDate) +
                                "\n formatiranata EndDate:"+ halp.returnCorrectFormatDate(MainActivity.EndDate),
                        Toast.LENGTH_LONG).show();
            }

        });
    }
    public void InisializeTimeTV(View view){
        BeginDateDateTV=(TextView) view.findViewById(R.id.BeginDateDateTV);
        BeginDateTimeTV=(TextView) view.findViewById(R.id.BeginDateTimeTV);
        EndDateDateTV=(TextView) view.findViewById(R.id.EndDateDateTv);
        EndDateTimeTV=(TextView) view.findViewById(R.id.EndDateTimeTv);
        MyDateHelper halp= new MyDateHelper();

        Calendar calendar = halp.convertFromString(halp.convertFromMilisec(MainActivity.BeginDate));
        BeginDateDateTV.setText(halp.getDateFromCalendar(calendar));
        BeginDateTimeTV.setText(halp.getTimeFromCalendar(calendar));
        calendar = halp.convertFromString(halp.convertFromMilisec(+MainActivity.EndDate));
        EndDateDateTV.setText(halp.getDateFromCalendar(calendar));
        EndDateTimeTV.setText(halp.getTimeFromCalendar(calendar));

    }

    public void passDataToFragment(String j){
        jsonData = j;

    }
    public void addTask() {
//        View focusView = null;
//        if (!TextUtils.isEmpty(title_of_task.getText())) {
//            title_of_task.setError(getString(R.string.error_title_not_found));
//            focusView = title_of_task;
//            focusView.requestFocus();
//        }
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(MainActivity.BeginDate);
        String date1 = "" + cl.get(Calendar.YEAR) + "-" + (cl.get(Calendar.MONTH) +1)+ "-" + cl.get(Calendar.DAY_OF_MONTH) + " " + cl.get(Calendar.HOUR_OF_DAY)+":"+cl.get(Calendar.MINUTE)+":"+cl.get(Calendar.SECOND);
        cl.setTimeInMillis(MainActivity.EndDate);
        String date2 = "" + cl.get(Calendar.YEAR) + "-" + (cl.get(Calendar.MONTH) +1)+ "-" + cl.get(Calendar.DAY_OF_MONTH) + " " + cl.get(Calendar.HOUR_OF_DAY)+":"+cl.get(Calendar.MINUTE)+":"+cl.get(Calendar.SECOND);
        TaskClass newTask;
        if(!jsonData.isEmpty()) {
            //here to call new insert into db and pass the list_id or send the hole data like string
            newTask = new TaskClass(title_of_task.getText().toString(), description_of_task.getText().toString(), SpinnerStatus.getSelectedItem().toString(), date1, date2, SharedPreferencesManage.getInstance().getUserId() + "", jsonData, "true");//
            Log.d(TAG, "addTask: This task was added to a list of ppl");
        }
        else {
            newTask = new TaskClass(title_of_task.getText().toString(), description_of_task.getText().toString(), SpinnerStatus.getSelectedItem().toString(), date1, date2, SharedPreferencesManage.getInstance().getUserId() + "", SharedPreferencesManage.getInstance().getUserId() + "", "false");//
            Toast.makeText(getActivity(), "This task was added to yourself", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "addTask: This task was added to yourself");
        }


        Gson gson = new Gson();
        String json = gson.toJson(newTask);
        Log.d(TAG, "updateTask:json "+json);
//        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URLs.URL_ADD_TASK)
                .post(RequestBody.create(Constants.JSON, json))
                .build();
        Log.d(TAG, "addTask: URL="+URLs.URL_ADD_TASK);

        // Get a handler that can be used to post to the main thread
        SharedPreferencesManage.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
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
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.w(TAG, "row added" );
                        Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
            }
        });
        title_of_task.setText("");
        description_of_task.setText("");
    }
}
