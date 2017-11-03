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
//Todo new : supprimer doublon proprieté user et mConfig
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
import com.prv.pressshare.lists.Capitals;
import com.prv.pressshare.lists.Cards;
import com.prv.pressshare.lists.Messages;
import com.prv.pressshare.lists.PressOperations;
import com.prv.pressshare.lists.Products;
import com.prv.pressshare.lists.Transactions;
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

    private ProgressBar mIBActivity;
    private  Button mIBNewAccount;
    private  Button mIBAnonyme;
    private  Button mIBLostPass;

    private Config mConfig = Config.sharedInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mIBActivity = (ProgressBar) findViewById(R.id.IBProgressBar);

        mIBLogin = (Button) findViewById(R.id.IBLogin);
        mIBLogin.setPaintFlags(mIBLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mIBNewAccount = (Button) findViewById(R.id.IBNewAccount);
        mIBNewAccount.setPaintFlags(mIBNewAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mIBAnonyme = (Button) findViewById(R.id.IBAnonyme);
        mIBAnonyme.setPaintFlags(mIBAnonyme.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mIBLostPass = (Button) findViewById(R.id.IBLostPass);
        mIBLostPass.setPaintFlags(mIBLostPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mIBUser = (EditText) findViewById(R.id.IBUser);
        mIBPassword = (EditText) findViewById(R.id.IBPassword);


        //Events

        mIBPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                actionLogin(v);
                return false;
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();


        initArray(PressOperations.sharedInstance().getOperationArray());
        initArray(Messages.sharedInstance().getMessagesArray());
        initArray(Transactions.sharedInstance().getTransactionArray());
        initArray(Capitals.sharedInstance().getCapitalsArray());
        initArray(Products.sharedInstance().getProductsArray());
        initArray(Products.sharedInstance().getProductsUserArray());
        initArray(Cards.sharedInstance().getCardsArray());

        mConfig.cleaner(this);

        mIBActivity.setVisibility(View.GONE);

        mConfig.setPreviousView("LoginViewController");


        //first time connected?
        SharedPreferences sharedPref = getSharedPreferences(mConfig.getFileParameters(), Context.MODE_PRIVATE);
        mConfig.setTokenString(sharedPref.getString(mConfig.getDomaineApp()+"tokenString",""));
        mConfig.setUser_pseudo(sharedPref.getString(mConfig.getDomaineApp()+"user_pseudo",""));
        mConfig.setUser_email(sharedPref.getString(mConfig.getDomaineApp()+"user_email",""));

        Boolean firstTime = sharedPref.getBoolean(mConfig.getDomaineApp()+"firstTime",true);

        mIBUser.setText(mConfig.getUser_pseudo());

        //show Tuto_Presentation
        if (firstTime) {

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(mConfig.getDomaineApp()+"firstTime", false);
            editor.apply();

            MyTools.sharedInstance().showHelp("Tuto_Presentation", this);

        }


    }


    private void initArray(JSONArray anArray) {

        while (anArray.length()>0) {

            anArray.remove(0);
        }

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

        Intent intent = new Intent(this, ChangePwdActivity.class);
        startActivity(intent);

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

        mConfig.setUser_pass(mIBPassword.getText().toString());
        mConfig.setUser_pseudo(mIBUser.getText().toString());

        MDBUser.sharedInstance().Authentification(this, mConfig, new MDBInterfaceArray() {

                @Override
            public void completionHandlerArray(Boolean success, JSONArray userArray, final String errorString) {

                if (success) {

                    final SharedPreferences sharedPref = getSharedPreferences(mConfig.getFileParameters(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    try {
                        editor.putString(mConfig.getDomaineApp()+"user_pseudo",  userArray.getJSONObject(0).getString("user_pseudo"));
                        editor.putString(mConfig.getDomaineApp()+"user_email", userArray.getJSONObject(0).getString("user_email"));
                        editor.apply();
                        assignUser(new User(userArray.getJSONObject(0)));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    MyTools.sharedInstance().performUIUpdatesOnMain(LoginActivity.this, new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {

                            callMenu();
                            mIBActivity.setVisibility(View.GONE);

                        }
                    });

                }
                else {

                    MyTools.sharedInstance().performUIUpdatesOnMain(LoginActivity.this, new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {
                            setUIEnabled(true);
                            MyTools.sharedInstance().displayAlert(LoginActivity.this,errorString);
                            mIBActivity.setVisibility(View.GONE);


                        }
                    });
                }
            }
        });


    }

    public void actionNewAccount(View view) {

        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);

    }


    private void callMenu() {

        if (!mConfig.getUser_pseudo().equals("")) {

            mIBPassword.setText("");
            mIBUser.setText("");
            setUIEnabled(true);

            if (mConfig.getUser_newpassword()) {
                mConfig.setPreviousView("SettingsTableViewContr");

                //Todo: Add an activity here for updating password
                MyTools.sharedInstance().displayAlert(this, "updating password activity is opened");

            }
            else {

                if (mConfig.getTokenString().equals("")) {

                    //Todo: Add an activity here for lunching main tabbar
                    Intent intent = new Intent(this, TabMainActivity.class);
                    startActivity(intent);


                }
                else {

                    MDBUser.sharedInstance().setUpdateUserToken(this, mConfig, new MDBInterface() {

                        @Override
                        public void completionHandler(Boolean success, final String errorString) {

                            if (success) {

                                MyTools.sharedInstance().performUIUpdatesOnMain(LoginActivity.this, new MainThreadInterface() {

                                    @Override
                                    public void completionUpdateMain() {

                                        //Todo: Add an activity here for lunching main tabbar
                                        Intent intent = new Intent(LoginActivity.this, TabMainActivity.class);
                                        intent.putExtra(mConfig.getDomaineApp()+"typeListe",0);
                                        intent.putExtra(mConfig.getDomaineApp()+"typeSListe",0);
                                        startActivity(intent);


                                    }
                                });

                            }
                            else  {

                                MyTools.sharedInstance().performUIUpdatesOnMain(LoginActivity.this, new MainThreadInterface() {

                                    @Override
                                    public void completionUpdateMain() {
                                        setUIEnabled(true);
                                        MyTools.sharedInstance().displayAlert(LoginActivity.this,errorString);


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


        mConfig.setUser_id(aUser.getUser_id());
        mConfig.setUser_pseudo(aUser.getUser_pseudo());
        mConfig.setUser_email(aUser.getUser_email());
        mConfig.setUser_nom(aUser.getUser_nom());
        mConfig.setUser_prenom(aUser.getUser_prenom());
        mConfig.setUser_newpassword(aUser.getUser_newpassword());
        mConfig.setUser_pays(aUser.getUser_pays());
        mConfig.setUser_ville(aUser.getUser_ville());
        mConfig.setUser_adresse(aUser.getUser_adresse());
        mConfig.setUser_codepostal(aUser.getUser_codepostal());
        mConfig.setVerifpassword("");
        mConfig.setLevel(aUser.getUser_level());
        mConfig.setUser_countNote(aUser.getUser_countNote());
        mConfig.setUser_note(aUser.getUser_note());

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
