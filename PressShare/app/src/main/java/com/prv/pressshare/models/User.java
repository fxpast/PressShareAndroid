package com.prv.pressshare.models;


import com.prv.pressshare.utils.MyTools;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;


//  User
//  PressShare
//
// Description : User account with physical adresse and geolocalization
//
//  Created by MacbookPRV on 20/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//



public class User {


    private String  user_adresse;
    public String getUser_adresse() { return user_adresse;}
    public void setUser_adresse(String user_adresse) {this.user_adresse = user_adresse;}

    private String  user_codepostal;
    public String getUser_codepostal() { return user_codepostal;}
    public void setUser_codepostal(String user_codepostal) {this.user_codepostal = user_codepostal;}

    private Date  user_date;
    public Date getUser_date() { return user_date;}
    public void setUser_date(Date user_date) {
        this.user_date = user_date;
    }


    private String  user_email;
    public String getUser_email() { return user_email;}
    public void setUser_email(String user_email) {
        this.user_email = user_email;
        
    }

    private int  user_id;
    public int getUser_id() { return user_id;}
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    private int  user_level;
    public int getUser_level() { return user_level;}
    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }


    private Boolean  user_newpassword;
    public Boolean getUser_newpassword() { return user_newpassword;}
    public void setUser_newpassword(Boolean user_newpassword) {
        this.user_newpassword = user_newpassword;
    }


    private String  user_nom;
    public String getUser_nom() { return user_nom;}
    public void setUser_nom(String user_nom) {
        this.user_nom = user_nom;
        
    }

    private String  user_pass;
    public String getUser_pass() { return user_pass;}
    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
        
    }

    private String  user_pays;
    public String getUser_pays() { return user_pays;}
    public void setUser_pays(String user_pays) {
        this.user_pays = user_pays;
        
    }


    private String  user_prenom;
    public String getUser_prenom() { return user_prenom;}
    public void setUser_prenom(String user_prenom) {
        this.user_prenom = user_prenom;
        
    }


    private String  user_pseudo;
    public String getUser_pseudo() { return user_pseudo;}
    public void setUser_pseudo(String user_pseudo) {
        this.user_pseudo = user_pseudo;
        
    }


    private String  user_ville;
    public String getUser_ville() { return user_ville;}
    public void setUser_ville(String user_ville) {
        this.user_ville = user_ville;
        
    }


    private String  user_tokenPush;
    public String getUser_tokenPush() { return user_tokenPush;}
    public void setUser_tokenPush(String user_tokenPush) {
        this.user_tokenPush = user_tokenPush;
    }

    private String  user_braintreeID;
    public String getUser_braintreeID() { return user_braintreeID;}
    public void setUser_braintreeID(String user_braintreeID) {
        this.user_braintreeID = user_braintreeID;
    }

    private int  user_note; //note per 5 stars
    public int getUser_note() { return user_note;}
    public void setUser_note(int user_note) {
        this.user_note = user_note;
        
    }

    private int  user_countNote; //count of note
    public int getUser_countNote() { return user_countNote;}
    public void setUser_countNote(int user_countNote) {
        this.user_countNote = user_countNote;
    }



    public User(JSONObject dico) {

        if (dico != null) {

            try {

                user_id= Integer.parseInt(dico.get("user_id").toString());
                user_pseudo = dico.get("user_pseudo").toString();
                user_pass = dico.get("user_pass").toString();
                user_email = dico.get("user_email").toString();
                user_date = MyTools.sharedInstance().dateFromString(dico.get("user_date").toString(), "yyyy-MM-dd HH:mm:ss");
                user_level= Integer.parseInt(dico.get("user_level").toString());
                user_nom = dico.get("user_nom").toString();
                user_prenom = dico.get("user_prenom").toString();
                user_adresse = dico.get("user_adresse").toString();
                user_codepostal = dico.get("user_codepostal").toString();
                user_pays = dico.get("user_pays").toString();
                user_ville = dico.get("user_ville").toString();
                user_newpassword= dico.get("user_newpassword").toString().equals("1");
                user_tokenPush = dico.get("user_tokenPush").toString();
                user_braintreeID = dico.get("user_braintreeID").toString();
                user_countNote= Integer.parseInt(dico.get("user_countNote").toString());
                user_note= Integer.parseInt(dico.get("user_note").toString());

            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        else {

            user_id = 0;
            user_pseudo = "";
            user_pass = "";
            user_email = "";
            user_date = null;
            user_level = 0; //level -1 = anonymous, level 0 = sign up, level 1 = subscriber, level 2 = admin
            user_nom = "";
            user_prenom = "";
            user_adresse = "";
            user_codepostal = "";
            user_ville = "";
            user_pays = "";
            user_newpassword = false;
            user_tokenPush = "";
            user_braintreeID = "";
            user_note = 0; //note per 5 stars
            user_countNote = 0; //count of note

        }

    }




}



