package com.piter.piterdiplomna3.fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piter.piterdiplomna3.ObjectClasses.CommentClass;
import com.piter.piterdiplomna3.R;
import com.piter.piterdiplomna3.adapters.CommentAdapter;
import com.piter.piterdiplomna3.helper.Constants;
import com.piter.piterdiplomna3.helper.MyDateHelper;
import com.piter.piterdiplomna3.helper.SharedPreferencesManage;
import com.piter.piterdiplomna3.helper.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public String ready="false";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private View view;
    private ArrayList<CommentClass> mCommentList;
    private String mIdTask;
    private String mParam2;
    private final String TAG = "TAG CommentsFragment";
//    public FunDapter adapter;
//    private ArrayList<UserClass> mUserDetails; //other adapter made
//    private boolean wait=false;
    public CommentAdapter mAdapter;
    public Button sendBtn;
    public EditText CommentText;


    private OnFragmentInteractionListener mListener;

    public CommentsFragment() {    }

    public static CommentsFragment newInstance(String param1, String param2) {
        CommentsFragment fragment = new CommentsFragment();
        Log.d("TAG", "newInstance: newInstance");
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
            mIdTask = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.cc_fragment_comments_list, container, false);
        CommentText = (EditText)view.findViewById(R.id.comment_textET);
