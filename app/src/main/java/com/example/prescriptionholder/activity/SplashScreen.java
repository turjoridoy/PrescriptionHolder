package com.example.prescriptionholder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prescriptionholder.R;
import com.example.prescriptionholder.utils.SharedPrefManager;

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer.
             */

            @Override
            public void run() {
                SharedPrefManager sharedPrefManager=new SharedPrefManager(SplashScreen.this);
               if(sharedPrefManager.isLoggedIn()){
                   Intent i = new Intent(SplashScreen.this, MainActivity.class);
                   startActivity(i);
               }else{
                   Intent i = new Intent(SplashScreen.this, UserActivity.class);
                   startActivity(i);
               }


                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}
