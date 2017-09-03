package com.prv.pressshare.models;

//
//  TypeCard
//  PressShare
//
//  Description : This class contains all properties for type of card as master card , visa
//
//  Created by MacbookPRV on 24/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//


import android.content.Context;
import org.json.JSONException;
import org.json.JSONObject;

public class TypeCard {


    private int  typeCard_id;
    public int getTypeCard_id() { return typeCard_id;}
    public void setTypeCard_id(int typeCard_id) {this.typeCard_id = typeCard_id;}

    private String  typeCard_ImageUrl;
    public String getTypeCard_ImageUrl() { return typeCard_ImageUrl;}
    public void setTypeCard_ImageUrl(String typeCard_ImageUrl) {this.typeCard_ImageUrl = typeCard_ImageUrl;}

    private String  typeCard_Wording;
    public String getTypeCard_Wording() { return typeCard_Wording;}
    public void setTypeCard_Wording(String typeCard_Wording) {this.typeCard_Wording = typeCard_Wording;}




    public TypeCard(final Context context, JSONObject dico) {

        if (dico.length() > 1) {

            try {

                typeCard_id= Integer.parseInt(dico.get("typeCard_id").toString());
                typeCard_ImageUrl = dico.get("typeCard_ImageUrl").toString();
                typeCard_Wording = dico.get("typeCard_Wording").toString();

            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        else {

            typeCard_id = 0;
            typeCard_ImageUrl = "";
            typeCard_Wording = "";

        }


    }


}
