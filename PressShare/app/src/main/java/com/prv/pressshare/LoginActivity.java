package com.prv.pressshare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;


/**
 * A login screen that offers login via pseudo/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private EditText mPseudoView;
    private EditText mPasswordView;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mPseudoView = (EditText) findViewById(R.id.pseudo);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                attemptLogin();
                return true;

            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogup();
            }
        });

        Button mGuestButton = (Button) findViewById(R.id.guest_button);
        mGuestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptGuest();
            }
        });


        Button mForgetButton = (Button) findViewById(R.id.forgetten_button);
        mForgetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptForget();
            }
        });

        ImageButton mHelpButton = (ImageButton) findViewById(R.id.help_button);
        mHelpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptHelp();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
    }


    private void attemptHelp() {

        startActivity(new Intent(this, HelpActivity.class));

    }

    private void attemptGuest() {

        startActivity(new Intent(this, TabMainActivity.class));

    }

    private void attemptForget() {

        startActivity(new Intent(this, ForgettenActivity.class));

    }

    private void attemptLogup() {

        startActivity(new Intent(this, NewUserActivity.class));


    }

    private void attemptLogin() {



        // Reset errors.
        mPseudoView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String pseudo = mPseudoView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid pseudo.
        if (TextUtils.isEmpty(pseudo)) {
            mPseudoView.setError(getString(R.string.error_field_required));
            focusView = mPseudoView;
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
            login(pseudo, password);

        }
    }

    private void login(final String pseudo, final String password){
        //Getting values from edit texts



        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        JSONObject result = null;
                        try {
                             result = (JSONObject) new JSONTokener(response).nextValue();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String success = null;
                        try {
                            success = (String) result.get(Config.LOGIN_SUCCESS);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                            if(success.equals("1")){

                                startActivity(new Intent(LoginActivity.this, TabMainActivity.class));

                            }else{
                                //If the server response is not success
                                //Displaying an error message on toast
                                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();

                            }
                            showProgress(false);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_PSEUDO, pseudo);
                params.put(Config.KEY_PASSWORD, password);
                params.put(Config.KEY_LANG, "us");

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private boolean isPseudoValid(String pseudo) {
        //TODO: Replace this with your own logic
        return pseudo.length() > 2;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
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

        }
    }







}

