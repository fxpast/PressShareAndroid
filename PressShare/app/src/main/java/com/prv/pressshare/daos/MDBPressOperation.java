package com.prv.pressshare.daos;

//
//  MDBPressOperation
//  PressShare
//
//  Created by MacbookPRV on 24/08/2017.
//  Copyright © 2017 Pastouret Roger. All rights reserved.
//


import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prv.pressshare.R;
import com.prv.pressshare.models.PressOperation;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MyTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class MDBPressOperation {


    private static MDBPressOperation  mInstance;



    public void  getAllOperations(final Context context, final int userId, final MDBInterfaceArray completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandlerArray(false, null, context.getString(R.string.errorConnection));
            return;
        }


        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_getAllOperations.php", new Response.Listener<String>() {
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
                        JSONArray resultArray = (JSONArray) resultDico.get("alloperations");

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
                params.put("user_id", String.valueOf(userId));
                params.put("lang",context.getString(R.string.lang));


                //Adding parameters to request
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }




    public void  setAddOperation(final Context context, final PressOperation operation, final MDBInterface completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandler(false,context.getString(R.string.errorConnection));
            return;
        }

        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_addOperation.php", new Response.Listener<String>() {
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
                    completion.completionHandler(false, context.getString(R.string.errorAddOperat));
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
                params.put("op_wording",operation.getOp_wording());
                params.put("op_amount", String.valueOf(operation.getOp_amount()));
                params.put("op_type", String.valueOf(operation.getOp_type()));
                params.put("user_id", String.valueOf(operation.getUser_id()));
                params.put("lang",context.getString(R.string.lang));


                //Adding parameters to request
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }



    public void  getBraintreeToken(final Context context, final int userId, final MDBInterfaceString completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandlerString(false, null, context.getString(R.string.errorConnection));
            return;
        }


        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"bt_getToken.php", new Response.Listener<String>() {
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
                        String token = (String) resultDico.get("clientToken");

                        completion.completionHandlerString(true, token, null);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else  {
                    completion.completionHandlerString(false, null, anError);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                completion.completionHandlerString(false, null, error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                // // Create your request string with parameter name as defined in PHP file
                params.put("user_id", String.valueOf(userId));
                params.put("lang",context.getString(R.string.lang));


                //Adding parameters to request
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }





    public void  operationBraintree(final Context context, final String type, final int userId, final Double amount, final MDBInterfaceString completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandlerString(false, null, context.getString(R.string.errorConnection));
            return;
        }


        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"bt_"+type+".php", new Response.Listener<String>() {
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
                        String rest = (String) resultDico.get("btTransaction");

                        completion.completionHandlerString(true, rest, null);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else  {
                    completion.completionHandlerString(false, null, anError);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                completion.completionHandlerString(false, null, error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                // // Create your request string with parameter name as defined in PHP file
                params.put("user_id", String.valueOf(userId));
                params.put("amount", String.valueOf(amount));
                params.put("lang",context.getString(R.string.lang));


                //Adding parameters to request
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }




    public static MDBPressOperation sharedInstance() {
        if (mInstance == null) {
            mInstance = new MDBPressOperation();
        }
        return mInstance;
    }

}
