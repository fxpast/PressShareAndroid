package com.prv.gootoor.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.prv.gootoor.daos.MDBInterfaceArray;
import com.prv.gootoor.daos.MDBParamTable;
import com.prv.gootoor.lists.ParamTables;
import com.prv.gootoor.models.ParamTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by roger on 19/08/2017.
 */

public class Config {

    private static Config mInstance;

    private final static String  urlServer = "http://pressshare.gootoor.com/";
    public static String getUrlServer() {return urlServer;}

    private int  user_id;
    public int getUser_id() { return user_id;}
    public void setUser_id(int user_id) {this.user_id = user_id;}

    private String  user_pseudo;
    public String getUser_pseudo() { return user_pseudo;}
    public void setUser_pseudo(String user_pseudo) {this.user_pseudo = user_pseudo;}

    private String  user_email;
    public String getUser_email() { return user_email;}
    public void setUser_email(String user_email) {this.user_email = user_email;}

    private Double latitude;
    public Double getLatitude() { return latitude;}
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    private Double longitude;
    public Double getLongitude() { return longitude;}
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    private String  mapString;
    public String getMapString() { return mapString;}
    public void setMapString(String mapString) {this.mapString = mapString;}

    private String  user_nom;
    public String getUser_nom() { return user_nom;}
    public void setUser_nom(String user_nom) {this.user_nom = user_nom;}

    private Boolean user_newpassword = false ;
    public Boolean getUser_newpassword() { return user_newpassword;}
    public void setUser_newpassword(Boolean user_newpassword) {this.user_newpassword = user_newpassword;}

    private String  user_prenom;
    public String getUser_prenom() { return user_prenom;}
    public void setUser_prenom(String user_prenom) {this.user_prenom = user_prenom;}

    private String  previousView;
    public String getPreviousView() { return previousView;}
    public void setPreviousView(String previousView) {this.previousView = previousView;}

    private String  user_adresse;
    public String getUser_adresse() { return user_adresse;}
    public void setUser_adresse(String user_adresse) {this.user_adresse = user_adresse;}

    private String  user_codepostal;
    public String getUser_codepostal() { return user_codepostal;}
    public void setUser_codepostal(String user_codepostal) {this.user_codepostal = user_codepostal;}

    private String  user_ville;
    public String getUser_ville() { return user_ville;}
    public void setUser_ville(String user_ville) {this.user_ville = user_ville;}

    private String  user_pays;
    public String getUser_pays() { return user_pays;}
    public void setUser_pays(String user_pays) {this.user_pays = user_pays;}

    private String  verifpassword;
    public String getVerifpassword() { return verifpassword;}
    public void setVerifpassword(String verifpassword) {this.verifpassword = verifpassword;}

    private String  user_pass;
    public String getUser_pass() { return user_pass;}
    public void setUser_pass(String user_pass) {this.user_pass = user_pass;}

    private String  user_lastpass;
    public String getUser_lastpass() { return user_lastpass;}
    public void setUser_lastpass(String user_lastpass) {this.user_lastpass = user_lastpass;}

    private Boolean transaction_maj = false;
    public Boolean getTransaction_maj() { return transaction_maj;}
    public void setTransaction_maj(Boolean transaction_maj) {this.transaction_maj = transaction_maj;}

    private int  mess_badge;
    public int getMess_badge() { return mess_badge;}
    public void setMess_badge(int mess_badge) {this.mess_badge = mess_badge;}

    private int  trans_badge;
    public int getTrans_badge() { return trans_badge;}
    public void setTrans_badge(int trans_badge) {this.trans_badge = trans_badge;}

    private Double balance;
    public Double getBalance() { return balance;}
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    private int  failure_count;
    public int getFailure_count() { return failure_count;}
    public void setFailure_count(int failure_count) {this.failure_count = failure_count;}

    private int  level;
    public int getLevel() { return level;}
    public void setLevel(int level) {this.level = level;}

