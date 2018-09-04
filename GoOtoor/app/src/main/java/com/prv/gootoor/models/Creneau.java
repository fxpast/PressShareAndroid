package com.prv.gootoor.models;


//  Creneau
//  GoOtoor
//
//  Description : This class contains all the properties for time slots / créneaux horaires
//
//  Created by MacbookPRV on 28/04/2018.
//  Copyright © 2018 Pastouret Roger. All rights reserved.
//


import android.content.Context;

import com.prv.gootoor.utils.MyTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Creneau {

    private int  cre_id;
    public int getCre_id() { return cre_id;}
    public void setCre_id(int cre_id) {this.cre_id = cre_id;}

    private int  prod_id;
    public int getProd_id() { return prod_id;}
    public void setProd_id(int prod_id) {this.prod_id = prod_id;}


    private Date cre_dateDebut;
    public Date getCre_dateDebut() { return cre_dateDebut;}
    public void setCre_dateDebut(Date cre_dateDebut) {
        this.cre_dateDebut = cre_dateDebut;
    }

    private Date cre_dateFin;
    public Date getCre_dateFin() { return cre_dateFin;}
    public void setCre_dateFin(Date cre_dateFin) {
        this.cre_dateFin = cre_dateFin;
    }

    private int  cre_repeat; // 0: none, 1: daily, 2:weekly
    public int getCre_repeat() { return cre_repeat;}
    public void setCre_repeat(int cre_repeat) {this.cre_repeat = cre_repeat;}


    private String  cre_mapString; //le nom du lieu sur la carte
    public String getCre_mapString() { return cre_mapString;}
    public void setCre_mapString(String cre_mapString) {this.cre_mapString = cre_mapString;}

    private Double cre_latitude;
    public Double getCre_latitude() { return cre_latitude;}
    public void setCre_latitude(Double cre_latitude) {this.cre_latitude = cre_latitude;}

    private Double cre_longitude;
    public Double getCre_longitude() { return cre_longitude;}
    public void setCre_longitude(Double cre_longitude) {this.cre_longitude = cre_longitude;}


    public Creneau(final Context context, JSONObject dico) {

        if (dico != null) {

            try {

                cre_id= Integer.parseInt(dico.get("cre_id").toString());
                prod_id= Integer.parseInt(dico.get("prod_id").toString());
                cre_dateDebut = MyTools.sharedInstance().dateFromServer(dico.get("cre_dateDebut").toString());
                cre_dateFin = MyTools.sharedInstance().dateFromServer(dico.get("cre_dateFin").toString());
                cre_repeat= Integer.parseInt(dico.get("cre_repeat").toString());
                cre_mapString = dico.get("cre_mapString").toString();
                cre_latitude = Double.parseDouble(dico.get("cre_latitude").toString());
                cre_longitude = Double.parseDouble(dico.get("cre_longitude").toString());



            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        else {

            cre_id = 0;
            prod_id = 0;
            cre_dateDebut = null;
            cre_dateFin = null;
            cre_repeat = 0;
            cre_mapString = "";
            cre_latitude = 0.0;
            cre_longitude = 0.0;


        }



    }


}
