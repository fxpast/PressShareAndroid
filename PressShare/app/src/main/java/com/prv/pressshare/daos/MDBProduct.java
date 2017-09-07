package com.prv.pressshare.daos;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.prv.pressshare.R;
import com.prv.pressshare.models.Product;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.MyTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by roger on 22/08/2017.
 */

public class MDBProduct {

    private static MDBProduct  mInstance;


    public void  getProduct(final Context context, final int prod_id, final MDBInterfaceArray completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandlerArray(false, null, context.getString(R.string.errorConnection));
            return;
        }


        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_getProduct.php", new Response.Listener<String>() {
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
                        JSONArray resultArray = (JSONArray) resultDico.get("aproduct");

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



    public void  getProductsByTrader(final Context context, final int userId, final MDBInterfaceArray completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandlerArray(false, null, context.getString(R.string.errorConnection));
            return;
        }


        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_getProductsByTrader.php", new Response.Listener<String>() {
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
                        JSONArray resultArray = (JSONArray) resultDico.get("allproducts");

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



    public void  getProductsByUser(final Context context, final int userId, final MDBInterfaceArray completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandlerArray(false, null, context.getString(R.string.errorConnection));
            return;
        }


        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_getProductsByUser.php", new Response.Listener<String>() {
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
                        JSONArray resultArray = (JSONArray) resultDico.get("allproducts");

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



    public void  getProductsByCoord(final Context context, final int userId, final Double minLon, final Double maxLon, final Double minLat, final Double maxLat,  final MDBInterfaceArray completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandlerArray(false, null, context.getString(R.string.errorConnection));
            return;
        }


        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_getProductsByCoord.php", new Response.Listener<String>() {
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
                        JSONArray resultArray = (JSONArray) resultDico.get("allproducts");

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
                params.put("minLon", String.valueOf(minLon));
                params.put("maxLon", String.valueOf(maxLon));
                params.put("minLat", String.valueOf(minLat));
                params.put("maxLat", String.valueOf(maxLat));
                params.put("lang",context.getString(R.string.lang));


