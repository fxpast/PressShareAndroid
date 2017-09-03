package com.prv.pressshare.activities;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.prv.pressshare.BuildConfig;
import com.prv.pressshare.R;
import com.prv.pressshare.activities.fragments.ListProductFragment;
import com.prv.pressshare.activities.fragments.MapFragment;
import com.prv.pressshare.activities.fragments.SettingsFragment;
import com.prv.pressshare.utils.Config;

public class TabMainActivity extends AppCompatActivity implements MapFragment.OnCommunicateWithActivity {


    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private FusedLocationProviderClient mFusedLocationClient;
    MapFragment mMapFragment;
    SettingsFragment mSettingsFragment;
    ListProductFragment mListProductFragment;

    // Represents a geographical location.
    private Location mLastLocation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.IBTabMap:

                    showFragment(mMapFragment);
                    return true;
                case R.id.IBTabList:

                    showFragment(mListProductFragment);
                    return true;
                case R.id.IBTabSettings:

                    showFragment(mSettingsFragment);
                    return true;
            }
            return false;
        }

    };

    @Override
    public void onUserGeolocation() {

        callCheckLocation();
    }

    @Override
    public void onSearchAdresseLocation() {

        mMapFragment.checkLoardMap();

    }

    @Override
    public void onStart() {
        super.onStart();

        callCheckLocation();
    }


    private void callCheckLocation() {
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mMapFragment = new MapFragment();
        mSettingsFragment = new SettingsFragment();
        mListProductFragment = new ListProductFragment();

        showFragment(mMapFragment);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    private void showFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.IBContent,fragment).commit();
    }


    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {

        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();

                            MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.IBContent);

                            if (mapFragment != null) {
                                mapFragment.mlatUser = mLastLocation.getLatitude();
                                mapFragment.mlonUser = mLastLocation.getLongitude();
                                mMapFragment.checkLoardMap();
                            }

                        } else {
                            Log.i(Config.sharedInstance().getDomaineApp(),"getLastLocation:exception",task.getException());
                            showSnackbar(getString(R.string.no_location_detected));
                        }


                    }
                });

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
        View container = findViewById(R.id.activity_tab_main_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_INDEFINITE);
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId, View.OnClickListener listener) {

        Snackbar.make(findViewById(android.R.id.content), getString(mainTextStringId), Snackbar.LENGTH_INDEFINITE).setAction(getString(actionStringId), listener);

    }

}