//        myfview = container;//.getChildAt(Integer.parseInt(mIdTask));
        sendBtn= (Button) view.findViewById(R.id.commentBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onCreateView:  klikna se butona");
                MyDateHelper halp = new MyDateHelper();
                try {
                    if(CommentText.length()>0){
                        Log.d(TAG, "onCreateView: onClick:if there is a comment post it ");
                        postComment(URLs.URL_SEND_COMMENT, CommentText.getText().toString().trim(), getTimeStamp(), Integer.parseInt(mIdTask), SharedPreferencesManage.getInstance().getUserId());

                        CommentClass newObj = new CommentClass(halp.convertFromMilisec(System.currentTimeMillis()),Integer.parseInt(mIdTask),CommentText.getText().toString().trim(),SharedPreferencesManage.getInstance().getUserId(),SharedPreferencesManage.getInstance().getUserFirsName(),SharedPreferencesManage.getInstance().getUserLastName());
                        if (mCommentList.isEmpty()) {
                            mCommentList.add(newObj);
                            AddAdapterForComments(getView(), mCommentList);
                        }else
                            mAdapter.add(newObj);
                        mAdapter.notifyDataSetChanged();
                        CommentText.setText("");
                    }
                } catch (Exception e) {                    e.printStackTrace();                }
            }
        });
        Log.d(TAG, "onCreateView: onCreateView Comments try comments now?");
        try {            getComments(URLs.URL_FETCH_COMMENTS+"?id="+ mIdTask);        }
        catch (Exception e) {            e.printStackTrace();        }
        return view;
    }


    //This showFragment will return current timestamp
    public static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
    //
    //function getComments uses Get url and write that to main fragment
    //
    public void getComments(String url) throws Exception{
//        OkHttpClient client = new OkHttpClient();
        Log.d(TAG, " getComments url= "+url);
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
                Log.d(TAG, "onResponse: getComments"+responseString);

                Type listType = new TypeToken<List<CommentClass>>() {
                }.getType();
                mCommentList = new Gson().fromJson(responseString, listType);
                AddAdapterForComments(getView(),mCommentList);
            }
        });
    }
    //
    //function postComment uses Get url and write that to main fragment
    //
    public void postComment(String url,String text, String time,int task_id,int user_id) throws Exception{
//        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        try {
            json.put("text", text);
            json.put("create_date_time", time);
            json.put("task_id", task_id);
            json.put("user_id", user_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(Constants.JSON, json.toString()))
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
                Log.d("TAG", "onResponse: getComments="+responseString);
                if(!responseString.contains("ok")){
                    Toast.makeText(getContext(),"Send unsuccessful",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    public void AddAdapterForComments(View view, ArrayList<CommentClass> yourList){
//        myfview=view;
        if(yourList.isEmpty()) return;
        //Made new adapter for this
//        Log.d(TAG, "AddAdapterForComments se izvika: ");
//        BindDictionary<CommentClass> dictionary = new BindDictionary<>();
//        dictionary.addStringField(R.id.CommentTextTV,new StringExtractor<CommentClass>(){
//            @Override
//            public String getStringValue(CommentClass item, int position) { return item.getText();            }
//        });
//
//        dictionary.addStringField(R.id.CommentDateTV,new StringExtractor<CommentClass>(){
//            @Override
//            public String getStringValue(CommentClass item, int position) {
//                return item.getCreate_date_time();
//            }
//
//        });
//
//        Log.d(TAG, "AddAdapterForComments: zadadoha se drugite 2 poleta");
//        dictionary.addStringField(R.id.CommentPersonNameTV,new StringExtractor<CommentClass>(){
//            @Override
//            public String getStringValue(CommentClass item, int position) {
//                try {
//                    Log.d(TAG, "getStringValue: vlezna v try bloka getUserDetails");
//                    wait=true;
//                    getUserDetails(URLs.URL_FETCH_USERS + "?id=" + item.getUser_id());
//                    while(wait==true){//wait
//                         }
//                    Log.d(TAG, "getStringValue: izlezna ot try bloka getUserDetails");
////                    Thread.sleep(500);
//                } catch (Exception e) {e.printStackTrace();Log.d(TAG, "getStringValue: cant get userName in time");}
////                try {                    Thread.sleep(400);                }
////                catch (InterruptedException e) {                    e.printStackTrace();                }
////                int i = 0;//                while(mUserDetails.isEmpty())
////                if (mUserDetails.isEmpty()) {
////                    return "exapmle name ";
////                }
////                Log.d(TAG, "getStringValue: " + "" + mUserDetails.get(0).getFname() + " " + mUserDetails.get(0).getLname());
//               // Log.d(TAG, "getStringValue: Start syncTask");
//               // LongOperation async =  new LongOperation("Fetching comments...");
//               /// try {
////                    async.execute(""+item.getUser_id());
////                    async.get(10000, TimeUnit.MILLISECONDS);
////                    Thread.sleep(10000);
//               // }         catch (Exception e) {            e.printStackTrace();        }
//                return "" + mUserDetails.get(0).getFname() + " " + mUserDetails.get(0).getLname();
//                //                return "exapmle name ";//                }else
//            }
//        });



        final View myfview = view;
//        adapter = new FunDapter(CommentsFragment.this.getActivity(), yourList,R.layout.cc_comment_fragment,dictionary);
        mAdapter = new CommentAdapter(CommentsFragment.this.getActivity(),R.layout.cc_comment_fragment, yourList);
        getActivity().runOnUiThread(new Runnable() {
           @Override
           public void run() {
               final ListView tasksLV = (ListView) myfview.findViewById(R.id.CommentsListView);
               tasksLV.setItemsCanFocus(true);
               tasksLV.setAdapter(mAdapter);
               ready="true";
            }
       });
    }



    public class getSupportedFrgManager extends AppCompatActivity{
        public FragmentManager getIt(){
            return getSupportFragmentManager();
        }

    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        String returnString;
        ProgressDialog progressDialog;
        String loadtext;

        public LongOperation(String loadtext) {
            this.progressDialog = progressDialog;
            this.loadtext = loadtext;
        }

        @Override
        protected String doInBackground(String... params) {
            String user_id=params[0];
//            for (int i = 0; i < 5; i++) {
            try {
                    Thread.sleep(400);
                Log.d(TAG, "TestSyncGet: vlezna user_id="+user_id);
//                  public void run() throws Exception {
                Request request = new Request.Builder()
                        .url(URLs.URL_FETCH_USERS + "?id=" + user_id)
                        .build();

                Response response = SharedPreferencesManage.getInstance().getOkhttpClient().newCall(request).execute();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

//                    Headers responseHeaders = response.headers();
//                    for (int i = 0; i < responseHeaders.size(); i++) {
//                        Log.d(TAG, "TestAsyncGet: "+responseHeaders.name(i) + ": " + responseHeaders.value(i));
//                    }
                returnString=response.body().string();
                response.close();
//

            } catch (Exception e) {
                Thread.interrupted();
            }
//            }
            return returnString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "onPostExecute: result="+result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(
                    getActivity());
            progressDialog.setMessage(loadtext);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}

