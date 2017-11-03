package com.prv.pressshare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.prv.pressshare.R;
import com.prv.pressshare.lists.Transactions;
import com.prv.pressshare.models.Transaction;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MyTools;
import com.prv.pressshare.views.CustomItemListTrans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roger on 23/10/2017.
 */

public class ListTransactActivity extends AppCompatActivity {

    private Config mConfig = Config.sharedInstance();
    private ListView mIBListTransactView;
    private List<Object> mTransactions;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transact);


        mTransactions = new ArrayList<>();


        mIBListTransactView = (ListView) findViewById(R.id.IBListTransactView);


        mIBListTransactView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Transaction transaction = (Transaction) mTransactions.get(position);
                Intent intent = new Intent(ListTransactActivity.this, DetailTransActivity.class);
                intent.putExtra(mConfig.getDomaineApp()+"trans_id",transaction.getTrans_id());
                startActivity(intent);
            }
        });


        actionListTransact();




    }

    private void actionListTransact() {

        mTransactions.clear();
        JSONArray transactionsJSON = Transactions.sharedInstance().getTransactionArray();

        try {

            for (int i = 0; i < transactionsJSON.length() ; i++) {

                JSONObject dictionary = transactionsJSON.getJSONObject(i);
                Transaction transaction = new Transaction(dictionary);
                mTransactions.add(i, transaction);

            }

        }  catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter mAdapter = new CustomItemListTrans(this, R.layout.list_transact_item, mTransactions);
        mIBListTransactView.setAdapter(mAdapter);


    }

    public void actionCloseWidows(View view) {

        finish();
    }

    public void actionListTransactHelp(View view) {

        //Todo Tuto_Presentation
        MyTools.sharedInstance().showHelp("transactions", this);

    }

}
