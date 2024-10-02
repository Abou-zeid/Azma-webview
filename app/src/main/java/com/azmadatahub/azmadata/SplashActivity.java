package com.azmadatahub.azmadata;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class SplashActivity extends Activity {
    ProgressDialog pd;
    ImageView imageView;
    TextView textView;
    boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pd = new ProgressDialog(this);
        imageView = findViewById(R.id.img);
        textView = findViewById(R.id.app_name);
        Thread timer = new Thread(){
            public  void run(){
                try{
                    sleep(2000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    call();

                }
            }
        };timer.start();
    }
    private void call() {
        if (isNetworkAvailable()){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else {
            if(flag){
                imageView.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
            }
            flag = false;
            pd.dismiss();
            Snackbar snackbar = Snackbar.make(findViewById(R.id.img),"Please! Check Your Internet Connection",Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pd.setCancelable(false);
                    pd.setMessage("Connecting...");
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pd.show();
                    call();
                }
            });
            snackbar.show();
        }

    }
    @SuppressLint("MissingPermission")
    public boolean isNetworkAvailable(){
        try {
            ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = null;
            if (manager != null){
                networkInfo = manager.getActiveNetworkInfo();
            }
            return  networkInfo != null && networkInfo.isConnected();
        }catch (NullPointerException e){
            return  false;
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}