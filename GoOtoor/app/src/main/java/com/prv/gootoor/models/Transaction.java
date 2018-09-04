package com.prv.gootoor.models;

//
//  Transaction.swift
//  PressShare
//
//  Description : This class contains all the properties for buy / exchange product
//
//  Created by MacbookPRV on 21/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//


import com.prv.gootoor.utils.MyTools;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

public class Transaction {

    private int  trans_id;
    public int getTrans_id() { return trans_id;}
    public void setTrans_id(int trans_id) {
        this.trans_id = trans_id;
    }

    private Date  trans_date;
    public Date getTrans_date() { return trans_date;}
    public void setTrans_date(Date trans_date) {this.trans_date = trans_date;}

    private int trans_type;  //1 : Buy. 2 : Exchange
    public int getTrans_type() { return trans_type;}
    public void setTrans_type(int trans_type) {
        this.trans_type = trans_type;
    }

    private String  trans_wording;
    public String getTrans_wording() { return trans_wording;}
    public void setTrans_wording(String trans_wording) {this.trans_wording = trans_wording;}

    private int prod_id;
    public int getProd_id() { return prod_id;}
    public void setProd_id(int prod_id) {
        this.prod_id = prod_id;
    }

    private Double trans_amount;
    public Double getTrans_amount() { return trans_amount;}
    public void setTrans_amount(Double trans_amount) {
        this.trans_amount = trans_amount;
    }

    private int client_id;
    public int getClient_id() { return client_id;}
    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    private int vendeur_id;
    public int getVendeur_id() { return vendeur_id;}
    public void setVendeur_id(int vendeur_id) {
        this.vendeur_id = vendeur_id;
    }

    private int proprietaire;
    public int getProprietaire() { return proprietaire;}
    public void setProprietaire(int proprietaire) {
        this.proprietaire = proprietaire;
    }

    private int trans_valid;  //0 : La transaction en cours. 1 : La transaction a été annulée. 2 : La transaction est confirmée.
    public int getTrans_valid() { return trans_valid;}
    public void setTrans_valid(int trans_valid) {
        this.trans_valid = trans_valid;
    }

    private String trans_avis;   //interlocuteur, conformite, absence, "tap text"
    public String getTrans_avis() { return trans_avis;}
    public void setTrans_avis(String trans_avis) {
        this.trans_avis = trans_avis;
    }

    private Boolean trans_arbitrage;
    public Boolean getTrans_arbitrage() { return trans_arbitrage;}
    public void setTrans_arbitrage(Boolean trans_arbitrage) {
        this.trans_arbitrage = trans_arbitrage;
    }

    private int trans_note;  //noter la transaction sur 5
    public int getTrans_note() { return trans_note;}
    public void setTrans_note(int trans_note) {
        this.trans_note = trans_note;
    }



    public Transaction(JSONObject dico) {

        if (dico != null) {

            try {



                trans_id= Integer.parseInt(dico.get("trans_id").toString());
                trans_date = MyTools.sharedInstance().dateFromServer(dico.get("trans_date").toString());
                trans_type= Integer.parseInt(dico.get("trans_type").toString());
                trans_wording = dico.get("trans_wording").toString();
                prod_id= Integer.parseInt(dico.get("prod_id").toString());
                trans_amount = Double.parseDouble(dico.get("trans_amount").toString());
                client_id= Integer.parseInt(dico.get("client_id").toString());
                vendeur_id= Integer.parseInt(dico.get("vendeur_id").toString());
                proprietaire= Integer.parseInt(dico.get("proprietaire").toString());
                trans_valid= Integer.parseInt(dico.get("trans_valid").toString());
                trans_avis= dico.get("trans_avis").toString();
                trans_arbitrage = Integer.parseInt(dico.get("trans_arbitrage").toString()) != 0;
                trans_note= Integer.parseInt(dico.get("trans_note").toString());


            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        else {

            trans_id = 0;
            trans_date = null;
            trans_type = 0;
            trans_wording = "";
            prod_id = 0;
            trans_amount = 0.0;
            client_id = 0;
            vendeur_id = 0;
            proprietaire = 0;
            trans_valid = 0;
            trans_avis = "";
            trans_arbitrage = false;
            trans_note = 0;



        }

    }


}
