package com.piter.piterdiplomna3.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.piter.piterdiplomna3.ObjectClasses.TaskClass;
import com.piter.piterdiplomna3.R;
import com.piter.piterdiplomna3.activities.MainActivity;
import com.piter.piterdiplomna3.adapters.MainTasksAdapter;
import com.piter.piterdiplomna3.helper.Constants;
import com.piter.piterdiplomna3.helper.MyDateHelper;
import com.piter.piterdiplomna3.helper.SharedPreferencesManage;
import com.piter.piterdiplomna3.helper.URLs;

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
 * Created by Piter on 07/12/2016.
 */

public class EditTaskFragment extends DialogFragment{
    private Spinner SpinnerStatus;
    private EditText title_of_task;
    private EditText description_of_task;
    private Button AddBtn;
    private Button BeginDateBtn;
    private boolean BeginDateBtnFlag=false;
    private Button EndDateBtn;
    private boolean EndDateBtnFlag=false;
    private Button selectUserBtn;
    private boolean selectUserBtnFlag=false;
    final String TAG = "TAG EditTaskFragment";
    public static String jsonData="";
//    private String task_id;
//    private String title;
    static private int positionOfList;
//    private static final String param1 = "param1";
//    private static final String param2 = "param2";
//    private static final String param3 = "param3";
    public static TaskClass taskToEdit;
    private TextView BeginDateDateTV,BeginDateTimeTV,EndDateDateTV,EndDateTimeTV;