    private Boolean message_maj = false;
    public Boolean getMessage_maj() { return message_maj;}
    public void setMessage_maj(Boolean message_maj) {this.message_maj = message_maj;}

    private Double distanceProduct; //CLLocationDistance! //product are grouped and display according to this distance in meters
    public Double getDistanceProduct() { return distanceProduct;}
    public void setDistanceProduct(Double distanceProduct) {
        this.distanceProduct = distanceProduct;
    }

    private Double regionGeoLocat; //CLLocationDistance! //product are grouped and display according to this distance in meters
    public Double getRegionGeoLocat() { return regionGeoLocat;}
    public void setRegionGeoLocat(Double regionGeoLocat) {
        this.regionGeoLocat = regionGeoLocat;
    }

    private Double regionProduct; //CLLocationDistance! //region of product dowload on the map in meters
    public Double getRegionProduct() { return regionProduct;}
    public void setRegionProduct(Double regionProduct) {
        this.regionProduct = regionProduct;
    }

    private Double commisPourcBuy;//commission pourcentage for buy product
    public Double getCommisPourcBuy() { return commisPourcBuy;}
    public void setCommisPourcBuy(Double commisPourcBuy) {
        this.commisPourcBuy = commisPourcBuy;
    }

    private Double commisFixEx;//commission fix for exchange product
    public Double getCommisFixEx() { return commisFixEx;}
    public void setCommisFixEx(Double commisFixEx) {
        this.commisFixEx = commisFixEx;
    }

    private Double minLongitude;
    public Double getMinLongitude() { return minLongitude;}
    public void setMinLongitude(Double minLongitude) {
        this.minLongitude = minLongitude;
    }

    private Double maxLongitude;
    public Double getMaxLongitude() { return maxLongitude;}
    public void setMaxLongitude(Double maxLongitude) {this.maxLongitude = maxLongitude;}

    private Double minLatitude;
    public Double getMinLatitude() { return minLatitude;}
    public void setMinLatitude(Double minLatitude) {this.minLatitude = minLatitude;}

    private Double maxLatitude;
    public Double getMaxLatitude() { return maxLatitude;}
    public void setMaxLatitude(Double maxLatitude) {this.maxLatitude = maxLatitude;}

    private String  tokenString;
    public String getTokenString() { return tokenString;}
    public void setTokenString(String tokenString) {this.tokenString = tokenString;}

    private String  user_device;
    public String getUser_device() { return user_device;}
    public void setUser_device(String user_device) {
        this.user_device = user_device;
    }

    private int  typeCard_id;
    public int getTypeCard_id() { return typeCard_id;}
    public void setTypeCard_id(int typeCard_id) {this.typeCard_id = typeCard_id;}

    private Boolean isReturnToTab = false;
    public Boolean getIsReturnToTab() { return isReturnToTab;}
    public void setIsReturnToTab(Boolean isReturnToTab) {this.isReturnToTab = isReturnToTab;}

    private int  maxDayTrigger;
    public int getMaxDayTrigger() { return maxDayTrigger;}
    public void setMaxDayTrigger(int maxDayTrigger) {this.maxDayTrigger = maxDayTrigger;}

    private Boolean isRememberMe = false;
    public Boolean getIsRememberMe() { return isRememberMe;}
    public void setIsRememberMe(Boolean isRememberMe) {this.isRememberMe = isRememberMe;}

    private String  clientTokenBraintree;
    public String getClientTokenBraintree() { return clientTokenBraintree;}
    public void setClientTokenBraintree(String clientTokenBraintree) {this.clientTokenBraintree = clientTokenBraintree;}

    private String  user_braintreeID;
    public String getUser_braintreeID() { return user_braintreeID;}
    public void setUser_braintreeID(String user_braintreeID) {this.user_braintreeID = user_braintreeID;}

    private Double subscriptAmount;//amount to deposit when subcribing
    public Double getSubscriptAmount() { return subscriptAmount;}
    public void setSubscriptAmount(Double subscriptAmount) {this.subscriptAmount = subscriptAmount;}

