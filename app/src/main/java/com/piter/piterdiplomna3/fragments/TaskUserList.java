package com.piter.piterdiplomna3.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piter.piterdiplomna3.ObjectClasses.UserClass;
import com.piter.piterdiplomna3.R;
import com.piter.piterdiplomna3.helper.Constants;
import com.piter.piterdiplomna3.helper.SharedPreferencesManage;
import com.piter.piterdiplomna3.helper.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Activities that contain this fragment must implement the
 * {@link TaskUserList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TaskUserList#newInstance} factory showFragment to
 * create an instance of this fragment.
 */
public class TaskUserList extends DialogFragment implements CompoundButton.OnCheckedChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG="TAG UserListFragment";
    public FunDapter adapter;
    ArrayList<UserClass> yourList;
    View view;
//    private static final String urlGetUser = MainActivity.urlIP+"/com.piter.jersey.first/api/v2/manage/users";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TaskUserList() {
        // Required empty public constructor
    }

    /**
     * Use this factory showFragment to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskUserList.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskUserList newInstance(String param1, String param2) {
        TaskUserList fragment = new TaskUserList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.d_fragment_user_list, container, false);
        Initialize(view);
        try {
            AsyncGetAndPrint(URLs.URL_FETCH_USERS,"");
        } catch (Exception e) {            e.printStackTrace();        }
        return view;
    }

    public void Initialize(final View view){
        Button DoneBtn = (Button) view.findViewById(R.id.doneAddinUserBtn);
        DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                StringBuilder result = new StringBuilder();//To be finished ;(
                JSONArray result = new JSONArray();
                for(int i=0;i<adapter.getCount();i++)
                {
                    JSONObject obj = new JSONObject();
                    UserClass object;
                    object = yourList.get(i);
                    if(object.isChecked())
                    {
                        try {
                            obj.put("user_id",object.getId());
                            result.put(obj);
                            Log.d(TAG, "onClick: result:"+result.toString().replace("[","").replace("]","")+" i= "+i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                                .append();
//                        Log.d(TAG, "onClick: id="+object.getId());
//                        result.append("\n");
                    }
                }
                Log.d("TAG", "onClick: result"+result.toString());
                Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_SHORT).show();

                if(mParam1.toString()=="TaskAddFragment") {
//                    here to send the broadcast to TaskAddFragment with the data
                    Intent dataListNotification = new Intent(Constants.USER_TASK_LIST);
                    //Adding notification data to the intent
//                    TaskAddFragment.jsonData = result.toString().replace("[","").replace("]","");
                    JSONObject mainObj = new JSONObject();
                    try {
                        mainObj.put("user_list", result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dataListNotification.putExtra("user_list",mainObj.toString());
                    TaskAddFragment.jsonData = mainObj.toString();
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(dataListNotification);
                    getDialog().cancel();
//                    TaskAddFragment fragment = new TaskAddFragment();
//                    android.support.v4.app.FragmentTransaction fragmentTransaction =
//                            getActivity().getSupportFragmentManager().beginTransaction();
////                    fragmentTransaction.show(fragment);

//                    fragmentTransaction.replace(R.id.fragment_container, fragment);
//                    fragmentTransaction.commit();
                }
            }
        });
    }

    //
    //function AsyncGetNameOfReceiver uses Get url and write that to main fragment
    //
    public void AsyncGetAndPrint(String url, final String S) throws Exception{
//        OkHttpClient client = new OkHttpClient();
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
                String responseString;
                responseString = response.body().string();
                response.body().close();
                    Type listType = new TypeToken<List<UserClass>>() {
                    }.getType();
                    yourList = new Gson().fromJson(responseString, listType);
                    //za da se izpulni vinagi ot glavnata ni6ka
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                printUsers(view, yourList);
                            } catch (Exception e) {
                                Log.i("TAG", "run: userListGet se precaka");
                                e.printStackTrace();
                            }
                        }
                    });
            }
        });
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void printUsers(View view, ArrayList<UserClass> yourList){
//        ArrayList<UserClass> yourList;

        BindDictionary<UserClass> dictionary = new BindDictionary<>();
        dictionary.addStringField(R.id.userNameTV,new StringExtractor<UserClass>(){
            @Override
            public String getStringValue(UserClass item, int position) { return "" + item.getFname()+" "+item.getLname();            }
        });

        dictionary.addStringField(R.id.userCB,new StringExtractor<UserClass>(){
            @Override
            public String getStringValue(UserClass item, int position) {
                return item.getId()+"";
            }
        });


        adapter = new FunDapter(TaskUserList.this.getActivity(), yourList,R.layout.e_user_entry_layout ,dictionary);
        final ListView tasksLV = (ListView) view.findViewById(R.id.userListLV);
        tasksLV.setItemsCanFocus(true);

        tasksLV.setAdapter(adapter);
        tasksLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserClass object= (UserClass) tasksLV.getItemAtPosition(position);
//                if(object.isChecked())
                object.setChecked(!object.isChecked());
                Log.d(TAG, "setAdapterUsers: UserClass is cheched");
                CheckBox cb = (CheckBox) view.findViewById(R.id.userCB);
                cb.setChecked(object.isChecked());
//                Log.d(TAG, "onClick: ListView se klikna nomer"+tasksLV.getId());
//                UserClass obj = (UserClass) tasksLV.getItemAtPosition(tasksLV.getCheckedItemPosition());
//                obj.setChecked(true);
            }
        });
    }

//
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            yourList.get(buttonView.getId()).setChecked(isChecked);
        Log.d(TAG, "onCheckedChanged: "+isChecked);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
