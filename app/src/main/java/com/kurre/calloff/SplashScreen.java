package com.kurre.calloff;

import android.content.Intent;
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
                    Thread.sleep(3000);
                    Intent i=new Intent(SplashScreen.this,Login.class);
                    startActivity(i);
                    finish();       //will finish splash activity
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
        }, 3000);
        */

        //Checks and Creates directory structure at the time of startup of app
        InitializeApp.initializeAll();
    }
}
