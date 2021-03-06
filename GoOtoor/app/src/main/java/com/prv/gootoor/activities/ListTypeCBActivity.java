package com.prv.gootoor.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.prv.gootoor.R;
import com.prv.gootoor.daos.MDBInterfaceArray;
import com.prv.gootoor.daos.MDBTypeCard;
import com.prv.gootoor.lists.TypeCards;
import com.prv.gootoor.models.TypeCard;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;
import com.prv.gootoor.views.CustomItemListTypeCB;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roger on 25/10/2017.
 */

public class ListTypeCBActivity extends AppCompatActivity {

    private Config mConfig = Config.sharedInstance();
    private ListView mIBListTypeCBListView;
    private List<Object> mTypeCard;
    private Handler myQueue;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_type_cb);

        mIBListTypeCBListView = (ListView) findViewById(R.id.IBListTypeCBListView);
        mTypeCard = new ArrayList<>();

        mIBListTypeCBListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TypeCard typeCard = (TypeCard) mTypeCard.get(position);
                mConfig.setTypeCard_id(typeCard.getTypeCard_id());
                finish();

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        initColor();

        if (TypeCards.sharedInstance().getTypeCardsArray().length() > 0) {

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


        ConstraintLayout mIBTypeCBBackgr;
        mIBTypeCBBackgr = (ConstraintLayout) findViewById(R.id.IBTypeCBBackgr);

        String hexColor = "#" + mConfig.getColorApp();
        mIBTypeCBBackgr.setBackgroundColor(Color.parseColor(hexColor));


    }



    public void actionCloseWidows(View view) {

        finish();
    }

    public void actionListTypeCBHelp(View view) {

        //Todo Tuto_Presentation
        MyTools.sharedInstance().showHelp("ListTypeCBTableViewCrtl", this);

    }



    private void chargeData() {

        mTypeCard.clear();
        JSONArray typeCardsJSON = TypeCards.sharedInstance().getTypeCardsArray();

        try {

            for (int i = 0; i < typeCardsJSON.length() ; i++) {

                JSONObject typeCd = typeCardsJSON.getJSONObject(i);
                TypeCard typeC = new TypeCard(this, typeCd);

                mTypeCard.add(i, typeC);

            }

        }  catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter mAdapter = new CustomItemListTypeCB(this, R.layout.list_type_cb_item, mTypeCard);
        mIBListTypeCBListView.setAdapter(mAdapter);


    }



    private void refreshData() {

        MDBTypeCard.sharedInstance().getAllTypeCards(this, new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {
                if (success) {
                    TypeCards.sharedInstance().setTypeCardsArray(anArray);
                    chargeData();
                } else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(ListTypeCBActivity.this, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(ListTypeCBActivity.this, errorString);

                        }
                    });
                }
            }
        });

    }



}