    private Double minimumAmount;// minimum amount in the balance
    public Double getMinimumAmount() { return minimumAmount;}
    public void setMinimumAmount(Double minimumAmount) {this.minimumAmount = minimumAmount;}

    private Float heightImage;
    public Float getHeightImage() { return heightImage;}
    public void setHeightImage(Float heightImage) {this.heightImage = heightImage;}

    private Float widthImage;
    public Float getWidthImage() { return widthImage;}
    public void setWidthImage(Float widthImage) {this.widthImage = widthImage;}

    private Boolean isTimer = false;
    public Boolean getIsTimer() { return isTimer;}
    public void setIsTimer(Boolean isTimer) {this.isTimer = isTimer;}

    private Boolean isFingerPrint = false;
    public Boolean getIsFingerPrint() { return isFingerPrint;}
    public void setIsFingerPrint(Boolean isFingerPrint) {this.isFingerPrint = isFingerPrint;}

    private Double dureeTimer;
    public Double getDureeTimer() { return dureeTimer;}
    public void setDureeTimer(Double dureeTimer) {this.dureeTimer = dureeTimer;}

    private int  user_note;//note per 5 stars
    public int getUser_note() { return user_note;}
    public void setUser_note(int user_note) {this.user_note = user_note;}

    private int  user_countNote;//counter of note
    public int getUser_countNote() { return user_countNote;}
    public void setUser_countNote(int user_countNote) {this.user_countNote = user_countNote;}

    private String  colorApp;//theme color app
    public String getColorApp() { return colorApp;}
    public void setColorApp(String colorApp) {this.colorApp = colorApp;}

    private String  colorAppLabel;//theme color app label
    public String getColorAppLabel() { return colorAppLabel;}
    public void setColorAppLabel(String colorAppLabel) {this.colorAppLabel = colorAppLabel;}

    private String  colorAppText;//theme color app text
    public String getColorAppText() { return colorAppText;}
    public void setColorAppText(String colorAppText) {this.colorAppText = colorAppText;}

    private String  colorAppPlHd;//theme color app placeholder
    public String getColorAppPlHd() { return colorAppPlHd;}
    public void setColorAppPlHd(String colorAppPlHd) {this.colorAppPlHd = colorAppPlHd;}

    private String  colorAppBt;//theme color app button
    public String getColorAppBt() { return colorAppBt;}
    public void setColorAppBt(String colorAppBt) {this.colorAppBt = colorAppBt;}

    private final String domaineApp = "com.prv.pressshare.";
    public String getDomaineApp() {return domaineApp;}

    private final String fileParameters  = "fileParameters";
    public String getFileParameters() {return fileParameters;}

    private final String listProdImage  = "listProdImage";
    public String getListProdImage() {return listProdImage;}

    public void cleaner(final Context context) {


        user_id = 0;
        user_pseudo = "";
        user_email = "";
        latitude = 0.0;
        longitude = 0.0;
        mapString = "";
        user_nom = "";
        user_prenom = "";
        user_newpassword = false;
        previousView = "";
        user_adresse = "";
        user_codepostal = "";
        user_ville = "";
        user_pays = "";
        verifpassword = "";
        user_pass = "";
        user_lastpass = "";
        transaction_maj = false;
        mess_badge = 0;
        trans_badge = 0;
        balance = 0.0;
        failure_count = 0;
        level = 0;
        message_maj = false;
        minLongitude = 0.0;
        maxLongitude = 0.0;
        minLatitude = 0.0;
        maxLatitude = 0.0;
        tokenString = "";
        user_device = "android";
        typeCard_id = 0;
        isReturnToTab = false;
        //isFingerPrint = false; //pas d'initialisation pour flag fingerPrint
        clientTokenBraintree = "";
        user_braintreeID = "";
        subscriptAmount = 0.0;
        minimumAmount = 0.0;
        heightImage = 0f;
        widthImage = 0f;
        isTimer = false;
        dureeTimer = 5.0;
        user_note = 0;
        user_countNote = 0;
        colorApp = "FFDBA3";
        colorAppLabel = "AAAAAA";
        colorAppText = "000000";
        colorAppPlHd = "D8D8D8";
        colorAppBt = "5858FA";


        final SharedPreferences sharedPref = context.getSharedPreferences(fileParameters, Context.MODE_PRIVATE);

        if (sharedPref.getString(domaineApp+"colorApp","").equals(""))
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(domaineApp+"colorApp", colorApp);
            editor.apply();
        }
        else {
            colorApp = sharedPref.getString(domaineApp+"colorApp","");
        }


