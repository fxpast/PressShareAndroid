package com.prv.gootoor.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.CardBuilder;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.prv.gootoor.R;
import com.prv.gootoor.daos.MDBCard;
import com.prv.gootoor.daos.MDBCreneau;
import com.prv.gootoor.daos.MDBInterface;
import com.prv.gootoor.daos.MDBInterfaceString;
import com.prv.gootoor.daos.MDBPressOperation;
import com.prv.gootoor.lists.Cards;
import com.prv.gootoor.lists.Creneaux;
import com.prv.gootoor.lists.TypeCards;
import com.prv.gootoor.models.Card;
import com.prv.gootoor.models.Creneau;
import com.prv.gootoor.models.TypeCard;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;
import com.prv.gootoor.utils.SaveImageInterface;
import com.prv.gootoor.views.CustomItemListCren;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * Description : Add slots for product
 *
 * Created by roger on 03/05/2018.
 */

public class AddCreneauActivity extends AppCompatActivity {

    private Config mConfig = Config.sharedInstance();

    private TextView mIBDateDebut;
    private DatePicker mIBDateDebutPicker;
    private TimePicker mIBTimeDebutPicker;
    private Date mDateDebut;


    private TextView mIBDateFin;
    private DatePicker mIBDateFinPicker;
    private TimePicker mIBTimeFinPicker;
    private Date mDateFin;

    private Switch mIBNone;
    private Switch mIBDaily;
    private Switch mIBWeekly;

    private EditText mIBInfoLocation;

    private Button mIBCreneauValid;
    private Button mIBFindOnMap;
    private Button mIBFindMyPos;

