package com.prv.pressshare.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.prv.pressshare.R;
import com.prv.pressshare.daos.MDBInterface;
import com.prv.pressshare.daos.MDBUser;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MainThreadInterface;
import com.prv.pressshare.utils.MyTools;

import java.util.UUID;

/**
 * Created by roger on 25/10/2017.
 */

public class ChangePwdActivity extends AppCompatActivity {

    private Config mConfig = Config.sharedInstance();
    private EditText mIBEmail;
    private EditText mIBOldPass;
    private EditText mIBNewPass;
    private EditText mIBCheckPass;
    private TextView mIBChangPwdTitle;

    private TextInputLayout mIBEmailLabel;
    private TextInputLayout mIBOldPassLabel;
    private TextInputLayout mIBNewPassLabel;
    private TextInputLayout mIBCheckPassLabel;



    private Button mIBChangPwdSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);


        mIBChangPwdTitle = (TextView) findViewById(R.id.IBChangPwdTitle);
        mIBChangPwdSave = (Button) findViewById(R.id.IBChangPwdSave);
        mIBEmail = (EditText) findViewById(R.id.IBEmail);
        mIBOldPass = (EditText) findViewById(R.id.IBOldPass);
        mIBNewPass = (EditText) findViewById(R.id.IBNewPass);
        mIBCheckPass = (EditText) findViewById(R.id.IBCheckPass);

        mIBEmailLabel = (TextInputLayout) findViewById(R.id.IBEmailLabel);
        mIBOldPassLabel = (TextInputLayout) findViewById(R.id.IBOldPassLabel);
        mIBNewPassLabel = (TextInputLayout) findViewById(R.id.IBNewPassLabel);
        mIBCheckPassLabel = (TextInputLayout) findViewById(R.id.IBCheckPassLabel);

        mIBEmailLabel.setVisibility(View.VISIBLE);
        mIBEmail.setText(mConfig.getUser_email());


        if (mConfig.getPreviousView().equals("LoginViewController")) {

            mIBChangPwdTitle.setText(getText(R.string.lostPassword));


            if (mConfig.getUser_newpassword()) {


                mIBNewPassLabel.setVisibility(View.VISIBLE);
                mIBCheckPassLabel.setVisibility(View.VISIBLE);
                mIBOldPassLabel.setVisibility(View.INVISIBLE);


            } else {

                mIBNewPassLabel.setVisibility(View.INVISIBLE);
                mIBCheckPassLabel.setVisibility(View.INVISIBLE);
                mIBOldPassLabel.setVisibility(View.INVISIBLE);


            }

        } else if (mConfig.getPreviousView().equals("SettingsTableViewContr")) {

            mIBChangPwdTitle.setText(mConfig.getUser_pseudo() + " (" + mConfig.getUser_id() + ")");

            mIBNewPassLabel.setVisibility(View.VISIBLE);
            mIBCheckPassLabel.setVisibility(View.VISIBLE);
            mIBOldPassLabel.setVisibility(View.VISIBLE);

        }
    }


    public void actionCloseWidows(View view) {

        finish();
    }


    public void actionChangPwdHelp(View view) {

        MyTools.sharedInstance().showHelp("ChangePwdTableViewContr", this);

    }

    public void actionChangPwdValid(View view) {

        if (mIBEmail.getText().toString().equals("")) {

            MyTools.sharedInstance().displayAlert(this, getString(R.string.errorMail));
            return;
        }

        //--- Login View Controller
        if (mConfig.getPreviousView().equals("LoginViewController")) {

            if (mConfig.getUser_newpassword()) {
                if (mIBNewPass.getText().length() == 0) {

                    MyTools.sharedInstance().displayAlert(this, getString(R.string.errorNewPassword));
                    return;
                }

                if (mIBCheckPass.getText().length() == 0) {

                    MyTools.sharedInstance().displayAlert(this, getString(R.string.errorCheckPassword));
                    return;
                }

                if (!mIBNewPass.getText().toString().equals(mIBCheckPass.getText().toString())) {

                    MyTools.sharedInstance().displayAlert(this, getString(R.string.loginPassword));
                    return;
                }

                mConfig.setUser_lastpass(mIBNewPass.getText().toString());
                mConfig.setUser_newpassword(false);

            } else {

                mConfig.setUser_lastpass(randomAlphaNumericString(8));
                mConfig.setUser_email(mIBEmail.getText().toString());
                mConfig.setUser_newpassword(true);

            }

        } else if (mConfig.getPreviousView().equals("SettingsTableViewContr")) {

            if (mIBOldPass.getText().length() == 0) {

                MyTools.sharedInstance().displayAlert(this, getString(R.string.errorOldPassword));
                return;
            }

            if (mIBNewPass.getText().length() == 0) {

                MyTools.sharedInstance().displayAlert(this, getString(R.string.errorNewPassword));
                return;
            }

            if (mIBCheckPass.getText().length() == 0) {

                MyTools.sharedInstance().displayAlert(this, getString(R.string.errorCheckPassword));
                return;
            }

            if (!mIBNewPass.getText().toString().equals(mIBCheckPass.getText().toString())) {

                MyTools.sharedInstance().displayAlert(this, getString(R.string.loginPassword));
                return;
            }

            mConfig.setUser_pass(mIBOldPass.getText().toString());
            mConfig.setUser_lastpass(mIBNewPass.getText().toString());
            mConfig.setUser_newpassword(false);

        }

        MDBUser.sharedInstance().setUpdatePass(this, mConfig, new MDBInterface() {
            @Override
            public void completionHandler(final Boolean success, final String errorString) {

                MyTools.sharedInstance().performUIUpdatesOnMain(ChangePwdActivity.this, new MainThreadInterface() {

                    @Override
                    public void completionUpdateMain() {

                        if (success) {

                            if (mConfig.getUser_newpassword()) {

                                mIBChangPwdSave.setEnabled(false);
                                AlertDialog.Builder builder;

                                builder = new AlertDialog.Builder(ChangePwdActivity.this);
                                builder.setTitle(getString(R.string.password));
                                builder.setMessage(getString(R.string.emailPassword));

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {

                                finish();
                            }

                        } else {

                            MyTools.sharedInstance().displayAlert(ChangePwdActivity.this, errorString);
                        }

                    }
                });


            }
        });


    }

    private String randomAlphaNumericString(int length) {

        String randomStr = UUID.randomUUID().toString();
        while(randomStr.length() < length) {
            randomStr += UUID.randomUUID().toString();
        }
        return randomStr.substring(0, length);

    }
}
