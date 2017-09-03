package com.prv.pressshare.lists;

import org.json.JSONArray;

/**
 * Created by roger on 24/08/2017.
 */


public class TypeCards {

    private static TypeCards  mInstance;

    private JSONArray typeCardsArray;
    public JSONArray getTypeCardsArray() { return typeCardsArray;}
    public void setTypeCardsArray(JSONArray typeCardsArray) {this.typeCardsArray = typeCardsArray;}


    private TypeCards(){

        typeCardsArray = new JSONArray();

    }

    public static TypeCards sharedInstance() {
        if (mInstance == null) {
            mInstance = new TypeCards();
        }
        return mInstance;
    }


}
