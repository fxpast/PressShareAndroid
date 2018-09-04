package com.prv.gootoor.activities;


import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.prv.gootoor.daos.MDBCapital;
import com.prv.gootoor.daos.MDBCommission;
import com.prv.gootoor.daos.MDBInterface;
import com.prv.gootoor.daos.MDBInterfaceArray;

import com.prv.gootoor.daos.MDBMessage;
import com.prv.gootoor.daos.MDBPressOperation;
import com.prv.gootoor.daos.MDBProduct;
import com.prv.gootoor.daos.MDBTransact;
import com.prv.gootoor.daos.MDBUser;

import com.prv.gootoor.lists.PressOperations;
import com.prv.gootoor.lists.Products;
import com.prv.gootoor.lists.Transactions;
import com.prv.gootoor.lists.Users;
import com.prv.gootoor.R;
import com.prv.gootoor.models.Capital;
import com.prv.gootoor.models.Commission;
import com.prv.gootoor.models.Message;
import com.prv.gootoor.models.PressOperation;
import com.prv.gootoor.models.Product;
import com.prv.gootoor.models.Transaction;
import com.prv.gootoor.models.User;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * Created by roger on 19/09/2017.
 */

public class DetailTransActivity extends AppCompatActivity {

    private Config mConfig = Config.sharedInstance();
    private Product aProduct = null;
    private int mTypeSListe = 0; //List :0, MyList :1, Historical:
    private ImageView mIBTranStar1;
    private ImageView mIBTranStar2;
    private ImageView mIBTranStar3;
    private ImageView mIBTranStar4;
    private ImageView mIBTranStar5;

    private Switch mIBisConfirm;
    private Switch mIBisCancel;
    private Switch mIBisInterlo;
    private Switch mIBisMyAbsent;
    private Switch mIBisCompliant;
    private Switch mIBisOther;
    private Button mIBDetTransValid;

    private EditText mIBOtherText;

    private int mStar;

