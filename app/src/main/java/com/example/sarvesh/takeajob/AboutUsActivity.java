package com.example.sarvesh.takeajob;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {

    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8;
    Typeface font;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        getSupportActionBar().setTitle("About Us");
        getSupportActionBar().setIcon(R.drawable.main_icon);

        font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );

        tv1= (TextView) findViewById(R.id.signal);
        tv1.setTypeface(font);
        tv2= (TextView) findViewById(R.id.globe);
        tv2.setTypeface(font);
        tv3= (TextView) findViewById(R.id.power);
        tv3.setTypeface(font);
        tv4= (TextView) findViewById(R.id.heart);
        tv4.setTypeface(font);
        tv5= (TextView) findViewById(R.id.brief);
        tv5.setTypeface(font);
        tv6= (TextView) findViewById(R.id.leaf);
        tv6.setTypeface(font);
        tv7= (TextView) findViewById(R.id.certi);
        tv7.setTypeface(font);
        tv8= (TextView) findViewById(R.id.hard_work);
        tv8.setTypeface(font);

    }
}
