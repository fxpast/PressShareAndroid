package com.prv.gootoor.daos;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prv.gootoor.R;
import com.prv.gootoor.models.Card;
import com.prv.gootoor.models.Creneau;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MyTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

//
//  MDBCreneau
//  GoOtoor
//
//  Created by MacbookPRV on 28/04/2018.
//  Copyright © 2018 Pastouret Roger. All rights reserved.
//


public class MDBCreneau {

    private static MDBCreneau mInstance;


    public void  getCreneauxProd(final Context context, final int prod_id, final MDBInterfaceArray completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandlerArray(false, null, context.getString(R.string.errorConnection));
            return;
        }


        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_getCreneauxProd.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server
                JSONObject resultDico = null;
                try {
                    resultDico = (JSONObject) new JSONTokener(response).nextValue();
                } catch (JSONException e){
                    e.printStackTrace();
                }


                String success  = null;
                String anError = null;

                try {
                    success = (String) resultDico.get("success");
                    anError = (String) resultDico.get("error");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (success.equals("1")){
                    try {
                        JSONArray resultArray = (JSONArray) resultDico.get("allcreneaux");

                        completion.completionHandlerArray(true, resultArray, null);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else  {
                    completion.completionHandlerArray(false, null, anError);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                completion.completionHandlerArray(false, null, error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                // // Create your request string with parameter name as defined in PHP file
                params.put("prod_id", String.valueOf(prod_id));
                params.put("lang",context.getString(R.string.lang));


                //Adding parameters to request
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }




    public void  setDeleteCreneau(final Context context, final Creneau creneau, final MDBInterface completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandler(false,context.getString(R.string.errorConnection));
            return;
        }

        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_delCreneau.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server
                JSONObject resultDico = null;
                try {
                    resultDico = (JSONObject) new JSONTokener(response).nextValue();
                } catch (JSONException e){
                    e.printStackTrace();
                }


                String success  = null;

                try {
                    success = (String) resultDico.get("success");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (success.equals("1")){


                    completion.completionHandler(true, null);


                } else  {
                    completion.completionHandler(false, context.getString(R.string.impossibleDelCre));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                completion.completionHandler(false, error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                // // Create your request string with parameter name as defined in PHP file
                params.put("cre_id", String.valueOf(creneau.getCre_id()));
                params.put("lang",context.getString(R.string.lang));


                //Adding parameters to request
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }




    public void  setAddCreneau(final Context context, final Creneau creneau, final MDBInterface completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandler(false,context.getString(R.string.errorConnection));
            return;
        }

        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_addCreneau.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server
                JSONObject resultDico = null;
                try {
                    resultDico = (JSONObject) new JSONTokener(response).nextValue();
                } catch (JSONException e){
                    e.printStackTrace();
                }


                String success  = null;
                String anError = null;

                try {
                    success = (String) resultDico.get("success");
                    anError = (String) resultDico.get("error");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (success.equals("1")){


                    completion.completionHandler(true, null);


                } else  {
                    completion.completionHandler(false, anError);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                completion.completionHandler(false, error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                // // Create your request string with parameter name as defined in PHP file
                params.put("prod_id", String.valueOf(creneau.getProd_id()));
                params.put("cre_dateDebut", MyTools.sharedInstance().stringFromDate(creneau.getCre_dateDebut()));
                params.put("cre_dateFin",  MyTools.sharedInstance().stringFromDate(creneau.getCre_dateFin()));
                params.put("cre_repeat", String.valueOf(creneau.getCre_repeat()));
                params.put("cre_latitude", String.valueOf(creneau.getCre_latitude()));
                params.put("cre_longitude", String.valueOf(creneau.getCre_longitude()));
                params.put("cre_mapString", creneau.getCre_mapString());
                params.put("lang",context.getString(R.string.lang));


                //Adding parameters to request
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }



    public static MDBCreneau sharedInstance() {
        if (mInstance == null) {
            mInstance = new MDBCreneau();
        }
        return mInstance;
    }

}