    private Transaction aTransaction;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_trans);

        mIBTranStar1 = (ImageView) findViewById(R.id.IBTranStar1);
        mIBTranStar2 = (ImageView) findViewById(R.id.IBTranStar2);
        mIBTranStar3 = (ImageView) findViewById(R.id.IBTranStar3);
        mIBTranStar4 = (ImageView) findViewById(R.id.IBTranStar4);
        mIBTranStar5 = (ImageView) findViewById(R.id.IBTranStar5);

        mIBDetTransValid = (Button) findViewById(R.id.IBDetTransValid);

        final TextView mIBClient = (TextView) findViewById(R.id.IBClient);
        final TextView mIBInfoContact = (TextView) findViewById(R.id.IBInfoContact);
        TextView mIBWording = (TextView) findViewById(R.id.IBWording);
        TextView mIBAmount = (TextView) findViewById(R.id.IBAmount);
        TextView mIBLabelType = (TextView) findViewById(R.id.IBLabelType);
        mIBOtherText = (EditText) findViewById(R.id.IBOtherText);

        mIBisConfirm = (Switch) findViewById(R.id.IBisConfirm);
        mIBisCancel = (Switch) findViewById(R.id.IBisCancel);
        mIBisInterlo = (Switch) findViewById(R.id.IBisInterlo);
        mIBisMyAbsent = (Switch) findViewById(R.id.IBisMyAbsent);
        mIBisCompliant = (Switch) findViewById(R.id.IBisCompliant);
        mIBisOther = (Switch) findViewById(R.id.IBisOther);

        int transId  =  getIntent().getIntExtra(mConfig.getDomaineApp()+"trans_id",0);
        aTransaction = getTransaction(transId);
        mStar = aTransaction.getTrans_note();
        if (mStar == 1) {
            actionTranStar1(mIBTranStar1);
        } else  if (mStar == 2) {
            actionTranStar2(mIBTranStar2);
        } else  if (mStar == 3) {
            actionTranStar3(mIBTranStar3);
        } else  if (mStar == 4) {
            actionTranStar4(mIBTranStar4);
        } else  if (mStar == 5) {
            actionTranStar5(mIBTranStar5);
        }

        mIBisCancel.setChecked(false);
        mIBisConfirm.setChecked(false);
        setUIHidden(true);

        //La transaction a été annulée
        if (aTransaction.getTrans_valid() == 1) {
            mIBisCancel.setChecked(true);
            actionCancel(mIBisCancel);

        } else if (aTransaction.getTrans_valid() == 2) {
            //La transaction est confirmée
            mIBisConfirm.setChecked(true);
            actionConfirm(mIBisConfirm);

        }

        if (aTransaction.getTrans_avis().equals("interlocuteur")) {
            //l'interlocuteur était absent
            mIBisInterlo.setChecked(true);
            actionInterlo(mIBisInterlo);

        } else if (aTransaction.getTrans_avis().equals("absence")) {
            //Je n'ai pu être au rendez-vous
            mIBisMyAbsent.setChecked(true);
            actionMyAbsent(mIBisMyAbsent);

        } else if (aTransaction.getTrans_avis().equals("conformite")) {
            //le produit vendu ou echangé n'était pas conforme à l'annonce
            mIBisCompliant.setChecked(true);
            actionCompliant(mIBisCompliant);

        } else {

            mIBisOther.setChecked(true);
            mIBOtherText.setText(aTransaction.getTrans_avis());
            actionOther(mIBisOther);
        }

        mIBWording.setText(getString(R.string.wording) + " " + aTransaction.getTrans_wording());
        mIBAmount.setText(getString(R.string.amount) + " " + aTransaction.getTrans_amount());

        if (aTransaction.getTrans_type() == 1) {
            mIBLabelType.setText(mIBLabelType.getText() + " " + getString(R.string.buy));
        } else if (aTransaction.getTrans_type() == 2) {
            mIBLabelType.setText(mIBLabelType.getText() + " " + getString(R.string.exchange));
        }

        if (aTransaction.getTrans_valid() == 1 || aTransaction.getTrans_valid() == 2 || (aTransaction.getTrans_type() == 1 && aTransaction.getVendeur_id() == aTransaction.getProprietaire())) {


            mIBDetTransValid = (Button) MyTools.sharedInstance().activerObjet(mIBDetTransValid, false);
            mIBisOther = (Switch) MyTools.sharedInstance().activerObjet(mIBisOther, false);
            mIBisMyAbsent = (Switch) MyTools.sharedInstance().activerObjet(mIBisMyAbsent, false);
            mIBisCompliant = (Switch) MyTools.sharedInstance().activerObjet(mIBisCompliant, false);
            mIBisInterlo = (Switch) MyTools.sharedInstance().activerObjet(mIBisInterlo, false);
            mIBisConfirm = (Switch) MyTools.sharedInstance().activerObjet(mIBisConfirm, false);
            mIBisCancel = (Switch) MyTools.sharedInstance().activerObjet(mIBisCancel, false);
            mIBTranStar1 = (ImageView) MyTools.sharedInstance().activerObjet(mIBTranStar1, false);
            mIBTranStar2 = (ImageView) MyTools.sharedInstance().activerObjet(mIBTranStar2, false);
            mIBTranStar3 = (ImageView) MyTools.sharedInstance().activerObjet(mIBTranStar3, false);
            mIBTranStar4 = (ImageView) MyTools.sharedInstance().activerObjet(mIBTranStar4, false);
            mIBTranStar5 = (ImageView) MyTools.sharedInstance().activerObjet(mIBTranStar5, false);

        } else {

            mIBDetTransValid = (Button) MyTools.sharedInstance().activerObjet(mIBDetTransValid, true);

        }

        final int paramId = (aTransaction.getClient_id() == aTransaction.getProprietaire()) ? aTransaction.getVendeur_id() : aTransaction.getClient_id();
        MDBUser.sharedInstance().getUser(this, paramId, new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, final JSONArray anArray, final String errorString) {
                if (success) {

                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {

                            try {

                                Users.sharedInstance().setUsersArray(anArray);
                                for (int i = 0; i < Users.sharedInstance().getUsersArray().length() ; i++) {

                                    JSONObject dictionary = Users.sharedInstance().getUsersArray().getJSONObject(i);
                                    User  user = new User(dictionary);
                                    mIBClient.setText((aTransaction.getClient_id() == aTransaction.getProprietaire()) ? getText(R.string.seller) : getText(R.string.customer));
                                    mIBClient.setText(mIBClient.getText() + " " + user.getUser_nom() + " " + user.getUser_prenom() + " (" + paramId + ")");
                                    mIBInfoContact.setText(mIBInfoContact.getText() + " " + user.getUser_ville() + " " + user.getUser_pays());

                                    break;
                                }

                            }  catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                } else {

                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            //Todo action alert
                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

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



        ConstraintLayout mIBDetailTranBackgr;
        mIBDetailTranBackgr = (ConstraintLayout) findViewById(R.id.IBDetailTranBackgr);

        String hexColor = "#" + mConfig.getColorApp();
        mIBDetailTranBackgr.setBackgroundColor(Color.parseColor(hexColor));

        TextView mIBNote = (TextView) findViewById(R.id.IBNote);
        TextView mIBClient = (TextView) findViewById(R.id.IBClient);
        TextView mIBInfoContact = (TextView) findViewById(R.id.IBInfoContact);
        TextView mIBWording = (TextView) findViewById(R.id.IBWording);
        TextView mIBAmount = (TextView) findViewById(R.id.IBAmount);
        TextView mIBLabelType = (TextView) findViewById(R.id.IBLabelType);

        Switch mIBisConfirm = (Switch) findViewById(R.id.IBisConfirm);
        Switch mIBisCancel = (Switch) findViewById(R.id.IBisCancel);
        Switch mIBisInterlo = (Switch) findViewById(R.id.IBisInterlo);
        Switch mIBisMyAbsent = (Switch) findViewById(R.id.IBisMyAbsent);
        Switch mIBisCompliant = (Switch) findViewById(R.id.IBisCompliant);
        Switch mIBisOther = (Switch) findViewById(R.id.IBisOther);

        EditText mIBOtherText = (EditText) findViewById(R.id.IBOtherText);


        hexColor = "#" + mConfig.getColorAppLabel();
        mIBNote.setTextColor(Color.parseColor(hexColor));
        mIBClient.setTextColor(Color.parseColor(hexColor));
        mIBInfoContact.setTextColor(Color.parseColor(hexColor));
        mIBWording.setTextColor(Color.parseColor(hexColor));
        mIBAmount.setTextColor(Color.parseColor(hexColor));
        mIBLabelType.setTextColor(Color.parseColor(hexColor));
        mIBisConfirm.setTextColor(Color.parseColor(hexColor));
        mIBisCancel.setTextColor(Color.parseColor(hexColor));
        mIBisInterlo.setTextColor(Color.parseColor(hexColor));
        mIBisMyAbsent.setTextColor(Color.parseColor(hexColor));
        mIBisCompliant.setTextColor(Color.parseColor(hexColor));
        mIBisOther.setTextColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppPlHd();
        mIBOtherText.setHintTextColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppText();
        mIBOtherText.setTextColor(Color.parseColor(hexColor));



    }


    public void actionConfirm(View view) {

        //confirmer la transaction
        mIBisConfirm.setChecked(true);
        mIBisCancel.setChecked(!mIBisConfirm.isChecked());
        setUIHidden(!mIBisCancel.isChecked());

    }


    public void actionCancel(View view) {

        //annuler la transaction
        mIBisCancel.setChecked(true);
        mIBisConfirm.setChecked(!mIBisCancel.isChecked());
        setUIHidden(!mIBisCancel.isChecked());
    }


    public void actionOther(View view) {

        //Autre cause d'annulation de la transaction
        mIBisOther.setChecked(true);
        mIBisInterlo.setChecked(!mIBisOther.isChecked());
        mIBisMyAbsent.setChecked(!mIBisOther.isChecked());
        mIBisCompliant.setChecked(!mIBisOther.isChecked());


        mIBOtherText = (EditText) MyTools.sharedInstance().activerObjet(mIBOtherText, mIBisOther.isChecked());


    }


    public void actionCompliant(View view) {

        //Cause d'annulation : produit non conforme
        mIBisCompliant.setChecked(true);
        mIBisInterlo.setChecked(!mIBisCompliant.isChecked());
        mIBisMyAbsent.setChecked(!mIBisCompliant.isChecked());
        mIBisOther.setChecked(!mIBisCompliant.isChecked());

        mIBOtherText = (EditText) MyTools.sharedInstance().activerObjet(mIBOtherText, mIBisOther.isChecked());


    }


    public void actionMyAbsent(View view) {
        //Cause d'annulation : je suis absent
        mIBisMyAbsent.setChecked(true);
        mIBisInterlo.setChecked(!mIBisMyAbsent.isChecked());
        mIBisCompliant.setChecked(!mIBisMyAbsent.isChecked());
        mIBisOther.setChecked(!mIBisMyAbsent.isChecked());

        mIBOtherText = (EditText) MyTools.sharedInstance().activerObjet(mIBOtherText, mIBisOther.isChecked());


    }

    public void actionInterlo(View view) {

        //Cause d'annulation : mon interlocuteur est absent
        mIBisInterlo.setChecked(true);
        mIBisMyAbsent.setChecked(!mIBisInterlo.isChecked());
        mIBisCompliant.setChecked(!mIBisInterlo.isChecked());
        mIBisOther.setChecked(!mIBisInterlo.isChecked());

        mIBOtherText = (EditText) MyTools.sharedInstance().activerObjet(mIBOtherText, mIBisOther.isChecked());


    }

    private void setUIHidden(Boolean hidden) {

        mIBisOther.setChecked(false);
        mIBisInterlo.setChecked(false);
        mIBisCompliant.setChecked(false);
        mIBisMyAbsent.setChecked(false);

        mIBOtherText = (EditText) MyTools.sharedInstance().activerObjet(mIBOtherText, !hidden);



        if (hidden) {

            mIBisOther.setVisibility(View.GONE);
            mIBisInterlo.setVisibility(View.GONE);
            mIBisMyAbsent.setVisibility(View.GONE);
            mIBisCompliant.setVisibility(View.GONE);

            mIBOtherText.setVisibility(View.GONE);

        } else {

            mIBisOther.setVisibility(View.VISIBLE);
            mIBisInterlo.setVisibility(View.VISIBLE);
            mIBisMyAbsent.setVisibility(View.VISIBLE);
            mIBisCompliant.setVisibility(View.VISIBLE);

            mIBOtherText.setVisibility(View.VISIBLE);
        }

    }



    private void changeStar(ImageView imageView) {


        if (imageView.getTag().equals("whitestar")){
            imageView.setImageResource(R.drawable.blackstar);
            imageView.setTag("blackstar");
        } else {
            imageView.setImageResource(R.drawable.whitestar);
            imageView.setTag("whitestar");
        }

    }


    private void initStar() {

        mIBTranStar1.setImageResource(R.drawable.whitestar);
        mIBTranStar2.setImageResource(R.drawable.whitestar);
        mIBTranStar3.setImageResource(R.drawable.whitestar);
        mIBTranStar4.setImageResource(R.drawable.whitestar);
        mIBTranStar5.setImageResource(R.drawable.whitestar);

        mIBTranStar1.setTag("whitestar");
        mIBTranStar2.setTag("whitestar");
        mIBTranStar3.setTag("whitestar");
        mIBTranStar4.setTag("whitestar");
        mIBTranStar5.setTag("whitestar");

    }



    public void actionTranStar1(View view) {

        mStar = 1;
        initStar();
        changeStar((ImageView) view);

    }


    public void actionTranStar2(View view) {

        mStar = 2;
        initStar();
        changeStar((ImageView) view);
        changeStar(mIBTranStar1);

    }


    public void actionTranStar3(View view) {

        mStar = 3;
        initStar();
        changeStar((ImageView) view);
        changeStar(mIBTranStar2);
        changeStar(mIBTranStar1);

    }


    public void actionTranStar4(View view) {

        mStar = 4;
        initStar();
        changeStar((ImageView) view);
        changeStar(mIBTranStar3);
        changeStar(mIBTranStar2);
        changeStar(mIBTranStar1);

    }


    public void actionTranStar5(View view) {

        mStar = 5;
        initStar();
        changeStar((ImageView) view);
        changeStar(mIBTranStar4);
        changeStar(mIBTranStar3);
        changeStar(mIBTranStar2);
        changeStar(mIBTranStar1);

    }



    private void tradeConfirmTransact() {

        final Message message = new Message(this, null);
        message.setExpediteur(mConfig.getUser_id());
        message.setDestinataire(aTransaction.getVendeur_id());
        message.setProprietaire(mConfig.getUser_id());
        message.setClient_id(aTransaction.getClient_id());
        message.setVendeur_id(aTransaction.getVendeur_id());
        message.setProduct_id(aTransaction.getProd_id());
        message.setContenu(getString(R.string.emailSender) + " " + mConfig.getUser_nom() + " " + mConfig.getUser_prenom()
                + "\n" + getString(R.string.theProduct)  + " " +  aTransaction.getTrans_wording() + " " + getString(R.string.buyConfirmed));

        MDBMessage.sharedInstance().setAddMessage(this, message, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {
                    MDBMessage.sharedInstance().setPushNotification(DetailTransActivity.this, message, new MDBInterface() {
                        @Override
                        public void completionHandler(Boolean success, final String errorString) {
                            if (success) {
                                //ok
                            } else {
                                MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        //Todo action alert
                                        MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                    }
                                });
                            }
                        }
                    });
                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            //Todo action alert
                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                        }
                    });
                }
            }
        });

        Product product = new Product(this, null);
        product.setProd_id(aTransaction.getProd_id());
        product.setProd_hidden(true);
        product.setProd_oth_user(aTransaction.getProprietaire());
        product.setProd_closed(true);

        MDBProduct.sharedInstance().setUpdateProduct(this, "ProductTrans", product, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {
                    //Menu Carte
                    MDBProduct.sharedInstance().getProductsByCoord(DetailTransActivity.this, mConfig.getUser_id(), mConfig.getMinLongitude(),
                            mConfig.getMaxLongitude(), mConfig.getMinLatitude(), mConfig.getMaxLatitude(), new MDBInterfaceArray() {
                        @Override
                        public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                            if (success) {
                                Products.sharedInstance().setProductsArray(anArray);
                            } else {
                                MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        //Todo action alert
                                        MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                    }
                                });
                            }
                        }
                    });

                    //Menu Historique
                    MDBProduct.sharedInstance().getProductsByTrader(DetailTransActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                        @Override
                        public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                            if (success) {
                                Products.sharedInstance().setProductsTraderArray(anArray);
                            } else {
                                MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        //Todo action alert
                                        MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                    }
                                });
                            }
                        }
                    });

                    //Menu MaListe
                    MDBProduct.sharedInstance().getProductsByUser(DetailTransActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                        @Override
                        public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                            if (success) {
                                Products.sharedInstance().setProductsUserArray(anArray);
                            } else {
                                MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        //Todo action alert
                                        MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                    }
                                });
                            }
                        }
                    });


                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            //Todo action alert
                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                        }
                    });
                }
            }
        });

        mConfig.setBalance(mConfig.getBalance() - aTransaction.getTrans_amount());
        mConfig.setBalance(mConfig.getBalance() - mConfig.getCommisPourcBuy() * aTransaction.getTrans_amount());
        final Capital capital = new Capital(this, null);
        final PressOperation operation = new PressOperation(this, null);

        //Acheteur confirme la transaction commerciale
        capital.setBalance(mConfig.getBalance());
        capital.setUser_id(aTransaction.getProprietaire());
        capital.setFailure_count(mConfig.getFailure_count());

        MDBCapital.sharedInstance().setUpdateCapital(this, capital, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {

                    Commission commission = new Commission(DetailTransActivity.this, null);
                    commission.setUser_id(aTransaction.getProprietaire());
                    commission.setProduct_id(aTransaction.getProd_id());
                    commission.setCom_amount(mConfig.getCommisPourcBuy() * aTransaction.getTrans_amount());

                    //Création d'une commission d'achat pour le client
                    MDBCommission.sharedInstance().setAddCommission(DetailTransActivity.this, commission, mConfig.getBalance(), new MDBInterface() {
                        @Override
                        public void completionHandler(Boolean success, final String errorString) {
                            if (success) {
                                //ok
                            } else {
                                MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        //Todo action alert
                                        MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                    }
                                });

                            }
                        }
                    });

                    operation.setUser_id(aTransaction.getProprietaire());
                    operation.setOp_type(3); //c'est une operation d'achat de produit
                    operation.setOp_amount(-1 * aTransaction.getTrans_amount());
                    operation.setOp_wording(getString(R.string.buy) + " " + getString(R.string.product));

                    //Création d'un operation d'achat pour le client
                    MDBPressOperation.sharedInstance().setAddOperation(DetailTransActivity.this, operation, new MDBInterface() {
                        @Override
                        public void completionHandler(Boolean success, final String errorString) {
                            if (success) {
                                MDBPressOperation.sharedInstance().getAllOperations(DetailTransActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                    @Override
                                    public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                                        if (success) {
                                            PressOperations.sharedInstance().setOperationArray(anArray);
                                        } else {
                                            MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                                @Override
                                                public void completionUpdateMain() {

                                                    //Todo action alert
                                                    MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                                }
                                            });
                                        }
                                    }
                                });

                                //Le compte du vendeur est consulté
                                MDBCapital.sharedInstance().getCapital(DetailTransActivity.this, aTransaction.getVendeur_id(), new MDBInterfaceArray() {
                                    @Override
                                    public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                                        if (success) {
                                            try {

                                                for (int i = 0; i < anArray.length(); i++) {

                                                    JSONObject dictionary = anArray.getJSONObject(i);
                                                    Capital cap = new Capital(DetailTransActivity.this, dictionary);
                                                    capital.setBalance(cap.getBalance() + aTransaction.getTrans_amount());
                                                    capital.setBalance(capital.getBalance() - mConfig.getCommisPourcBuy() * aTransaction.getTrans_amount());
                                                    capital.setUser_id(cap.getUser_id());
                                                    capital.setFailure_count(cap.getFailure_count());
                                                    break;
                                                }
                                                //Le compte du vendeur est crédité
                                                MDBCapital.sharedInstance().setUpdateCapital(DetailTransActivity.this, capital, new MDBInterface() {
                                                    @Override
                                                    public void completionHandler(Boolean success, final String errorString) {
                                                        if (success) {
                                                            Commission commission1 = new Commission(DetailTransActivity.this, null);
                                                            commission1.setUser_id(aTransaction.getVendeur_id());
                                                            commission1.setProduct_id(aTransaction.getProd_id());
                                                            commission1.setCom_amount(mConfig.getCommisPourcBuy() * aTransaction.getTrans_amount());

                                                            //Création d'une commission d'achat pour le client
                                                            MDBCommission.sharedInstance().setAddCommission(DetailTransActivity.this,
                                                                    commission1, capital.getBalance(), new MDBInterface() {
                                                                @Override
                                                                public void completionHandler(Boolean success, final String errorString) {
                                                                    if (success) {
                                                                        //OK
                                                                    } else {
                                                                        MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this,
                                                                                new MainThreadInterface() {
                                                                            @Override
                                                                            public void completionUpdateMain() {

                                                                                //Todo action alert
                                                                                MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                            });

                                                            operation.setUser_id(aTransaction.getVendeur_id());
                                                            operation.setOp_type(4); //C'est une opération de vente de produit
                                                            operation.setOp_amount(aTransaction.getTrans_amount());
                                                            operation.setOp_wording(getString(R.string.sell) + " " + getString(R.string.product));

                                                            //Création d'un operation de vente pour le vendeur
                                                            MDBPressOperation.sharedInstance().setAddOperation(DetailTransActivity.this, operation,
                                                                    new MDBInterface() {
                                                                @Override
                                                                public void completionHandler(Boolean success, final String errorString) {
                                                                    if (success) {
                                                                        MDBPressOperation.sharedInstance().getAllOperations(DetailTransActivity.this,
                                                                                mConfig.getUser_id(), new MDBInterfaceArray() {
                                                                            @Override
                                                                            public void completionHandlerArray(Boolean success, JSONArray anArray,
                                                                                                               final String errorString) {
                                                                                if (success) {
                                                                                    PressOperations.sharedInstance().setOperationArray(anArray);
                                                                                } else {
                                                                                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this,
                                                                                            new MainThreadInterface() {
                                                                                        @Override
                                                                                        public void completionUpdateMain() {

                                                                                            //Todo action alert
                                                                                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                                                                        }
                                                                                    });
                                                                                }
                                                                            }
                                                                        });

                                                                        MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this,
                                                                                new MainThreadInterface() {
                                                                                    @Override
                                                                                    public void completionUpdateMain() {

                                                                                      finish();
                                                                                    }
                                                                                });

                                                                    } else {
                                                                        MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this,
                                                                                new MainThreadInterface() {
                                                                                    @Override
                                                                                    public void completionUpdateMain() {

                                                                                        //Todo action alert
                                                                                        MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                                                                    }
                                                                                });

                                                                    }
                                                                }
                                                            });

                                                        } else {
                                                            MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this,
                                                                    new MainThreadInterface() {
                                                                        @Override
                                                                        public void completionUpdateMain() {

                                                                            //Todo action alert
                                                                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                                                        }
                                                                    });
                                                        }
                                                    }
                                                });



                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        } else {

                                            MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this,
                                                    new MainThreadInterface() {
                                                        @Override
                                                        public void completionUpdateMain() {

                                                            //Todo action alert
                                                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                                        }
                                                    });
                                        }
                                    }
                                });

                            } else {

                                MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this,
                                        new MainThreadInterface() {
                                            @Override
                                            public void completionUpdateMain() {

                                                //Todo action alert
                                                MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                            }
                                        });
                            }
                        }
                    });

                } else {

                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this,
                            new MainThreadInterface() {
                                @Override
                                public void completionUpdateMain() {

                                    //Todo action alert
                                    MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                }
                            });
                }
            }
        });


    }

    private void tradeCancelTransact() {

        final Message message = new Message(this, null);
        message.setExpediteur(mConfig.getUser_id());
        message.setDestinataire(aTransaction.getVendeur_id());
        message.setProprietaire(mConfig.getUser_id());
        message.setClient_id(aTransaction.getClient_id());
        message.setVendeur_id(aTransaction.getVendeur_id());
        message.setProduct_id(aTransaction.getProd_id());
        message.setContenu(getString(R.string.emailSender) + " " + mConfig.getUser_nom() + " " + mConfig.getUser_prenom()
                + "\n" + getString(R.string.theProduct)  + " " +  aTransaction.getTrans_wording() + " " + getString(R.string.buyCanceled));


        MDBMessage.sharedInstance().setAddMessage(this, message, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {
                    MDBMessage.sharedInstance().setPushNotification(DetailTransActivity.this, message, new MDBInterface() {
                        @Override
                        public void completionHandler(Boolean success, final String errorString) {
                            if (success) {
                                //ok
                            } else {
                                MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        //Todo action alert
                                        MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                    }
                                });
                            }
                        }
                    });
                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            //Todo action alert
                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                        }
                    });
                }
            }
        });


        mConfig.setFailure_count(mConfig.getFailure_count() + 1);

        final Capital capital = new Capital(this, null);

        //Acheteur annule

        capital.setBalance(mConfig.getBalance());
        capital.setUser_id(aTransaction.getProprietaire());
        capital.setFailure_count(mConfig.getFailure_count());

        MDBCapital.sharedInstance().setUpdateCapital(this, capital, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {

                    Product product = new Product(DetailTransActivity.this, null);
                    product.setProd_id(aTransaction.getProd_id());
                    product.setProd_hidden(false);
                    product.setProd_oth_user(0);
                    product.setProd_closed(false);


                    MDBProduct.sharedInstance().setUpdateProduct(DetailTransActivity.this, "ProductTrans", product, new MDBInterface() {
                        @Override
                        public void completionHandler(Boolean success, final String errorString) {
                            if (success) {
                                //Menu Carte
                                MDBProduct.sharedInstance().getProductsByCoord(DetailTransActivity.this, mConfig.getUser_id(), mConfig.getMinLongitude(),
                                        mConfig.getMaxLongitude(), mConfig.getMinLatitude(), mConfig.getMaxLatitude(), new MDBInterfaceArray() {
                                            @Override
                                            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                                                if (success) {
                                                    Products.sharedInstance().setProductsArray(anArray);
                                                } else {
                                                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                                        @Override
                                                        public void completionUpdateMain() {

                                                            //Todo action alert
                                                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                                        }
                                                    });
                                                }
                                            }
                                        });

                                //Menu Historique
                                MDBProduct.sharedInstance().getProductsByTrader(DetailTransActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                    @Override
                                    public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                                        if (success) {
                                            Products.sharedInstance().setProductsTraderArray(anArray);
                                        } else {
                                            MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                                @Override
                                                public void completionUpdateMain() {

                                                    //Todo action alert
                                                    MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                                }
                                            });
                                        }
                                    }
                                });

                                //Menu MaListe
                                MDBProduct.sharedInstance().getProductsByUser(DetailTransActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                    @Override
                                    public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                                        if (success) {
                                            Products.sharedInstance().setProductsUserArray(anArray);

                                            MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                                @Override
                                                public void completionUpdateMain() {

                                                  finish();

                                                }
                                            });

                                        } else {
                                            MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                                @Override
                                                public void completionUpdateMain() {

                                                    //Todo action alert
                                                    MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                                }
                                            });
                                        }
                                    }
                                });


                            } else {
                                MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        //Todo action alert
                                        MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                    }
                                });
                            }
                        }
                    });


                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            //Todo action alert
                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                        }
                    });
                }
            }
        });



    }

    private void exchangeConfirmTransact() {

        final Message message = new Message(this, null);
        message.setExpediteur(mConfig.getUser_id());

        if (aTransaction.getProprietaire() == aTransaction.getClient_id()) {

            message.setDestinataire(aTransaction.getVendeur_id());

        } else if (aTransaction.getProprietaire() == aTransaction.getVendeur_id()) {

            message.setDestinataire(aTransaction.getClient_id());
        }

        message.setProprietaire(mConfig.getUser_id());
        message.setClient_id(aTransaction.getClient_id());
        message.setVendeur_id(aTransaction.getVendeur_id());
        message.setProduct_id(aTransaction.getProd_id());
        message.setContenu(getString(R.string.emailSender) + " " + mConfig.getUser_nom() + " " + mConfig.getUser_prenom()
                + "\n" + getString(R.string.theProduct)  + " " +  aTransaction.getTrans_wording() + " " + getString(R.string.exchangeConfirmed));


        MDBMessage.sharedInstance().setAddMessage(this, message, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {
                    MDBMessage.sharedInstance().setPushNotification(DetailTransActivity.this, message, new MDBInterface() {
                        @Override
                        public void completionHandler(Boolean success, final String errorString) {
                            if (success) {
                                //ok
                            } else {
                                MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        //Todo action alert
                                        MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                    }
                                });
                            }
                        }
                    });
                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            //Todo action alert
                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                        }
                    });
                }
            }
        });


        Commission commission = new Commission(DetailTransActivity.this, null);
        //l'utilisateur confirme l'echange
        mConfig.setBalance(mConfig.getBalance() - mConfig.getCommisFixEx());

        commission.setUser_id(aTransaction.getProprietaire());
        commission.setProduct_id(aTransaction.getProd_id());
        commission.setCom_amount(mConfig.getCommisFixEx());

        //Création d'une commission d'achat pour le client
        MDBCommission.sharedInstance().setAddCommission(DetailTransActivity.this, commission, mConfig.getBalance(), new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {
                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            finish();
                        }
                    });
                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            //Todo action alert
                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                        }
                    });

                }
            }
        });


    }

    private void exchangeCancelTransact() {

        final Message message = new Message(this, null);
        message.setExpediteur(mConfig.getUser_id());

        if (aTransaction.getProprietaire() == aTransaction.getClient_id()) {

            message.setDestinataire(aTransaction.getVendeur_id());

        } else if (aTransaction.getProprietaire() == aTransaction.getVendeur_id()) {

            message.setDestinataire(aTransaction.getClient_id());
        }

        message.setProprietaire(mConfig.getUser_id());
        message.setClient_id(aTransaction.getClient_id());
        message.setVendeur_id(aTransaction.getVendeur_id());
        message.setProduct_id(aTransaction.getProd_id());
        message.setContenu(getString(R.string.emailSender) + " " + mConfig.getUser_nom() + " " + mConfig.getUser_prenom()
                + "\n" + getString(R.string.theProduct)  + " " +  aTransaction.getTrans_wording() + " " + getString(R.string.exchangeCanceled));


        MDBMessage.sharedInstance().setAddMessage(this, message, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {
                    MDBMessage.sharedInstance().setPushNotification(DetailTransActivity.this, message, new MDBInterface() {
                        @Override
                        public void completionHandler(Boolean success, final String errorString) {
                            if (success) {
                                //ok
                            } else {
                                MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        //Todo action alert
                                        MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                    }
                                });
                            }
                        }
                    });
                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            //Todo action alert
                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                        }
                    });
                }
            }
        });

        mConfig.setFailure_count(mConfig.getFailure_count() + 1);

        final Capital capital = new Capital(this, null);

        //Acheteur annule

        capital.setBalance(mConfig.getBalance());
        capital.setUser_id(aTransaction.getProprietaire());
        capital.setFailure_count(mConfig.getFailure_count());


        MDBCapital.sharedInstance().setUpdateCapital(this, capital, new MDBInterface() {
            @Override
            public void completionHandler(Boolean success, final String errorString) {
                if (success) {

                    Product product = new Product(DetailTransActivity.this, null);
                    product.setProd_id(aTransaction.getProd_id());
                    product.setProd_hidden(false);
                    product.setProd_oth_user(0);
                    product.setProd_closed(false);


                    MDBProduct.sharedInstance().setUpdateProduct(DetailTransActivity.this, "ProductTrans", product, new MDBInterface() {
                        @Override
                        public void completionHandler(Boolean success, final String errorString) {
                            if (success) {
                                //Menu Carte
                                MDBProduct.sharedInstance().getProductsByCoord(DetailTransActivity.this, mConfig.getUser_id(), mConfig.getMinLongitude(),
                                        mConfig.getMaxLongitude(), mConfig.getMinLatitude(), mConfig.getMaxLatitude(), new MDBInterfaceArray() {
                                            @Override
                                            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                                                if (success) {
                                                    Products.sharedInstance().setProductsArray(anArray);
                                                } else {
                                                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                                        @Override
                                                        public void completionUpdateMain() {

                                                            //Todo action alert
                                                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                                        }
                                                    });
                                                }
                                            }
                                        });

                                //Menu Historique
                                MDBProduct.sharedInstance().getProductsByTrader(DetailTransActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                    @Override
                                    public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                                        if (success) {
                                            Products.sharedInstance().setProductsTraderArray(anArray);
                                        } else {
                                            MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                                @Override
                                                public void completionUpdateMain() {

                                                    //Todo action alert
                                                    MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                                }
                                            });
                                        }
                                    }
                                });

                                //Menu MaListe
                                MDBProduct.sharedInstance().getProductsByUser(DetailTransActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                    @Override
                                    public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                                        if (success) {
                                            Products.sharedInstance().setProductsUserArray(anArray);

                                            MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                                @Override
                                                public void completionUpdateMain() {

                                                    finish();

                                                }
                                            });

                                        } else {
                                            MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                                @Override
                                                public void completionUpdateMain() {

                                                    //Todo action alert
                                                    MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                                }
                                            });
                                        }
                                    }
                                });


                            } else {
                                MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        //Todo action alert
                                        MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                    }
                                });
                            }
                        }
                    });


                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            //Todo action alert
                            MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                        }
                    });
                }
            }
        });


    }


    public void actionTransValid(View view) {


        if (!mIBisConfirm.isChecked() &&  !mIBisCancel.isChecked()) {

            MyTools.sharedInstance().displayAlert(DetailTransActivity.this,getString(R.string.errorAcceptReject));
            return;
        }

        if (mIBisConfirm.isChecked()) {

            double minAmount = mConfig.getMinimumAmount() + aTransaction.getTrans_amount();
            if (mConfig.getBalance() <  minAmount) {
                MyTools.sharedInstance().displayAlert(DetailTransActivity.this,getString(R.string.errorBalanceTrans));
                return;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Transaction");
        builder.setMessage(R.string.errorEndedTrans);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (mIBisOther.isChecked()) {
                    aTransaction.setTrans_avis(mIBOtherText.getText().toString());
                } else if (mIBisInterlo.isChecked()) {
                    aTransaction.setTrans_avis("interlocuteur"); //l'interlocuteur était absent
                } else if (mIBisCompliant.isChecked()) {
                    aTransaction.setTrans_avis("conformite"); //le produit vendu ou echangé n'était pas conforme à l'annonce
                } else if (mIBisMyAbsent.isChecked()) {
                    aTransaction.setTrans_avis("absence"); //Je n'ai pu etre au rendez-vous
                }

                if (mIBisCancel.isChecked()) {
                    aTransaction.setTrans_valid(1); //La transaction a été annulée
                } else if (mIBisConfirm.isChecked()) {
                    aTransaction.setTrans_valid(2); //La transaction est confirmée
                }

                aTransaction.setTrans_note(mStar);

                if (mStar > 0) {
                    mConfig.setUser_countNote(mConfig.getUser_countNote()+1);
                    mConfig.setUser_note((mConfig.getUser_note()+mStar)/mConfig.getUser_countNote());
                    MDBUser.sharedInstance().setUpdUserStar(DetailTransActivity.this, mConfig, aTransaction, new MDBInterface() {
                        @Override
                        public void completionHandler(Boolean success,final String errorString) {
                           if (success) {
                               //ok
                           } else {
                               MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                   @Override
                                   public void completionUpdateMain() {

                                       //Todo action alert
                                       MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                   }
                               });
                           }
                        }
                    });
                }

                MDBTransact.sharedInstance().setUpdateTransaction(DetailTransActivity.this, aTransaction, new MDBInterface() {
                    @Override
                    public void completionHandler(Boolean success, final String errorString) {

                        if (success) {
                            MDBTransact.sharedInstance().getAllTransactions(DetailTransActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                @Override
                                public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                                    if (success) {
                                        Transactions.sharedInstance().setTransactionArray(anArray);
                                    } else {
                                        MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                            @Override
                                            public void completionUpdateMain() {

                                                //Todo action alert
                                                MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

                                            }
                                        });
                                    }
                                }
                            });

                            mConfig.setTrans_badge(mConfig.getTrans_badge()-1);
                            mConfig.setTransaction_maj(true);

                            if (aTransaction.getTrans_type() == 1 && aTransaction.getTrans_valid() == 2
                                    && aTransaction.getClient_id() ==  aTransaction.getProprietaire()) {
                                //Cas où le client confirme la transaction commerciale. Alors son compte est debité produit + la commission
                                tradeConfirmTransact();
                            } else if (aTransaction.getTrans_type() == 1 && aTransaction.getTrans_valid() == 1
                                    && aTransaction.getClient_id() == aTransaction.getProprietaire()) {
                                //Cas où le client annule la transaction commerciale. alors son compte n'est pas debité et le compteur rejet est incrémenté 
                                tradeCancelTransact();
                            } else if (aTransaction.getTrans_type() == 2 && aTransaction.getTrans_valid() == 2) {
                                //Cas où l'utilisateur confirme la transaction d'echange. Alors son compte est debité de la commission 
                                exchangeConfirmTransact();
                            }  else if (aTransaction.getTrans_type() == 2 && aTransaction.getTrans_valid() == 1) {
                                //Cas où l'utilisateur annule la transaction d'echange. alors son compte n'est pas debité et le compteur rejet est incrémenté
                                exchangeCancelTransact();
                            }

                        } else {
                            MyTools.sharedInstance().performUIUpdatesOnMain(DetailTransActivity.this, new MainThreadInterface() {
                                @Override
                                public void completionUpdateMain() {

                                    //Todo action alert
                                    MyTools.sharedInstance().displayAlert(DetailTransActivity.this, errorString);

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
        MyTools.sharedInstance().showHelp("DetailTransViewController", this);

    }

    public void actionCloseWidows(View view) {

        finish();
    }



    private Transaction getTransaction(int transId) {


        if (transId == 0) {
            return null;
        } else {
            try {

                JSONArray transactionsJSON = Transactions.sharedInstance().getTransactionArray();

                for (int i = 0; i < transactionsJSON.length(); i++) {

                    JSONObject dictionary = transactionsJSON.getJSONObject(i);
                    Transaction transaction = new Transaction(dictionary);
                    if (transaction.getTrans_id() == transId) {
                        return  transaction;
                    }

                }

            }  catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return null;



    }



}
