package com.prv.pressshare.daos;

//
//  MDBFeedback
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
import com.prv.pressshare.models.Feedback;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MyTools;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class MDBFeedback {

    private static MDBFeedback  mInstance;




    public void  setAddFeedback(final Context context, final Feedback feedback, final MDBInterface completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandler(false,context.getString(R.string.errorConnection));
            return;
        }

        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_addFeedback.php", new Response.Listener<String>() {
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
                params.put("comment", String.valueOf(feedback.getComment()));
                params.put("origin", String.valueOf(feedback.getOrigin()));
                params.put("lang",context.getString(R.string.lang));

                //Adding parameters to request
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }


    public static MDBFeedback sharedInstance() {
        if (mInstance == null) {
            mInstance = new MDBFeedback();
        }
        return mInstance;
    }
}
