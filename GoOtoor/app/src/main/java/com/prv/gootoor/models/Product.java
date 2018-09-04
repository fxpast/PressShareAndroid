package com.prv.gootoor.models;

//
//  Product
//  PressShare
//
//  Description : This class contains all properties for item to buy / exchange
//
//  Created by MacbookPRV on 22/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//


//Properties


import android.content.Context;
import android.widget.ImageView;
import com.prv.gootoor.R;
import com.prv.gootoor.utils.MyTools;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;


public class Product {


    private int  prod_id;
    public int getProd_id() { return prod_id;}
    public void setProd_id(int prod_id) {this.prod_id = prod_id;}

    private ImageView prod_image; // stocker l'image
    public ImageView getProd_image() { return prod_image;}
    public void setProd_image(ImageView prod_image) {this.prod_image = prod_image;}

    private String  prod_imageUrl; //stoker url de l'image
    public String getProd_imageUrl() { return prod_imageUrl;}
    public void setProd_imageUrl(String prod_imageUrl) {this.prod_imageUrl = prod_imageUrl;}

    private String  prod_nom; 
    public String getProd_nom() { return prod_nom;}
    public void setProd_nom(String prod_nom) {this.prod_nom = prod_nom;}

    private Date prod_date;
    public Date getProd_date() { return prod_date;}
    public void setProd_date(Date prod_date) {
        this.prod_date = prod_date;
    }

    private Double prod_prix;
    public Double getProd_prix() { return prod_prix;}
    public void setProd_prix(Double prod_prix) {
        this.prod_prix = prod_prix;
    }

    private int  prod_by_user; //le vendeur
    public int getProd_by_user() { return prod_by_user;}
    public void setProd_by_user(int prod_by_user) {this.prod_by_user = prod_by_user;}

    private int  prod_oth_user; //le client
    public int getProd_oth_user() { return prod_oth_user;}
    public void setProd_oth_user(int prod_oth_user) {this.prod_oth_user = prod_oth_user;}

    private int  prod_by_cat;
    public int getProd_by_cat() { return prod_by_cat;}
    public void setProd_by_cat(int prod_by_cat) {this.prod_by_cat = prod_by_cat;}

    private Double prod_latitude;
    public Double getProd_latitude() { return prod_latitude;}
    public void setProd_latitude(Double prod_latitude) {
        this.prod_latitude = prod_latitude;
    }

    private Double prod_longitude;
    public Double getProd_longitude() { return prod_longitude;}
    public void setProd_longitude(Double prod_longitude) {
        this.prod_longitude = prod_longitude;
    }

    private String  prod_mapString; //le nom du lieu sur la carte
    public String getProd_mapString() { return prod_mapString;}
    public void setProd_mapString(String prod_mapString) {this.prod_mapString = prod_mapString;}

    private String  prod_comment;
    public String getProd_comment() { return prod_comment;}
    public void setProd_comment(String prod_comment) {this.prod_comment = prod_comment;}

    private int  prod_etat; //number of star
    public int getProd_etat() { return prod_etat;}
    public void setProd_etat(int prod_etat) {this.prod_etat = prod_etat;}

    private Boolean prod_hidden;
    public Boolean getProd_hidden() { return prod_hidden;}
    public void setProd_hidden(Boolean prod_hidden) {this.prod_hidden = prod_hidden;}

    private Boolean prod_echange; // autoriser l'echange de produit
    public Boolean getProd_echange() { return prod_echange;}
    public void setProd_echange(Boolean prod_echange) {this.prod_echange = prod_echange;}

    private String  prodImageOld;
    public String getProdImageOld() { return prodImageOld;}
    public void setProdImageOld(String prodImageOld) {this.prodImageOld = prodImageOld;}

    private Boolean prod_closed;
    public Boolean getProd_closed() { return prod_closed;}
    public void setProd_closed(Boolean prod_closed) {this.prod_closed = prod_closed;}



    public Product(final Context context, JSONObject dico) {

        if (dico != null) {

            try {

                prod_id= Integer.parseInt(dico.get("prod_id").toString());
                prod_imageUrl = dico.get("prod_imageUrl").toString();
                prod_nom = dico.get("prod_nom").toString();
                prod_date = MyTools.sharedInstance().dateFromServer(dico.get("prod_date").toString());
                prod_prix = Double.parseDouble(dico.get("prod_prix").toString());
                prod_by_user= Integer.parseInt(dico.get("prod_by_user").toString());
                prod_oth_user= Integer.parseInt(dico.get("prod_oth_user").toString());
                prod_by_cat= Integer.parseInt(dico.get("prod_by_cat").toString());
                prod_latitude = Double.parseDouble(dico.get("prod_latitude").toString());
                prod_longitude = Double.parseDouble(dico.get("prod_longitude").toString());
                prod_mapString = dico.get("prod_mapString").toString();
                prod_comment = dico.get("prod_comment").toString();
                prod_etat= Integer.parseInt(dico.get("prod_etat").toString());
                prod_hidden = Integer.parseInt(dico.get("prod_hidden").toString()) != 0;
                prod_echange = Integer.parseInt(dico.get("prod_echange").toString()) != 0;
                prod_closed = Integer.parseInt(dico.get("prod_closed").toString()) != 0;


            } catch (JSONException e){
                e.printStackTrace();
            }


        }
        else {

            prod_id = 0;
            prod_imageUrl = "";
            prod_nom = "";
            prod_date = null;
            prod_prix = 0.0;
            prod_by_user = 0;
            prod_oth_user = 0;
            prod_by_cat = 0;
            prod_latitude = 0.0;
            prod_longitude = 0.0;
            prod_mapString = "";
            prod_comment = "";
            prod_etat = 0;
            prod_hidden= false;
            prod_echange= false;
            prod_closed= false;

        }

        prod_image = new ImageView(context);
        prod_image.setImageResource(R.drawable.noimage);
        prodImageOld = "";

    }


}
