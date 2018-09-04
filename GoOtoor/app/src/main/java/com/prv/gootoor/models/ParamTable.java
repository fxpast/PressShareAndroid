package com.prv.gootoor.models;

//
//  ParamTable.swift
//  PressShare
//
//  Description : This class contains all properties for setting general app's informations
//
//  Created by MacbookPRV on 25/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//


import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class ParamTable {

    private int  param_id;
    public int getParam_id() { return param_id;}
    public void setParam_id(int param_id) {this.param_id = param_id;}

    private Double distanceProduct;
    public Double getDistanceProduct() { return distanceProduct;}
    public void setDistanceProduct(Double distanceProduct) {this.distanceProduct = distanceProduct;}

    private Double regionGeoLocat;
    public Double getRegionGeoLocat() { return regionGeoLocat;}
    public void setRegionGeoLocat(Double regionGeoLocat) {this.regionGeoLocat = regionGeoLocat;}

    private Double regionProduct;
    public Double getRegionProduct() { return regionProduct;}
    public void setRegionProduct(Double regionProduct) {this.regionProduct = regionProduct;}

    private Double commisPourcBuy;
    public Double getCommisPourcBuy() { return commisPourcBuy;}
    public void setCommisPourcBuy(Double commisPourcBuy) {this.commisPourcBuy = commisPourcBuy;}

    private Double commisFixEx;
    public Double getCommisFixEx() { return commisFixEx;}
    public void setCommisFixEx(Double commisFixEx) {this.commisFixEx = commisFixEx;}

    private int  maxDayTrigger;
    public int getMaxDayTrigger() { return maxDayTrigger;}
    public void setMaxDayTrigger(int maxDayTrigger) {this.maxDayTrigger = maxDayTrigger;}

    private Double subscriptAmount;
    public Double getSubscriptAmount() { return subscriptAmount;}
    public void setSubscriptAmount(Double subscriptAmount) {this.subscriptAmount = subscriptAmount;}

    private Double minimumAmount;
    public Double getMinimumAmount() { return minimumAmount;}
    public void setMinimumAmount(Double minimumAmount) {this.minimumAmount = minimumAmount;}

    private String  colorApp;
    public String getColorApp() { return colorApp;}
    public void setColorApp(String colorApp) {this.colorApp = colorApp;}

    private String  colorAppLabel;
    public String getColorAppLabel() { return colorAppLabel;}
    public void setColorAppLabel(String colorAppLabel) {this.colorAppLabel = colorAppLabel;}

    private String  colorAppText;
    public String getColorAppText() { return colorAppText;}
    public void setColorAppText(String colorAppText) {this.colorAppText = colorAppText;}

    private String  colorAppPlHd;
    public String getColorAppPlHd() { return colorAppPlHd;}
    public void setColorAppPlHd(String colorAppPlHd) {this.colorAppPlHd = colorAppPlHd;}

    private String  colorAppBt;
    public String getColorAppBt() { return colorAppBt;}
    public void setColorAppBt(String colorAppBt) {this.colorAppBt = colorAppBt;}



    public ParamTable(final Context context, JSONObject dico) {

        if (dico != null) {

            try {

                param_id= Integer.parseInt(dico.get("param_id").toString());
                distanceProduct = Double.parseDouble(dico.get("distanceProduct").toString());
                regionGeoLocat = Double.parseDouble(dico.get("regionGeoLocat").toString());
                regionProduct = Double.parseDouble(dico.get("regionProduct").toString());
                commisPourcBuy = Double.parseDouble(dico.get("commisPourcBuy").toString());
                commisFixEx = Double.parseDouble(dico.get("commisFixEx").toString());
                maxDayTrigger= Integer.parseInt(dico.get("maxDayTrigger").toString());
                subscriptAmount = Double.parseDouble(dico.get("subscriptAmount").toString());
                minimumAmount = Double.parseDouble(dico.get("minimumAmount").toString());
                colorApp = dico.get("colorApp").toString();
                colorAppLabel = dico.get("colorAppLabel").toString();
                colorAppText = dico.get("colorAppText").toString();
                colorAppPlHd = dico.get("colorAppPlHd").toString();
                colorAppBt = dico.get("colorAppBt").toString();

            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        else {


            param_id = 0;
            distanceProduct = 0.0;
            regionGeoLocat = 0.0;
            regionProduct = 0.0;
            commisPourcBuy = 0.0;
            commisFixEx = 0.0;
            maxDayTrigger = 0;
            subscriptAmount = 0.0;
            minimumAmount = 0.0;
            colorApp = "";
            colorAppLabel = "";
            colorAppText = "";
            colorAppPlHd = "";
            colorAppBt = "";

        }



    }

}
