package com.example.sarvesh.takeajob;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CollaborationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collaborations);

        getSupportActionBar().setTitle("Collaborations");
        getSupportActionBar().setIcon(R.drawable.main_icon);
    }
}
