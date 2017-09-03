package com.prv.pressshare.lists;

import org.json.JSONArray;

/**
 * Created by roger on 22/08/2017.
 */

public class Transactions {

    private static Transactions  mInstance;

    private JSONArray transactionArray;
    public JSONArray getTransactionArray() { return transactionArray;}
    public void setTransactionArray(JSONArray transactionArray) {this.transactionArray = transactionArray;}


    private Transactions(){

        transactionArray = new JSONArray();

    }

    public static Transactions sharedInstance() {
        if (mInstance == null) {
            mInstance = new Transactions();
        }
        return mInstance;
    }


}
