package com.prv.pressshare.activities;

//
//  LoginActivity
//  PressShare
//
//  Description : Sign in and if you have forgotten you can ask a new one. Connection by user/password or by facebook
//
//  Created by MacbookPRV on 18/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//
//Todo new : supprimer doublon proprieté user et config
//Todo new : login par pseudo ou email


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.prv.pressshare.R;
import com.prv.pressshare.utils.MyTools;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.daos.MDBInterfaceArray;
import com.prv.pressshare.daos.MDBUser;
import com.prv.pressshare.daos.MDBInterface;
import com.prv.pressshare.models.User;
import com.prv.pressshare.utils.MainThreadInterface;
import org.json.JSONArray;
import org.json.JSONException;


public class LoginActivity extends AppCompatActivity {



    private EditText mIBPassword;
    private EditText mIBUser;
    private Button mIBLogin;
    private Config config;
    private ProgressBar mIBActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mIBActivity = (ProgressBar) findViewById(R.id.IBActivity);

        mIBLogin = (Button) findViewById(R.id.IBLogin);
        mIBLogin.setPaintFlags(mIBLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button mIBNewAccount = (Button) findViewById(R.id.IBNewAccount);
        mIBNewAccount.setPaintFlags(mIBNewAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button mIBAnonyme = (Button) findViewById(R.id.IBAnonyme);
        mIBAnonyme.setPaintFlags(mIBAnonyme.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button mIBLostPass = (Button) findViewById(R.id.IBLostPass);
        mIBLostPass.setPaintFlags(mIBLostPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mIBUser = (EditText) findViewById(R.id.IBUser);


        config = Config.sharedInstance();
        config.cleaner(this);

        mIBActivity.setVisibility(View.GONE);



        config.setPreviousView("LoginViewController");

        /*
        PressOperations.sharedInstance.operationArray = nil
        Messages.sharedInstance.MessagesArray = nil
        Transactions.sharedInstance.transactionArray = nil
        Capitals.sharedInstance.capitalsArray = nil
        Products.sharedInstance.productsArray = nil
        Products.sharedInstance.productsUserArray = nil
        Cards.sharedInstance.cardsArray = nil
        * */



        //first time connected?
        SharedPreferences sharedPref = getSharedPreferences(config.getFileParameters(), Context.MODE_PRIVATE);
        config.setTokenString(sharedPref.getString(config.getDomaineApp()+"tokenString",""));
        config.setUser_pseudo(sharedPref.getString(config.getDomaineApp()+"user_pseudo",""));
        config.setUser_email(sharedPref.getString(config.getDomaineApp()+"user_email",""));

        Boolean firstTime = sharedPref.getBoolean(config.getDomaineApp()+"firstTime",true);

        mIBUser.setText(config.getUser_pseudo());

        //show Tuto_Presentation
        if (firstTime) {

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(config.getDomaineApp()+"firstTime", false);
            editor.apply();

            MyTools.sharedInstance().showHelp("Tuto_Presentation", this);

        }



        //Events

        mIBPassword = (EditText) findViewById(R.id.IBPassword);
        mIBPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                actionLogin(v);
                return false;
            }
        });



    }



    public void actionInfo(View view) {


        MyTools.sharedInstance().showHelp("Tuto_Presentation", this);


    }

    public void actionAnonyme(View view) {

        mIBUser.setText("anonymous");
        mIBPassword.setText("anonymous");

        actionLogin(mIBLogin);



    }

    public void actionPassword(View view) {


        MyTools.sharedInstance().displayAlert(this, "This is forgetten password button");

    }

