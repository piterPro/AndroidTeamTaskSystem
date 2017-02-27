package com.piter.piterdiplomna.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piter.piterdiplomna.ObjectClasses.UserClass;
import com.piter.piterdiplomna.helper.SharedPreferencesManage;
import com.piter.piterdiplomna.helper.Constants;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.piter.piterdiplomna.R;
import com.piter.piterdiplomna.helper.URLs;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
//    private static final int REQUEST_user_login = 1;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "hello:world", "hello1:world"
    };
    String TAG="TAG LoginActivity";
    //Progress dialog
//    private ProgressDialog progressDialog;//wtf
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask;
    private UserRegisterTask mAuthRegisterTask;
    private CompanyRegisterTask mAuthCompanyRegisterTask;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mFName;
    private EditText mLName;
    private EditText mCName;
    private EditText mCDesc;
    private View mProgressView;
    private Switch accSw;
    private Button mEmailSignInButton;
    private View mLoginFormView;
    private ArrayList<UserClass> USER;
//    public static final MediaType JSON
//            = MediaType.parse("application/json; charset=utf-8");
//    private String urlTryLogin = MainActivity.urlIP+"/com.piter.jersey.first/api/v2/manage/trylogin";
//    private String urlRegisterUser = MainActivity.urlIP+"/com.piter.jersey.first/api/v2/manage/addUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_activity_login);
        //check if the user is allready loged on

        setupActionBar();
//        progressDialog = new ProgressDialog(getBaseContext());
//        progressDialog.setMessage("Login...");
        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();
        mFName = (EditText) findViewById(R.id.fNameET);
        mLName = (EditText) findViewById(R.id.lNameET);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //cales after the editText is done being edit
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    //Enter = login
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: klikna se login butona");
                attemptLogin();
            }
        });

        Button mRegisterButton = (Button) findViewById(R.id.registerBtn);

        accSw = (Switch) findViewById(R.id.AccSwitch);
        accSw.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCName= (EditText) findViewById(R.id.CNameET);
                mCDesc= (EditText) findViewById(R.id.CDescET);
                if(mFName.getVisibility()==View.VISIBLE) {
                    mFName.setVisibility(View.GONE);
                    mLName.setVisibility(View.GONE);
                    mCName.setVisibility(View.VISIBLE);
                    mCDesc.setVisibility(View.VISIBLE);
                }
                else{
                    mFName.setVisibility(View.VISIBLE);
                    mLName.setVisibility(View.VISIBLE);
                    mCName.setVisibility(View.GONE);
                    mCDesc.setVisibility(View.GONE);
                }
            }
        });
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFName.getVisibility()==View.GONE && !accSw.isChecked()) {
                    expandFields();


                    //
                }
                else
                    attemptRegister();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    private void expandFields(){
        mFName.setVisibility(View.VISIBLE);
        mLName.setVisibility(View.VISIBLE);
//        mCompanyName.setText("Register user");
        accSw = (Switch) findViewById(R.id.AccSwitch);
        accSw.setVisibility(View.VISIBLE);
        mEmailSignInButton.setVisibility(View.GONE);
//        mEmailSignInButton.setText("Register Business");
    }
    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    private void attemptRegister() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mFName.setError(null);
        mLName.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        if(!accSw.isChecked()){
            Log.w(TAG, "attemptRegister: swich is checked" );
            String fname = mFName.getText().toString();
            String lname = mLName.getText().toString();

            boolean cancel = false;
            View focusView = null;

            // Check for a valid password, if the user entered one.
            if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }

            // Check for a valid username address.
            if (TextUtils.isEmpty(username)) {
                mUsernameView.setError(getString(R.string.error_field_required));
                focusView = mUsernameView;
                cancel = true;
            } else if (!isUsernameValid(username)) {
                mUsernameView.setError(getString(R.string.error_invalid_username));
                focusView = mUsernameView;
                cancel = true;
            }
            // Check for a valid Fname address.
            if (TextUtils.isEmpty(fname)) {
                mFName.setError(getString(R.string.error_field_required));
                focusView = mFName;
                cancel = true;
            }
            // Check for a valid username address.
            if (TextUtils.isEmpty(lname)) {
                mLName.setError(getString(R.string.error_field_required));
                focusView = mLName;
                cancel = true;
            }


            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                showProgress(true);
                mAuthRegisterTask = new UserRegisterTask(username, password, fname, lname);
                mAuthRegisterTask.execute((Void) null);
            }
        }
        else{//register comapany here
            Log.w(TAG, "attemptRegister: swich is NOT checked" );
            String cName = mCName.getText().toString();
            String cDesc = mCDesc.getText().toString();

            boolean cancel = false;
            View focusView = null;

            // Check for a valid password, if the user entered one.
            if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                mPasswordView.setError(getString(R.string.error_invalid_password));
                focusView = mPasswordView;
                cancel = true;
            }

            // Check for a valid username address.
            if (TextUtils.isEmpty(username)) {
                mUsernameView.setError(getString(R.string.error_field_required));
                focusView = mUsernameView;
                cancel = true;
            } else if (!isUsernameValid(username)) {
                mUsernameView.setError(getString(R.string.error_invalid_username));
                focusView = mUsernameView;
                cancel = true;
            }
            // Check for a valid Fname address.
            if (TextUtils.isEmpty(cName)) {
                mFName.setError(getString(R.string.error_field_required));
                focusView = mFName;
                cancel = true;
            }
            // Check for a valid username address.
            if (TextUtils.isEmpty(cDesc)) {
                mLName.setError(getString(R.string.error_field_required));
                focusView = mLName;
                cancel = true;
            }
            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                showProgress(true);
                mAuthCompanyRegisterTask = new CompanyRegisterTask(username, password, cName, cDesc);
                mAuthCompanyRegisterTask.execute((Void) null);
            }
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUsernameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

