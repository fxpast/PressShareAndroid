package com.prv.pressshare.activities;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.prv.pressshare.R;
import com.prv.pressshare.daos.MDBCapital;
import com.prv.pressshare.daos.MDBCard;
import com.prv.pressshare.daos.MDBInterface;
import com.prv.pressshare.daos.MDBInterfaceArray;
import com.prv.pressshare.daos.MDBInterfaceString;
import com.prv.pressshare.daos.MDBPressOperation;
import com.prv.pressshare.daos.MDBTypeCard;
import com.prv.pressshare.daos.MDBUser;
import com.prv.pressshare.lists.Capitals;
import com.prv.pressshare.lists.Cards;
import com.prv.pressshare.lists.PressOperations;

import com.prv.pressshare.lists.TypeCards;
import com.prv.pressshare.models.Capital;
import com.prv.pressshare.models.Card;

import com.prv.pressshare.models.PressOperation;

import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MainThreadInterface;
import com.prv.pressshare.utils.MyTools;
import com.prv.pressshare.views.CustomItemListOpe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roger on 23/10/2017.
 * Description : This class contains account balance, withdrawal, deposit, operation history

 */

public class AbonnerActivity  extends AppCompatActivity {

    private Config mConfig = Config.sharedInstance();
    private TextView mIBBalance;
    private Button mIBButtonSubUnsub;
    private Button mIBButtonDeposit;
    private Button mIBButtonWithdr;
    private Boolean isOpen = false;
    private Handler myQueue;
    private ListView mIBAbonnerListView;
    private List<Object> mOperations;
    private EditText mIBDeposit;
    private EditText mIBWithdrawal;