    public void actionLogin(View view) {




        if (mIBUser.getText().length() == 0){

            MyTools.sharedInstance().displayAlert(this, getString(R.string.errorLogin));
            return;

        }

        if (mIBPassword.getText().length() == 0){

            MyTools.sharedInstance().displayAlert(this, getString(R.string.errorPassword));
            return;

        }

        mIBActivity.setVisibility(View.VISIBLE);

        setUIEnabled(false);

        config.setUser_pass(mIBPassword.getText().toString());
        config.setUser_pseudo(mIBUser.getText().toString());

        MDBUser.sharedInstance().Authentification(this, config, new MDBInterfaceArray() {

                @Override
            public void completionHandlerArray(Boolean success, JSONArray userArray, final String errorString) {

                if (success) {

                    final SharedPreferences sharedPref = getSharedPreferences(config.getFileParameters(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    try {
                        editor.putString(config.getDomaineApp()+"user_pseudo",  userArray.getJSONObject(0).getString("user_pseudo"));
                        editor.putString(config.getDomaineApp()+"user_email", userArray.getJSONObject(0).getString("user_email"));
                        editor.apply();
                        assignUser(new User(userArray.getJSONObject(0)));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    MyTools.sharedInstance().performUIUpdatesOnMain(getBaseContext(), new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {

                            callMenu();
                            mIBActivity.setVisibility(View.GONE);

                        }
                    });

                }
                else {

                    MyTools.sharedInstance().performUIUpdatesOnMain(getBaseContext(), new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {
                            setUIEnabled(true);
                            MyTools.sharedInstance().displayAlert(getBaseContext(),errorString);
                            mIBActivity.setVisibility(View.GONE);


                        }
                    });
                }
            }
        });


    }

    public void actionNewAccount(View view) {



        MyTools.sharedInstance().displayAlert(this, "This is sign up button");

    }


    private void callMenu() {

        if (!config.getUser_pseudo().equals("")) {

            mIBPassword.setText("");
            setUIEnabled(true);

            if (config.getUser_newpassword()) {
                config.setPreviousView("SettingsTableViewContr");

                //Todo: Add an activity here for updating password
                MyTools.sharedInstance().displayAlert(this, "updating password activity is opened");

            }
            else {

                if (config.getTokenString().equals("")) {

                    //Todo: Add an activity here for lunching main tabbar
                    Intent intent = new Intent(this, TabMainActivity.class);
                    startActivity(intent);


                }
                else {

                    MDBUser.sharedInstance().setUpdateUserToken(this, config, new MDBInterface() {

                        @Override
                        public void completionHandler(Boolean success, final String errorString) {

                            if (success) {

                                MyTools.sharedInstance().performUIUpdatesOnMain(getBaseContext(), new MainThreadInterface() {

                                    @Override
                                    public void completionUpdateMain() {

                                        //Todo: Add an activity here for lunching main tabbar
                                        Intent intent = new Intent(LoginActivity.this, TabMainActivity.class);
                                        startActivity(intent);


                                    }
                                });

                            }
                            else  {

                                MyTools.sharedInstance().performUIUpdatesOnMain(getBaseContext(), new MainThreadInterface() {

                                    @Override
                                    public void completionUpdateMain() {
                                        setUIEnabled(true);
                                        MyTools.sharedInstance().displayAlert(getBaseContext(),errorString);


                                    }
                                });

                            }
                        }
                    });
                }
            }
        }
    }


    private void assignUser(User aUser) {


        config.setUser_id(aUser.getUser_id());
        config.setUser_pseudo(aUser.getUser_pseudo());
        config.setUser_email(aUser.getUser_email());
        config.setUser_nom(aUser.getUser_nom());
        config.setUser_prenom(aUser.getUser_prenom());
        config.setUser_newpassword(aUser.getUser_newpassword());
        config.setUser_pays(aUser.getUser_pays());
        config.setUser_ville(aUser.getUser_ville());
        config.setUser_adresse(aUser.getUser_adresse());
        config.setUser_codepostal(aUser.getUser_codepostal());
        config.setVerifpassword("");
        config.setLevel(aUser.getUser_level());
        config.setUser_countNote(aUser.getUser_countNote());
        config.setUser_note(aUser.getUser_note());

    }



    private void setUIEnabled(Boolean enabled) {

        mIBUser.setEnabled(enabled);
        mIBPassword.setEnabled(enabled);
        mIBLogin.setEnabled(enabled);
        // adjust login button alpha
        if (enabled) {
            mIBLogin.setAlpha(1.0f);
        } else {
            mIBLogin.setAlpha(0.5f);
        }


    }





}
