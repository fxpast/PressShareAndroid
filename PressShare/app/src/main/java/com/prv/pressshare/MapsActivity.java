package com.prv.pressshare;

import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prv.pressshare.customview.TouchInterceptFrameLayout;
import com.prv.pressshare.fragments.FormFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private MapView mapView;

    private InfoWindowManager infoWindowManager;

    private List<String> products;
    private List<String> arrayLat;
    private List<String> arrayLon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        final TouchInterceptFrameLayout mapViewContainer =
                (TouchInterceptFrameLayout) findViewById(R.id.mapViewContainer);

        mapView.getMapAsync(this);

        infoWindowManager = new InfoWindowManager(getSupportFragmentManager());
        infoWindowManager.onParentViewCreated(mapViewContainer, savedInstanceState);



    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
        infoWindowManager.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        infoWindowManager.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        infoWindowManager.onMapReady(googleMap);


        getProductsByCoord(googleMap, "24","0","99","0","99");

        final Marker marker1 = googleMap.addMarker(new MarkerOptions().position(new LatLng(48.846781, 2.3951775)).snippet("_MARKER1"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker1.getPosition(),10));

        googleMap.setOnMarkerClickListener(this);

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        InfoWindow infoWindow = null;

        final int offsetX = (int) getResources().getDimension(R.dimen.marker_offset_x);
        final int offsetY = (int) getResources().getDimension(R.dimen.marker_offset_y);

        final InfoWindow.MarkerSpecification markerSpec =
                new InfoWindow.MarkerSpecification(offsetX, offsetY);

        FormFragment infoFragment = new FormFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("title",marker.getSnippet());
        infoFragment.setArguments(args);

        infoWindow = new InfoWindow(marker, markerSpec, infoFragment);



        if (infoWindow != null) {
            infoWindowManager.toggle(infoWindow, true);
        }

        return true;
    }



    private void getProductsByCoord(final GoogleMap googleMap, final String userId, final String minLon, final String maxLon, final String minLat, final String maxLat){
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

                            products  = new ArrayList<>();
                            arrayLat  = new ArrayList<>();
                            arrayLon  = new ArrayList<>();

                            try {
                                JSONArray arrayProduct = (JSONArray) result.get(Config.PRODUCT_allproducts);

                                for (int i = 0 ; i  < arrayProduct.length(); i++) {
                                    JSONObject product = arrayProduct.getJSONObject(i);
                                    products.add(i, (String) product.get("prod_nom"));
                                    arrayLat.add(i, (String) product.get("prod_latitude"));
                                    arrayLon.add(i, (String) product.get("prod_longitude"));
                                    double lat = Double.parseDouble((String) product.get("prod_latitude"));
                                    double lon = Double.parseDouble((String) product.get("prod_longitude"));
                                    String flag =  (String) product.get("prod_nom");

                                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).snippet(flag));

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }







                        }else{
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(MapsActivity.this, "Invalid connection", Toast.LENGTH_LONG).show();

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