    private int mProd_id;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creneau);

        mProd_id  =  getIntent().getIntExtra(mConfig.getDomaineApp()+"prod_id",0);

        mIBDateDebut = (TextView) findViewById(R.id.IBDateDebut);
        mIBDateDebutPicker = (DatePicker) findViewById(R.id.IBDateDebutPicker);
        mIBTimeDebutPicker = (TimePicker) findViewById(R.id.IBTimeDebutPicker);

        mIBDateFin = (TextView) findViewById(R.id.IBDateFin);
        mIBDateFinPicker = (DatePicker) findViewById(R.id.IBDateFinPicker);
        mIBTimeFinPicker = (TimePicker) findViewById(R.id.IBTimeFinPicker);

        mIBNone = (Switch) findViewById(R.id.IBNone);
        mIBDaily = (Switch) findViewById(R.id.IBDaily);
        mIBWeekly = (Switch) findViewById(R.id.IBWeekly);

        mIBCreneauValid = (Button) findViewById(R.id.IBCreneauValid);
        mIBFindOnMap = (Button) findViewById(R.id.IBFindOnMap);
        mIBFindMyPos = (Button) findViewById(R.id.IBFindMyPos);


        mIBNone.setChecked(true);
        mIBDaily.setChecked(false);
        mIBWeekly.setChecked(false);

        mIBInfoLocation = (EditText) findViewById(R.id.IBInfoLocation);

        mConfig.setLatitude(0.0);
        mConfig.setLongitude(0.0);

        Calendar calendar = Calendar.getInstance();

        mIBDateDebutPicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                actionSetDateDebut(view);
            }
        });


        mIBTimeDebutPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                actionSetDateDebut(view);
            }
        });


        mIBDateFinPicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                actionSetDateFin(view);
            }
        });



        mIBTimeFinPicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                actionSetDateFin(view);
            }
        });



    }



    @Override
    protected void onStart() {
        super.onStart();

        initColor();

    }



    private void initColor() {


        ConstraintLayout mIBCrenBackgr;
        mIBCrenBackgr = (ConstraintLayout) findViewById(R.id.IBCrenBackgr);

        String hexColor = "#" + mConfig.getColorApp();
        mIBCrenBackgr.setBackgroundColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppLabel();
        mIBDateDebut.setTextColor(Color.parseColor(hexColor));
        mIBDateFin.setTextColor(Color.parseColor(hexColor));
        mIBNone.setTextColor(Color.parseColor(hexColor));
        mIBDaily.setTextColor(Color.parseColor(hexColor));
        mIBWeekly.setTextColor(Color.parseColor(hexColor));


        hexColor = "#" + mConfig.getColorAppPlHd();
        mIBInfoLocation.setHintTextColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppText();
        mIBInfoLocation.setTextColor(Color.parseColor(hexColor));


    }



    public void actionCloseWidows(View view) {

        finish();
    }

    public void actionCreneauValid(View view) {


        if (mIBDateDebut.getText().length() == 0) {

            MyTools.sharedInstance().displayAlert(AddCreneauActivity.this,getString(R.string.dateDebut));
            return;
        }


        if (mIBDateFin.getText().length() == 0) {

            MyTools.sharedInstance().displayAlert(AddCreneauActivity.this,getString(R.string.dateFin));
            return;
        }


        if (mIBInfoLocation.getText().length() == 0) {

            MyTools.sharedInstance().displayAlert(AddCreneauActivity.this,getString(R.string.tapALoc));
            return;
        }


        if (mConfig.getLatitude() == 0.0 && mConfig.getLongitude() == 0.0) {

            MyTools.sharedInstance().displayAlert(AddCreneauActivity.this,getString(R.string.findOnMap));
            return;
        }


        if (mDateDebut.after(mDateFin)) {
            MyTools.sharedInstance().displayAlert(AddCreneauActivity.this,getString(R.string.dateError));
            return;
        }



        if (mIBDateDebutPicker.getDayOfMonth() == mIBDateFinPicker.getDayOfMonth() && mIBDateDebutPicker.getMonth() == mIBDateFinPicker.getMonth() && mIBDateDebutPicker.getYear() == mIBDateFinPicker.getYear() && !mIBNone.isChecked() || mIBNone.isChecked()) {
           //OK
        } else {
            MyTools.sharedInstance().displayAlert(AddCreneauActivity.this,getString(R.string.sameday));
            return;
        }


        if (checkOverlap()) {

            MyTools.sharedInstance().displayAlert(AddCreneauActivity.this,getString(R.string.overLapSlot));
            return;
        }


        mIBCreneauValid = (Button) MyTools.sharedInstance().activerObjet(mIBCreneauValid, false);
        mIBFindOnMap = (Button) MyTools.sharedInstance().activerObjet(mIBFindOnMap, false);
        mIBFindMyPos = (Button) MyTools.sharedInstance().activerObjet(mIBFindMyPos, false);


        Creneau creneau = new Creneau(AddCreneauActivity.this, null);
        creneau.setCre_dateDebut(MyTools.sharedInstance().dateToServer(mDateDebut));
        creneau.setCre_dateFin(MyTools.sharedInstance().dateToServer(mDateFin));
        creneau.setCre_latitude(mConfig.getLatitude());
        creneau.setCre_longitude(mConfig.getLongitude());
        creneau.setCre_mapString(mConfig.getMapString());
        creneau.setProd_id(mProd_id);
        if (mIBNone.isChecked()) {
            creneau.setCre_repeat(0);
        } else if (mIBDaily.isChecked()) {
            creneau.setCre_repeat(1);
        } else if (mIBWeekly.isChecked()) {
            creneau.setCre_repeat(2);
        }

        //add creneau
        MDBCreneau.sharedInstance().setAddCreneau(AddCreneauActivity.this, creneau, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {

                    MyTools.sharedInstance().performUIUpdatesOnMain(AddCreneauActivity.this, new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {


                            mIBCreneauValid = (Button) MyTools.sharedInstance().activerObjet(mIBCreneauValid, true);
                            mIBFindOnMap = (Button) MyTools.sharedInstance().activerObjet(mIBFindOnMap, true);
                            mIBFindMyPos = (Button) MyTools.sharedInstance().activerObjet(mIBFindMyPos, true);

                            finish();


                        }
                    });


                } else {

                    MyTools.sharedInstance().performUIUpdatesOnMain(AddCreneauActivity.this, new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {

                            mIBCreneauValid = (Button) MyTools.sharedInstance().activerObjet(mIBCreneauValid, true);
                            mIBFindOnMap = (Button) MyTools.sharedInstance().activerObjet(mIBFindOnMap, true);
                            mIBFindMyPos = (Button) MyTools.sharedInstance().activerObjet(mIBFindMyPos, true);
                            MyTools.sharedInstance().displayAlert(AddCreneauActivity.this, errorString);
                            return;



                        }
                    });


                }
            }
        });



    }

    private boolean checkOverlap() {


        boolean result = false;

        JSONArray creneauxJSON = Creneaux.sharedInstance().getCreneauxArray();

        try {

            for (int i = 0; i < creneauxJSON.length() ; i++) {

                JSONObject dictionary = creneauxJSON.getJSONObject(i);
                Creneau creneau = new Creneau(AddCreneauActivity.this, dictionary);

                if (mDateDebut.after(creneau.getCre_dateDebut()) && mDateDebut.before(creneau.getCre_dateFin())) {
                    result = true;
                    break;
                }

                if (creneau.getCre_dateDebut().after(mDateDebut) && creneau.getCre_dateDebut().before(mDateFin)) {
                    result = true;
                    break;
                }


            }

        }  catch (JSONException e) {
            e.printStackTrace();
        }


        return result;
    }


    public void actionSetDateDebut(View view) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mIBDateDebutPicker.getYear());
        calendar.set(Calendar.MONTH, mIBDateDebutPicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, mIBDateDebutPicker.getDayOfMonth());
        calendar.set(Calendar.DAY_OF_MONTH, mIBDateDebutPicker.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, mIBTimeDebutPicker.getHour());
        calendar.set(Calendar.MINUTE, mIBTimeDebutPicker.getMinute());
        mDateDebut = new Date(calendar.getTimeInMillis());

        mIBDateDebut.setText(MyTools.sharedInstance().stringFromDate(mDateDebut));

    }


    public void actionSetDateFin(View view) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mIBDateFinPicker.getYear());
        calendar.set(Calendar.MONTH, mIBDateFinPicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, mIBDateFinPicker.getDayOfMonth());
        calendar.set(Calendar.DAY_OF_MONTH, mIBDateFinPicker.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, mIBTimeFinPicker.getHour());
        calendar.set(Calendar.MINUTE, mIBTimeFinPicker.getMinute());
        mDateFin = new Date(calendar.getTimeInMillis());

        mIBDateFin.setText(MyTools.sharedInstance().stringFromDate(mDateFin));


    }

    public void actionCrenHelp(View view) {

        //Todo Tuto_Presentation
        MyTools.sharedInstance().showHelp("creneaux", this);


    }

    public void actionNone(View view) {

        mIBNone.setChecked(true);
        mIBDaily.setChecked(false);
        mIBWeekly.setChecked(false);

    }

    public void actionDaily(View view) {

        mIBDaily.setChecked(true);
        mIBNone.setChecked(false);
        mIBWeekly.setChecked(false);

    }

    public void actionWeekly(View view) {


        mIBWeekly.setChecked(true);
        mIBNone.setChecked(false);
        mIBDaily.setChecked(false);

    }


    public void actionFindMap(View view) {

        if (mIBInfoLocation.getText().length() == 0) {

            MyTools.sharedInstance().displayAlert(AddCreneauActivity.this,getString(R.string.ErrorGeolocation));
            return;
        }


        mConfig.setMapString(mIBInfoLocation.getText().toString());
        Intent intent = new Intent(getBaseContext(), MapProdActivity.class);
        intent.putExtra(mConfig.getDomaineApp()+"IBInfoLocation", mIBInfoLocation.getText().toString());
        startActivity(intent);


    }


    public void actionFindMe(View view) {

        Intent intent = new Intent(getBaseContext(), MapProdActivity.class);
        intent.putExtra(mConfig.getDomaineApp()+"IBInfoLocation", "");
        startActivity(intent);


    }



}
