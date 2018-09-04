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
import com.prv.gootoor.models.Transaction;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.MyTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by roger on 22/08/2017.
 */

public class MDBTransact {

    private static MDBTransact  mInstance;


    public void  getAllTransactions(final Context context, final int userId, final MDBInterfaceArray completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandlerArray(false, null, context.getString(R.string.errorConnection));
            return;
        }



        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_getAllTransactions.php", new Response.Listener<String>() {
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
                        JSONArray resultArray = (JSONArray) resultDico.get("alltransactions");

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




    public void  setUpdateTransaction(final Context context, final Transaction transaction, final MDBInterface completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandler(false,context.getString(R.string.errorConnection));
            return;
        }

        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_updateTransaction.php", new Response.Listener<String>() {
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
                    completion.completionHandler(false, context.getString(R.string.errorUpdateTrans));
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
                params.put("trans_id", String.valueOf(transaction.getTrans_id()));
                params.put("trans_avis", String.valueOf(transaction.getTrans_avis()));
                params.put("trans_valid", String.valueOf(transaction.getTrans_valid()));
                params.put("trans_note", String.valueOf(transaction.getTrans_note()));
                params.put("lang",context.getString(R.string.lang));


                //Adding parameters to request
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);




    }






    public void  setAddTransaction(final Context context, final Transaction transaction, final MDBInterface completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandler(false,context.getString(R.string.errorConnection));
            return;
        }

        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_addTransaction.php", new Response.Listener<String>() {
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
                    completion.completionHandler(false, context.getString(R.string.errorAddTrans));
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
                params.put("trans_type", String.valueOf(transaction.getTrans_type()));
                params.put("trans_valid", String.valueOf(transaction.getTrans_valid()));
                params.put("client_id", String.valueOf(transaction.getClient_id()));
                params.put("prod_id", String.valueOf(transaction.getProd_id()));
                params.put("trans_wording", transaction.getTrans_wording());
                params.put("trans_amount", String.valueOf(transaction.getTrans_amount()));
                params.put("vendeur_id", String.valueOf(transaction.getVendeur_id()));
                params.put("proprietaire", String.valueOf(transaction.getProprietaire()));
                params.put("trans_avis", String.valueOf(transaction.getTrans_avis()));
                params.put("lang",context.getString(R.string.lang));


                //Adding parameters to request
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);




    }


    public static MDBTransact sharedInstance() {
        if (mInstance == null) {
            mInstance = new MDBTransact();
        }
        return mInstance;
    }


}