//        if (username.isEmpty() && mAuthTask != null) {
//            mAuthTask.execute((Void) null);
//            return;
//        }

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String email) {
        //TODO: Replace this with your own logic
        return true;// email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    public void onBackPressed() {                super.finishAffinity();    }
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUsernameView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
    //
    //function logUserAndSetDetails uses Get url and write that to logs the user
    //
    public void logUserAndSetDetails(String url) throws Exception{
//        OkHttpClient client = new OkHttpClient();
        Log.d(TAG, "logUserAndSetDetails: url="+url);
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
                Log.d("TAG", "onResponse: "+responseString);

                Type listType = new TypeToken<List<UserClass>>() {
                }.getType();
                USER = new Gson().fromJson(responseString, listType);
                SharedPreferencesManage.getInstance().loginUser(USER.get(0).getId(),USER.get(0).getFname(),USER.get(0).getLname(), USER.get(0).getCompany_name(), USER.get(0).getPosition_name() );
            }
        });
    }

    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        String response_string;
        int user_id=0;
//        OkHttpClient client = new OkHttpClient();
        UserLoginTask(String username, String password) {
            Log.d(TAG, "UserLoginTask: constructor");
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //network access.
            final boolean flag=true;
            String json="{ \"username\":\""+ mUsername +"\",\"password\":\""+mPassword+"\"}";
            Log.i(TAG, "UserLoginTask:doInBackground: input info"+json);
            RequestBody body = RequestBody.create(Constants.JSON, json);
            Request request = new Request.Builder()
                    .url(URLs.URL_TRY_LOGIN)
                    .post(body)
                    .build();
            try {
                Response response = SharedPreferencesManage.client.newCall(request).execute();
                response_string =response.body().string();
                response.close();
                Log.i(TAG, "UserLoginTask:doInBackground: the response from the server is :"+ response_string);
                if(response_string.contains(":")) {
                    String[] pieces = response_string.split(":");
                    user_id = Integer.parseInt(pieces[1]);
                    Log.d(TAG, "doInBackground: user_id="+user_id);
                    return true;
                }else {
                    Log.d(TAG, "UserLoginTask:doInBackground: Error \n"+response_string);
                    Toast.makeText(LoginActivity.this.getBaseContext(),"Error \n"+response_string, Toast.LENGTH_SHORT).show();
                }
                return false;
            }catch (Exception e) {
                Log.d(TAG, "doInBackground: Can't connect to server");
                if(response_string!=null)
                    response_string = "Can't connect to server";
//                e.printStackTrace();//TODO:da se mahnat vsi4ki takiva predi puskaneto v upotreba
            }
            return false;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
            showProgress(false);

            if (success) {
                try {
                    logUserAndSetDetails(URLs.URL_FETCH_USERS+"?id="+user_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",user_id+"");
                startActivity(intent);

//                LoginActivity.this.finish();//zatvarq activity-to
                Toast.makeText(getBaseContext(),"Login successful",Toast.LENGTH_LONG).show();
            } else {
                if(response_string!=null) {
                    Toast.makeText(getBaseContext(), "Error \n" + response_string, Toast.LENGTH_SHORT).show();
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
            }
        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
            showProgress(false);
        }
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private final String mFName;
        private final String mLName;
        String response_string;
        int user_id;

//        OkHttpClient client = new OkHttpClient();
        UserRegisterTask(String username, String password, String fname,String lname) {
            Log.d(TAG, "UserRegisterTask: constructor");
            mUsername = username;
            mPassword = password;
            mFName = fname;
            mLName = lname;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //network access.
            String json="{ \"username\":\""+ mUsername +"\",\"password\":\""+mPassword+"\",\"fname\":\""+mFName+"\",\"lname\":\""+mLName+"\"}";
            Log.i("TAG UserRegisterTask", "doInBackground: input info"+json);
            Log.d(TAG, "doInBackground: url="+URLs.URL_REGISTER_USER);
            RequestBody body = RequestBody.create(Constants.JSON, json);
            Request request = new Request.Builder()
                    .url(URLs.URL_REGISTER_USER)
                    .post(body)
                    .build();
            try {
                Response response = SharedPreferencesManage.client.newCall(request).execute();
                response_string = response.body().string();
                response.close();

                Log.i("TAG", "doInBackground: the response from the server is :"+ response_string);
                if(response_string.contains(":")) {
                    String[] pieces = response_string.split(":");
                    if(!pieces[0].equals("true")){
                        Log.d(TAG, "doInBackground: pieces[0]="+pieces[0]);
//                        response_string="user already exists";
//                        return false;
                    }
                    user_id = Integer.parseInt(pieces[1]);
                    Log.d(TAG, "doInBackground: pieces[1]="+user_id);
                    return true;
                }else {
                    Log.d("TAG", "doInBackground: Error \n"+response_string);
//                    Toast.makeText(LoginActivity.this.getBaseContext(),"Error \n"+response_string, Toast.LENGTH_SHORT).show();
                }
//                return true;
            }catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }


        //it is not called anywhere in this code
        //haha ama 4e si preburzal sus zaklu4eniqta sa6e
        @Override
        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
            showProgress(false);

            if (success) {//successful registration of user and now log that user
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",user_id+"");
                startActivity(intent);
                try {
                    logUserAndSetDetails(URLs.URL_FETCH_USERS+"?id="+user_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                LoginActivity.this.finish();//finish();//zatvarq activity-to na mesto tova da se vika main Activity
                Toast.makeText(getBaseContext(),"YAY register successful \nUsername ="+ mUsername +" Pass="+mPassword,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(),"Error \n"+response_string, Toast.LENGTH_SHORT).show();
//                if(response_string!=null) {
//                    mPasswordView.setError(getString(R.string.error_incorrect_password));
//                    mPasswordView.requestFocus();
//                }
            }
        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
            showProgress(false);
        }
    }
    /**
     * Represents an asynchronous registration of a company with admin username and password.
     */
    public class CompanyRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private final String mCompanyName;
        private final String mCompanyDesc;
        String response_string;

//        OkHttpClient client = new OkHttpClient();
        CompanyRegisterTask(String username, String password, String CompanyName,String CompanyDesc) {
            Log.d(TAG, "UserRegisterTask: constructor");
            mUsername = username;
            mPassword = password;
            mCompanyName = CompanyName;
            mCompanyDesc = CompanyDesc;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //network access.
            String json="{ \"username\":\""+ mUsername +"\",\"password\":\""+mPassword+"\",\"name\":\""+ mCompanyName +"\",\"description\":\""+ mCompanyDesc +"\"}";
            Log.i("TAG CompanyRegisterTask", "doInBackground: input info"+json);
            Log.d(TAG, "doInBackground: url="+URLs.URL_REGISTER_COMPANY);
            RequestBody body = RequestBody.create(Constants.JSON, json);
            Request request = new Request.Builder()
                    .url(URLs.URL_REGISTER_COMPANY)
                    .post(body)
                    .build();
            try {
                Response response = SharedPreferencesManage.client.newCall(request).execute();
                response_string = response.body().string();
                response.close();

                Log.i("TAG", "doInBackground: the response from the server is :"+ response_string);
                if(response_string.contains("Company already exists")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "Company already exists", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, response_string, Toast.LENGTH_SHORT).show();
                    }
                });
                    return true;
//                return true;
            }catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }


        //it is not called anywhere in this code
        //haha ama 4e si preburzal sus zaklu4eniqta sa6e
        @Override
        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
            showProgress(false);

            if (success) {
//                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("id",user_id+"");
//                startActivity(intent);
//                try {
//                    logUserAndSetDetails(URLs.URL_FETCH_USERS+"?id="+user_id);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                LoginActivity.this.finish();//finish();//zatvarq activity-to na mesto tova da se vika main Activity
                Toast.makeText(getBaseContext(),"YAY register Company successful \nUsername ="+ mUsername +" Pass="+mPassword,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(),"Error \n"+response_string, Toast.LENGTH_SHORT).show();
//                if(response_string!=null) {
//                    mPasswordView.setError(getString(R.string.error_incorrect_password));
//                    mPasswordView.requestFocus();
//                }
            }
        }

        @Override
        protected void onCancelled() {
//            mAuthTask = null;
            showProgress(false);
        }
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop:");
        super.onStop();
//        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();

    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}

