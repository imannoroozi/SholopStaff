package com.sholop.sholopstaff.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.config.AppConfig;
import com.sholop.sholopstaff.config.AppController;
import com.sholop.sholopstaff.utilities.SQLiteHandler;
import com.sholop.sholopstaff.utilities.SessionManager;
import com.sholop.sholopstaff.utilities.Util;


public class LoginActivity extends Activity {
    private View btnLogin;
    private EditText inputEmail;
    private EditText inputPassword;

    TextView login, loginIcon, emailIcon, passIcon;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    Typeface regular, awesome;
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(this);

        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
            // User is already logged in. Take him to main activity
            startActivity(intent);
            finish();
        }

        regular = Util.getInstance().getFontRegular(LoginActivity.this);
        awesome = Util.getInstance().getFontAwesome(LoginActivity.this);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin =  findViewById(R.id.btnLogin);

        login = (TextView) findViewById(R.id.login_textview);
        loginIcon = (TextView) findViewById(R.id.login_icon);
        emailIcon = (TextView) findViewById(R.id.email_icon);
        passIcon = (TextView) findViewById(R.id.password_icon);

        inputEmail.setTypeface(regular);
        inputPassword.setTypeface(regular);
        login.setTypeface(regular);

        loginIcon.setTypeface(awesome);
        emailIcon.setTypeface(awesome);
        passIcon.setTypeface(awesome);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.enter_credentials), Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

    }

    /**
     * function to verify login details in mysql db
     */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage(getString(R.string.logging_in));
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session

                        // Now store the user in SQLite
                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("display_name");
                        String email = user.getString("user_email");
                        String uid = user.getString("user_id");

                        // Inserting row in users table
                        db.addUser(name, email, uid);

                        session.setLogin(true);
                        session.setCurrentUser(uid, email, name);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this,
                                MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getString(R.string.appointment_loading_error), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.appointment_loading_error), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("action", "LOGIN");

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}