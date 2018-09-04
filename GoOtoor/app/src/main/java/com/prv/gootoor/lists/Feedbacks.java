package com.prv.gootoor.lists;

import org.json.JSONArray;

//
//  Feedbacks
//  PressShare
//
//  Created by MacbookPRV on 24/08/2017.
//  Copyright © 2017 Pastouret Roger. All rights reserved.
//


public class Feedbacks {

    private static Feedbacks  mInstance;

    private JSONArray feedbackArray;
    public JSONArray getFeedbackArray() { return feedbackArray;}
    public void setFeedbackArray(JSONArray feedbackArray) {this.feedbackArray = feedbackArray;}


    private Feedbacks(){

        feedbackArray = new JSONArray();

    }

    public static Feedbacks sharedInstance() {
        if (mInstance == null) {
            mInstance = new Feedbacks();
        }
        return mInstance;
    }

}