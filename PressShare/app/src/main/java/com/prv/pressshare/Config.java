package com.prv.pressshare;

/**
 * Created by Belal on 11/14/2015.
 */
public class Config {
    //URL to our login.php file
    public static final String LOGIN_URL = "http://presssharedev.fxpast.com/api_signIn.php";
    //URL to our api_getProductsByCoord.php file
    public static final String ProductsByCoord_URL = "http://presssharedev.fxpast.com/api_getProductsByCoord.php";


    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_PSEUDO = "user_pseudo";
    public static final String KEY_PASSWORD = "user_pass";
    //If server response is equal to this that means login is successful
    public static final String KEY_LANG = "lang";
    //If server response is equal to this that means login is successful
    public static final String KEY_user_id = "user_id";
    //If server response is equal to this that means login is successful
    public static final String KEY_minLon = "minLon";
    //If server response is equal to this that means login is successful
    public static final String KEY_maxLon = "maxLon";
    //If server response is equal to this that means login is successful
    public static final String KEY_minLat = "minLat";
    //If server response is equal to this that means login is successful
    public static final String KEY_maxLat = "maxLat";



    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";
    //If server response is equal to this that means login is successful
    public static final String PRODUCT_allproducts = "allproducts";


}
