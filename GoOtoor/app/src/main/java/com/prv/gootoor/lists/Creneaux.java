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


public class Creneaux {

    private static Creneaux mInstance;

    private JSONArray creneauxArray;
    public JSONArray getCreneauxArray() { return creneauxArray;}
    public void setCreneauxArray(JSONArray creneauxArray) {this.creneauxArray = creneauxArray;}


    private Creneaux(){

        creneauxArray = new JSONArray();

    }

    public static Creneaux sharedInstance() {
        if (mInstance == null) {
            mInstance = new Creneaux();
        }
        return mInstance;
    }


}
