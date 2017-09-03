package com.prv.pressshare.lists;

//
//  ParamTables.swift
//  PressShare
//
//  Created by MacbookPRV on 25/08/2017.
//  Copyright © 2017 Pastouret Roger. All rights reserved.
//


import org.json.JSONArray;



public class ParamTables {

    private static ParamTables  mInstance;

    private JSONArray paramTableArray;
    public JSONArray getParamTableArray() { return paramTableArray;}
    public void setParamTableArray(JSONArray paramTableArray) {this.paramTableArray = paramTableArray;}


    private ParamTables(){

        paramTableArray = new JSONArray();

    }

    public static ParamTables sharedInstance() {
        if (mInstance == null) {
            mInstance = new ParamTables();
        }
        return mInstance;
    }


}