package com.prv.pressshare.utils;


//
//  MyTools
//  PressShare
//
//  Created by MacbookPRV on 19/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prv.pressshare.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class MyTools {


    private static MyTools mInstance;


    public  void geocodeAddressString(Context context, final String adresseString, final  GeoLocInterface completion) {

        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandlerGeo(false, null ,context.getString(R.string.errorConnection));
            return;
        }


        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://maps.google.com/maps/api/geocode/json?address="+adresseString+"&sensor=false", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //If we are getting success from server
                JSONObject resultDico = null;
                try {
                    resultDico = (JSONObject) new JSONTokener(response).nextValue();
                } catch (JSONException e){
                    completion.completionHandlerGeo(true,  null, e.toString());
                    e.printStackTrace();
                }

                String success  = null;
                String anError = null;

                try {
                    success = (String) resultDico.get("status");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (success.equals("OK")) {

                    try {

                        JSONObject jsonLocation = ((JSONArray)resultDico.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

                        completion.completionHandlerGeo(true,  jsonLocation, null);

                    } catch (JSONException e) {
                        completion.completionHandlerGeo(false,  null, e.toString());
                        e.printStackTrace();
                    }

                } else {

                    try {
                        anError = (String) resultDico.get("error_message");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    completion.completionHandlerGeo(false,  null, anError);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                completion.completionHandlerGeo(false, null, error.toString());
            }
        }){
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);



    }

    public void displayAlert(Context context, CharSequence text) {

        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context,text,duration);
        toast.show();

    }

    public Date dateFromString(String date, String format) {

        DateFormat df = new SimpleDateFormat(format, Locale.ENGLISH);
        Date dt = new Date();
        try {
            dt = df.parse(date);
        }
        catch (Exception e) {
            Log.i("dateFromString", e.toString());
        }

        return  dt;

    }


    public void  performUIUpdatesOnMain (Context context, final MainThreadInterface completion) {

        // Get a handler that can be used to post to the main thread
        Handler mainHandler = new Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {

                completion.completionUpdateMain();
            }
        };
        mainHandler.post(myRunnable);


    }

    public void showHelp(String title, Context context) {

        displayAlert(context, title+" is showing");

    }




    public  boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            return true;
        }
        return false;
    }


    public static MyTools sharedInstance() {
        if (mInstance == null) {
            mInstance = new MyTools();
        }
        return mInstance;
    }


}