    //Broadcast receiver to receive broadcasts
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    View view;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    public static EditTaskFragment newInstance(TaskClass task,int position) {
        EditTaskFragment fragment = new EditTaskFragment();
        Bundle args = new Bundle();
//        args.putString(param1, task_id1);
//        args.putString(param2, title2);
//        args.putString(param3, descr3);
        positionOfList=position;
        EditTaskFragment.taskToEdit=task;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            task_id = getArguments().getString(param1);
//            title = getArguments().getString(param2);
//            descr = getArguments().getString(param3);
        }
    }
    public EditTaskFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.e_fragment_add_task, container, false);
        addSpinnerItems(view);
        addListenerOnButtons(view);
        InisializeTimeTV(view);
        try {
//            AsyncGetAndPrint(URLs.URL_FETCH_USERS,"");
        } catch (Exception e) {            e.printStackTrace();        }
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
        selectUserBtn = (Button) view.findViewById(R.id.userSelectBtn);

        BeginDateBtn = (Button) view.findViewById(R.id.beginDateBtn);
        EndDateBtn = (Button) view.findViewById(R.id.endBtn);
        Button btnCheck = (Button) view.findViewById(R.id.viewBtn);
        BeginDateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BeginDateBtnFlag=true;
                DialogFragment newFragment = new DatePickerFragment();
                MainActivity.WhichDate = 1;//to know witch date are we setting begin or end date
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

            }
        });

        EndDateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EndDateBtnFlag=true;
                DialogFragment newFragment = new DatePickerFragment();
                MainActivity.WhichDate = 2;
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
//                MainActivity.WhichDate=0;
            }
        });
        selectUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUserBtnFlag=true;
                UserListFragment fragment = UserListFragment.newInstance("TaskAddFragment", "");
                fragment.show(getFragmentManager(),"user_list_task");
            }
        });
        title_of_task = (EditText) view.findViewById(R.id.titleET);
        title_of_task.setText(taskToEdit.getTitle().toString());
        description_of_task = (EditText) view.findViewById(R.id.decsriptionET);
        description_of_task.setText(taskToEdit.getDescription().toString());
        AddBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateTask();
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

        Calendar calendar = halp.convertFromString(taskToEdit.getBegin_date());
        BeginDateDateTV.setText(halp.getDateFromCalendar(calendar));
        BeginDateTimeTV.setText(halp.getTimeFromCalendar(calendar));
        calendar = halp.convertFromString(taskToEdit.getEnd_date());
        EndDateDateTV.setText(halp.getDateFromCalendar(calendar));
        EndDateTimeTV.setText(halp.getTimeFromCalendar(calendar));

    }

    public void passDataToFragment(String j){
        jsonData = j;

    }
    public void updateTask() {
//        View focusView = null;
//        if (!TextUtils.isEmpty(title_of_task.getText())) {
//            title_of_task.setError(getString(R.string.error_title_not_found));
//            focusView = title_of_task;
//            focusView.requestFocus();
//        }
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(MainActivity.BeginDate);
        String date1 = "" + cl.get(Calendar.YEAR) + "-" + (cl.get(Calendar.MONTH) +1)+ "-" + cl.get(Calendar.DAY_OF_MONTH) + " " + "00:00:00";//cl.get(Calendar.HOUR_OF_DAY)"-"+cl.get(Calendar.MINUTE)+"-"+cl.get(Calendar.SECOND);
        cl.setTimeInMillis(MainActivity.EndDate);
        String date2 = "" + cl.get(Calendar.YEAR) + "-" + (cl.get(Calendar.MONTH) +1)+ "-" + cl.get(Calendar.DAY_OF_MONTH) + " " + "23:59:59";//cl.get(Calendar.HOUR_OF_DAY)+"-"+cl.get(Calendar.MINUTE)+"-"+cl.get(Calendar.SECOND);
        //change the current task with needed modification
        if(!title_of_task.getText().toString().equals(taskToEdit.getTitle())) {
            Log.d(TAG, "updateTask: title_of_task is dirty");
            taskToEdit.setTitle(title_of_task.getText().toString());
        }
        if(!description_of_task.getText().toString().equals(taskToEdit.getDescription())) {
            Log.d(TAG, "updateTask: description_of_task is dirty");
            taskToEdit.setDescription(description_of_task.getText().toString());
        }

        if(selectUserBtnFlag){
            Log.d(TAG, "updateTask: butona za list userite is dirty");
            if(!jsonData.isEmpty()) {

                Log.d(TAG, "updateTask to a lot of ppl json="+jsonData);
//            taskToEdit.setUser_made_for_id(jsonData);
            }
            selectUserBtnFlag=false;
        }
        if(BeginDateBtnFlag){
            Log.d(TAG, "updateTask: BeginDateBtnFlag is dirty");
//            MyDateHelper dateHelp = new MyDateHelper();
            taskToEdit.setBegin_date(date1);//dateHelp.convertFromMilisec(MainActivity.BeginDate));
            BeginDateBtnFlag=false;
        }
        if(EndDateBtnFlag){
            Log.d(TAG, "updateTask: EndDateBtnFlag is dirty");
//            MyDateHelper dateHelp = new MyDateHelper();
            taskToEdit.setEnd_date(date2);//dateHelp.convertFromMilisec(MainActivity.EndDate));
            EndDateBtnFlag=false;
        }

//        TaskClass newTask;

//        else {
//            newTask = new TaskClass(Integer.parseInt(task_id),title_of_task.getText().toString(), description_of_task.getText().toString(), SpinnerStatus.getSelectedItem().toString(), date1, date2, SharedPreferencesManage.getInstance().getUserId() + "", SharedPreferencesManage.getInstance().getUserId() + "", "false");//
//            Toast.makeText(getActivity(), "Task updated", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "updateTask to yourself");
//        }


        Gson gson = new Gson();
//        Log.d(TAG, "updateTask: Beginning of Gson parcing error right after");
        String json = gson.toJson(taskToEdit);//newTask
        Log.d(TAG, "updateTask:json "+json);
//        final OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URLs.URL_UPDATE_TASK)
                .post(RequestBody.create(Constants.JSON, json))
                .build();

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
//                getDialog().cancel();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                Log.w(TAG, "Task updated" );
                MainTasksAdapter.flagToWait=false;
//                        getDialog().cancel();
//                        Toast.makeText(getContext(), "Task updated", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                });
            }
        });
//        title_of_task.setText("");
//        description_of_task.setText("");
//        Intent sendIntent = new Intent(MainFragment.class);
//        sendIntent.

//        getParentFragment().getTargetFragment().no
        getDialog().cancel();
    }
}
