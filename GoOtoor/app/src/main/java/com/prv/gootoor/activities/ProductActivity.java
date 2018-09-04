package com.prv.gootoor.activities;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;


import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.prv.gootoor.R;
import com.prv.gootoor.daos.MDBInterface;
import com.prv.gootoor.daos.MDBInterfaceArray;
import com.prv.gootoor.daos.MDBProduct;
import com.prv.gootoor.lists.Messages;
import com.prv.gootoor.lists.Products;
import com.prv.gootoor.lists.Transactions;
import com.prv.gootoor.models.Message;
import com.prv.gootoor.models.Product;
import com.prv.gootoor.models.Transaction;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;
import com.prv.gootoor.utils.SaveImageInterface;
import com.readystatesoftware.viewbadger.BadgeView;
import com.stfalcon.contentmanager.ContentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.UUID;

public class  ProductActivity extends AppCompatActivity implements ContentManager.PickContentListener  {

    private ContentManager contentManager;
    private Config mConfig = Config.sharedInstance();
    private Product aProduct = null;
    private ImageView mIBProdAddImage;
    private ImageView mIBProdStar1;
    private ImageView mIBProdStar2;
    private ImageView mIBProdStar3;
    private ImageView mIBProdStar4;
    private ImageView mIBProdStar5;
    private ImageView mIBProdMess;
    private EditText mIBNom;
    private EditText mIBComment;
    private EditText mIBPrix;
    private Switch mIBEchangeChoice;
    private Boolean isClient;
    private Button mIBProdValid;
    private Button mIBAddCreneau;
    private Button mIBProdTransact;

    private int mStar;
    private Transaction aTransaction;
    private Boolean isMajImage = false;
    private int mTypeSListe = 0; //List :0, MyList :1, Historical:2
    private int mTypeListe = 0; //Map :0, List :1, Settings:2



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        mIBProdAddImage = (ImageView) findViewById(R.id.IBProdAddImage);
        mIBProdStar1 = (ImageView) findViewById(R.id.IBProdStar1);
        mIBProdStar2 = (ImageView) findViewById(R.id.IBProdStar2);
        mIBProdStar3 = (ImageView) findViewById(R.id.IBProdStar3);
        mIBProdStar4 = (ImageView) findViewById(R.id.IBProdStar4);
        mIBProdStar5 = (ImageView) findViewById(R.id.IBProdStar5);
        mIBProdMess = (ImageView) findViewById(R.id.IBProdMess);
        mIBEchangeChoice = (Switch) findViewById(R.id.IBEchangeChoice);
        mIBProdValid = (Button) findViewById(R.id.IBProdValid);
        mIBAddCreneau = (Button) findViewById(R.id.IBAddCreneau);
        mIBProdTransact = (Button) findViewById(R.id.IBProdTransact);
        mIBNom = (EditText) findViewById(R.id.IBNom);
        mIBComment = (EditText) findViewById(R.id.IBComment);
        mIBPrix = (EditText) findViewById(R.id.IBPrix);

        contentManager = new ContentManager(this, this);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        isClient = false;
        int prodId  =  getIntent().getIntExtra(mConfig.getDomaineApp()+"prod_id",0);
        mTypeListe  =  getIntent().getIntExtra(mConfig.getDomaineApp()+"typeListe",0);
        mTypeSListe  =  getIntent().getIntExtra(mConfig.getDomaineApp()+"typeSListe",0);
        aProduct = getProduct(prodId);

        if (aProduct == null) {
            mIBAddCreneau = (Button) MyTools.sharedInstance().activerObjet(mIBAddCreneau, false);
        }

