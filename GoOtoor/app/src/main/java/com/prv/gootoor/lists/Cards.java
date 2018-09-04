package com.prv.gootoor.lists;

import org.json.JSONArray;


//
//  Card
//  PressShare
//
//  Description : This class contains all properties for card account like visa, paypal
//
//  Created by MacbookPRV on 23/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//



public class Cards {

    private static Cards  mInstance;

    private JSONArray cardsArray;
    public JSONArray getCardsArray() { return cardsArray;}
    public void setCardsArray(JSONArray cardsArray) {this.cardsArray = cardsArray;}


    private Cards(){

        cardsArray = new JSONArray();

    }

    public static Cards sharedInstance() {
        if (mInstance == null) {
            mInstance = new Cards();
        }
        return mInstance;
    }


}
