package com.prv.pressshare.models;

//
//  Capital.swift
//  PressShare
//
//  Description : This class contains user's balance
//
//  Created by MacbookPRV on 24/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//

import android.content.Context;
import com.prv.pressshare.utils.MyTools;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

public class Capital {

    private int  user_id;
    public int getUser_id() { return user_id;}
    public void setUser_id(int user_id) {this.user_id = user_id;}

    private Date date_maj;
    public Date getDate_maj() { return date_maj;}
    public void setDate_maj(Date date_maj) {
        this.date_maj = date_maj;
    }

    private Double balance;
    public Double getBalance() { return balance;}
    public void setBalance(Double balance) {this.balance = balance;}

    private int  failure_count;
    public int getFailure_count() { return failure_count;}
    public void setFailure_count(int failure_count) {this.failure_count = failure_count;}


    public Capital(final Context context, JSONObject dico) {

        if (dico.length() > 1) {

            try {

                user_id= Integer.parseInt(dico.get("user_id").toString());
                date_maj = MyTools.sharedInstance().dateFromString(dico.get("date_maj").toString(), "yyyy-MM-dd HH:mm:ss");
                balance = Double.parseDouble(dico.get("balance").toString());
                failure_count= Integer.parseInt(dico.get("failure_count").toString());


            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        else {

            user_id = 0;
            date_maj = null;
            balance = 0.0;
            failure_count = 0;

        }



    }


}
