package com.prv.gootoor.models;

//
//  Commission
//  PressShare
//
//  Description : This class contains all properties for PressShare income
//
//  Created by MacbookPRV on 24/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//


import android.content.Context;
import com.prv.gootoor.utils.MyTools;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Commission {

    private int  com_id;
    public int getCom_id() { return com_id;}
    public void setCom_id(int com_id) {
        this.com_id = com_id;
    }

    private int  user_id;
    public int getUser_id() { return user_id;}
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    private int  product_id;
    public int getProduct_id() { return product_id;}
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    private Date com_date;
    public Date getCom_date() { return com_date;}
    public void setCom_date(Date com_date) {this.com_date = com_date;}

    private Double com_amount;
    public Double getCom_amount() { return com_amount;}
    public void setCom_amount(Double com_amount) {
        this.com_amount = com_amount;
    }



    public Commission(final Context context, JSONObject dico) {

        if (dico != null) {

            try {

                com_id= Integer.parseInt(dico.get("com_id").toString());
                user_id= Integer.parseInt(dico.get("user_id").toString());
                product_id= Integer.parseInt(dico.get("product_id").toString());
                com_date = MyTools.sharedInstance().dateFromServer(dico.get("com_date").toString());
                com_amount = Double.parseDouble(dico.get("com_amount").toString());

            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        else {

            com_id = 0;
            user_id = 0;
            product_id = 0;
            com_date = null;
            com_amount = 0.0;

        }

    }


}
