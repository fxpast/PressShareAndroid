package com.prv.gootoor.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.prv.gootoor.R;
import com.prv.gootoor.daos.MDBCard;
import com.prv.gootoor.daos.MDBCreneau;
import com.prv.gootoor.daos.MDBInterface;
import com.prv.gootoor.daos.MDBInterfaceArray;
import com.prv.gootoor.lists.Cards;
import com.prv.gootoor.lists.Creneaux;
import com.prv.gootoor.lists.Transactions;
import com.prv.gootoor.models.Card;
import com.prv.gootoor.models.Creneau;
import com.prv.gootoor.models.Product;
import com.prv.gootoor.models.Transaction;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;
import com.prv.gootoor.views.CustomItemListCard;
import com.prv.gootoor.views.CustomItemListCren;
import com.prv.gootoor.views.CustomItemListTrans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Description : Add slots for product
 *
 * Created by roger on 29/04/2018.
 */

public class ListCreneauxActivity extends AppCompatActivity {

    private Config mConfig = Config.sharedInstance();
    private ListView mIBListCreneauxView;
    private List<Object> mCreneaux;
    private ImageView mIBAddCreneau;
    private int prod_id;
    private Boolean isClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_crenaux);

        isClient  =  getIntent().getBooleanExtra(mConfig.getDomaineApp()+"isClient",false);
        prod_id  =  getIntent().getIntExtra(mConfig.getDomaineApp()+"prod_id",0);



        mCreneaux = new ArrayList<>();

        mIBAddCreneau = (ImageView) findViewById(R.id.IBAddCreneau);
        mIBListCreneauxView = (ListView) findViewById(R.id.IBListCreneauxView);

        if (isClient) {

            mIBAddCreneau = (ImageView) MyTools.sharedInstance().activerObjet(mIBAddCreneau, false);

        }

        mIBListCreneauxView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                    if (!isClient && mConfig.getLevel() > 0) {

                    deleteRow(position);

                }


            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        initColor();

        if (mConfig.getLevel() <= 0) {

            mIBAddCreneau = (ImageView) MyTools.sharedInstance().activerObjet(mIBAddCreneau, false);

        } else {

            mIBAddCreneau = (ImageView) MyTools.sharedInstance().activerObjet(mIBAddCreneau, true);

        }



        actionListCreneaux();

    }



    private void deleteRow(final int position) {


        final Creneau creneau1 = (Creneau) mCreneaux.get(position);

        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(ListCreneauxActivity.this);
        builder.setTitle(getString(R.string.delete));
        builder.setMessage(getString(R.string.deleteRow));

        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MDBCreneau.sharedInstance().setDeleteCreneau(ListCreneauxActivity.this, creneau1, new MDBInterface() {
                    @Override
                    public void completionHandler(Boolean success, final String errorString) {
                        if (success) {

                            JSONArray creneauxJSON = Creneaux.sharedInstance().getCreneauxArray();

                            try {

                                for (int i = 0; i < creneauxJSON.length() ; i++) {

                                    JSONObject dictionary = creneauxJSON.getJSONObject(i);
                                    Creneau creneau2 = new Creneau(ListCreneauxActivity.this, dictionary);
                                    if (creneau2.getCre_id() == creneau1.getCre_id()) {
                                        mCreneaux.remove(position);
                                        Creneaux.sharedInstance().getCreneauxArray().remove(i);
                                        break;
                                    }

                                }

                                ListAdapter mAdapter = new CustomItemListCren(ListCreneauxActivity.this,
                                        R.layout.list_creneaux_item, mCreneaux);
                                mIBListCreneauxView.setAdapter(mAdapter);


                            }  catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            MyTools.sharedInstance().performUIUpdatesOnMain(ListCreneauxActivity.this, new MainThreadInterface() {
                                @Override
                                public void completionUpdateMain() {

                                    MyTools.sharedInstance().displayAlert(ListCreneauxActivity.this, errorString);

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



    private void initColor() {



        ConstraintLayout mIBListCrenBackgr;
        mIBListCrenBackgr = (ConstraintLayout) findViewById(R.id.IBListCrenBackgr);

        String hexColor = "#" + mConfig.getColorApp();
        mIBListCrenBackgr.setBackgroundColor(Color.parseColor(hexColor));

        TextView mIBEntDateDebut = (TextView) findViewById(R.id.IBEntDateDebut);
        TextView mIBEntDateFin = (TextView) findViewById(R.id.IBEntDateFin);
        TextView mIBEntTapAloc = (TextView) findViewById(R.id.IBEntTapAloc);
        TextView mIBEntRepeat = (TextView) findViewById(R.id.IBEntRepeat);

        hexColor = "#" + mConfig.getColorAppLabel();
        mIBEntDateDebut.setTextColor(Color.parseColor(hexColor));
        mIBEntDateFin.setTextColor(Color.parseColor(hexColor));
        mIBEntTapAloc.setTextColor(Color.parseColor(hexColor));
        mIBEntRepeat.setTextColor(Color.parseColor(hexColor));

    }


    private void actionListCreneaux() {

        mCreneaux.clear();


        MDBCreneau.sharedInstance().getCreneauxProd(ListCreneauxActivity.this, prod_id, new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                if (success) {
                    try {

                        Creneaux.sharedInstance().setCreneauxArray(anArray);
                        JSONArray creneauxJSON = Creneaux.sharedInstance().getCreneauxArray();

                        try {

                            for (int i = 0; i < creneauxJSON.length() ; i++) {

                                JSONObject dictionary = creneauxJSON.getJSONObject(i);
                                Creneau creneau = new Creneau(ListCreneauxActivity.this, dictionary);
                                mCreneaux.add(i, creneau);

                            }

                        }  catch (JSONException e) {
                            e.printStackTrace();
                        }



                        MyTools.sharedInstance().performUIUpdatesOnMain(ListCreneauxActivity.this, new MainThreadInterface() {

                            @Override
                            public void completionUpdateMain() {

                                ListAdapter mAdapter = new CustomItemListCren(ListCreneauxActivity.this, R.layout.list_creneaux_item, mCreneaux);
                                mIBListCreneauxView.setAdapter(mAdapter);

                            }
                        });


                    }  catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {


                    MyTools.sharedInstance().performUIUpdatesOnMain(ListCreneauxActivity.this, new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(ListCreneauxActivity.this, errorString);

                        }
                    });

                }
            }
        });








    }

    public void actionCloseWidows(View view) {

        finish();
    }

    public void actionListAddCreneau(View view) {

        if (!isClient) {

            Intent intent = new Intent(getBaseContext(), AddCreneauActivity.class);
            intent.putExtra(mConfig.getDomaineApp()+"prod_id", prod_id);
            startActivity(intent);
        }




    }

    public void actionListCrenauxHelp(View view) {

        //Todo Tuto_Presentation
        MyTools.sharedInstance().showHelp("creneaux", this);

    }

}
