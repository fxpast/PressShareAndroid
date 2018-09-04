package com.prv.gootoor.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.prv.gootoor.R;
import com.prv.gootoor.daos.MDBInterface;
import com.prv.gootoor.daos.MDBInterfaceArray;
import com.prv.gootoor.daos.MDBMessage;
import com.prv.gootoor.daos.MDBProduct;
import com.prv.gootoor.lists.Messages;
import com.prv.gootoor.lists.Products;
import com.prv.gootoor.lists.Transactions;
import com.prv.gootoor.models.Message;
import com.prv.gootoor.models.Product;
import com.prv.gootoor.models.Transaction;
import com.prv.gootoor.utils.CheckBadgeInterface;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;
import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by roger on 21/09/2017.
 */

public class DetailMessageActivity extends AppCompatActivity {

    private ImageView mIBDetMessSend;
    private ImageView mIBDetMessRefresh;
    private EditText mIBDetMessInput;
    private NestedScrollView mIBDetMessScrollView;
    private LinearLayout mIBDetMessLayout;
    private Config mConfig = Config.sharedInstance();
    private Product aProduct;
    private int mTypeSListe = 0; //List :0, MyList :1, Historical:
    private Boolean readOnly = false;
    private int mOffSetY = 10;
    private int mLabelId = 0;
    private Boolean mSendMessage = false;
    private String dateStrAfter = "";
    private String dateStrBefore = "";
    private Handler timerBadge;
    private Runnable runnableBadge;


