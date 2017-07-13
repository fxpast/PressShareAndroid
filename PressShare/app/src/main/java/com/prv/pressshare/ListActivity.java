package com.prv.pressshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Adil Soomro
 *
 */
public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private String[] adapterData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getProductsByCoord("24","0","99","0","99");

    }



    private void getProductsByCoord(final String userId, final String minLon, final String maxLon, final String minLat, final String maxLat){
        //Getting values from edit texts

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ProductsByCoord_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server

                        JSONObject result = null;
                        try {
                            result = (JSONObject) new JSONTokener(response).nextValue();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String success = null;
                        try {
                            success = (String) result.get(Config.LOGIN_SUCCESS);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if(success.equals("1")){

                            listView = (ListView)findViewById(R.id.list_view_product);


                            List<String> aList = new ArrayList<>();



                            try {
                                JSONArray arrayProduct = (JSONArray) result.get(Config.PRODUCT_allproducts);



                                for (int i = 0 ; i  < arrayProduct.length(); i++) {
                                    JSONObject product = arrayProduct.getJSONObject(i);
                                    aList.add(i, (String) product.get("prod_nom"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            adapterData = new String[aList.size()];
                            aList.toArray(adapterData);

                            listView.setAdapter(new ArrayAdapter<>(ListActivity.this,R.layout.list_item, adapterData));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    startActivity(new Intent(ListActivity.this, ProductActivity.class));

                                }
                            });


                        }else{
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(ListActivity.this, "Invalid connection", Toast.LENGTH_LONG).show();

                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_user_id, userId);
                params.put(Config.KEY_minLon, minLon);
                params.put(Config.KEY_maxLon, maxLon);
                params.put(Config.KEY_maxLat, maxLat);
                params.put(Config.KEY_minLat, minLat);


                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
