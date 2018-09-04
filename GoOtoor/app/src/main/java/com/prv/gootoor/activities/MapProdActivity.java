package com.prv.gootoor.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.prv.gootoor.BuildConfig;
import com.prv.gootoor.R;
import com.prv.gootoor.utils.Config;
import com.prv.gootoor.utils.GeoLocInterface;
import com.prv.gootoor.utils.MainThreadInterface;
import com.prv.gootoor.utils.MyTools;

import org.json.JSONException;
import org.json.JSONObject;

public class MapProdActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {


    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private FusedLocationProviderClient mFusedLocationClient;
    // Represents a geographical location.
    private Location mLastLocation;
    private static final int DEFAULT_ZOOM = 13;
    private MapView mIBProdMapView;
    private Double mlatUser = 0.0;
    private Double mlonUser = 0.0;
    private Boolean mIsAddMarker = false;
    private String mInfoLocation;
    private Config mConfig = Config.sharedInstance();


    @Override
    public void onStart() {
        super.onStart();

        initColor();

        callCheckLocation();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mIBProdMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIBProdMapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mIBProdMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mIBProdMapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mIBProdMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_prod);


        mIBProdMapView = (MapView) findViewById(R.id.IBProdMapView);
        mIBProdMapView.onCreate(savedInstanceState);


        mInfoLocation = getIntent().getStringExtra(mConfig.getDomaineApp()+"IBInfoLocation");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }




    private void initColor() {

        LinearLayout mIBMapProdBackgr = (LinearLayout) findViewById(R.id.IBMapProdBackgr);

        String hexColor = "#" + mConfig.getColorApp();
        mIBMapProdBackgr.setBackgroundColor(Color.parseColor(hexColor));


    }


    public void actionProdMapClose(View view) {
        finish();
    }

    public void actionProdMapValid(View view) {

        mConfig.setLongitude(mlonUser);
        mConfig.setLatitude(mlatUser);
        finish();
    }

    private void actionSearchAdresse(String adresse){


        //Search location from adresse
        MyTools.sharedInstance().geocodeAddressString(this, adresse, new GeoLocInterface() {
            @Override
            public void completionHandlerGeo(Boolean success, JSONObject jsonLocation, final String errorString) {

                if (success) {

                    try {
                        mlatUser = jsonLocation.getDouble("lat");
                        mlonUser = jsonLocation.getDouble("lng");
                        mIBProdMapView.getMapAsync(MapProdActivity.this);

                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    MyTools.sharedInstance().performUIUpdatesOnMain(MapProdActivity.this, new MainThreadInterface() {

                        @Override
                        public void completionUpdateMain() {
                            MyTools.sharedInstance().displayAlert(MapProdActivity.this,errorString);

                        }
                    });
                }
            }
        });

    }


    private void callCheckLocation() {

        if (mInfoLocation.equals("")) {

            if (!checkPermissions()) {
                requestPermissions();
            } else {
                getLastLocation();
            }

        } else {
            actionSearchAdresse(mInfoLocation);
        }

    }


    private boolean checkPermissions() {

        int permissionState = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);

    }


    private void requestPermissions() {

        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale) {

            Log.i(Config.sharedInstance().getDomaineApp(),"Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {

            Log.i(Config.sharedInstance().getDomaineApp(),"Requesting permission.");

            startLocationPermissionRequest();

        }

    }


    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {

        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            mlatUser = mLastLocation.getLatitude();
                            mlonUser = mLastLocation.getLongitude();

                            MyTools.sharedInstance().performUIUpdatesOnMain(MapProdActivity.this, new MainThreadInterface() {
                                @Override
                                public void completionUpdateMain() {

                                    mIBProdMapView.getMapAsync(MapProdActivity.this);
                                }
                            });

                        } else {
                            Log.i(Config.sharedInstance().getDomaineApp(),"getLastLocation:exception",task.getException());
                            showSnackbar(getString(R.string.no_location_detected));
                        }


                    }
                });

    }


    //Callback received when a permissions request has been completed.


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.i(Config.sharedInstance().getDomaineApp(),"onRequestPermissionResult");

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {

            if (grantResults.length <= 0) {

                Log.i(Config.sharedInstance().getDomaineApp(),"User interaction was cancelled.");

            }  else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }

    }


    private void showSnackbar(final String text) {
        View container = findViewById(R.id.IBTabMainBackgr);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_INDEFINITE);
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId, View.OnClickListener listener) {

        Snackbar.make(findViewById(android.R.id.content), getString(mainTextStringId), Snackbar.LENGTH_INDEFINITE).setAction(getString(actionStringId), listener);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if ((mLastLocation != null || !mInfoLocation.equals(""))  && !mIsAddMarker) {

            Marker markerUser = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mlatUser, mlonUser))
                    .title(getString(R.string.product))
                    .snippet(getString(R.string.product)));
            markerUser.setTag(null);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerUser.getPosition(), DEFAULT_ZOOM));
            googleMap.setOnMarkerClickListener(this);
            mIsAddMarker = !mIsAddMarker;

        }




    }
}
