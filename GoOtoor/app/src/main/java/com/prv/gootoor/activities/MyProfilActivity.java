package com.prv.gootoor.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.prv.gootoor.R;
import com.prv.gootoor.daos.MDBInterface;
import com.prv.gootoor.daos.MDBUser;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;

/**
 * Created by roger on 20/10/2017.
 */

public class MyProfilActivity extends AppCompatActivity {

    private Config mConfig = Config.sharedInstance();
    private EditText mIBMyPseudo;
    private EditText mIBMyEmail;
    private EditText mIBMyNickName;
    private EditText mIBMySurname;
    private EditText mIBMyAdresse;
    private EditText mIBMyZipCode;
    private EditText mIBMyCity;
    private EditText mIBMyCountry;
    private Button mIBMyProfilSave;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofil);

        mIBMyProfilSave = (Button) findViewById(R.id.IBMyProfilSave);
        mIBMyPseudo = (EditText) findViewById(R.id.IBMyPseudo);
        mIBMyEmail = (EditText) findViewById(R.id.IBMyEmail);
        mIBMyNickName = (EditText) findViewById(R.id.IBMyNickName);
        mIBMySurname = (EditText) findViewById(R.id.IBMySurname);
        mIBMyAdresse = (EditText) findViewById(R.id.IBMyAdresse);
        mIBMyZipCode = (EditText) findViewById(R.id.IBMyZipCode);
        mIBMyCity = (EditText) findViewById(R.id.IBMyCity);
        mIBMyCountry = (EditText) findViewById(R.id.IBMyCountry);

        mIBMyPseudo.setText(mConfig.getUser_pseudo());
        mIBMyEmail.setText(mConfig.getUser_email());
        mIBMyNickName.setText(mConfig.getUser_nom());
        mIBMySurname.setText(mConfig.getUser_prenom());
        mIBMyAdresse.setText(mConfig.getUser_adresse());
        mIBMyZipCode.setText(mConfig.getUser_codepostal());
        mIBMyCity.setText(mConfig.getUser_ville());
        mIBMyCountry.setText(mConfig.getUser_pays());

        TextView mIBMyProfilTitle = (TextView) findViewById(R.id.IBMyProfilTitle);
        mIBMyProfilTitle.setText(mConfig.getUser_pseudo()+"("+mConfig.getUser_id()+")");


    }

    @Override
    protected void onStart() {
        super.onStart();

        initColor();

    }


    private void initColor() {



        ConstraintLayout mIBMyProfilBackgr;
        mIBMyProfilBackgr = (ConstraintLayout) findViewById(R.id.IBMyProfilBackgr);

        String hexColor = "#" + mConfig.getColorApp();
        mIBMyProfilBackgr.setBackgroundColor(Color.parseColor(hexColor));


        hexColor = "#" + mConfig.getColorAppPlHd();
        mIBMyPseudo.setHintTextColor(Color.parseColor(hexColor));
        mIBMyEmail.setHintTextColor(Color.parseColor(hexColor));
        mIBMyNickName.setHintTextColor(Color.parseColor(hexColor));
        mIBMySurname.setHintTextColor(Color.parseColor(hexColor));
        mIBMyAdresse.setHintTextColor(Color.parseColor(hexColor));
        mIBMyZipCode.setHintTextColor(Color.parseColor(hexColor));
        mIBMyCity.setHintTextColor(Color.parseColor(hexColor));
        mIBMyCountry.setHintTextColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppText();
        mIBMyPseudo.setTextColor(Color.parseColor(hexColor));
        mIBMyEmail.setTextColor(Color.parseColor(hexColor));
        mIBMyNickName.setTextColor(Color.parseColor(hexColor));
        mIBMySurname.setTextColor(Color.parseColor(hexColor));
        mIBMyAdresse.setTextColor(Color.parseColor(hexColor));
        mIBMyZipCode.setTextColor(Color.parseColor(hexColor));
        mIBMyCity.setTextColor(Color.parseColor(hexColor));
        mIBMyCountry.setTextColor(Color.parseColor(hexColor));


    }


    public void actionCloseWidows(View view) {

        finish();
    }


    public void actionMyProfilHelp(View view) {

        //Todo Tuto_Presentation
        MyTools.sharedInstance().showHelp("MyProfilTableViewContr", this);

    }

    public void actionMyProfilValid(View view) {


        if (mIBMyPseudo.getText().length() == 0) {

            MyTools.sharedInstance().displayAlert(this, getString(R.string.errorLogin));
            return;
        }

        if (mIBMyEmail.getText().length() == 0) {

            MyTools.sharedInstance().displayAlert(this, getString(R.string.errorMail));
            return;
        }


        mIBMyProfilSave = (Button) MyTools.sharedInstance().activerObjet(mIBMyProfilSave, false);

        mConfig.setUser_pseudo(mIBMyPseudo.getText().toString());
        mConfig.setUser_email(mIBMyEmail.getText().toString());
        mConfig.setUser_nom(mIBMyNickName.getText().toString());
        mConfig.setUser_prenom(mIBMySurname.getText().toString());
        mConfig.setUser_adresse(mIBMyAdresse.getText().toString());
        mConfig.setUser_codepostal(mIBMyZipCode.getText().toString());
        mConfig.setUser_ville(mIBMyCity.getText().toString());
        mConfig.setUser_pays(mIBMyCountry.getText().toString());

        MDBUser.sharedInstance().setUpdateUser(this, mConfig, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {

                if (success) {

                    MyTools.sharedInstance().performUIUpdatesOnMain(MyProfilActivity.this, new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {

                            finish();

                        }
                    });

                } else {

                    MyTools.sharedInstance().performUIUpdatesOnMain(MyProfilActivity.this, new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(MyProfilActivity.this, errorString);

                        }
                    });
                }

            }
        });


    }


}
