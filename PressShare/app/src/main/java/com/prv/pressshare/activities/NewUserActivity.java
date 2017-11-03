package com.prv.pressshare.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.prv.pressshare.R;
import com.prv.pressshare.daos.MDBInterface;
import com.prv.pressshare.daos.MDBUser;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MainThreadInterface;
import com.prv.pressshare.utils.MyTools;

/**
 * Created by roger on 31/10/2017.
 */

public class NewUserActivity extends AppCompatActivity {

    private Config mConfig = Config.sharedInstance();

    private EditText mIBNewPseudo;
    private EditText mIBNewEmail;
    private EditText mIBNewPwd;
    private EditText mIBNewCheckPwd;
    private Button mIBNewSave;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        mIBNewPseudo = (EditText) findViewById(R.id.IBNewPseudo);
        mIBNewEmail = (EditText) findViewById(R.id.IBNewEmail);
        mIBNewPwd = (EditText) findViewById(R.id.IBNewPwd);
        mIBNewCheckPwd = (EditText) findViewById(R.id.IBNewCheckPwd);
        mIBNewSave = (Button) findViewById(R.id.IBNewSave);


    }

    public void actionNewValid(View view) {

        if (mIBNewPseudo.getText().toString().length() == 0) {
            MyTools.sharedInstance().displayAlert(this,getString(R.string.errorLogin));
            return;
        }

        if (mIBNewEmail.getText().toString().length() == 0) {
            MyTools.sharedInstance().displayAlert(this,getString(R.string.errorMail));
            return;
        }

        if (mIBNewPwd.getText().toString().length() == 0) {
            MyTools.sharedInstance().displayAlert(this,getString(R.string.errorPassword));
            return;
        }

        if (!mIBNewPwd.getText().toString().equals(mIBNewCheckPwd.getText().toString())) {
            MyTools.sharedInstance().displayAlert(this,getString(R.string.errorPassword));
            return;
        }


        mIBNewSave.setEnabled(false);
        mConfig.setUser_pseudo(mIBNewPseudo.getText().toString());
        mConfig.setUser_email(mIBNewEmail.getText().toString());
        mConfig.setUser_pass(mIBNewPwd.getText().toString());

        MDBUser.sharedInstance().setAddUser(this, mConfig, new MDBInterface() {
            @Override
            public void completionHandler(final Boolean success, final String errorString) {

                MyTools.sharedInstance().performUIUpdatesOnMain(NewUserActivity.this, new MainThreadInterface() {
                    @Override
                    public void completionUpdateMain() {

                        mIBNewSave.setEnabled(true);

                        if (success) {

                            finish();

                        } else {
                            MyTools.sharedInstance().displayAlert(NewUserActivity.this,errorString);

                        }


                    }
                });

            }
        });



    }


    public void actionCloseWidows(View view) {

        finish();
    }


    public void actionAbonnerHelp(View view) {


        MyTools.sharedInstance().showHelp("NewUserTableViewContr", this);

    }

}
