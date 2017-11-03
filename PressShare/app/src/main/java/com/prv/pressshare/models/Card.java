package com.prv.pressshare.models;

//
//  Card
//  PressShare
//
//  Description : This class contains all properties for card account like visa, paypal
//
//  Created by MacbookPRV on 23/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//


import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class Card {


    private int  card_id;
    public int getCard_id() { return card_id;}
    public void setCard_id(int card_id) {this.card_id = card_id;}

    private int  typeCard_id;
    public int getTypeCard_id() { return typeCard_id;}
    public void setTypeCard_id(int typeCard_id) {this.typeCard_id = typeCard_id;}

    private int  user_id;
    public int getUser_id() { return user_id;}
    public void setUser_id(int user_id) {this.user_id = user_id;}

    private String  tokenizedCard;
    public String getTokenizedCard() { return tokenizedCard;}
    public void setTokenizedCard(String tokenizedCard) {this.tokenizedCard = tokenizedCard;}

    private String  typeCard_ImageUrl;
    public String getTypeCard_ImageUrl() { return typeCard_ImageUrl;}
    public void setTypeCard_ImageUrl(String typeCard_ImageUrl) {this.typeCard_ImageUrl = typeCard_ImageUrl;}


    private String  card_lastNumber;
    public String getCard_lastNumber() { return card_lastNumber;}
    public void setCard_lastNumber(String card_lastNumber) {this.card_lastNumber = card_lastNumber;}

    private Boolean main_card;
    public Boolean getMain_card() { return main_card;}
    public void setMain_card(Boolean main_card) {this.main_card = main_card;}




    public Card(final Context context, JSONObject dico) {

        if (dico != null) {

            try {

                card_id= Integer.parseInt(dico.get("card_id").toString());
                typeCard_id= Integer.parseInt(dico.get("typeCard_id").toString());
                user_id= Integer.parseInt(dico.get("user_id").toString());
                tokenizedCard = dico.get("tokenizedCard").toString();
                card_lastNumber = dico.get("card_lastNumber").toString();
                main_card = dico.get("main_card").toString().equals("1");
                typeCard_ImageUrl = "";


            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        else {

            card_id = 0;
            typeCard_id = 0;
            user_id = 0;
            tokenizedCard = "";
            card_lastNumber = "";
            main_card = false;
            typeCard_ImageUrl = "";

        }


    }


}
