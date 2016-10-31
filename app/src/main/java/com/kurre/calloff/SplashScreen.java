package com.kurre.calloff;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Java way of doing it
        Thread thread = new Thread()
        {
            public void run() {
                try {
                    MyDatabaseHelper myDbHelper = new MyDatabaseHelper(SplashScreen.this);
                    if(myDbHelper.checkActiveLogin()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Worker worker = new Worker(SplashScreen.this, Constants.task.GET_CONTACTS);
                                worker.execute(Constants.GET_CONTACTS_URL);
                            }
                        });
                    }
                    else {
                        Thread.sleep(1000);
                        Intent i = new Intent(SplashScreen.this, Login.class);
                        startActivity(i);
                        finish();       //will finish splash activity
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        //Android way of doing this
        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashScreen.this,Login.class);
                startActivity(i);
                finish();
            }
        }, 2000);
        */

        // Assume thisActivity is the current activity
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        //Checks and Creates directory structure at the time of startup of app
        InitializeApp.initializeAll();
    }
}
