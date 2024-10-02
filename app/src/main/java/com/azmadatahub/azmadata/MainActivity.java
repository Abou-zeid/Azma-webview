package com.azmadatahub.azmadata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends Activity {

    // some important variables
    private final static String url = "https://app.azmadatahub.com/mobile/login/";
    boolean doubleBackToExitPressedOnce = false;
    double longitude, latitude;

    // creating components
    android.webkit.WebView web;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
//    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        getLastLocation();
        setUpUi();
        setUpWebView(url);
    }
//
//    @SuppressLint("MissingPermission")
////    private void requestNewLocationData() {
////
////        // Initializing LocationRequest
////        // object with appropriate methods
////        LocationRequest mLocationRequest = new LocationRequest();
////        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
////        mLocationRequest.setInterval(5);
////        mLocationRequest.setFastestInterval(0);
////        mLocationRequest.setNumUpdates(1);
////
////        // setting LocationRequest
////        // on FusedLocationClient
////        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
////        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
////    }
//
////    private LocationCallback mLocationCallback = new LocationCallback() {
////
////        @Override
////        public void onLocationResult(LocationResult locationResult) {
////            Location mLastLocation = locationResult.getLastLocation();
////            latitude = mLastLocation.getLatitude();
////            longitude = mLastLocation.getLongitude();
////        }
////    };
//
//    // method to check for permissions
////    private boolean checkPermissions() {
////        return ActivityCompat.checkSelfPermission(this, Manifest
////                .permission.ACCESS_COARSE_LOCATION) == PackageManager
////                .PERMISSION_GRANTED && ActivityCompat
////                .checkSelfPermission(this, Manifest
////                        .permission.ACCESS_FINE_LOCATION) == PackageManager
////                .PERMISSION_GRANTED;
////
////        // If we want background location
////        // on Android 10.0 and higher,
////        // use:
////        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
////    }
//
//    // method to request for permissions
////    private void requestPermissions() {
////        ActivityCompat.requestPermissions(this, new String[]{
////                Manifest.permission.ACCESS_COARSE_LOCATION,
////                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
////    }
//
//    // method to check
//    // if location is enabled
////    private boolean isLocationEnabled() {
////        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
////    }
//
//    // If everything is alright then
////    @Override
////    public void
////    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////
////        if (requestCode == PERMISSION_ID) {
////            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                getLastLocation();
////            }
////        }
////    }
//
////    @Override
////    public void onResume() {
////        super.onResume();
////        if (checkPermissions()) {
////            getLastLocation();
////        }
////    }
//
//    //Get user location.
////    @SuppressLint("MissingPermission")
////    public void getLastLocation() {
////        // check if permissions are given
////        if (checkPermissions()) {
////
////            // check if location is enabled
////            if (isLocationEnabled()) {
////
////                // getting last
////                // location from
////                // FusedLocationClient
////                // object
////                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Location> task) {
////                        Location location = task.getResult();
////                        if (location == null) {
////                            requestNewLocationData();
////                        } else {
////                            latitude = location.getLatitude();
////                            longitude = location.getLongitude();
////
////                        }
////                    }
////                });
////            } else {
////                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
////                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////                startActivity(intent);
////            }
////        } else {
////            // if permissions aren't available,
////            // request for permissions
////            requestPermissions();
////        }
////
////    }

    // setting components
    void setUpUi(){
        progressBar = findViewById(R.id.progressbar);
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        web = findViewById(R.id.web_view);
    }

    // setting and modifying web view
    @SuppressLint("SetJavaScriptEnabled")
    void setUpWebView(String url){
        web.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress)
            {
                progressBar.setProgress(progress);
                progressBar.incrementProgressBy(progress);

                if (progress == 100 && progressBar.isShown()){
                    progressBar.setVisibility(View.GONE);
                }

            }
        });

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setSupportZoom(false);
        web.getSettings().setBuiltInZoomControls(false);
        web.getSettings().setLoadsImagesAutomatically(true);
        web.getSettings().setDomStorageEnabled(true);
        web.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(!progressBar.isShown()){
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (url != null && url.startsWith("whatsapp://")){
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return  true;
                }else if(url != null && url.startsWith("https://twitter.com/intent")) {
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }else if (url!=  null && url.startsWith("mailto:")){
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }else {
                    return false;
                }

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(!progressBar.isShown()){
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onPageFinished(android.webkit.WebView view, String url) {
                findViewById(R.id.web_view).setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                if(progressBar.isShown()){
                    progressBar.setVisibility(View.GONE);
                }

            }

        });
        web.loadUrl(url);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!progressBar.isShown()){
                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.VISIBLE);
                }
                web.reload();

            }

        });


    }

    // handling android device back pressed
    @Override
    public void onBackPressed() {
        if(web.canGoBack()){
            if(!progressBar.isShown()) {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }
            web.goBack();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to go back", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

}