        if (sharedPref.getString(domaineApp+"colorAppLabel","").equals(""))
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(domaineApp+"colorAppLabel", colorAppLabel);
            editor.apply();
        }
        else {
            colorAppLabel = sharedPref.getString(domaineApp+"colorAppLabel","");
        }


        if (sharedPref.getString(domaineApp+"colorAppText","").equals(""))
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(domaineApp+"colorAppText", colorAppText);
            editor.apply();
        }
        else {
            colorAppText = sharedPref.getString(domaineApp+"colorAppText","");
        }


        if (sharedPref.getString(domaineApp+"colorAppPlHd","").equals(""))
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(colorAppPlHd+"colorAppPlHd", colorAppPlHd);
            editor.apply();
        }
        else {
            colorAppPlHd = sharedPref.getString(domaineApp+"colorAppPlHd","");
        }


        if (sharedPref.getString(domaineApp+"colorAppBt","").equals(""))
        {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(domaineApp+"colorAppBt", colorAppBt);
            editor.apply();
        }
        else {
            colorAppBt = sharedPref.getString(domaineApp+"colorAppBt","");
        }


        MDBParamTable.sharedInstance().getAllParamTables(context, new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, JSONArray anArray, String errorString) {

                if (success) {

                    try {

                        ParamTables.sharedInstance().setParamTableArray(anArray);
                        JSONObject dico = (JSONObject) ParamTables.sharedInstance().getParamTableArray().get(0);
                        ParamTable param = new ParamTable(context, dico);
                        distanceProduct = param.getDistanceProduct();
                        regionGeoLocat = param.getRegionGeoLocat();
                        regionProduct = param.getRegionProduct();
                        commisPourcBuy = param.getCommisPourcBuy();
                        commisFixEx = param.getCommisFixEx();
                        maxDayTrigger = param.getMaxDayTrigger();
                        subscriptAmount = param.getSubscriptAmount();
                        minimumAmount = param.getMinimumAmount();


                        SharedPreferences.Editor editor = sharedPref.edit();
                        colorApp = param.getColorApp();
                        editor.putString(domaineApp+"colorApp", colorApp);

                        colorAppLabel = param.getColorAppLabel();
                        editor.putString(domaineApp+"colorAppLabel", colorAppLabel);

                        colorAppText = param.getColorAppText();
                        editor.putString(domaineApp+"colorAppText", colorAppText);

                        colorAppPlHd = param.getColorAppPlHd();
                        editor.putString(domaineApp+"colorAppPlHd", colorAppPlHd);

                        colorAppBt = param.getColorAppBt();
                        editor.putString(domaineApp+"colorAppBt", colorAppBt);

                        editor.apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                else {

                    distanceProduct = 0.0;
                    regionGeoLocat = 0.0;
                    regionProduct = 0.0;
                    commisPourcBuy = 0.0;
                    commisFixEx = 0.0;
                    maxDayTrigger = 0;
                    subscriptAmount = 0.0;
                    minimumAmount = 0.0;

                }

            }
        });




    }


    public static Config sharedInstance() {
        if (mInstance == null) {
            mInstance = new Config();
        }
        return mInstance;
    }

}
