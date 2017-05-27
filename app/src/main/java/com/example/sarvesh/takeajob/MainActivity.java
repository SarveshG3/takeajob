package com.example.sarvesh.takeajob;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.IntentCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        final int id=getSharedPreferences("takeajobdata",MODE_PRIVATE).getInt("loginId",0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(id==0) {
                    Intent intent = new Intent(MainActivity.this, LoginPage.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(MainActivity.this,HomePage.class);
                    startActivity(intent);

                    finish();
                }
            }
        },1000);
 /*       Runnable runnable=new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        */


    }
}
   /* getSharedPreferences("AppCheck.xml",1).edit()*/