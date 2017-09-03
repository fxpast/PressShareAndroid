package com.prv.pressshare.daos;


//
//  MDBCommission
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
import com.prv.pressshare.models.Commission;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MyTools;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class MDBCommission {


    private static MDBCommission  mInstance;



    public void  setAddCommission(final Context context, final Commission commission, final Double balance, final MDBInterface completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandler(false,context.getString(R.string.errorConnection));
            return;
        }

        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_addCommission.php", new Response.Listener<String>() {
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
                params.put("user_id", String.valueOf(commission.getUser_id()));
                params.put("product_id", String.valueOf(commission.getProduct_id()));
                params.put("com_amount", String.valueOf(commission.getCom_amount()));
                params.put("balance", String.valueOf(balance));
                params.put("lang",context.getString(R.string.lang));

                //Adding parameters to request
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }


    public static MDBCommission sharedInstance() {
        if (mInstance == null) {
            mInstance = new MDBCommission();
        }
        return mInstance;
    }


}
