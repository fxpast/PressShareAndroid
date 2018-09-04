package com.prv.gootoor.activities;

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


import android.Manifest;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.prv.gootoor.R;
import com.prv.gootoor.lists.Capitals;
import com.prv.gootoor.lists.Cards;
import com.prv.gootoor.lists.Messages;
import com.prv.gootoor.lists.PressOperations;
import com.prv.gootoor.lists.Products;
import com.prv.gootoor.lists.Transactions;
import com.prv.gootoor.services.QuickstartPreferences;
import com.prv.gootoor.services.RegistrationIntentService;
import com.prv.gootoor.utils.MyTools;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.daos.MDBInterfaceArray;
import com.prv.gootoor.daos.MDBUser;
import com.prv.gootoor.daos.MDBInterface;
import com.prv.gootoor.models.User;
import com.prv.gootoor.utils.MainThreadInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class LoginActivity extends AppCompatActivity {


    private EditText mIBPassword;

    private EditText mIBUser;
    private Button mIBLogin;
    private ProgressBar mIBActivity;
    private Config mConfig = Config.sharedInstance();

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "LoginActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ImageView mIBTouchID;
    private FingerprintManager fingerprintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mIBActivity = (ProgressBar) findViewById(R.id.IBProgressBar);

        mIBLogin = (Button) findViewById(R.id.IBLogin);
        mIBLogin.setPaintFlags(mIBLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

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

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (!sentToken) {

                    MyTools.sharedInstance().performUIUpdatesOnMain(LoginActivity.this, new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(LoginActivity.this, getString(R.string.errorToken));

                        }
                    });

                }


            }
        };


        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }


        mIBActivity.setVisibility(View.GONE);

        mConfig.setPreviousView("LoginViewController");

        initColor();

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

        if (mConfig.getIsFingerPrint()) {
            mConfig.setIsFingerPrint(false);
            callAuthFacebook();
        }


        if (!isSensorAvialable()) {
            mIBTouchID.setVisibility(View.GONE);
        }



    }



    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));



    }


    private boolean isSensorAvialable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            return ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED &&
                    getSystemService(FingerprintManager.class).isHardwareDetected();

        } else {

            return FingerprintManagerCompat.from(this).isHardwareDetected();
        }
    }


    /**
     * Vérifier si notre utilisateur a l'application Google Play Service
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int resultCode = googleAPI.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(resultCode)) {
                googleAPI.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void initColor() {

        Button mIBAnonyme;
        Button mIBLostPass;
        Button mIBNewAccount;


        TextView mIBPressConnect;
        ConstraintLayout mIBLoginBackgr;
        ImageView mIBInfo;

        mIBNewAccount = (Button) findViewById(R.id.IBNewAccount);
        mIBNewAccount.setPaintFlags(mIBNewAccount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mIBAnonyme = (Button) findViewById(R.id.IBAnonyme);
        mIBAnonyme.setPaintFlags(mIBAnonyme.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mIBLostPass = (Button) findViewById(R.id.IBLostPass);
        mIBLostPass.setPaintFlags(mIBLostPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mIBLoginBackgr = (ConstraintLayout) findViewById(R.id.IBLoginBackgr);
        mIBInfo = (ImageView) findViewById(R.id.IBInfo);
        mIBTouchID = (ImageView) findViewById(R.id.IBTouchID);

        mIBPressConnect = (TextView) findViewById(R.id.IBPressConnect);


        String hexColor = "#" + mConfig.getColorApp();
        mIBLoginBackgr.setBackgroundColor(Color.parseColor(hexColor));
        mIBInfo.setBackgroundColor(Color.parseColor(hexColor));
        mIBTouchID.setBackgroundColor(Color.parseColor(hexColor));
        hexColor = "#" + mConfig.getColorAppBt();
        mIBAnonyme.setTextColor(Color.parseColor(hexColor));
        mIBNewAccount.setTextColor(Color.parseColor(hexColor));
        mIBLogin.setTextColor(Color.parseColor(hexColor));
        mIBLostPass.setTextColor(Color.parseColor(hexColor));
        hexColor = "#" + mConfig.getColorAppLabel();
        mIBPressConnect.setTextColor(Color.parseColor(hexColor));
        hexColor = "#" + mConfig.getColorAppPlHd();
        mIBUser.setHintTextColor(Color.parseColor(hexColor));
        mIBPassword.setHintTextColor(Color.parseColor(hexColor));
        hexColor = "#" + mConfig.getColorAppText();
        mIBUser.setTextColor(Color.parseColor(hexColor));
        mIBPassword.setTextColor(Color.parseColor(hexColor));


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


    public void authWithTouchID(View view) {

        if (mConfig.getUser_email().equals("")) {
            MyTools.sharedInstance().displayAlert(this, getString(R.string.firstCon));
        } else {

            Intent intent = new Intent(this, FingerPrintActivity.class);
            startActivity(intent);
        }


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

    private void callAuthFacebook() {

        mIBActivity.setVisibility(View.VISIBLE);

        setUIEnabled(false);


        MDBUser.sharedInstance().AuthentiFacebook(this, mConfig, new MDBInterfaceArray() {

            @Override
            public void completionHandlerArray(Boolean success, JSONArray userArray, final String errorString) {

                if (success) {

                    try {
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

    private void callMenu() {

        if (!mConfig.getUser_pseudo().equals("")) {

            mIBPassword.setText("");
            mIBUser.setText("");
            setUIEnabled(true);

            if (mConfig.getUser_newpassword()) {
                mConfig.setPreviousView("SettingsTableViewContr");

                //Todo: Add an activity here for updating password
                mIBActivity.setVisibility(View.GONE);
                Intent intent = new Intent(this, ChangePwdActivity.class);
                startActivity(intent);

            }
            else {

                if (mConfig.getTokenString().equals("")) {

                    //Todo: Add an activity here for lunching main tabbar
                    mIBActivity.setVisibility(View.GONE);
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
                                        mIBActivity.setVisibility(View.GONE);
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
                                        mIBActivity.setVisibility(View.GONE);
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

        mIBUser = (EditText) MyTools.sharedInstance().activerObjet(mIBUser, enabled);
        mIBPassword = (EditText) MyTools.sharedInstance().activerObjet(mIBPassword, enabled);
        mIBLogin = (Button) MyTools.sharedInstance().activerObjet(mIBLogin, enabled);


    }





}
