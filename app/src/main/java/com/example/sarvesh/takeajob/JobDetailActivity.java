package com.example.sarvesh.takeajob;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class JobDetailActivity extends AppCompatActivity {

    TextView tv1,tv2,tv3,tv4,tv5,tv6;
    Button button;
    Bundle bundle;
    String fromIntent,title;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_job_detail);

        AdapterData data=getIntent().getParcelableExtra("detailsParcel");
        fromIntent=getIntent().getStringExtra("fromIntent");
        title=getIntent().getStringExtra("title");

        getSupportActionBar().setTitle(title);

        tv1= (TextView) findViewById(R.id.job_t);
        tv2= (TextView) findViewById(R.id.salary_range);
        tv3= (TextView) findViewById(R.id.company_name);
        tv4= (TextView) findViewById(R.id.job_location);
        tv5= (TextView) findViewById(R.id.desc);
        tv6= (TextView) findViewById(R.id.date);
        button= (Button) findViewById(R.id.apply_button);

        tv1.setText(data.j_title);
        tv2.setText("₹ "+data.salary_range);
        tv3.setText(data.c_name);
        tv4.setText(data.location);
        tv5.setText(data.description);
        tv6.setText(data.p_date);
        if(fromIntent.equals("Applied")){button.setText(R.string.already_applied);}
    }

    public void mApply(View v){
        //Need not do anything in here if the intent is received from AppliedJobsActivity.

        if(fromIntent.equals("Saved")){
            SharedPreferences preferences=getSharedPreferences("takeajobdata",MODE_PRIVATE);
            int loginId=preferences.getInt("loginId",0);
            if(loginId!=0) {
                Toast.makeText(this, "You Sucessfully Applied for this job", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(this,SavedJobsActivity.class);
                i.putExtra("title",title);
                startActivity(i);
                finish();
            }
        }
    }
}