    private Card aCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abonner);


        mIBBalance = (TextView) findViewById(R.id.IBBalance);
        mIBButtonSubUnsub = (Button) findViewById(R.id.IBButtonSubUnsub);
        mIBButtonDeposit = (Button) findViewById(R.id.IBButtonDeposit);
        mIBButtonWithdr = (Button) findViewById(R.id.IBButtonWithdr);
        mIBDeposit = (EditText) findViewById(R.id.IBDeposit);
        mIBWithdrawal = (EditText) findViewById(R.id.IBWithdrawal);

        mOperations = new ArrayList<>();

        mIBBalance.setText(": " + mConfig.getBalance().toString());

        mIBAbonnerListView = (ListView) findViewById(R.id.IBAbonnerListView);


    }

    @Override
    protected void onStart() {
        super.onStart();


        if (Cards.sharedInstance().getCardsArray().length() == 0) {

            MDBTypeCard.sharedInstance().getAllTypeCards(this, new MDBInterfaceArray() {
                @Override
                public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                    if (success) {

                        TypeCards.sharedInstance().setTypeCardsArray(anArray);

                    } else {


                        MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                            @Override
                            public void completionUpdateMain() {

                                MyTools.sharedInstance().displayAlert(AbonnerActivity.this,errorString);

                            }
                        });

                    }
                }
            });

            MDBCard.sharedInstance().getAllCards(this, mConfig.getUser_id(), new MDBInterfaceArray() {
                @Override
                public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                    if (success) {

                        Cards.sharedInstance().setCardsArray(anArray);

                        try {

                            JSONArray cardsJSON = Cards.sharedInstance().getCardsArray();

                            for (int i = 0; i < cardsJSON.length(); i++) {

                                JSONObject dictionary = cardsJSON.getJSONObject(i);
                                aCard = new Card(AbonnerActivity.this, dictionary);
                                if (aCard.getMain_card()) {
                                    break;
                                } else {

                                    aCard = null;
                                }


                            }

                        }  catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {

                        MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                            @Override
                            public void completionUpdateMain() {

                                MyTools.sharedInstance().displayAlert(AbonnerActivity.this,errorString);

                            }
                        });

                    }
                }
            });
        } else {
            try {

                JSONArray cardsJSON = Cards.sharedInstance().getCardsArray();

                for (int i = 0; i < cardsJSON.length(); i++) {

                    JSONObject dictionary = cardsJSON.getJSONObject(i);
                    aCard = new Card(AbonnerActivity.this, dictionary);
                    if (aCard.getMain_card()) {
                        break;
                    } else {

                        aCard = null;
                    }


                }

            }  catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if (mConfig.getLevel() <= 0) {

            mIBButtonSubUnsub.setText(getText(R.string.subscribe));
            mIBButtonDeposit.setEnabled(false);
            mIBButtonWithdr.setEnabled(false);

        } else if (mConfig.getLevel() > 0) {

            mIBButtonSubUnsub.setText(getText(R.string.unsubscribe));
            mIBButtonDeposit.setEnabled(true);
            mIBButtonWithdr.setEnabled(true);

        }



    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (!isOpen) {
            isOpen = true;
            if (PressOperations.sharedInstance().getOperationArray().length() > 0) {

                myQueue = new Handler();
                myQueue.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        chargeData();

                    }
                }, 0);


            } else {
                refreshData();
            }

        }
    }



    private void refreshData() {

        MDBCapital.sharedInstance().getCapital(this, mConfig.getUser_id(), new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                if (success) {

                    Capitals.sharedInstance().setCapitalsArray(anArray);
                    try {

                        JSONArray capitalsJSON = Capitals.sharedInstance().getCapitalsArray();

                        for (int i = 0; i < capitalsJSON.length() ; i++) {

                            JSONObject dictionary = capitalsJSON.getJSONObject(i);
                            Capital capital = new Capital(AbonnerActivity.this, dictionary);
                            mConfig.setBalance(capital.getBalance());
                            mConfig.setFailure_count(capital.getFailure_count());
                            break;
                        }

                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                         mIBBalance.setText(": " + mConfig.getBalance().toString());

                        }
                    });


                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(AbonnerActivity.this,errorString);

                        }
                    });

                }
            }
        });


        MDBPressOperation.sharedInstance().getAllOperations(this, mConfig.getUser_id(), new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                if (success) {

                    PressOperations.sharedInstance().setOperationArray(anArray);
                    chargeData();

                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(AbonnerActivity.this,errorString);

                        }
                    });
                }
            }
        });

    }

    private void chargeData() {

        mOperations.clear();
        JSONArray operationsJSON = PressOperations.sharedInstance().getOperationArray();

        try {

            for (int i = 0; i < operationsJSON.length() ; i++) {

                JSONObject dictionary = operationsJSON.getJSONObject(i);
                PressOperation operation = new PressOperation(this, dictionary);
                mOperations.add(i, operation);

            }

        }  catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter mAdapter = new CustomItemListOpe(this, R.layout.list_operation_item, mOperations);
        mIBAbonnerListView.setAdapter(mAdapter);



    }

    public void actionAbonnerHelp(View view) {


        MyTools.sharedInstance().showHelp("SubscriptViewController", this);

    }

    public void actionAbonnerRefresh(View view) {

        refreshData();

    }

    public void actionWithdrawal(View view) {



        if (mIBWithdrawal.getText().length() == 0) {

            MyTools.sharedInstance().displayAlert(this, getString(R.string.withdrawal) +
                    " : " + getString(R.string.ErrorPrice) );
            return;
        }

        final Double amount = Double.parseDouble(mIBWithdrawal.getText().toString());

        if (mConfig.getBalance() < (amount + mConfig.getMinimumAmount())) {

            MyTools.sharedInstance().displayAlert(this, getString(R.string.errorBalanceTrans) +
                    "\n" + getString(R.string.errorMinimumBal) + mConfig.getMinimumAmount().toString());
            return;
        }

        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.withdrawal));
        builder.setMessage(getString(R.string.confirmWithdrawal));

        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                braintreeOperation(amount, "withdrawal");

            }
        });



        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();



    }

    public void actionDeposit(View view) {

        AlertDialog.Builder builder;

        if (aCard == null) {

            builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.creditCard));
            builder.setMessage(getString(R.string.addCreditCard));

            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //CB
                    Intent intent = new Intent(AbonnerActivity.this, CardActivity.class);
                    startActivity(intent);

                }
            });


        } else {

            if (mIBDeposit.getText().length() == 0) {

                MyTools.sharedInstance().displayAlert(this, getString(R.string.deposit) +
                        " : " + getString(R.string.ErrorPrice) );
                return;
            }

            final Double amount = Double.parseDouble(mIBDeposit.getText().toString());

            builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.deposit));
            builder.setMessage(getString(R.string.confirmPayment));

            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    braintreeOperation(amount, "deposit");

                }
            });

        }


        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();


    }


    public void actionCloseWidows(View view) {

        finish();
    }

    public void actionSubUnsub(View view) {

        String mess = "";
        if (mConfig.getLevel() <= 0 && mConfig.getBalance() == 0) {
            mess = getString(R.string.confirmSubsWithDepot);
        } else if (mConfig.getLevel() <= 0 && mConfig.getBalance() >  0) {
            mess = getString(R.string.confirmSubs);
        } else if (mConfig.getLevel() > 0) {
            mess = getString(R.string.confirmTermin);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle((mConfig.getLevel() <= 0) ? getString(R.string.subscribeSubs) : getString(R.string.cancelSubs));
        builder.setMessage(mess);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (mConfig.getLevel() <= 0 && mConfig.getBalance() == 0) {

                    braintreeOperation(mConfig.getSubscriptAmount(), "deposit");

                } else if (mConfig.getLevel() > 0 && mConfig.getBalance() > 0) {

                    braintreeOperation(mConfig.getBalance(), "withdrawal");
                }

                mConfig.setLevel((mConfig.getLevel() <= 0) ? 1 : 0);

                MDBUser.sharedInstance().setUpdateUser(AbonnerActivity.this, mConfig, new MDBInterface() {
                    @Override
                    public void completionHandler(Boolean success, final String errorString) {

                        if (success) {
                            MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                                @Override
                                public void completionUpdateMain() {

                                    if (mConfig.getLevel() <= 0) {
                                        mIBButtonSubUnsub.setText(getText(R.string.subscribe));
                                        mIBButtonDeposit.setEnabled(false);
                                        mIBButtonWithdr.setEnabled(false);

                                    } else if (mConfig.getLevel() > 0) {
                                        mIBButtonSubUnsub.setText(getText(R.string.unsubscribe));
                                        mIBButtonDeposit.setEnabled(true);
                                        mIBButtonWithdr.setEnabled(true);
                                    }

                                    MyTools.sharedInstance().displayAlert(AbonnerActivity.this,"Info : " +
                                            getString(R.string.subscriptionHas) + ((mConfig.getLevel() <= 0) ?
                                            getString(R.string.canceled) :  getString(R.string.confirmed)));


                                }
                            });
                        } else {

                            MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                                @Override
                                public void completionUpdateMain() {

                                    MyTools.sharedInstance().displayAlert(AbonnerActivity.this,errorString);

                                }
                            });
                        }
                    }
                });

            }
        });


        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();



    }


    // Data operation , capital
    private void withdrawal(Double amount, int typeOp) {

        mConfig.setBalance(mConfig.getBalance() - amount);
        Capital capital = new Capital(this, null);
        capital.setBalance(mConfig.getBalance());
        capital.setUser_id(mConfig.getUser_id());
        capital.setFailure_count(mConfig.getFailure_count());

        final PressOperation operation = new PressOperation(this, null);
        operation.setUser_id(mConfig.getUser_id());
        operation.setOp_type(typeOp);
        operation.setOp_amount(-1 * amount);
        if (typeOp == 2) {
            //withdrawal
            operation.setOp_wording(getString(R.string.OneTimeWithd));

        }
        else if (typeOp == 6) {
            //refund
            operation.setOp_wording(getString(R.string.OneTimeRefund));

        }


        MDBCapital.sharedInstance().setUpdateCapital(this, capital, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {

                    MDBPressOperation.sharedInstance().setAddOperation(AbonnerActivity.this, operation, new MDBInterface() {
                        @Override
                        public void completionHandler(Boolean success, final String errorString) {
                            if (success) {
                                MDBPressOperation.sharedInstance().getAllOperations(AbonnerActivity.this,
                                        mConfig.getUser_id(), new MDBInterfaceArray() {
                                            @Override
                                            public void completionHandlerArray(Boolean success, JSONArray anArray,
                                                                               final String errorString) {
                                                if (success) {
                                                    PressOperations.sharedInstance().setOperationArray(anArray);

                                                } else {
                                                    MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                                                        @Override
                                                        public void completionUpdateMain() {

                                                            MyTools.sharedInstance().displayAlert(AbonnerActivity.this,errorString);

                                                        }
                                                    });
                                                }
                                            }
                                        });

                                MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        mIBBalance.setText(" : " + mConfig.getBalance().toString());
                                        mIBWithdrawal.setText("");
                                        refreshData();
                                        MyTools.sharedInstance().displayAlert(AbonnerActivity.this,getString(R.string.withdrawalMade));

                                    }
                                });

                            } else {
                                MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        MyTools.sharedInstance().displayAlert(AbonnerActivity.this,errorString);

                                    }
                                });
                            }
                        }
                    });
                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(AbonnerActivity.this,errorString);

                        }
                    });
                }
            }
        });





    }


    private void deposit(Double amount) {

        mConfig.setBalance(mConfig.getBalance() + amount);
        Capital capital = new Capital(this, null);
        capital.setBalance(mConfig.getBalance());
        capital.setUser_id(mConfig.getUser_id());
        capital.setFailure_count(mConfig.getFailure_count());

        final PressOperation operation = new PressOperation(this, null);
        operation.setUser_id(mConfig.getUser_id());
        operation.setOp_type(1);
        operation.setOp_amount(amount);
        operation.setOp_wording(getString(R.string.OneTimeDepo));


        MDBCapital.sharedInstance().setUpdateCapital(this, capital, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {

                    MDBPressOperation.sharedInstance().setAddOperation(AbonnerActivity.this, operation, new MDBInterface() {
                        @Override
                        public void completionHandler(Boolean success, final String errorString) {
                            if (success) {
                                MDBPressOperation.sharedInstance().getAllOperations(AbonnerActivity.this,
                                        mConfig.getUser_id(), new MDBInterfaceArray() {
                                    @Override
                                    public void completionHandlerArray(Boolean success, JSONArray anArray,
                                                                       final String errorString) {
                                        if (success) {
                                            PressOperations.sharedInstance().setOperationArray(anArray);

                                        } else {
                                            MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                                                @Override
                                                public void completionUpdateMain() {

                                                    MyTools.sharedInstance().displayAlert(AbonnerActivity.this,errorString);

                                                }
                                            });
                                        }
                                    }
                                });

                                MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        mIBBalance.setText(" : " + mConfig.getBalance().toString());
                                        mIBDeposit.setText("");
                                        refreshData();
                                        MyTools.sharedInstance().displayAlert(AbonnerActivity.this,getString(R.string.paymentMade));

                                    }
                                });

                            } else {
                                MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        MyTools.sharedInstance().displayAlert(AbonnerActivity.this,errorString);

                                    }
                                });
                            }
                        }
                    });
                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(AbonnerActivity.this,errorString);

                        }
                    });
                }
            }
        });


    }


    private void braintreeOperation(final Double amount, final String type) {

        MDBPressOperation.sharedInstance().operationBraintree(this, type, mConfig.getUser_id(),
                amount, new MDBInterfaceString() {
            @Override
            public void completionHandlerString(Boolean success, String anString, final String errorString) {
                if (success) {

                    if (type.equals("deposit")) {

                        deposit(amount);

                    } else if (type.equals("withdrawal")) {

                        Double rest = Double.parseDouble(anString);
                        if (rest>0) {

                            if (amount > rest) {
                                //refund
                                withdrawal(amount - rest, 6);
                            }
                            //withdrawal
                            withdrawal(rest, 2);

                        } else {
                            //refund
                            withdrawal(amount - rest, 6);
                        }

                    }

                } else {

                    MyTools.sharedInstance().performUIUpdatesOnMain(AbonnerActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(AbonnerActivity.this,errorString);

                        }
                    });
                }

            }
        });


    }




}
