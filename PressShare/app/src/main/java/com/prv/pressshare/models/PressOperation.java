package com.prv.pressshare.models;

//
//  Operation
//  PressShare
//
//  Description : This class contains all properties for history of capital updating
//
//  Created by MacbookPRV on 24/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//


import android.content.Context;

import com.prv.pressshare.utils.MyTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class PressOperation {

    private int  op_id;
    public int getOp_id() { return op_id;}
    public void setOp_id(int op_id) {this.op_id = op_id;}

    private int  user_id;
    public int getUser_id() { return user_id;}
    public void setUser_id(int user_id) {this.user_id = user_id;}


    private Date op_date;
    public Date getOp_date() { return op_date;}
    public void setOp_date(Date op_date) {this.op_date = op_date;}

    private int  op_type; //1: deposit, 2: withdrawal, 3: buy, 4: sell, 5:Commission, 6:refund
    public int getOp_type() { return op_type;}
    public void setOp_type(int op_type) {this.op_type = op_type;}

    private Double op_amount;
    public Double getOp_amount() { return op_amount;}
    public void setOp_amount(Double op_amount) {
        this.op_amount = op_amount;
    }

    private String  op_wording;
    public String getOp_wording() { return op_wording;}
    public void setOp_wording(String op_wording) {this.op_wording = op_wording;}


    public PressOperation(final Context context, JSONObject dico) {

        if (dico.length() > 1) {

            try {

                op_id= Integer.parseInt(dico.get("op_id").toString());
                user_id= Integer.parseInt(dico.get("user_id").toString());
                op_date = MyTools.sharedInstance().dateFromString(dico.get("op_date").toString(), "yyyy-MM-dd HH:mm:ss");
                op_type= Integer.parseInt(dico.get("op_type").toString());
                op_amount = Double.parseDouble(dico.get("op_amount").toString());
                op_wording = dico.get("op_wording").toString();


            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        else {

            op_id = 0;
            user_id = 0;
            op_date = null;
            op_type = 0;
            op_amount = 0.0;
            op_wording = "";
        }


    }


}
