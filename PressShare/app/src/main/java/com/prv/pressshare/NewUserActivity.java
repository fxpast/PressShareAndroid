package com.prv.pressshare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewUserActivity extends AppCompatActivity {

    private NewUserTask mAuthTask = null;

    // UI references.
    private EditText mPseudoView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mCheckPwdView;
    private View mProgressView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // Set up the login form.
        mPseudoView = (EditText) findViewById(R.id.pseudo);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mCheckPwdView = (EditText) findViewById(R.id.checkpwd);

        mCheckPwdView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

                attemptValidate();
                return true;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.save_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptValidate();
            }
        });

        mProgressView = findViewById(R.id.login_progress);

    }

    private void attemptValidate() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPseudoView.setError(null);
        mPasswordView.setError(null);
        mCheckPwdView.setError(null);
        mEmailView.setError(null);


        // Store values at the time of the login attempt.
        String pseudo = mPseudoView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String checkpwd = mCheckPwdView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid pseudo.
        if (TextUtils.isEmpty(pseudo)) {
            mPseudoView.setError(getString(R.string.error_field_required));
            focusView = mPseudoView;
            cancel = true;
        }


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(email) || !isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(checkpwd) || !isCheckPwd(password, checkpwd)) {
            mPasswordView.setError(getString(R.string.error_invalid_checkpwd));
            focusView = mPasswordView;
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
            mAuthTask = new NewUserTask(pseudo, password, email, checkpwd);
            mAuthTask.execute((Void) null);
        }
    }


    private boolean isPseudoValid(String pseudo) {
        //TODO: Replace this with your own logic
        return pseudo.length() > 2;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean isCheckPwd(String password, String checkpwd) {
        //TODO: Replace this with your own logic
        return password.equals(checkpwd);
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


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class NewUserTask extends AsyncTask<Void, Void, Boolean> {

        private final String mPseudo;
        private final String mPassword;
        private final String mCheckPwd;
        private final String mEmail;

        NewUserTask(String pseudo, String password, String email, String checkpwd) {
            mPseudo = pseudo;
            mEmail = email;
            mCheckPwd = checkpwd;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                finish();

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


}
