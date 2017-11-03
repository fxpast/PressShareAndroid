package com.prv.pressshare.models;

//
//  Message
//  PressShare
//
//  Description : This class contains all properties for messaging between users
//
//  Created by MacbookPRV on 24/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//


import android.content.Context;
import com.prv.pressshare.utils.MyTools;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

public class Message {

    private int  message_id;
    public int getMessage_id() { return message_id;}
    public void setMessage_id(int message_id) {this.message_id = message_id;}

    private int  expediteur;
    public int getExpediteur() { return expediteur;}
    public void setExpediteur(int expediteur) {this.expediteur = expediteur;}

    private int  destinataire;
    public int getDestinataire() { return destinataire;}
    public void setDestinataire(int destinataire) {this.destinataire = destinataire;}

    private int  proprietaire;
    public int getProprietaire() { return proprietaire;}
    public void setProprietaire(int proprietaire) {this.proprietaire = proprietaire;}

    private int  vendeur_id;
    public int getVendeur_id() { return vendeur_id;}
    public void setVendeur_id(int vendeur_id) {this.vendeur_id = vendeur_id;}

    private int  client_id;
    public int getClient_id() { return client_id;}
    public void setClient_id(int client_id) {this.client_id = client_id;}

    private int  product_id;
    public int getProduct_id() { return product_id;}
    public void setProduct_id(int product_id) {this.product_id = product_id;}

    private Date date_ajout;
    public Date getDate_ajout() { return date_ajout;}
    public void setDate_ajout(Date date_ajout) {this.date_ajout = date_ajout;}

    private String  contenu;
    public String getContenu() { return contenu;}
    public void setContenu(String contenu) {this.contenu = contenu;}

    private Boolean deja_lu;
    public Boolean getDeja_lu() { return deja_lu;}
    public void setDeja_lu(Boolean deja_lu) {this.deja_lu = deja_lu;}



    public Message(final Context context, JSONObject dico) {

        if (dico != null) {

            try {

                message_id= Integer.parseInt(dico.get("message_id").toString());
                expediteur= Integer.parseInt(dico.get("expediteur").toString());
                destinataire= Integer.parseInt(dico.get("destinataire").toString());
                proprietaire= Integer.parseInt(dico.get("proprietaire").toString());
                vendeur_id= Integer.parseInt(dico.get("vendeur_id").toString());
                client_id= Integer.parseInt(dico.get("client_id").toString());
                product_id= Integer.parseInt(dico.get("product_id").toString());
                date_ajout = MyTools.sharedInstance().dateFromString(dico.get("date_ajout").toString(), "yyyy-MM-dd HH:mm:ss");
                contenu = dico.get("contenu").toString();
                deja_lu= Boolean.parseBoolean(dico.get("deja_lu").toString());


            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        else {

            message_id = 0;
            expediteur = 0;
            destinataire = 0;
            proprietaire = 0;
            vendeur_id = 0;
            client_id = 0;
            product_id = 0;
            date_ajout = null;
            contenu = "";
            deja_lu = false;

        }


    }



}
