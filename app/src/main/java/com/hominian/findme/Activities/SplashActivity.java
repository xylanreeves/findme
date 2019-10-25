package com.hominian.findme.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.hominian.findme.R;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Boolean isFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

        if (isFirstRun){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    isFirstRun = false;
                    editor.putBoolean("isFirstRun", isFirstRun);
                    editor.apply();

                    startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                    finish();

                }
            }, 2000);


        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 500);

        }




    }




}
