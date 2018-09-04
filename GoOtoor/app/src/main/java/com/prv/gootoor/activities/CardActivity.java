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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.prv.gootoor.daos.MDBInterface;
import com.prv.gootoor.daos.MDBInterfaceString;
import com.prv.gootoor.daos.MDBPressOperation;
import com.prv.gootoor.lists.Cards;
import com.prv.gootoor.lists.TypeCards;
import com.prv.gootoor.models.Card;
import com.prv.gootoor.models.TypeCard;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;
import com.prv.gootoor.utils.SaveImageInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by roger on 25/10/2017.
 */

public class CardActivity extends AppCompatActivity implements PaymentMethodNonceCreatedListener, BraintreeErrorListener, BraintreeCancelListener {

    private Config mConfig = Config.sharedInstance();
    private DatePicker mIBDatePicker;
    private TextView mIBDate;
    private EditText mIBNumber;
    private Card aCard;
    private TextView mIBImageCardLabel;
    private ImageView mIBImageCard;
    private BraintreeFragment mBraintreeFragment;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        mIBDatePicker = (DatePicker) findViewById(R.id.IBDatePicker);
        mIBDate = (TextView) findViewById(R.id.IBDate);
        mIBNumber = (EditText) findViewById(R.id.IBNumber);
        mIBImageCardLabel = (TextView) findViewById(R.id.IBImageCardLabel);
        mIBImageCard = (ImageView) findViewById(R.id.IBImageCard);

        aCard = new Card(this, null);

        Calendar calendar = Calendar.getInstance();

        mIBDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                actionSetDate(view);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();


        initColor();

        if (mConfig.getTypeCard_id() != 0) {

            //PayPal
            if (mConfig.getTypeCard_id() == 6) {

                mIBNumber.setVisibility(View.GONE);
                mIBDate.setVisibility(View.GONE);
                mIBDatePicker.setVisibility(View.GONE);

            } else {

                mIBNumber.setVisibility(View.VISIBLE);
                mIBDate.setVisibility(View.VISIBLE);
                mIBDatePicker.setVisibility(View.VISIBLE);
            }

            JSONArray typeCardsJSON = TypeCards.sharedInstance().getTypeCardsArray();

            try {

                for (int i = 0; i < typeCardsJSON.length() ; i++) {

                    JSONObject typeCd = typeCardsJSON.getJSONObject(i);
                    TypeCard typeC = new TypeCard(this, typeCd);
                    if (typeC.getTypeCard_id() == mConfig.getTypeCard_id()) {
                        aCard.setTypeCard_id(mConfig.getTypeCard_id());
                        mConfig.setTypeCard_id(0);
                        mIBImageCardLabel.setText(typeC.getTypeCard_Wording());

                        MyTools.sharedInstance().saveImageArchive(this, typeC.getTypeCard_ImageUrl(),
                                "images_cb/" , new SaveImageInterface() {
                            @Override
                            public void completionHandlerSaveImage(Boolean success, Drawable drawable) {

                                if (!success) {

                                    mIBImageCard.setTag("noimage");
                                    Log.v(mConfig.getDomaineApp()+"CustomItemListCard", "error image");

                                } else {
                                    mIBImageCard.setTag("");
                                }

                                mIBImageCard.setImageDrawable(drawable);


                            }
                        });


                        break;

                    }


                }

            }  catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }


    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        // Communicate the tokenizedCard.nonce to your server, or handle error

        aCard.setTokenizedCard(paymentMethodNonce.getNonce());
        //user_id
        aCard.setUser_id(mConfig.getUser_id());
        if (Cards.sharedInstance().getCardsArray().length() == 0) {
            aCard.setMain_card(true);
        }