        fillTheForm();


    }


    @Override
    protected void onStart() {
        super.onStart();

        initColor();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mConfig.getIsReturnToTab()) {
            finish();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        contentManager.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        contentManager.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        contentManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        contentManager.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public void onContentLoaded(Uri uri, String contentType) {

        if (contentType.equals(ContentManager.Content.IMAGE.toString())) {

            ImageLoader.getInstance().displayImage(uri.toString(), mIBProdAddImage);
            mIBProdAddImage.setTag("");
            isMajImage = true;


        }


    }

    @Override
    public void onError(String error) {

    }


    @Override
    public void onLoadContentProgress(int loadPercent) {

    }

    @Override
    public void onCanceled() {

    }


    private void initColor() {


        TextView mIBEchangeLabel;
        mIBEchangeLabel = (TextView) findViewById(R.id.IBEchangeLabel);

        TextView mIBEtat;
        mIBEtat = (TextView) findViewById(R.id.IBEtat);

        ConstraintLayout mIBProdBackgr;
        ConstraintLayout mIBContProdBackgr;

        mIBAddCreneau.setPaintFlags(mIBAddCreneau.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mIBContProdBackgr = (ConstraintLayout) findViewById(R.id.IBContProdBackgr);
        mIBProdBackgr = (ConstraintLayout) findViewById(R.id.IBProdBackgr);

        String hexColor = "#" + mConfig.getColorApp();
        mIBContProdBackgr.setBackgroundColor(Color.parseColor(hexColor));
        mIBProdBackgr.setBackgroundColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppBt();
        mIBAddCreneau.setTextColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppLabel();
        mIBEchangeLabel.setTextColor(Color.parseColor(hexColor));
        mIBEtat.setTextColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppPlHd();
        mIBNom.setHintTextColor(Color.parseColor(hexColor));
        mIBPrix.setHintTextColor(Color.parseColor(hexColor));
        mIBComment.setHintTextColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppText();
        mIBNom.setTextColor(Color.parseColor(hexColor));
        mIBPrix.setTextColor(Color.parseColor(hexColor));
        mIBComment.setTextColor(Color.parseColor(hexColor));


    }


    public void actionProdHelp(View view) {

        //Todo Tuto_Presentation
        MyTools.sharedInstance().showHelp("informations_article", ProductActivity.this);

    }

    public  void actionMessage(View view) {


        mIBProdMess = (ImageView) MyTools.sharedInstance().activerObjet(mIBProdMess, false);

        Intent intent = new Intent(this, DetailMessageActivity.class);
        intent.putExtra(mConfig.getDomaineApp()+"prod_id",aProduct.getProd_id());
        intent.putExtra(mConfig.getDomaineApp()+"typeSListe",mTypeSListe);
        startActivity(intent);



    }

    public void actionCloseWidows(View view) {

        finish();
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


    public void actionListCreneau(View view) {


        Intent intent = new Intent(this, ListCreneauxActivity.class);
        intent.putExtra(mConfig.getDomaineApp()+"isClient", isClient.booleanValue());
        intent.putExtra(mConfig.getDomaineApp()+"prod_id",aProduct.getProd_id());
        startActivity(intent);


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

        mIBProdStar1.setImageResource(R.drawable.whitestar);
        mIBProdStar2.setImageResource(R.drawable.whitestar);
        mIBProdStar3.setImageResource(R.drawable.whitestar);
        mIBProdStar4.setImageResource(R.drawable.whitestar);
        mIBProdStar5.setImageResource(R.drawable.whitestar);

        mIBProdStar1.setTag("whitestar");
        mIBProdStar2.setTag("whitestar");
        mIBProdStar3.setTag("whitestar");
        mIBProdStar4.setTag("whitestar");
        mIBProdStar5.setTag("whitestar");

    }

    public void actionProdStar1(View view) {

        mStar = 1;
        initStar();
        changeStar((ImageView) view);

    }


    public void actionProdStar2(View view) {

        mStar = 2;
        initStar();
        changeStar((ImageView) view);
        changeStar(mIBProdStar1);

    }


    public void actionProdStar3(View view) {

        mStar = 3;
        initStar();
        changeStar((ImageView) view);
        changeStar(mIBProdStar2);
        changeStar(mIBProdStar1);

    }


    public void actionProdStar4(View view) {

        mStar = 4;
        initStar();
        changeStar((ImageView) view);
        changeStar(mIBProdStar3);
        changeStar(mIBProdStar2);
        changeStar(mIBProdStar1);

    }


    public void actionProdStar5(View view) {

        mStar = 5;
        initStar();
        changeStar((ImageView) view);
        changeStar(mIBProdStar4);
        changeStar(mIBProdStar3);
        changeStar(mIBProdStar2);
        changeStar(mIBProdStar1);

    }




    public void actionAddImage(View view) {

        if (!isClient && mConfig.getLevel() > 0) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getText(R.string.takePicture));

            final CharSequence mChoice[] = { getString(R.string.library), getString(R.string.cameraPhoto) };

            builder.setMultiChoiceItems(mChoice, null, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    if (which == 0) {
                        //load image gallery

                        contentManager.pickContent(ContentManager.Content.IMAGE);


                    } else  if (which == 1) {

                        //take image camera
                        contentManager.takePhoto();

                    }

                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();


        }

    }

    public void actionProdValid(View view) {

        if (mIBNom.length() == 0) {

            MyTools.sharedInstance().displayAlert(ProductActivity.this,getString(R.string.ErrorDescription));
            return;
        }

        if (mIBPrix.length() == 0) {

            MyTools.sharedInstance().displayAlert(ProductActivity.this,getString(R.string.ErrorPrice));
            return;
        }

        if (!mIBEchangeChoice.isChecked() &&  (mIBPrix.length() == 0 || mIBPrix.getText().toString().equals("0") || mIBPrix.getText().toString().equals("0.0"))) {

            mIBEchangeChoice.setChecked(true);
        }

        if (isClient) {

            actionClient();

        } else {
            actionUser();
        }

    }


    private void actionClient(){

        Intent intent = new Intent(this, CreateTransActivity.class);
        intent.putExtra(mConfig.getDomaineApp()+"prod_id",aProduct.getProd_id());
        intent.putExtra(mConfig.getDomaineApp()+"typeSListe",mTypeSListe);
        startActivity(intent);


    }


    private void actionUser() {


        mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, false);
        mIBAddCreneau = (Button) MyTools.sharedInstance().activerObjet(mIBAddCreneau, false);


        Product product = new Product(this, null);

        if (aProduct != null) {
            product.setProd_imageUrl(product.getProd_imageUrl().equals("") ? "xxxxxxx" : aProduct.getProd_imageUrl());
        }

        if (mIBProdAddImage.getTag().equals("noimage")) {
            product.setProd_imageUrl("");
        } else {

            if (isMajImage) {
                product.setProdImageOld(product.getProd_imageUrl());
                product.setProd_imageUrl("photo-"+ mConfig.getUser_id()+ UUID.randomUUID().toString());
                product.setProd_image(mIBProdAddImage);
            }

        }

        if (aProduct != null) {
            product.setProd_id(aProduct.getProd_id());
        }

        product.setProd_prix(Double.valueOf(mIBPrix.getText().toString()));
        product.setProd_nom(mIBNom.getText().toString());
        product.setProd_by_user(mConfig.getUser_id());
        product.setProd_comment(mIBComment.getText().toString());
        product.setProd_etat(mStar);
        product.setProd_hidden(false);
        product.setProd_closed(false);
        product.setProd_echange(mIBEchangeChoice.isChecked());

        if (aProduct != null) {
            //update product

            MDBProduct.sharedInstance().setUpdateProduct(this, "Product", product, new MDBInterface() {
                @Override
                public void completionHandler(Boolean success,final String errorString) {

                    if (success) {

                        //List :0, MyList:1, Historical:2
                        if (mTypeSListe == 1) {

                            MDBProduct.sharedInstance().getProductsByUser(ProductActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                @Override
                                public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                                    if (success) {
                                        try {

                                            Products.sharedInstance().setProductsUserArray(anArray);

                                            MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                                                @Override
                                                public void completionUpdateMain() {

                                                    mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, true);
                                                    mIBAddCreneau = (Button) MyTools.sharedInstance().activerObjet(mIBAddCreneau, true);

                                                    finish();
                                                    return;
                                                }
                                            });


                                        }  catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {


                                        MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                                            @Override
                                            public void completionUpdateMain() {

                                                MyTools.sharedInstance().displayAlert(ProductActivity.this, errorString);

                                            }
                                        });

                                    }
                                }
                            });


                        } else if (mTypeSListe == 0) {
                            MDBProduct.sharedInstance().getProductsByCoord(ProductActivity.this, mConfig.getUser_id(), mConfig.getMinLongitude(), mConfig.getMaxLongitude(), mConfig.getMinLatitude(), mConfig.getMaxLatitude(), new MDBInterfaceArray() {
                                @Override
                                public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {


                                    if (success) {
                                        try {

                                            Products.sharedInstance().setProductsArray(anArray);

                                            MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                                                @Override
                                                public void completionUpdateMain() {

                                                    mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, true);
                                                    mIBAddCreneau = (Button) MyTools.sharedInstance().activerObjet(mIBAddCreneau, true);

                                                    finish();
                                                    return;
                                                }
                                            });


                                        }  catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {


                                        MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                                            @Override
                                            public void completionUpdateMain() {
                                                MyTools.sharedInstance().displayAlert(ProductActivity.this, errorString);

                                            }
                                        });

                                    }
                                }
                            });
                        } else if (mTypeSListe == 2) {
                            MDBProduct.sharedInstance().getProductsByTrader(ProductActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                @Override
                                public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {


                                    if (success) {
                                        try {

                                            Products.sharedInstance().setProductsTraderArray(anArray);

                                            MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                                                @Override
                                                public void completionUpdateMain() {

                                                    mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, true);
                                                    mIBAddCreneau = (Button) MyTools.sharedInstance().activerObjet(mIBAddCreneau, true);

                                                    finish();
                                                    return;
                                                }
                                            });


                                        }  catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {


                                        MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                                            @Override
                                            public void completionUpdateMain() {
                                                MyTools.sharedInstance().displayAlert(ProductActivity.this, errorString);

                                            }
                                        });

                                    }


                                }
                            });
                        }

                    } else {

                        MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                            @Override
                            public void completionUpdateMain() {

                                mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, true);
                                mIBAddCreneau = (Button) MyTools.sharedInstance().activerObjet(mIBAddCreneau, true);

                                MyTools.sharedInstance().displayAlert(ProductActivity.this, errorString);

                            }
                        });
                    }

                }
            });

        } else {
            //add product
            MDBProduct.sharedInstance().setAddProduct(this, product, new MDBInterface() {
                @Override
                public void completionHandler(Boolean success, final String errorString) {


                    if (success) {

                        //List :0, MyList:1, Historical:2
                        if (mTypeSListe == 1) {

                            MDBProduct.sharedInstance().getProductsByUser(ProductActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                @Override
                                public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                                    if (success) {
                                        try {

                                            Products.sharedInstance().setProductsUserArray(anArray);

                                            MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                                                @Override
                                                public void completionUpdateMain() {

                                                    mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, true);
                                                    mIBAddCreneau = (Button) MyTools.sharedInstance().activerObjet(mIBAddCreneau, true);


                                                    finish();
                                                    return;
                                                }
                                            });


                                        }  catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {


                                        MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                                            @Override
                                            public void completionUpdateMain() {

                                                MyTools.sharedInstance().displayAlert(ProductActivity.this, errorString);

                                            }
                                        });

                                    }
                                }
                            });


                        } else if (mTypeSListe == 0) {
                            MDBProduct.sharedInstance().getProductsByCoord(ProductActivity.this, mConfig.getUser_id(), mConfig.getMinLongitude(), mConfig.getMaxLongitude(), mConfig.getMinLatitude(), mConfig.getMaxLatitude(), new MDBInterfaceArray() {
                                @Override
                                public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {


                                    if (success) {
                                        try {

                                            Products.sharedInstance().setProductsArray(anArray);

                                            MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                                                @Override
                                                public void completionUpdateMain() {

                                                    mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, true);
                                                    mIBAddCreneau = (Button) MyTools.sharedInstance().activerObjet(mIBAddCreneau, true);

                                                    finish();
                                                    return;
                                                }
                                            });


                                        }  catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {


                                        MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                                            @Override
                                            public void completionUpdateMain() {
                                                MyTools.sharedInstance().displayAlert(ProductActivity.this, errorString);

                                            }
                                        });

                                    }
                                }
                            });
                        } else if (mTypeSListe == 2) {
                            MDBProduct.sharedInstance().getProductsByTrader(ProductActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                                @Override
                                public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {


                                    if (success) {
                                        try {

                                            Products.sharedInstance().setProductsTraderArray(anArray);

                                            MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                                                @Override
                                                public void completionUpdateMain() {

                                                    mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, true);
                                                    mIBAddCreneau = (Button) MyTools.sharedInstance().activerObjet(mIBAddCreneau, true);


                                                    finish();
                                                    return;
                                                }
                                            });


                                        }  catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {


                                        MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                                            @Override
                                            public void completionUpdateMain() {
                                                MyTools.sharedInstance().displayAlert(ProductActivity.this, errorString);

                                            }
                                        });

                                    }


                                }
                            });
                        }

                    } else {

                        MyTools.sharedInstance().performUIUpdatesOnMain(ProductActivity.this, new MainThreadInterface() {

                            @Override
                            public void completionUpdateMain() {

                                mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, true);
                                mIBAddCreneau = (Button) MyTools.sharedInstance().activerObjet(mIBAddCreneau, true);


                                MyTools.sharedInstance().displayAlert(ProductActivity.this, errorString);

                            }
                        });
                    }



                }
            });
        }

    }


    private void fillTheForm() {

        if (aProduct != null) {

            if (!aProduct.getProd_closed()) {

                if (Transactions.sharedInstance().getTransactionArray() != null) {

                    try {

                        for (int i = 0; i < Transactions.sharedInstance().getTransactionArray().length() ; i++) {

                            JSONObject dictionary = Transactions.sharedInstance().getTransactionArray().getJSONObject(i);
                            Transaction tran = new Transaction( dictionary);
                            if (tran.getProd_id() == aProduct.getProd_id()) {
                                //0 : La transaction en cours. 1 : La transaction a été annulée. 2 : La transaction est confirmée.
                                if (tran.getTrans_valid() == 0) {

                                    mIBProdTransact.setVisibility(View.VISIBLE);


                                }
                            }
                        }

                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }


            mIBProdMess = (ImageView) MyTools.sharedInstance().activerObjet(mIBProdMess, false);

            if (mConfig.getLevel() <= 0) {

                mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, false);

            } else {

                mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, true);

                if (Messages.sharedInstance().getMessagesArray() != null) {

                    try {

                        for (int i = 0; i < Messages.sharedInstance().getMessagesArray().length() ; i++) {

                            JSONObject dictionary = Messages.sharedInstance().getMessagesArray().getJSONObject(i);
                            Message message = new Message(ProductActivity.this, dictionary);
                            if (message.getProduct_id() == aProduct.getProd_id()) {

                                mIBProdMess = (ImageView) MyTools.sharedInstance().activerObjet(mIBProdMess, true);
                                break;
                            }
                        }

                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            MyTools.sharedInstance().saveImageArchive(this, aProduct.getProd_imageUrl(),  "images/", new SaveImageInterface() {
                @Override
                public void completionHandlerSaveImage(Boolean success, Drawable drawable) {

                    if (!success) {

                        mIBProdAddImage.setTag("noimage");
                        Log.v(mConfig.getDomaineApp()+"ProductActivity", "error image");

                    } else {
                        mIBProdAddImage.setTag("");
                    }

                    mIBProdAddImage.setImageDrawable(drawable);

                }
            });

            mStar = aProduct.getProd_etat();
            if (mStar == 1) {
                actionProdStar1(mIBProdStar1);
            } else if (mStar == 2) {
                actionProdStar2(mIBProdStar2);
            } else if (mStar == 3) {
                actionProdStar3(mIBProdStar3);
            } else if (mStar == 4) {
                actionProdStar4(mIBProdStar4);
            } else if (mStar == 5) {
                actionProdStar5(mIBProdStar5);
            }

            mIBEchangeChoice.setChecked(aProduct.getProd_echange());
            mIBNom.setText(aProduct.getProd_nom());
            mIBPrix.setText(aProduct.getProd_prix().toString());
            mIBComment.setText(aProduct.getProd_comment());

            mConfig.setMapString(aProduct.getProd_mapString());
            mConfig.setLatitude(aProduct.getProd_latitude());
            mConfig.setLongitude(aProduct.getProd_longitude());

            if (mConfig.getUser_id() == aProduct.getProd_by_user()) {
                isClient = false;
                if (!aProduct.getProd_closed()) {
                    setUIEnabled(true);
                } else {

                    mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, false);
                    setUIEnabled(false);
                }
            } else {

                if (mConfig.getLevel() <= 0) {

                    isClient = false;

                } else  {

                    isClient = true;
                }

                if (aProduct.getProd_closed()) {
                    mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, false);

                }
                setUIEnabled(false);
            }

        } else {

            mIBProdAddImage.setImageResource(R.drawable.noimage);
            mIBProdAddImage.setTag("noimage");
            mIBProdMess = (ImageView) MyTools.sharedInstance().activerObjet(mIBProdMess, false);
            mIBPrix.setText("0");
            mIBEchangeChoice.setChecked(true);

        }

        if (isClient) {
            mIBProdValid.setText(getString(R.string.exchangeBuy));

        } else {

            mIBProdValid.setText(getString(R.string.save));
        }


        if (aProduct != null) {
            if (!aProduct.getProd_closed()) {
                Boolean isRunning = false;
                if (Transactions.sharedInstance().getTransactionArray() != null) {

                    try {

                        for (int i = 0; i < Transactions.sharedInstance().getTransactionArray().length() ; i++) {

                            JSONObject dictionary = Transactions.sharedInstance().getTransactionArray().getJSONObject(i);
                            Transaction tran = new Transaction( dictionary);
                            if (tran.getProd_id() == aProduct.getProd_id()) {
                                //0 : La transaction en cours. 1 : La transaction a été annulée. 2 : La transaction est confirmée.
                                if (tran.getTrans_valid() == 0) {

                                    isRunning = true;

                                }

                                if (tran.getTrans_valid() == 0 || tran.getTrans_valid() == 2)  {
                                    aTransaction = tran;
                                    mIBProdValid = (Button) MyTools.sharedInstance().activerObjet(mIBProdValid, false);

                                }

                            }
                        }

                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                if  (mIBProdTransact.getVisibility() == View.VISIBLE  && !isRunning) {
                    mIBProdTransact.setVisibility(View.GONE);

                }


            }
        }


        }


    private void setUIEnabled(Boolean enabled){

        mIBNom = (EditText) MyTools.sharedInstance().activerObjet(mIBNom, enabled);
        mIBComment = (EditText) MyTools.sharedInstance().activerObjet(mIBComment, enabled);
        mIBPrix = (EditText) MyTools.sharedInstance().activerObjet(mIBPrix, enabled);
        mIBProdStar1 = (ImageView) MyTools.sharedInstance().activerObjet(mIBProdStar1, enabled);
        mIBProdStar2 = (ImageView) MyTools.sharedInstance().activerObjet(mIBProdStar2, enabled);
        mIBProdStar3 = (ImageView) MyTools.sharedInstance().activerObjet(mIBProdStar3, enabled);
        mIBProdStar4 = (ImageView) MyTools.sharedInstance().activerObjet(mIBProdStar4, enabled);
        mIBProdStar5 = (ImageView) MyTools.sharedInstance().activerObjet(mIBProdStar5, enabled);
        mIBEchangeChoice = (Switch) MyTools.sharedInstance().activerObjet(mIBEchangeChoice, enabled);


        if (aProduct == null) {
            mIBAddCreneau = (Button) MyTools.sharedInstance().activerObjet(mIBAddCreneau, enabled);
        }

    }


    public void actionTransact(View view) {

        Intent intent = new Intent(this, DetailTransActivity.class);
        intent.putExtra(mConfig.getDomaineApp()+"trans_id",aTransaction.getTrans_id());
        startActivity(intent);
    }



}
