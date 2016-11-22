package com.piter.piterdiplomna3.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piter.piterdiplomna3.ObjectClasses.SuggestCompanyClass;
import com.piter.piterdiplomna3.ObjectClasses.SuggestPositionClass;
import com.piter.piterdiplomna3.ObjectClasses.UserClass;
import com.piter.piterdiplomna3.R;
import com.piter.piterdiplomna3.helper.Constants;
import com.piter.piterdiplomna3.helper.SharedPreferencesManage;
import com.piter.piterdiplomna3.helper.URLs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditInfoFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG="TAG EditInfoFrg";

    private String mParam1;
    private String mParam2;
    private View view;
    private EditText company_add;
    private EditText position_add;
    ArrayList<SuggestCompanyClass> suggest_list = new ArrayList();
    ArrayList<SuggestCompanyClass> suggest_position_list = new ArrayList();
    private RecyclerView mRecyclerView;
    private FunDapter mAdapter;
    private FunDapter mAdapter2;
    private RecyclerView.LayoutManager mLayoutManager;
    public ListView suggestLV;
    public ListView suggestPositionLV;
    public Button RequestChange;

    private OnFragmentInteractionListener mListener;

    public EditInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditInfoFragment.
     */
    public static EditInfoFragment newInstance(String param1, String param2) {
        EditInfoFragment fragment = new EditInfoFragment();
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
        view = inflater.inflate(R.layout.g_fragment_edit_info, container, false);
        company_add = (EditText) view.findViewById(R.id.joinCompanyET);
        position_add = (EditText) view.findViewById(R.id.joinPositionET);
        RequestChange = (Button) view.findViewById(R.id.requestCompanyPositionBTN);
        company_add.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged");
                try {
                    Log.d(TAG, "onTextChanged: URL="+URLs.URL_FETCH_COMPANY+"?id="+company_add.getText().toString().trim());
                    AsyncGetAndPrint(URLs.URL_FETCH_COMPANY+"?name="+company_add.getText().toString().trim(),false);
                } catch (Exception e) {                    e.printStackTrace();                }
            }
        });
        position_add.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged");
                try {
                    Log.d(TAG, "onTextChanged: URL="+URLs.URL_FETCH_POSITIONS+"?name="+position_add.getText().toString().trim());
                    AsyncGetAndPrint(URLs.URL_FETCH_POSITIONS+"?name="+position_add.getText().toString().trim(),true);
                } catch (Exception e) {                    e.printStackTrace();                }
            }
        });
        RequestChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject json = new JSONObject();
                    try {
                        if(company_add.getText().length()>0) {
                            json.put("object_name", company_add.getText());
                            json.put("user_id", SharedPreferencesManage.getInstance().getUserId());
                            json.put("type", "1");

                            Log.d(TAG, "onClick: URL="+URLs.URL_SEND_REQUEST);
                            AsyncRequestChanges(URLs.URL_SEND_REQUEST,json.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject json2 = new JSONObject();
                    try {
                        if(position_add.getText().length()>0) {
                            json2.put("object_name", position_add.getText());
                            json2.put("user_id", SharedPreferencesManage.getInstance().getUserId());
                            json2.put("type", "2");

                            Log.d(TAG, "onClick: URL="+URLs.URL_SEND_REQUEST);
                            AsyncRequestChanges(URLs.URL_SEND_REQUEST,json2.toString());
                        }
                        getActivity().onBackPressed();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {                    e.printStackTrace();                }
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
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
    //
    //function AsyncGetNameOfReceiver uses Get url and write that to main fragment
    //
    public void AsyncGetAndPrint(String url,final boolean flag) throws Exception {
//        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        SharedPreferencesManage.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "chatUserList:async task  ");
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
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString;
                responseString = response.body().string();
                Log.d(TAG, "onResponse: responseString=" + responseString);
                response.body().close();
                Log.d(TAG, "onResponse: responseString="+responseString);
                if(responseString.contains("Doctype")){
                    Log.d(TAG, "onResponse: responseString"+responseString);
                    return;
                }
                //za da se izpulni vinagi ot glavnata ni6ka
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            setAdapter(responseString, flag);
                        } catch (Exception e) {
                            Log.i("TAG", "run: userCardViewList se precaka");
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void setAdapter(String responseString, boolean flag) {
        //recycler view
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.chatUserRV);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        if(flag==false) {
            BindDictionary<SuggestCompanyClass> dictionary = new BindDictionary<>();
            dictionary.addStringField(R.id.suggestCompanyTV, new StringExtractor<SuggestCompanyClass>() {
                @Override
                public String getStringValue(SuggestCompanyClass item, int position) {
                    return item.getName();
                }
            });
            Type listType = new TypeToken<List<SuggestCompanyClass>>() {
            }.getType();
            suggest_list = new Gson().fromJson(responseString, listType);
            Log.d(TAG, "setAdapter: suggest_list" + suggest_list.toArray().toString());
            mAdapter = new FunDapter(getActivity(), suggest_list, R.layout.g_suggest, dictionary);

            suggestLV = (ListView) view.findViewById(R.id.suggestCompanyLV);
            suggestLV.setAdapter(mAdapter);
            suggestLV.setVisibility(View.VISIBLE);
        }
        else
        {
            BindDictionary<SuggestPositionClass> dictionary = new BindDictionary<>();
            dictionary.addStringField(R.id.suggestCompanyTV, new StringExtractor<SuggestPositionClass>() {
                @Override
                public String getStringValue(SuggestPositionClass item, int position) {
                    return item.getName();
                }
            });
            Type listType = new TypeToken<List<SuggestPositionClass>>() {
            }.getType();
            suggest_position_list = new Gson().fromJson(responseString, listType);
            Log.d(TAG, "setAdapter: suggest_list" + suggest_position_list.toArray().toString());
            mAdapter2 = new FunDapter(getActivity(), suggest_position_list, R.layout.g_suggest, dictionary);

            suggestPositionLV = (ListView) view.findViewById(R.id.suggestPositionLV);
            suggestPositionLV.setAdapter(mAdapter2);
            suggestPositionLV.setVisibility(View.VISIBLE);
        }
    }

    //
    //function AsyncRequestChanges
    //
    public void AsyncRequestChanges(String url,String json) throws Exception{
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
                response.body().close();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Request added, now wait for the admin to approve it", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onResponse request added");
                    }
                });
            }
        });
    }
}
