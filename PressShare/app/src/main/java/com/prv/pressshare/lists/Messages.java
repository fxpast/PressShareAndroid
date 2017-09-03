package com.prv.pressshare.lists;

//
//  Messages.swift
//  PressShare
//
//  Created by MacbookPRV on 24/08/2017.
//  Copyright © 2017 Pastouret Roger. All rights reserved.
//


import org.json.JSONArray;



public class Messages {

    private static Messages  mInstance;

    private JSONArray MessagesArray;
    public JSONArray getMessagesArray() { return MessagesArray;}
    public void setMessagesArray(JSONArray MessagesArray) {this.MessagesArray = MessagesArray;}


    private Messages(){

        MessagesArray = new JSONArray();

    }

    public static Messages sharedInstance() {
        if (mInstance == null) {
            mInstance = new Messages();
        }
        return mInstance;
    }


}
