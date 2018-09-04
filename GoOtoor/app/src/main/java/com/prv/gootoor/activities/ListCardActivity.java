package com.prv.gootoor.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.prv.gootoor.R;
import com.prv.gootoor.daos.MDBCard;
import com.prv.gootoor.daos.MDBInterface;
import com.prv.gootoor.daos.MDBInterfaceArray;
import com.prv.gootoor.daos.MDBTypeCard;
import com.prv.gootoor.lists.Cards;

import com.prv.gootoor.lists.TypeCards;
import com.prv.gootoor.models.Card;

import com.prv.gootoor.models.TypeCard;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;
import com.prv.gootoor.views.CustomItemListCard;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roger on 25/10/2017.
 */

public class ListCardActivity extends AppCompatActivity {

    private Config mConfig = Config.sharedInstance();
    private ListView mIBListCardListView;
    private List<Object> mCards;
    private Handler myQueue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_card);

        mIBListCardListView = (ListView) findViewById(R.id.IBListCardListView);
        mCards = new ArrayList<>();





        mIBListCardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ListCardActivity.this);
                builder.setTitle(getText(R.string.makeYourChoice));

                final CharSequence mChoice[] = { getString(R.string.defaultCB), getString(R.string.deleteCB) };

                builder.setMultiChoiceItems(mChoice, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        if (which == 0) {
                            //checked default CB

                            checkedRow(position);

                        } else  if (which == 1) {

                            //delete CB
                            deleteRow(position);
                        }

                        dialog.dismiss();
                    }
                });

               AlertDialog dialog = builder.create();
               dialog.show();

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();


        initColor();

        if (Cards.sharedInstance().getCardsArray().length() > 0) {

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




    private void initColor() {



        ConstraintLayout mIBListCardBackgr;
        mIBListCardBackgr = (ConstraintLayout) findViewById(R.id.IBListCardBackgr);

        TextView mIBCBLabel = (TextView) findViewById(R.id.IBCBLabel);
        TextView mIBInfoLabel = (TextView) findViewById(R.id.IBInfoLabel);

        String hexColor = "#" + mConfig.getColorApp();
        mIBListCardBackgr.setBackgroundColor(Color.parseColor(hexColor));

        hexColor = "#" + mConfig.getColorAppLabel();
        mIBCBLabel.setTextColor(Color.parseColor(hexColor));
        mIBInfoLabel.setTextColor(Color.parseColor(hexColor));





    }


    public void actionCloseWidows(View view) {

        finish();
    }

    public void actionListCardHelp(View view) {

        //Todo Tuto_Presentation
        MyTools.sharedInstance().showHelp("ListCardViewController", ListCardActivity.this);

    }



    public void actionAddCard(View view) {

        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);

    }

    private void checkedRow(final int position) {


        for (int i = 0; i <mCards.size() ; i++) {

            if (i == position) {
                ((Card) mCards.get(i)).setMain_card(true);

            } else {
                ((Card) mCards.get(i)).setMain_card(false);
            }

            MDBCard.sharedInstance().setUpdateCard(ListCardActivity.this, ((Card) mCards.get(i)), new MDBInterface() {
                @Override
                public void completionHandler(final Boolean success, final String errorString) {

                    MyTools.sharedInstance().performUIUpdatesOnMain(ListCardActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            if (success) {

                                while (Cards.sharedInstance().getCardsArray().length()>0) {

                                    Cards.sharedInstance().getCardsArray().remove(0);
                                }

                                ListAdapter mAdapter = new CustomItemListCard(ListCardActivity.this,
                                        R.layout.list_card_item, mCards);
                                mIBListCardListView.setAdapter(mAdapter);

                            } else {

                                MyTools.sharedInstance().displayAlert(ListCardActivity.this, errorString);

                            }

                        }
                    });


                }
            });



        }

    }



        private void deleteRow(final int position) {


        final Card card1 = (Card) mCards.get(position);

        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(ListCardActivity.this);
        builder.setTitle(getString(R.string.delete));
        builder.setMessage(getString(R.string.deleteRow) + " " + card1.getCard_lastNumber());

        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                MDBCard.sharedInstance().setDeleteCard(ListCardActivity.this, card1, new MDBInterface() {
                    @Override
                    public void completionHandler(Boolean success, final String errorString) {
                        if (success) {

                            JSONArray cardsJSON = Cards.sharedInstance().getCardsArray();

                            try {

                                for (int i = 0; i < cardsJSON.length() ; i++) {

                                    JSONObject dictionary = cardsJSON.getJSONObject(i);
                                    Card card2 = new Card(ListCardActivity.this, dictionary);
                                    if (card2.getCard_id() == card1.getCard_id()) {
                                        mCards.remove(position);
                                        Cards.sharedInstance().getCardsArray().remove(i);
                                        break;
                                    }

                                }

                                ListAdapter mAdapter = new CustomItemListCard(ListCardActivity.this,
                                        R.layout.list_card_item, mCards);
                                mIBListCardListView.setAdapter(mAdapter);


                            }  catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            MyTools.sharedInstance().performUIUpdatesOnMain(ListCardActivity.this, new MainThreadInterface() {
                                @Override
                                public void completionUpdateMain() {

                                    MyTools.sharedInstance().displayAlert(ListCardActivity.this, errorString);

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


    private void refreshData() {

        MDBTypeCard.sharedInstance().getAllTypeCards(this, new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                if (success) {
                    TypeCards.sharedInstance().setTypeCardsArray(anArray);

                    MDBCard.sharedInstance().getAllCards(ListCardActivity.this, mConfig.getUser_id(), new MDBInterfaceArray() {
                        @Override
                        public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                            if (success) {
                                Cards.sharedInstance().setCardsArray(anArray);
                                chargeData();

                            } else {
                                MyTools.sharedInstance().performUIUpdatesOnMain(ListCardActivity.this, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        MyTools.sharedInstance().displayAlert(ListCardActivity.this, errorString);

                                    }
                                });
                            }
                        }
                    });


                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(ListCardActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(ListCardActivity.this, errorString);

                        }
                    });
                }
            }
        });

    }

    private void chargeData() {

        mCards.clear();
        JSONArray cardsJSON = Cards.sharedInstance().getCardsArray();
        JSONArray typeCardsJSON = TypeCards.sharedInstance().getTypeCardsArray();

        try {

            for (int i = 0; i < cardsJSON.length() ; i++) {

                JSONObject card = cardsJSON.getJSONObject(i);
                Card cd = new Card(this, card);

                for (int j = 0; j < typeCardsJSON.length() ; j++) {

                    JSONObject typeCd = typeCardsJSON.getJSONObject(j);
                    TypeCard typeC = new TypeCard(this, typeCd);

                    if (typeC.getTypeCard_id() == cd.getTypeCard_id()) {
                        cd.setTypeCard_ImageUrl(typeC.getTypeCard_ImageUrl());
                        mCards.add(i, cd);
                    }
                }

            }

        }  catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter mAdapter = new CustomItemListCard(this, R.layout.list_card_item, mCards);
        mIBListCardListView.setAdapter(mAdapter);



    }


}
