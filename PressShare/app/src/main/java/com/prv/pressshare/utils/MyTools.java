package com.prv.pressshare.utils;


//
//  MyTools
//  PressShare
//
//  Created by MacbookPRV on 19/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.prv.pressshare.R;
import com.prv.pressshare.activities.HelpActivity;
import com.prv.pressshare.daos.MDBInterfaceArray;
import com.prv.pressshare.daos.MDBMessage;
import com.prv.pressshare.daos.MDBTransact;
import com.prv.pressshare.lists.Messages;
import com.prv.pressshare.lists.Transactions;
import com.prv.pressshare.models.Message;
import com.prv.pressshare.models.Transaction;
import com.prv.pressshare.views.ImageDownloader;
import com.prv.pressshare.views.ImageInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class MyTools {


    private static MyTools mInstance;
    private final Config mConfig = Config.sharedInstance();


    public void checkBadge(final Context context, final CheckBadgeInterface completion) {


        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHdlerBadge(false ,context.getString(R.string.errorConnection));
            return;
        }

        mConfig.setIsTimer(true);

        MDBMessage.sharedInstance().getAllMessages(context, mConfig.getUser_id(), new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                if (success) {
                    Messages.sharedInstance().setMessagesArray(anArray);


                    int i = 0;
                    try {

                        JSONArray messagesJSON = Messages.sharedInstance().getMessagesArray();

                        for (int j = 0; j < messagesJSON.length(); j++) {

                            JSONObject dictionary = messagesJSON.getJSONObject(j);
                            Message message = new Message(context, dictionary);
                            if (message.getDestinataire() == mConfig.getUser_id() && !message.getDeja_lu()){
                                i+=1;
                            }
                        }
                        if (i > mConfig.getMess_badge()) {
                            mConfig.setMess_badge(i);
                            completion.completionHdlerBadge(true, "mess_badge");
                        } else {
                            completion.completionHdlerBadge(false, null);
                        }

                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }


                    MDBTransact.sharedInstance().getAllTransactions(context, mConfig.getUser_id(), new MDBInterfaceArray() {
                        @Override
                        public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                            if (success) {
                                Transactions.sharedInstance().setTransactionArray(anArray);

                                int i = 0;
                                try {

                                    JSONArray transJSON = Messages.sharedInstance().getMessagesArray();

                                    for (int j = 0; j < transJSON.length(); j++) {

                                        JSONObject dictionary = transJSON.getJSONObject(j);
                                        Transaction tran1 = new Transaction(dictionary);
                                        if (tran1.getTrans_valid() != 1 && tran1.getTrans_valid() != 2) {
                                            i+=1;
                                        }
                                    }

                                    if (i > mConfig.getTrans_badge()) {
                                        mConfig.setTrans_badge(i);
                                        completion.completionHdlerBadge(true, "trans_badge");
                                    } else {
                                        completion.completionHdlerBadge(false, null);
                                    }

                                    mConfig.setIsTimer(false);

                                }  catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            } else {

                                MyTools.sharedInstance().performUIUpdatesOnMain(context, new MainThreadInterface() {
                                    @Override
                                    public void completionUpdateMain() {

                                        MyTools.sharedInstance().displayAlert(context, errorString);

                                    }
                                });

                            }
                        }
                    });




                } else {

                    MyTools.sharedInstance().performUIUpdatesOnMain(context, new MainThreadInterface() {
                        @Override
                        public void completionUpdateMain() {

                            MyTools.sharedInstance().displayAlert(context, errorString);

                        }
                    });


                }

            }
        });


    }

    public  void geocodeAddressString(Context context, final String adresseString, final  GeoLocInterface completion) {

        if (!MyTools.sharedInstance().isConnectedToNetwork(context)) {

            completion.completionHandlerGeo(false, null ,context.getString(R.string.errorConnection));
            return;
        }

        String adresseStr = adresseString.replace(" ", "%20");

        //Creating a string request

        StringRequest stringRequest = new StringRequest(Request.Method.GET,"http://maps.google.com/maps/api/geocode/json?address="+adresseStr+"&sensor=false", new Response.Listener<String>() {
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

        Intent intent = new Intent(context, HelpActivity.class);
        intent.putExtra(mConfig.getDomaineApp()+"helpTitre",title);
        context.startActivity(intent);


    }




    public  boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            return true;
        }
        return false;
    }


    public void saveImageArchive(final Context context, final String prod_imageUrl, final String folder, final SaveImageInterface completion) {

        final Config mConfig;
        String jpg = ".jpg";

        if (folder.equals("images/")) {
            jpg = ".jpg";
        } else if (folder.equals("images_cb/")) {

            jpg = "";
        }

        mConfig = Config.sharedInstance();
        final SharedPreferences listProdImage = context.getSharedPreferences(mConfig.getListProdImage(), Context.MODE_PRIVATE);

        final File file;

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {

            file = new File(context.getExternalFilesDir(null), prod_imageUrl+jpg);

        } else {

             file = new File(context.getFilesDir(), prod_imageUrl+jpg);
        }


        if (file.exists()) {

            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
            Drawable drawable = new  BitmapDrawable(context.getResources(), bitmap);
            completion.completionHandlerSaveImage(true, drawable);

        } else {


            String  mUrl = mConfig.getUrlServer()+folder+prod_imageUrl+jpg;

            ImageDownloader downloadImage =  new ImageDownloader(context, new ImageInterface() {
                @Override
                public void completionHandlerImage(final Boolean success, final Drawable drawable) {

                    performUIUpdatesOnMain(context, new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {

                            ImageView imageView = new ImageView(context);

                            try {

                                Bitmap bitmap;

                                if (success) {
                                    imageView.setImageDrawable(drawable);
                                    imageView.setTag(prod_imageUrl);
                                    imageView.setDrawingCacheEnabled(true);
                                    imageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                                    imageView.layout(0,0, imageView.getMeasuredWidth(), imageView.getMeasuredHeight());
                                    imageView.buildDrawingCache(true);

                                    bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
                                    imageView.setDrawingCacheEnabled(false); //clean cache


                                    Gson gson = new Gson();
                                    String gsonArray="";
                                    SharedPreferences.Editor editor;

                                    file.createNewFile();
                                    FileOutputStream ostream = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                                    ostream.close();

                                    if (listProdImage.getString(mConfig.getDomaineApp()+"listProdImage","").equals(""))
                                    {

                                        List<String> arrayImage = new ArrayList<>();
                                        arrayImage.add((new Date()).toString());
                                        arrayImage.add(file.getPath());

                                        gsonArray = gson.toJson(arrayImage);
                                        editor = listProdImage.edit();
                                        editor.putString(mConfig.getDomaineApp()+"listProdImage", gsonArray);
                                        editor.apply();

                                    }
                                    else {

                                        gsonArray = listProdImage.getString(mConfig.getDomaineApp()+"listProdImage","");
                                        Type type = new TypeToken<List<String>>(){}.getType();
                                        List<String> arrayImage = gson.fromJson(gsonArray, type);
                                        String dateText = arrayImage.get(0);
                                        Date aDate = dateFromString(dateText, "yyyy-MM-dd HH:mm:ss");
                                        Calendar c = Calendar.getInstance();
                                        c.add(Calendar.DATE, 7);
                                        Date futureDate = new Date(c.getTimeInMillis());

                                        if (aDate.compareTo(futureDate) > 0) {

                                            for (int i = 1; i <arrayImage.size() ; i++) {

                                                File aFile = new File(arrayImage.get(i));
                                                aFile.delete();
                                            }


                                            arrayImage  = new ArrayList<>();
                                            arrayImage.add((new Date()).toString());
                                            arrayImage.add(file.getPath());

                                        } else {

                                            arrayImage.add(file.getPath());

                                        }

                                        gsonArray = gson.toJson(arrayImage);
                                        editor = listProdImage.edit();
                                        editor.putString(mConfig.getDomaineApp()+"listProdImage", gsonArray);
                                        editor.apply();

                                    }

                                    completion.completionHandlerSaveImage(true, drawable);

                                } else {

                                    completion.completionHandlerSaveImage(false, context.getDrawable(R.drawable.noimage));

                                }




                            } catch (IOException e) {
                                Log.w("ExternalStorage", "Error writing " + file, e);
                                completion.completionHandlerSaveImage(false, context.getDrawable(R.drawable.noimage));
                            }

                        }
                    });

                }
            });

            downloadImage.execute(mUrl);


        }



    }




    public static MyTools sharedInstance() {
        if (mInstance == null) {
            mInstance = new MyTools();
        }
        return mInstance;
    }


}