        MDBCard.sharedInstance().setAddCard(this, aCard, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {

                    while (Cards.sharedInstance().getCardsArray().length()>0) {
                        Cards.sharedInstance().getCardsArray().remove(0);
                    }

                    finish();


                } else {

                    MyTools.sharedInstance().performUIUpdatesOnMain(CardActivity.this, new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {
                            MyTools.sharedInstance().displayAlert(CardActivity.this, errorString);

                        }
                    });

                }
            }
        });

    }


    @Override
    public void onCancel(int requestCode) {
        // Use this to handle a canceled activity, if the given requestCode is important.
        // You may want to use this callback to hide loading indicators, and prepare your UI for input
    }

    @Override
    public void onError(Exception error) {
        if (error instanceof ErrorWithResponse) {
            ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;
            BraintreeError cardErrors = ((ErrorWithResponse) error).errorFor("creditCard");
            if (cardErrors != null) {
                // There is an issue with the credit card.
                BraintreeError expirationMonthError = cardErrors.errorFor("expirationMonth");
                if (expirationMonthError != null) {
                    // There is an issue with the expiration month.
                    MyTools.sharedInstance().displayAlert(this, expirationMonthError.getMessage());
                }
            }
        }
    }



    private void initColor() {


        ConstraintLayout mIBCardBackgr;
        mIBCardBackgr = (ConstraintLayout) findViewById(R.id.IBCardBackgr);

        String hexColor = "#" + mConfig.getColorApp();
        mIBCardBackgr.setBackgroundColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppLabel();
        mIBImageCardLabel.setTextColor(Color.parseColor(hexColor));
        mIBDate.setTextColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppPlHd();
        mIBNumber.setHintTextColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppText();
        mIBNumber.setTextColor(Color.parseColor(hexColor));



    }



    public void actionImageCard(View view) {

        Intent intent = new Intent(this, ListTypeCBActivity.class);
        startActivity(intent);
    }

    public void actionCloseWidows(View view) {

        finish();
    }

    public void actionCardValid(View view) {

        //Paypal
        if (aCard.getTypeCard_id() == 6) {

            callPayPal();

        } else {

            //type card
            if (aCard.getTypeCard_id() == 0) {

                MyTools.sharedInstance().displayAlert(CardActivity.this, getString(R.string.errorCardType));
                return;

            }

            //number card
            if (mIBNumber.getText().length() == 0) {
                MyTools.sharedInstance().displayAlert(CardActivity.this, getString(R.string.errorCardNumber));
                return;
            }

            //date
            if (mIBDate.getText().length() == 0) {
                MyTools.sharedInstance().displayAlert(CardActivity.this, getString(R.string.errorExpiryDate));
                return;
            }


            String numberStr = mIBNumber.getText().toString();
            aCard.setCard_lastNumber(numberStr.substring(numberStr.length() - 4, numberStr.length()));

            if (mConfig.getClientTokenBraintree().length() == 0) {

                MDBPressOperation.sharedInstance().getBraintreeToken(this, mConfig.getUser_id(), new MDBInterfaceString() {
                    @Override
                    public void completionHandlerString(Boolean success, String anString, final String errorString) {
                        if (success) {
                            mConfig.setClientTokenBraintree(anString);

                            try {

                                mBraintreeFragment = BraintreeFragment.newInstance(CardActivity.this, anString);
                                CardBuilder cardBuilder = new CardBuilder().cardNumber(mIBNumber.getText().toString())
                                        .expirationDate(mIBDate.getText().toString());
                                com.braintreepayments.api.Card.tokenize(mBraintreeFragment, cardBuilder);

                            } catch (InvalidArgumentException e) {
                                // There was an issue with your authorization string.
                            }



                        } else {

                            MyTools.sharedInstance().performUIUpdatesOnMain(CardActivity.this, new MainThreadInterface() {

                                @Override
                                public void completionUpdateMain() {
                                    MyTools.sharedInstance().displayAlert(CardActivity.this, errorString);

                                }
                            });
                        }
                    }
                });

            } else {

                try {

                    mBraintreeFragment = BraintreeFragment.newInstance(this, mConfig.getClientTokenBraintree());
                    CardBuilder cardBuilder = new CardBuilder().cardNumber(mIBNumber.getText().toString())
                            .expirationDate(mIBDate.getText().toString());
                    com.braintreepayments.api.Card.tokenize(mBraintreeFragment, cardBuilder);

                } catch (InvalidArgumentException e) {
                    // There was an issue with your authorization string.
                }

            }


        }

    }


    private void callPayPal() {

        if (mConfig.getClientTokenBraintree().length() == 0) {

            MDBPressOperation.sharedInstance().getBraintreeToken(this, mConfig.getUser_id(), new MDBInterfaceString() {
                @Override
                public void completionHandlerString(Boolean success, String anString, String errorString) {
                    if (success) {
                        mConfig.setClientTokenBraintree(anString);
                        try {

                            mBraintreeFragment = BraintreeFragment.newInstance(CardActivity.this, mConfig.getClientTokenBraintree());

                            PayPal.authorizeAccount(mBraintreeFragment);
                            // ...start the Checkout flow
                            PayPalRequest paypalRequest = new PayPalRequest("1.00");
                            PayPal.requestBillingAgreement(mBraintreeFragment, paypalRequest);




                        } catch (InvalidArgumentException e) {
                            // There was an issue with your authorization string.
                        }


                    } else {

                    }
                }
            });
        } else {

            try {

                mBraintreeFragment = BraintreeFragment.newInstance(this, mConfig.getClientTokenBraintree());

                PayPal.authorizeAccount(mBraintreeFragment);
                // ...start the Checkout flow
                PayPalRequest paypalRequest = new PayPalRequest("1.00");
                PayPal.requestBillingAgreement(mBraintreeFragment, paypalRequest);




            } catch (InvalidArgumentException e) {
                // There was an issue with your authorization string.
            }


        }

    }


    public void actionSetDate(View view) {

        String mm = "";
        String yy = "" +  mIBDatePicker.getYear();
        yy = yy.substring(2,4);

        if ((mIBDatePicker.getMonth()+1) < 10 ) {
            mm = "0" + (mIBDatePicker.getMonth() + 1);
        }
        else {
            mm = "" + (mIBDatePicker.getMonth() + 1);

        }

        String mmyy = mm  + "/" + yy;
        mIBDate.setText(mmyy);

    }

    public void actionCardHelp(View view) {

        MyTools.sharedInstance().showHelp("CardTableViewController", this);

    }


}
