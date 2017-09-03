package com.prv.pressshare.activities.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prv.pressshare.daos.MDBCapital;
import com.prv.pressshare.daos.MDBInterfaceArray;
import com.prv.pressshare.daos.MDBProduct;
import com.prv.pressshare.lists.Capitals;
import com.prv.pressshare.lists.Products;
import com.prv.pressshare.models.Capital;
import com.prv.pressshare.models.Product;
import com.prv.pressshare.utils.Config;
import com.prv.pressshare.utils.GeoLocInterface;
import com.prv.pressshare.utils.MainThreadInterface;
import com.prv.pressshare.utils.MyTools;
import com.prv.pressshare.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//
//  MapFragment
//  PressShare
//
//  Description : Map all products according the selected area. It is possible to look for a city or place.
//                  The user is geolocalized by a blue pin on the map.
//
//  Created by MacbookPRV on 25/08/2017.
//  Copyright © 2016 Pastouret Roger. All rights reserved.
//


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private MapView mIBMap;
    private Config mConfig;
    public Double mlatUser = 0.0;
    public Double mlonUser = 0.0;
    private OnCommunicateWithActivity mCallback;
    private int mProdIdNow = 0;
    private Product mAProduct;
    private GoogleMap googleMap;



    public MapFragment() {
        // Required empty public constructor
    }


    // Container Activity must implement this interface
    public interface OnCommunicateWithActivity {
        public void onUserGeolocation();
        public void onSearchAdresseLocation();
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mIBMap.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIBMap.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mIBMap.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIBMap.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnCommunicateWithActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCommunicateWithActivity");
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mIBMap.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_map, container, false);

        mIBMap = (MapView) view.findViewById(R.id.IBMap);
        mIBMap.onCreate(savedInstanceState);
        mIBMap.getMapAsync(this);

        mConfig = Config.sharedInstance();

        ImageButton mIBMapLogout = (ImageButton) view.findViewById(R.id.IBMapLogout);
        mIBMapLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //action logout
                actionLogout();
            }
        });


        ImageButton mIBMapHelp = (ImageButton) view.findViewById(R.id.IBMapHelp);
        mIBMapHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo Tuto_Presentation
                MyTools.sharedInstance().showHelp("Tuto_Presentation", getContext());
            }
        });


        ImageButton mIBMapRefresh = (ImageButton) view.findViewById(R.id.IBMapRefresh);
        mIBMapRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //refresh function
                mCallback.onUserGeolocation();
            }
        });


        EditText mIBMapSearchLoc = (EditText) view.findViewById(R.id.IBMapSearchLoc);
        mIBMapSearchLoc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                actionSearchAdresse(v.getText().toString());
                v.setText("");
                return false;
            }
        });

        FloatingActionButton mIBMapAddProduct = (FloatingActionButton)  view.findViewById(R.id.IBMapAddProduct);
        mIBMapAddProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Todo  floating action buttion function
                MyTools.sharedInstance().showHelp("floating action buttion function", getContext());
            }
        });


        return view;
    }



    private void  actionLogout() {


        final SharedPreferences sharedPref = getContext().getSharedPreferences(mConfig.getFileParameters(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("user_pseudo");
        editor.remove("user_email");
        editor.apply();
        getActivity().finish();

    }


    private void actionSearchAdresse(String adresse){


        //Search location from adresse
        MyTools.sharedInstance().geocodeAddressString(getContext(), adresse, new GeoLocInterface() {
            @Override
            public void completionHandlerGeo(Boolean success, JSONObject jsonLocation, final String errorString) {

                if (success) {

                    try {
                        mlatUser = jsonLocation.getDouble("lat");
                        mlonUser = jsonLocation.getDouble("lng");
                       refreshData();

                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(getContext(), new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {
                            MyTools.sharedInstance().displayAlert(getContext(),errorString);

                        }
                    });
                }
            }
        });

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        checkLoardMap();
    }

    public void checkLoardMap() {

        if (Products.sharedInstance().getProductsArray().length()>0) {
            loadPins();
        }
        else {
            refreshData();
        }


        Marker markerUser = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(mlatUser, mlonUser))
                .title("user:")
                .snippet(mConfig.getUser_nom()+ " "+mConfig.getUser_prenom()+ "("+mConfig.getUser_id()+")"));

        markerUser.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerUser.getPosition(), 12));

        googleMap.setOnMarkerClickListener(this);
    }


    private void refreshData() {

        MDBCapital.sharedInstance().getCapital(getContext(), mConfig.getUser_id(), new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                if (success) {

                    try {

                        Capitals.sharedInstance().setCapitalsArray(anArray);
                        for (int i = 0; i < Capitals.sharedInstance().getCapitalsArray().length() ; i++) {

                            JSONObject dictionary = Capitals.sharedInstance().getCapitalsArray().getJSONObject(i);
                            Capital capital = new Capital(getContext(), dictionary);
                            mConfig.setBalance(capital.getBalance());
                            mConfig.setFailure_count(capital.getFailure_count());

                        }

                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(getContext(), new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {
                            MyTools.sharedInstance().displayAlert(getContext(),errorString);

                        }
                    });
                }

            }
        });


        if (mlatUser > 0 && mlonUser > 0) {

            //Setting search Area
            // The lat and long are used to create aLLocation Coordinates2D instance.

            double latspan = (mConfig.getRegionProduct()/111325);
            double longspan = (mConfig.getRegionProduct()/111325)*(1/ Math.cos(Math.toRadians(mlatUser)));
            LatLngBounds coordinateRegion = new LatLngBounds(new LatLng(mlatUser-latspan, mlonUser-longspan), new LatLng(mlatUser+latspan, mlonUser+longspan));
            mConfig.setMinLongitude(coordinateRegion.getCenter().longitude-longspan);
            mConfig.setMaxLongitude(coordinateRegion.getCenter().longitude+longspan);
            mConfig.setMinLatitude(coordinateRegion.getCenter().latitude-latspan);
            mConfig.setMaxLatitude(coordinateRegion.getCenter().latitude+latspan);

        }

        MDBProduct.sharedInstance().getProductsByCoord(getContext(), mConfig.getUser_id(), mConfig.getMinLongitude(), mConfig.getMaxLongitude(), mConfig.getMinLatitude(), mConfig.getMaxLatitude(), new MDBInterfaceArray() {
            @Override
            public void completionHandlerArray(Boolean success, JSONArray anArray, final String errorString) {

                if (success) {

                    try {

                        Products.sharedInstance().setProductsArray(anArray);
                        //refresh function

                        if (Products.sharedInstance().getProductsArray().length()>0) {
                            mCallback.onSearchAdresseLocation();
                        }


                    }  catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(getContext(), new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {
                            MyTools.sharedInstance().displayAlert(getContext(),errorString);

                        }
                    });
                }
            }
        });
    }


    private int countProduct() {

        //Setting search Area
        // The lat and long are used to create aLLocation Coordinates2D instance.

        double latspan = (mConfig.getRegionProduct()/111325);
        double longspan = (mConfig.getRegionProduct()/111325)*(1/ Math.cos(Math.toRadians(mlatUser)));
        LatLngBounds coordinateRegion = new LatLngBounds(new LatLng(mlatUser-latspan, mlonUser-longspan), new LatLng(mlatUser+latspan, mlonUser+longspan));
        Double minimumLon = coordinateRegion.getCenter().longitude-longspan;
        Double maximumLon = coordinateRegion.getCenter().longitude+longspan;
        Double minimumLat = coordinateRegion.getCenter().latitude-latspan;
        Double maximumLat = coordinateRegion.getCenter().latitude+latspan;


        int count = 0;

        try {
            for (int i = 0; i < Products.sharedInstance().getProductsArray().length() ; i++) {

                JSONObject prod = Products.sharedInstance().getProductsArray().getJSONObject(i);
                Product produ = new Product(getContext(), prod);

                if (produ.getProd_latitude() >= minimumLat && produ.getProd_latitude()  <= maximumLat && produ.getProd_longitude()  >= minimumLon && produ.getProd_longitude()  <= maximumLon) {
                    i += 1;
                }


                if (produ.getProd_id() == mProdIdNow) {
                    mAProduct = produ;
                }

                count = i;
            }
        }  catch (JSONException e) {
            e.printStackTrace();
        }



        return count;

    }


    private void loadPins() {

        //Remove Marker from MapView

        googleMap.clear();


        try {

            for (int i = 0; i < Products.sharedInstance().getProductsArray().length() ; i++) {

                JSONObject dictionary = Products.sharedInstance().getProductsArray().getJSONObject(i);
                Product product = new Product(getContext(), dictionary);

                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(product.getProd_latitude(), product.getProd_longitude()))
                        .title(product.getProd_nom()+" (user:"+product.getProd_by_user()+")")
                        .snippet(product.getProd_mapString()+ " / " +product.getProd_comment()));
                marker.setTag(product);
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


            }


        }  catch (JSONException e) {
            e.printStackTrace();
        }



    }



}
