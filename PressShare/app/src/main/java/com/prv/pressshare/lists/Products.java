package com.prv.pressshare.lists;

import org.json.JSONArray;

/**
 * Created by roger on 22/08/2017.
 */

public class Products {

    private static Products  mInstance;

    private JSONArray productsArray;
    public JSONArray getProductsArray() { return productsArray;}
    public void setProductsArray(JSONArray productsArray) {this.productsArray = productsArray;}

    private JSONArray productsUserArray;
    public JSONArray getProductsUserArray() { return productsUserArray;}
    public void setProductsUserArray(JSONArray productsUserArray) {this.productsUserArray = productsUserArray;}

    private JSONArray productsTraderArray;
    public JSONArray getProductsTraderArray() { return productsTraderArray;}
    public void setProductsTraderArray(JSONArray productsTraderArray) {this.productsTraderArray = productsTraderArray;}


    private Products(){

        productsArray = new JSONArray();
        productsUserArray = new JSONArray();
        productsTraderArray = new JSONArray();

    }

    public static Products sharedInstance() {
        if (mInstance == null) {
            mInstance = new Products();
        }
        return mInstance;
    }


}