                //Adding parameters to request
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }




    public void  setUpdateProduct(final Context context, final String typeUpdate, final Product product, final MDBInterface completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandler(false,context.getString(R.string.errorConnection));
            return;
        }

        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_update"+typeUpdate+".php", new Response.Listener<String>() {
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


                if (typeUpdate.equals("ProductTrans")) {

                    params.put("prod_id", String.valueOf(product.getProd_id()));
                    params.put("prod_oth_user", String.valueOf(product.getProd_oth_user()));
                    params.put("prod_hidden", String.valueOf(product.getProd_hidden()));
                    params.put("prod_closed", String.valueOf(product.getProd_closed()));
                    params.put("lang", context.getString(R.string.lang));
                }
                else if (typeUpdate.equals("Product")) {

                    if (product.getProdImageOld().equals("")) {

                        params.put("prod_id", String.valueOf(product.getProd_id()));
                        params.put("prod_nom", String.valueOf(product.getProd_nom()));
                        params.put("prod_date", String.valueOf(product.getProd_date()));
                        params.put("prod_prix", String.valueOf(product.getProd_prix()));
                        params.put("prod_by_user", String.valueOf(product.getProd_by_user()));
                        params.put("prod_by_cat", String.valueOf(product.getProd_by_cat()));
                        params.put("prod_latitude", String.valueOf(product.getProd_latitude()));
                        params.put("prod_longitude", String.valueOf(product.getProd_longitude()));
                        params.put("prod_mapString", String.valueOf(product.getProd_mapString()));
                        params.put("prod_comment", String.valueOf(product.getProd_comment()));
                        params.put("prod_tempsDispo", String.valueOf(product.getProd_tempsDispo()));
                        params.put("prod_hidden", String.valueOf(product.getProd_hidden()));
                        params.put("prod_echange", String.valueOf(product.getProd_echange()));
                        params.put("prod_closed", String.valueOf(product.getProd_closed()));
                        params.put("prod_etat", String.valueOf(product.getProd_etat()));
                        params.put("prod_imageUrl", String.valueOf(product.getProd_imageUrl()));
                        params.put("prodImageOld", String.valueOf(product.getProdImageOld()));
                        params.put("lang", context.getString(R.string.lang));

                    }
                    else  {

                        params.put("prod_id", String.valueOf(product.getProd_id()));
                        params.put("prod_nom", String.valueOf(product.getProd_nom()));
                        params.put("prod_date", String.valueOf(product.getProd_date()));
                        params.put("prod_prix", String.valueOf(product.getProd_prix()));
                        params.put("prod_by_user", String.valueOf(product.getProd_by_user()));
                        params.put("prod_by_cat", String.valueOf(product.getProd_by_cat()));
                        params.put("prod_latitude", String.valueOf(product.getProd_latitude()));
                        params.put("prod_longitude", String.valueOf(product.getProd_longitude()));
                        params.put("prod_mapString", String.valueOf(product.getProd_mapString()));
                        params.put("prod_comment", String.valueOf(product.getProd_comment()));
                        params.put("prod_tempsDispo", String.valueOf(product.getProd_tempsDispo()));
                        params.put("prod_hidden", String.valueOf(product.getProd_hidden()));
                        params.put("prod_echange", String.valueOf(product.getProd_echange()));
                        params.put("prod_closed", String.valueOf(product.getProd_closed()));
                        params.put("prod_etat", String.valueOf(product.getProd_etat()));
                        params.put("prod_imageUrl", String.valueOf(product.getProd_imageUrl()));
                        params.put("prodImageOld", String.valueOf(product.getProdImageOld()));
                        params.put("lang", context.getString(R.string.lang));


                    }


                }


                //Adding parameters to request
                return params;
            }

            @Override
            public String getBodyContentType() {

                if (typeUpdate.equals("Product") && !product.getProdImageOld().equals("")) {

                    return "multipart/form-data; boundary="+product.getProd_imageUrl();
                }
                else {
                    return super.getBodyContentType();

                }

            }

            @Override
            public byte[] getBody() throws AuthFailureError {


                if (typeUpdate.equals("Product") && !product.getProdImageOld().equals("")) {

                    Bitmap bitmap =((BitmapDrawable) product.getProd_image().getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 1, stream);
                    final byte[] imageDataKey = stream.toByteArray();

                    return createBodyWithParameters(getParams(), "file", imageDataKey, product.getProd_imageUrl());
                }
                else {
                    return super.getBody();
                }

            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }





    public void  setDeleteProduct(final Context context, final Product product, final MDBInterface completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandler(false,context.getString(R.string.errorConnection));
            return;
        }

        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_delProduct.php", new Response.Listener<String>() {
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
                    completion.completionHandler(false, context.getString(R.string.impossibleDeldPr));
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
                params.put("prod_id", String.valueOf(product.getProd_id()));
                params.put("prod_imageUrl", product.getProd_imageUrl());
                params.put("lang",context.getString(R.string.lang));


                //Adding parameters to request
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }




    public void  setAddProduct(final Context context, final Product product, final MDBInterface completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandler(false,context.getString(R.string.errorConnection));
            return;
        }

        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getUrlServer()+"api_addProduct.php", new Response.Listener<String>() {
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


                params.put("prod_nom", String.valueOf(product.getProd_nom()));
                params.put("prod_date", String.valueOf(product.getProd_date()));
                params.put("prod_prix", String.valueOf(product.getProd_prix()));
                params.put("prod_by_cat", String.valueOf(product.getProd_by_cat()));
                params.put("prod_latitude", String.valueOf(product.getProd_latitude()));
                params.put("prod_longitude", String.valueOf(product.getProd_longitude()));
                params.put("prod_mapString", String.valueOf(product.getProd_mapString()));
                params.put("prod_comment", String.valueOf(product.getProd_comment()));
                params.put("prod_tempsDispo", String.valueOf(product.getProd_tempsDispo()));
                params.put("prod_echange", String.valueOf(product.getProd_echange()));
                params.put("prod_closed", String.valueOf(product.getProd_closed()));
                params.put("prod_etat", String.valueOf(product.getProd_etat()));
                params.put("prod_imageUrl", String.valueOf(product.getProd_imageUrl()));
                params.put("lang", context.getString(R.string.lang));


                //Adding parameters to request
                return params;
            }

            @Override
            public String getBodyContentType() {

                return "multipart/form-data; boundary="+product.getProd_imageUrl();

            }

            @Override
            public byte[] getBody() throws AuthFailureError {

                return createBodyWithParameters(getParams(), "file", createImageData(product.getProd_image()) , product.getProd_imageUrl());

            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);




    }


    private byte[] createImageData(ImageView iv) {

        Bitmap bitmap =((BitmapDrawable) iv.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 1, stream);
        return  stream.toByteArray();

    }



    private byte[]  createBodyWithParameters(Map<String, String> parameters, String filePathKey, byte[] imageDataKey, String boundary) {


        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {

            String chaine = "";


            if (parameters != null) {
                for (String key : parameters.keySet()) {
                    if (parameters.get(key) != null) {

                        chaine = "--"+boundary+"\r\n";
                        dos.writeBytes(chaine);

                        chaine = "Content-Disposition: form-data; name=\"" + key + "\"" + "\r\n\r\n";
                        dos.writeBytes(chaine);

                        chaine = parameters.get(key).toString()+"\r\n";
                        dos.writeBytes(chaine);



                    }
                }
            }


            final String filename = boundary+".jpg";
            final String  mimetype = "image/jpg";

            chaine = "--"+boundary+"\r\n";
            dos.writeBytes(chaine);

            chaine = "Content-Disposition: form-data; name=\""+filePathKey+"\"; filename=\""+filename+"\"\r\n";
            dos.writeBytes(chaine);

            chaine = "Content-Type: "+mimetype+"\r\n\r\n";
            dos.writeBytes(chaine);

            ByteArrayInputStream fileInputStream = new ByteArrayInputStream(imageDataKey);
            int bytesAvailable = fileInputStream.available();
            int maxBufferSize = 1024 * 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            // read file and write it into form...
            int  bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }


            chaine = "\r\n";
            dos.writeBytes(chaine);

            chaine = "--"+boundary+"--\r\n";
            dos.writeBytes(chaine);


        }catch (IOException e){
            e.printStackTrace();
        }


        return bos.toByteArray();

    }



    public static MDBProduct sharedInstance() {
        if (mInstance == null) {
            mInstance = new MDBProduct();
        }
        return mInstance;
    }


}
