package com.prv.gootoor.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.prv.gootoor.activities.ProductActivity;
import com.prv.gootoor.daos.MDBCapital;
import com.prv.gootoor.daos.MDBInterfaceArray;
import com.prv.gootoor.daos.MDBProduct;
import com.prv.gootoor.lists.Capitals;
import com.prv.gootoor.lists.Products;
import com.prv.gootoor.models.Capital;
import com.prv.gootoor.models.Product;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.GeoLocInterface;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;
import com.prv.gootoor.R;
import com.prv.gootoor.utils.SaveImageInterface;
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


    private static final int DEFAULT_ZOOM = 13;
    private MapView mIBMap;
    private Config mConfig = Config.sharedInstance();
    public Double mlatUser = 0.0;
    public Double mlonUser = 0.0;
    private OnCommunicateWithActivity mCallback;
    private GoogleMap googleMap;
    private FloatingActionButton mIBMapAddProduct;

    public MapFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_map, container, false);

        mIBMap = (MapView) view.findViewById(R.id.IBMap);
        mIBMap.onCreate(savedInstanceState);
        mIBMap.getMapAsync(this);




        ImageView mIBMapLogout = (ImageView) view.findViewById(R.id.IBMapLogout);
        mIBMapLogout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //action logout
                actionLogout();
                return false;
            }
        });


        ImageView mIBMapHelp = (ImageView) view.findViewById(R.id.IBMapHelp);
        mIBMapHelp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Todo Tuto_Presentation
                MyTools.sharedInstance().showHelp("carte_liste", getContext());
                return false;
            }
        });


        ImageView mIBMapRefresh = (ImageView) view.findViewById(R.id.IBMapRefresh);
        mIBMapRefresh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //refresh function
                mCallback.onUserGeolocation();
                return false;
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

        mIBMapAddProduct = (FloatingActionButton)  view.findViewById(R.id.IBMapAddProduct);
        mIBMapAddProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (mConfig.getLevel() > 0) {

                    Intent intent = new Intent(getContext(), ProductActivity.class);
                    intent.putExtra(mConfig.getDomaineApp()+"prod_id", 0);
                    intent.putExtra(mConfig.getDomaineApp()+"typeListe", 0);
                    intent.putExtra(mConfig.getDomaineApp()+"typeSListe", 0);
                    startActivity(intent);

                }


            }
        });


        return view;
    }



    @Override
    public void onStart() {
        super.onStart();


            if (mConfig.getLevel() <= 0) {

                mIBMapAddProduct = (FloatingActionButton) MyTools.sharedInstance().activerObjet(mIBMapAddProduct, false);

            } else {

                mIBMapAddProduct = (FloatingActionButton) MyTools.sharedInstance().activerObjet(mIBMapAddProduct, true);

            }


        }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mIBMap.onLowMemory();


    }

    @Override
    public void onDestroy() {

        mIBMap.onDestroy();
        mConfig.setIsTimer(false);
        super.onDestroy();
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



    private void  actionLogout() {


        /*
        final SharedPreferences sharedPref = getContext().getSharedPreferences(mConfig.getFileParameters(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("user_pseudo");
        editor.remove("user_email");
        editor.apply();

        */

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
    public boolean onMarkerClick(Marker marker) {return false;}



    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {



                if (marker.getTag() != null) {
                    Product product = (Product) marker.getTag();

                    Intent intent = new Intent(getContext(), ProductActivity.class);
                    intent.putExtra(mConfig.getDomaineApp()+"prod_id", product.getProd_id());
                    intent.putExtra(mConfig.getDomaineApp()+"typeListe", 0);
                    startActivity(intent);

                }


            }
        });

        checkLoardMap();
    }


    public void checkLoardMap() {

        if (Products.sharedInstance().getProductsArray().length()>0) {
            loadPins();
        }
        else {
            refreshData();
        }

        if (googleMap != null) {

            Marker markerUser = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mlatUser, mlonUser))
                    .title("user:")
                    .snippet(mConfig.getUser_nom()+ " "+mConfig.getUser_prenom()+ "("+mConfig.getUser_id()+")"));
            markerUser.setTag(null);

            markerUser.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));


            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerUser.getPosition(), DEFAULT_ZOOM));

            googleMap.setOnMarkerClickListener(this);
        }

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


    class CustomInfoWindowAdapter implements InfoWindowAdapter {

        private final View mWindow;
        private final View mContents;
        private Boolean mIsRefresh = false;

        CustomInfoWindowAdapter() {
            mWindow = getActivity().getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getActivity().getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }


        @Override
        public View getInfoWindow(Marker marker) {

            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {

            render(marker, mContents);
            return mContents;

        }

        private void render(final Marker marker, final View view) {

           final ImageView mIBInfoImage = (ImageView) view.findViewById(R.id.IBInfoImage);


            if (marker.getTag() != null) {

                Product product = (Product) marker.getTag();


                MyTools.sharedInstance().saveImageArchive(getContext(), product.getProd_imageUrl(), "images/", new SaveImageInterface() {
                    @Override
                    public void completionHandlerSaveImage(Boolean success, Drawable drawable) {

                        if (!success) {

                            mIBInfoImage.setTag("noimage");
                            Log.v(mConfig.getDomaineApp()+"MapFragement", "error image");

                        } else {
                            mIBInfoImage.setTag("");
                        }

                        mIBInfoImage.setImageDrawable(drawable);

                    }
                });


            } else {

                mIBInfoImage.setImageResource(R.drawable.noimage);
            }

            Button mIBPinTitle = (Button) view.findViewById(R.id.IBPinTitle);
            mIBPinTitle.setText(marker.getTitle());

            TextView mIBPinSnippet = (TextView) view.findViewById(R.id.IBPinSnippet);
            mIBPinSnippet.setText(marker.getSnippet());

        }
    }


    // Container Activity must implement this interface
    public interface OnCommunicateWithActivity {
        public void onUserGeolocation();
        public void onSearchAdresseLocation();
    }


}