    @Override
    public void onStart() {
        super.onStart();

        initColor();

        timerBadge = new Handler();
        timerBadge.postDelayed(new Runnable(){
            public void run(){
                if (!mConfig.getIsTimer()) {

                    MyTools.sharedInstance().checkBadge(DetailMessageActivity.this, new CheckBadgeInterface() {
                        @Override
                        public void completionHdlerBadge(Boolean success, String result) {
                            if (success) {
                                actionDetMessRefresh(mIBDetMessRefresh);
                            }
                        }
                    });

                    runnableBadge = this;
                    timerBadge.postDelayed(runnableBadge, mConfig.getDureeTimer().longValue()*1000);
                }

            }
        }, mConfig.getDureeTimer().longValue()*1000);





        try {

            JSONArray messagesJSON = Messages.sharedInstance().getMessagesArray();

            for (int j = 0; j < messagesJSON.length(); j++) {

                JSONObject dictionary = messagesJSON.getJSONObject(j);
                Message message = new Message(this, dictionary);
                if (message.getProduct_id() == aProduct.getProd_id() && !message.getDeja_lu()){

                    if (!message.getDeja_lu()){

                        mConfig.setMess_badge(mConfig.getMess_badge()-1);
                        ShortcutBadger.applyCount(this, mConfig.getMess_badge());

                    }

                    message.setDeja_lu(true);
                    MDBMessage.sharedInstance().setUpdateMessage(this, message, new MDBInterface() {
                        @Override
                        public void completionHandler(Boolean success, String errorString) {
                            if (success) {

                                MDBMessage.sharedInstance().getAllMessages(DetailMessageActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                    @Override
                                    public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                                        if (success) {
                                            Messages.sharedInstance().setMessagesArray(anArray);

                                        } else {

                                            MyTools.sharedInstance().performUIUpdatesOnMain(DetailMessageActivity.this, new MainThreadInterface() {
                                                @Override
                                                public void completionUpdateMain() {

                                                    MyTools.sharedInstance().displayAlert(DetailMessageActivity.this, errorString);

                                                }
                                            });


                                        }

                                    }
                                });

                            } else {

                            }

                        }
                    });
                }
            }



        }  catch (JSONException e) {
            e.printStackTrace();
        }



    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mConfig.setIsTimer(false);
    }

    @Override
    public void onPause() {

        mConfig.setIsTimer(false);
        timerBadge.removeCallbacks(runnableBadge);
        super.onPause();

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);

        mIBDetMessSend = (ImageView) findViewById(R.id.IBDetMessSend);
        mIBDetMessRefresh = (ImageView) findViewById(R.id.IBDetMessRefresh);
        mIBDetMessInput = (EditText) findViewById(R.id.IBDetMessInput);
        mIBDetMessScrollView = (NestedScrollView) findViewById(R.id.IBDetMessScrollView);
        mIBDetMessLayout = (LinearLayout) findViewById(R.id.IBDetMessLayout);


        int prodId  =  getIntent().getIntExtra(mConfig.getDomaineApp()+"prod_id",0);
        mTypeSListe  =  getIntent().getIntExtra(mConfig.getDomaineApp()+"typeSListe",0);
        aProduct = getProduct(prodId);


        if (aProduct == null) {
            MDBProduct.sharedInstance().getProduct(this, prodId, new MDBInterfaceArray() {
                @Override
                public void completionHandlerArray(Boolean success, JSONArray anArray, String errorString) {

                    try {


                        for (int i = 0; i < anArray.length(); i++) {

                            JSONObject dictionary = anArray.getJSONObject(i);
                            aProduct = new Product(DetailMessageActivity.this, dictionary);
                            refreshData();
                            break;

                        }



                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } else {

            refreshData();

        }



        try {

            JSONArray productsJSON = Transactions.sharedInstance().getTransactionArray();

            for (int i = 0; i < productsJSON.length(); i++) {

                JSONObject dictionary = productsJSON.getJSONObject(i);
                Transaction tran = new Transaction(dictionary);
                if (tran.getProd_id() == aProduct.getProd_id() && (tran.getTrans_valid() == 1 || tran.getTrans_valid() == 2)) {
                    mIBDetMessSend = (ImageView) MyTools.sharedInstance().activerObjet(mIBDetMessSend, false);
                    mIBDetMessInput = (EditText) MyTools.sharedInstance().activerObjet(mIBDetMessInput, false);

                    readOnly = true;
                    break;
                }

            }

        }  catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void initColor() {


        LinearLayout mIBDetMessLayout;
        mIBDetMessLayout = (LinearLayout) findViewById(R.id.IBDetMessLayout);
        LinearLayout mIBDetMessBackgr;
        mIBDetMessBackgr = (LinearLayout) findViewById(R.id.IBDetMessBackgr);

        String hexColor = "#" + mConfig.getColorApp();
        mIBDetMessLayout.setBackgroundColor(Color.parseColor(hexColor));
        mIBDetMessBackgr.setBackgroundColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppPlHd();
        mIBDetMessInput.setHintTextColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppText();
        mIBDetMessInput.setTextColor(Color.parseColor(hexColor));



    }



    public void actionDetMessRefresh(View view) {

        initScrollView();
        refreshData();

    }


    private void initScrollView() {


       mIBDetMessLayout.removeAllViews();
        dateStrAfter = "";
        dateStrBefore = "";

    }



    private void chargeData() {

        Boolean flgMessExist = false;


        try {

            JSONArray messagesJSON = Messages.sharedInstance().getMessagesArray();

            for (int i = 0; i < messagesJSON.length(); i++) {

                JSONObject dictionary = messagesJSON.getJSONObject(i);
                Message messa = new Message(this, dictionary);
                if (messa.getProduct_id() == aProduct.getProd_id()) {
                    flgMessExist = true;
                    LinearLayout.LayoutParams layoutParams = createLabelMess(messa);

                    createLabelTime(messa.getDate_ajout(), layoutParams);
                }

            }

            if (flgMessExist && !readOnly) {

                mIBDetMessSend = (ImageView) MyTools.sharedInstance().activerObjet(mIBDetMessSend, true);


            } else {

                mIBDetMessSend = (ImageView) MyTools.sharedInstance().activerObjet(mIBDetMessSend, false);

            }


            mIBDetMessScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mIBDetMessScrollView.fullScroll(mIBDetMessScrollView.FOCUS_DOWN);
                }
            });



        }  catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private void refreshData() {

        MDBMessage.sharedInstance().getAllMessages(this, mConfig.getUser_id(), new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                if (success) {
                    Messages.sharedInstance().setMessagesArray(anArray);

                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailMessageActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            chargeData();

                        }
                    });

                } else {

                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailMessageActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(DetailMessageActivity.this, errorString);

                        }
                    });


                }

            }
        });
    }

    private Product getProduct(int prodId) {


        if (prodId == 0) {
            return null;
        } else {
            try {

                JSONArray productsJSON = Products.sharedInstance().getProductsArray();

                if (mTypeSListe == 1) {

                    //MyList
                    productsJSON = Products.sharedInstance().getProductsUserArray();

                } else  if (mTypeSListe == 2) {

                    //History
                    productsJSON = Products.sharedInstance().getProductsTraderArray();
                }
                for (int i = 0; i < productsJSON.length(); i++) {

                    JSONObject dictionary = productsJSON.getJSONObject(i);
                    Product product = new Product(this, dictionary);
                    if (product.getProd_id() == prodId) {
                        return  product;
                    }

                }

            }  catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return null;



    }


    public void actionDetMessSend(View view) {



        if (mIBDetMessInput.getText().length() == 0) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(DetailMessageActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        mIBDetMessSend = (ImageView) MyTools.sharedInstance().activerObjet(mIBDetMessSend, false);


        final Message message = new Message(this, null);
        message.setDate_ajout(new Date());
        message.setExpediteur(mConfig.getUser_id());
        message.setMessage_id(0);

        if (aProduct.getProd_by_user() == message.getExpediteur()) {
            message.setDestinataire(aProduct.getProd_oth_user());
        } else if (aProduct.getProd_oth_user() == message.getExpediteur()) {

            message.setDestinataire(aProduct.getProd_by_user());
        }
        message.setProprietaire(mConfig.getUser_id());
        message.setClient_id(aProduct.getProd_oth_user());
        message.setVendeur_id(aProduct.getProd_oth_user());
        message.setProduct_id(aProduct.getProd_id());
        message.setContenu(getString(R.string.emailSender) + " " + mConfig.getUser_nom() + " " + mConfig.getUser_prenom() + "\n" + mIBDetMessInput.getText().toString());

        final LinearLayout.LayoutParams layoutParams = createLabelMess(message);



        MDBMessage.sharedInstance().setAddMessage(this, message, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {

                    MDBMessage.sharedInstance().setPushNotification(DetailMessageActivity.this, message, new MDBInterface() {
                        @Override
                        public void completionHandler(Boolean success, final String errorString) {
                            if (success) {
                                //ok
                            } else {

                                MyTools.sharedInstance().performUIUpdatesOnMain(DetailMessageActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        MyTools.sharedInstance().displayAlert(DetailMessageActivity.this, errorString);

                                    }
                                });


                            }
                        }
                    });

                    MDBMessage.sharedInstance().getAllMessages(DetailMessageActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                        @Override
                        public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                            if (success) {
                                Messages.sharedInstance().setMessagesArray(anArray);

                            } else {

                                MyTools.sharedInstance().performUIUpdatesOnMain(DetailMessageActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        MyTools.sharedInstance().displayAlert(DetailMessageActivity.this, errorString);

                                    }
                                });


                            }

                        }
                    });

                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailMessageActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            createLabelTime(message.getDate_ajout(), layoutParams);
                            mIBDetMessInput.setText("");

                            mIBDetMessSend = (ImageView) MyTools.sharedInstance().activerObjet(mIBDetMessSend, true);

                            mIBDetMessScrollView.post(new Runnable() {
                                @Override
                                public void run() {
                                    mIBDetMessScrollView.fullScroll(mIBDetMessScrollView.FOCUS_DOWN);
                                }
                            });

                        }
                    });


                } else {

                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailMessageActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            mIBDetMessSend = (ImageView) MyTools.sharedInstance().activerObjet(mIBDetMessSend, true);

                            MyTools.sharedInstance().displayAlert(DetailMessageActivity.this, errorString);
                        }
                    });


                }
            }
        });



    }


    private void DrawLineTime(Date date) {

        int width = mIBDetMessLayout.getWidth();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day =  c.get(c.DAY_OF_MONTH);
        int month = c.get(c.MONTH);
        int year = c.get(c.YEAR);
        dateStrAfter = year + "" + month + "" + day;
        if (!dateStrAfter.equals(dateStrBefore)) {
            dateStrBefore = dateStrAfter;

            DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
            String dateString = dateFormatter.format(date).toLowerCase();

            TextView labelLine = new TextView(this);
            String line = "_";
            int lenDateString = (int) labelLine.getPaint().measureText(dateString);
            int lenLine = (int) labelLine.getPaint().measureText(line);

            int widthLine = (width - lenDateString*3/2) / 2;
            widthLine /= lenLine;
            for(int i=0;i<widthLine;i++) {
                line += "_";

            }

            labelLine.setText(line + " " + dateString + " " + line);

            mIBDetMessLayout.addView(labelLine);


        }


    }


    private void createLabelTime(Date date, LinearLayout.LayoutParams layoutParams) {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int hour =  c.get(c.HOUR);
        int minute = c.get(c.MINUTE);

        TextView labelTime = new TextView(this);
        labelTime.setText(hour + ":" + minute  +  " ✓");

        labelTime.setLayoutParams(layoutParams);
        mIBDetMessLayout.addView(labelTime);

    }


    private LinearLayout.LayoutParams createLabelMess(Message messa) {

        DrawLineTime(messa.getDate_ajout());


        LinearLayout.LayoutParams layoutParams;

        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView labelMessage = new TextView(this);
        labelMessage.setText(messa.getContenu());

        if (messa.getDestinataire() == mConfig.getUser_id()) {

            labelMessage.setTextColor(Color.BLACK);
            labelMessage.setBackgroundColor(Color.GRAY);

            layoutParams.setMargins(10, 10, 0, 10);

        }

        if (messa.getExpediteur() == mConfig.getUser_id()) {

            labelMessage.setTextColor(Color.WHITE);
            labelMessage.setBackgroundColor(Color.BLUE);


            int length = 0;
            String string = messa.getContenu();
            String[] array = string.split("\n");

            for (int j = 0; j < array.length; j++) {

                int length1 = (int) labelMessage.getPaint().measureText(array[j]);

                if (length1 > length) {
                    length = length1;
                }

            }


            int width = mIBDetMessLayout.getWidth();

            int margeLeft = width - length;
            if (margeLeft<10) {
                margeLeft = 10;
            } else {

            }

            layoutParams.setMargins(margeLeft, 10, 10, 10);


        }

        labelMessage.setLayoutParams(layoutParams);

        mIBDetMessLayout.addView(labelMessage);


        return layoutParams;





    }




    public void actionCloseWidows(View view) {

        finish();
    }

    public void actionDetMessHelp(View view) {

        //Todo Tuto_Presentation
        MyTools.sharedInstance().showHelp("transactions", this);

    }



}
