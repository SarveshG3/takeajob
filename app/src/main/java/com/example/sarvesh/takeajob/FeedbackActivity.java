package com.example.sarvesh.takeajob;

import android.content.Intent;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{

    EditText et;
    Button button;
    Typeface font;
    int loginId;
    String username;
    String d;
    String feedback;
    HashMap<String,String> map=new HashMap<>();

    private static final String LINK="http://192.168.43.187/takeajob/post_feedback.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getSupportActionBar().setTitle("Feedback");
        getSupportActionBar().setIcon(R.drawable.main_icon);

        font=Typeface.createFromAsset(getAssets(),"fontawesome-webfont.ttf");

        et= (EditText) findViewById(R.id.feedback);
        button= (Button) findViewById(R.id.button4);
        button.setText(getResources().getString(R.string.icon_feedback) +"   post Feedback");
        button.setTypeface(font);
        button.setOnClickListener(this);

        loginId=getSharedPreferences("takeajobdata",MODE_PRIVATE).getInt("loginId",0);
        username=getSharedPreferences("takeajobdata",MODE_PRIVATE).getString("username",null);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if(loginId!=0) {

            if (!et.getText().toString().trim().equals("")) {
                Date date = new Date(System.currentTimeMillis());
                Log.d("TIME", date.toString());
                d = date.toString();
                feedback = et.getText().toString().trim();
                map.put("loginId", String.valueOf(loginId));
                map.put("feed", feedback);
                map.put("date", d);
                map.put("user", username);

                RequestPackage rp = new RequestPackage();
                rp.setMethod("POST");
                rp.setUri(LINK);
                rp.setParams(map);

                if (isOnline()) {
                    PostFeedTask task = new PostFeedTask();
                    task.execute(rp);
                }
            } else {
                Toast.makeText(FeedbackActivity.this, "An empty feedback cannot be posted", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "You need to login First", Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(this,LoginPage.class);
            startActivity(intent);
            finish();
        }
    }
    private boolean isOnline(){
        ConnectivityManager cm= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info.isConnectedOrConnecting();
    }
    private class PostFeedTask extends AsyncTask<RequestPackage,Void,String>{

        @Override
        protected String doInBackground(RequestPackage[] params) {
            return ServerCon.uploadFeed(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
        /*Intent to HomePage*/
            if(s.contains("successfull")) {
                Toast.makeText(FeedbackActivity.this, "Your Feedback was received", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(FeedbackActivity.this,HomePage.class);
                finish();
                startActivity(intent);

            }else{
                Toast.makeText(FeedbackActivity.this, "Some error occurred posting your feedback", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
