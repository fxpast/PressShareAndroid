package com.prv.gootoor.lists;


//
//  Capitals
//  PressShare
//
//  Created by MacbookPRV on 24/08/2017.
//  Copyright © 2017 Pastouret Roger. All rights reserved.
//


import org.json.JSONArray;



public class Capitals {

    private static Capitals  mInstance;

    private JSONArray capitalsArray;
    public JSONArray getCapitalsArray() { return capitalsArray;}
    public void setCapitalsArray(JSONArray capitalsArray) {this.capitalsArray = capitalsArray;}


    private Capitals(){

        capitalsArray = new JSONArray();

    }

    public static Capitals sharedInstance() {
        if (mInstance == null) {
            mInstance = new Capitals();
        }
        return mInstance;
    }


}
