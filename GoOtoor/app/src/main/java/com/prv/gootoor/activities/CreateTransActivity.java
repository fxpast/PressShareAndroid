package com.prv.gootoor.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import com.prv.gootoor.R;
import com.prv.gootoor.daos.MDBInterface;
import com.prv.gootoor.daos.MDBInterfaceArray;
import com.prv.gootoor.daos.MDBMessage;
import com.prv.gootoor.daos.MDBProduct;
import com.prv.gootoor.daos.MDBTransact;
import com.prv.gootoor.daos.MDBUser;
import com.prv.gootoor.lists.Messages;
import com.prv.gootoor.lists.Products;
import com.prv.gootoor.lists.Transactions;
import com.prv.gootoor.lists.Users;
import com.prv.gootoor.models.Message;
import com.prv.gootoor.models.Product;
import com.prv.gootoor.models.Transaction;
import com.prv.gootoor.models.User;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by roger on 19/09/2017.
 */

public class CreateTransActivity extends AppCompatActivity {

    private Config mConfig = Config.sharedInstance();
    private Product aProduct = null;
    private int mTypeSListe = 0; //List :0, MyList :1, Historical:
    private TextView mIBInfoContact1;
    private TextView mIBInfoContact2;
    private TextView mIBInfoProduct;
    private Switch mIBisTrade;
    private Switch mIBisExchange;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trans);

        int prodId  =  getIntent().getIntExtra(mConfig.getDomaineApp()+"prod_id",0);
        mTypeSListe  =  getIntent().getIntExtra(mConfig.getDomaineApp()+"typeSListe",0);
        aProduct = getProduct(prodId);


        mIBInfoContact1 = (TextView) findViewById(R.id.IBInfoContact1);
        mIBInfoContact2 = (TextView) findViewById(R.id.IBInfoContact2);
        mIBInfoProduct = (TextView) findViewById(R.id.IBInfoProduct);
        mIBisTrade = (Switch) findViewById(R.id.IBisTrade);
        mIBisExchange = (Switch) findViewById(R.id.IBisExchange);

        if (!aProduct.getProd_echange()) {


            mIBisExchange.setChecked(false);
            mIBisExchange = (Switch) MyTools.sharedInstance().activerObjet(mIBisExchange, false);

        } else {
            mIBisExchange.setChecked(false);
            mIBisTrade.setChecked(false);
        }

        if (aProduct.getProd_prix() == 0) {

            mIBisTrade = (Switch) MyTools.sharedInstance().activerObjet(mIBisTrade, false);

        }

        if (mConfig.getIsReturnToTab()) {

            finish();
            return;

        }


        mIBInfoProduct.setText(getText(R.string.product) + ": " + aProduct.getProd_nom() + ", " + getText(R.string.price) + ": " + aProduct.getProd_prix());

        MDBUser.sharedInstance().getUser(this, aProduct.getProd_by_user(), new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, final JSONArray anArray, final String errorString) {

                if (success) {


                    MyTools.sharedInstance().performUIUpdatesOnMain(CreateTransActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            try {

                                Users.sharedInstance().setUsersArray(anArray);

                                for (int i = 0; i < Users.sharedInstance().getUsersArray().length() ; i++) {

                                    JSONObject dictionary = Users.sharedInstance().getUsersArray().getJSONObject(i);
                                    User user = new User(dictionary);

                                    if (!user.getUser_nom().equals("") || !user.getUser_prenom().equals("")) {

                                        mIBInfoContact1.setText(user.getUser_nom() + " " + user.getUser_prenom() + " ("+ getString(R.string.userNote) + ") " + user.getUser_note() + getString(R.string.star));

                                    }

                                    if (!user.getUser_ville().equals("") || !user.getUser_pays().equals("")) {

                                        mIBInfoContact2.setText(user.getUser_ville() + " " + user.getUser_pays());
                                    }

                                    break;

                                }

                            }  catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                }
                else {


                    MyTools.sharedInstance().performUIUpdatesOnMain(CreateTransActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(CreateTransActivity.this,errorString);

                        }
                    });


                }




            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        initColor();

    }

    private void initColor() {



        ConstraintLayout mIBCreTranBackgr;
        mIBCreTranBackgr = (ConstraintLayout) findViewById(R.id.IBCreTranBackgr);

        String hexColor = "#" + mConfig.getColorApp();
        mIBCreTranBackgr.setBackgroundColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppLabel();
        mIBInfoProduct.setTextColor(Color.parseColor(hexColor));
        mIBInfoContact1.setTextColor(Color.parseColor(hexColor));
        mIBInfoContact2.setTextColor(Color.parseColor(hexColor));
        mIBisTrade.setTextColor(Color.parseColor(hexColor));
        mIBisExchange.setTextColor(Color.parseColor(hexColor));


    }

        public void actionTransValid(View view) {

        if (!mIBisTrade.isChecked() && !mIBisExchange.isChecked()) {
            
             MyTools.sharedInstance().displayAlert(CreateTransActivity.this,getString(R.string.errorTypeTrans));
             return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contact");
        builder.setMessage(R.string.errorContactSeller);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (mConfig.getBalance() < aProduct.getProd_prix()) {

                    MyTools.sharedInstance().performUIUpdatesOnMain(CreateTransActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(CreateTransActivity.this,getString(R.string.errorBalanceTrans));

                        }
                    });

                    return;

                }

                mConfig.setIsReturnToTab(true);

                final Message message = new Message(CreateTransActivity.this, null);
                message.setExpediteur(mConfig.getUser_id());
                message.setDestinataire(aProduct.getProd_by_user());
                message.setProprietaire(mConfig.getUser_id());
                message.setClient_id(mConfig.getUser_id());
                message.setVendeur_id(aProduct.getProd_by_user());
                message.setProduct_id(aProduct.getProd_id());

                DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, mConfig.getMaxDayTrigger());
                Date dateExpire = new Date(c.getTimeInMillis());
                String dateExpireString = dateFormatter.format(dateExpire).toLowerCase();

                String typetransaction = "";

                if (mIBisTrade.isChecked()) {
                    typetransaction = getString(R.string.buy).toLowerCase();

                    message.setContenu(getString(R.string.emailSender) + " " + mConfig.getUser_nom() + " " + mConfig.getUser_prenom() + "\n" + getString(R.string.theProduct) + " " + mIBInfoProduct.getText().toString() + " " + getString(R.string.hastobechosen) + " " + typetransaction + ". " + getString(R.string.customerFor) + " " + getString(R.string.transactExpire) + " " + dateExpireString);

                } else if (mIBisExchange.isChecked()) {
                    typetransaction = getString(R.string.exchange).toLowerCase();

                    message.setContenu(getString(R.string.emailSender) + " " + mConfig.getUser_nom() + " " + mConfig.getUser_prenom() + "\n" + getString(R.string.theProduct) + " " + aProduct.getProd_nom()  + " " + getString(R.string.hastobechosen) + " " + typetransaction + ". " + getString(R.string.customerFor) + " " + getString(R.string.transactExpire) + " " + dateExpireString);
                }

                MDBMessage.sharedInstance().setAddMessage(CreateTransActivity.this, message, new MDBInterface() {
                    @Override
                    public void completionHandler(Boolean success, final String errorString) {

                        if (success) {
                            MDBMessage.sharedInstance().setPushNotification(CreateTransActivity.this, message, new MDBInterface() {
                                @Override
                                public void completionHandler(Boolean success, final String errorString) {
                                    if (success) {
                                        //ok
                                    } else {

                                        MyTools.sharedInstance().performUIUpdatesOnMain(CreateTransActivity.this, new MainThreadInterface() {
                                            @Override
                                            public void completionUpdateMain() {

                                                MyTools.sharedInstance().displayAlert(CreateTransActivity.this, errorString);

                                            }
                                        });


                                    }
                                }
                            });

                            MDBMessage.sharedInstance().getAllMessages(CreateTransActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                @Override
                                public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                                    if (success) {
                                        Messages.sharedInstance().setMessagesArray(anArray);

                                    } else {

                                        MyTools.sharedInstance().performUIUpdatesOnMain(CreateTransActivity.this, new MainThreadInterface() {
                                            @Override
                                            public void completionUpdateMain() {

                                                MyTools.sharedInstance().displayAlert(CreateTransActivity.this, errorString);

                                            }
                                        });


                                    }

                                }
                            });

                            final Transaction atransaction = new Transaction(null);
                            atransaction.setClient_id(message.getClient_id());
                            atransaction.setVendeur_id(message.getVendeur_id());
                            atransaction.setProd_id(message.getProduct_id());
                            atransaction.setProprietaire(message.getProprietaire());
                            atransaction.setTrans_wording("transaction :" + aProduct.getProd_nom());

                            if (mIBisTrade.isChecked()) {
                                atransaction.setTrans_type(1);
                                atransaction.setTrans_amount(aProduct.getProd_prix());

                            } else if (mIBisExchange.isChecked()) {

                                atransaction.setTrans_type(2);
                                atransaction.setTrans_amount(0.0);

                            }

                            MDBTransact.sharedInstance().setAddTransaction(CreateTransActivity.this, atransaction, new MDBInterface() {
                                @Override
                                public void completionHandler(Boolean success, final String errorString) {
                                    if (success) {
                                        MDBTransact.sharedInstance().getAllTransactions(CreateTransActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                            @Override
                                            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                                                if (success) {
                                                    Transactions.sharedInstance().setTransactionArray(anArray);
                                                } else {

                                                    MyTools.sharedInstance().performUIUpdatesOnMain(CreateTransActivity.this, new MainThreadInterface() {
                                                        @Override
                                                        public void completionUpdateMain() {

                                                            MyTools.sharedInstance().displayAlert(CreateTransActivity.this, errorString);

                                                        }
                                                    });

                                                }
                                            }
                                        });

                                        Product product = new Product(CreateTransActivity.this, null);
                                        product.setProd_id(atransaction.getProd_id());
                                        product.setProd_hidden(true);
                                        product.setProd_oth_user(mConfig.getUser_id());
                                        product.setProd_closed(false);

                                        MDBProduct.sharedInstance().setUpdateProduct(CreateTransActivity.this, "ProductTrans", product, new MDBInterface() {
                                            @Override
                                            public void completionHandler(Boolean success,final String errorString) {
                                                if (success) {
                                                    //Menu Carte
                                                    MDBProduct.sharedInstance().getProductsByCoord(CreateTransActivity.this, mConfig.getUser_id(), mConfig.getMinLongitude(), mConfig.getMaxLongitude(), mConfig.getMinLatitude(), mConfig.getMaxLatitude(), new MDBInterfaceArray() {
                                                        @Override
                                                        public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                                                            if (success) {

                                                                Products.sharedInstance().setProductsArray(anArray);

                                                            } else {

                                                                MyTools.sharedInstance().performUIUpdatesOnMain(CreateTransActivity.this, new MainThreadInterface() {
                                                                    @Override
                                                                    public void completionUpdateMain() {

                                                                        MyTools.sharedInstance().displayAlert(CreateTransActivity.this, errorString);

                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });


                                                    //Menu MaListe
                                                    MDBProduct.sharedInstance().getProductsByUser(CreateTransActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                                        @Override
                                                        public void completionHandlerArray(Boolean success, JSONArray anArray,final String errorString) {
                                                            if (success) {
                                                                Products.sharedInstance().setProductsUserArray(anArray);

                                                            } else {
                                                                MyTools.sharedInstance().performUIUpdatesOnMain(CreateTransActivity.this, new MainThreadInterface() {
                                                                    @Override
                                                                    public void completionUpdateMain() {

                                                                        MyTools.sharedInstance().displayAlert(CreateTransActivity.this, errorString);

                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });

                                                    //Menu Historique
                                                    MDBProduct.sharedInstance().getProductsByTrader(CreateTransActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                                        @Override
                                                        public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                                                            if (success) {
                                                                Products.sharedInstance().setProductsTraderArray(anArray);

                                                                MyTools.sharedInstance().performUIUpdatesOnMain(CreateTransActivity.this, new MainThreadInterface() {
                                                                    @Override
                                                                    public void completionUpdateMain() {

                                                                        mTypeSListe = 2;
                                                                        Intent intent = new Intent(CreateTransActivity.this, DetailMessageActivity.class);
                                                                        intent.putExtra(mConfig.getDomaineApp()+"prod_id",aProduct.getProd_id());
                                                                        intent.putExtra(mConfig.getDomaineApp()+"typeListe",mTypeSListe);
                                                                        startActivity(intent);

                                                                    }
                                                                });

                                                            } else {
                                                                MyTools.sharedInstance().performUIUpdatesOnMain(CreateTransActivity.this, new MainThreadInterface() {
                                                                    @Override
                                                                    public void completionUpdateMain() {

                                                                        MyTools.sharedInstance().displayAlert(CreateTransActivity.this, errorString);

                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });


                                                } else {

                                                    MyTools.sharedInstance().performUIUpdatesOnMain(CreateTransActivity.this, new MainThreadInterface() {
                                                        @Override
                                                        public void completionUpdateMain() {

                                                            MyTools.sharedInstance().displayAlert(CreateTransActivity.this, errorString);

                                                        }
                                                    });
                                                }
                                            }
                                        });

                                    } else {
                                        MyTools.sharedInstance().performUIUpdatesOnMain(CreateTransActivity.this, new MainThreadInterface() {
                                            @Override
                                            public void completionUpdateMain() {

                                                MyTools.sharedInstance().displayAlert(CreateTransActivity.this, errorString);

                                            }
                                        });
                                    }
                                }
                            });

                        } else {
                            MyTools.sharedInstance().performUIUpdatesOnMain(CreateTransActivity.this, new MainThreadInterface() {
                                @Override
                                public void completionUpdateMain() {

                                    MyTools.sharedInstance().displayAlert(CreateTransActivity.this, errorString);

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


    public void actionTransHelp(View view) {

        //Todo Tuto_Presentation
        MyTools.sharedInstance().showHelp("transactions", this);

    }

    public void actionCloseWidows(View view) {

        finish();
    }


    public void actionExchange(View view) {

        mIBisExchange.setChecked(true);
        mIBisTrade.setChecked(mIBisExchange.isChecked() ? false : true);

    }


    public void actionTrade(View view) {

        mIBisTrade.setChecked(true);
        mIBisExchange.setChecked(mIBisTrade.isChecked() ? false : true);

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



}
