package com.prv.gootoor.models;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

//
//  Feedback
//  PressShare
//
//  Description : This class contains all properties for card account like visa, paypal
//
//  Created by MacbookPRV on 24/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//


public class Feedback {


    private int  feedback_id;
    public int getFeedback_id() { return feedback_id;}
    public void setFeedback_id(int feedback_id) {this.feedback_id = feedback_id;}

    private String  comment;
    public String getComment() { return comment;}
    public void setComment(String comment) {this.comment = comment;}

    private String  origin;
    public String getOrigin() { return origin;}
    public void setOrigin(String origin) {this.origin = origin;}


    public Feedback(final Context context, JSONObject dico) {

        if (dico != null) {

            try {

                feedback_id= Integer.parseInt(dico.get("feedback_id").toString());
                comment = dico.get("comment").toString();
                origin = dico.get("origin").toString();

            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        else {

            feedback_id = 0;
            comment = "";
            origin = "";

        }


    }


}
