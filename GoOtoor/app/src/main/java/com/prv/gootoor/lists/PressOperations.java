package com.prv.gootoor.lists;

//
//  PressOperations
//  PressShare
//
//  Created by MacbookPRV on 24/08/2017.
//  Copyright © 2017 Pastouret Roger. All rights reserved.
//


import org.json.JSONArray;


public class PressOperations {

    private static PressOperations  mInstance;

    private JSONArray operationArray;
    public JSONArray getOperationArray() { return operationArray;}
    public void setOperationArray(JSONArray operationArray) {this.operationArray = operationArray;}


    private PressOperations(){

        operationArray = new JSONArray();

    }

    public static PressOperations sharedInstance() {
        if (mInstance == null) {
            mInstance = new PressOperations();
        }
        return mInstance;
    }


}
