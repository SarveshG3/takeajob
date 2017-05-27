package com.example.sarvesh.takeajob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.util.HashMap;
import java.util.List;

public class LoginPage extends AppCompatActivity {
    EditText et1,et2;
    String email,pass;
    AwesomeValidation awesomeValidation;
    private final String LINK="http://192.168.43.187/takeajob/login.php";
    Typeface font;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);

       // Button button = (Button)findViewById( R.id.button );
        //button.setTypeface(font);
     /*   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    */
        progressDialog=new ProgressDialog(this);

        et1= (EditText) findViewById(R.id.editText);
        et2= (EditText) findViewById(R.id.editText2);
        awesomeValidation.addValidation(this, R.id.editText, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.editText2, "(?=[#$-/:-?{-~!\"^_`\\[\\]a-zA-Z]*([0-9#$-/:-?{-~!\"^_`\\[\\]]))(?=[#$-/:-?{-~!\"^_`\\[\\]a-zA-Z0-9]*[a-zA-Z])[#$-/:-?{-~!\"^_`\\[\\]a-zA-Z0-9]{4,}", R.string.passworderror);

    }
    public void mLogin(View view){
        if(awesomeValidation.validate()){
            email = et1.getText().toString().trim();
            pass = et2.getText().toString().trim();

            progressDialog.setMessage("Loading...");
            progressDialog.show();

            RequestPackage rp = new RequestPackage();
            rp.setMethod("GET"); //when we want to add parameters in the body of request
            rp.setUri(LINK); // Ipv4  Address Wireless LAN adapter network connection

            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("pass", pass);
            rp.setParams(params);
            if (isOnline()) {

                MyNetTask task = new MyNetTask();
                task.execute(rp);
            } else {
                Toast.makeText(this, "Connection is Offline", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting())
            return true;
        else
            return false;
    }
    public void mSignUp(View view){
        Intent intent=new Intent(this,SignUpPage.class);
        startActivity(intent);
    }
    public void mBrowseJobs(View view){
        Intent intent=new Intent(this,HomePage.class);
        startActivity(intent);
    }
    private class MyNetTask extends AsyncTask<RequestPackage,Void,List<String>> {

        @Override
        protected List<String> doInBackground(RequestPackage... params) {
            return  ServerCon.fetchData(params[0]);
        }

        @Override
        protected void onPostExecute(List<String> s) {
            if(s==null){
                Toast.makeText(LoginPage.this, "Username or Password did not match", Toast.LENGTH_SHORT).show();
            }
                else{
                int id=Integer.parseInt(s.get(0));
                String u=s.get(1);
                String e=s.get(3);
                String p=s.get(2);
                if(e.equals(email)&&p.equals(pass)) {
                    SharedPreferences sharedPreferences=getSharedPreferences("takeajobdata",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("email",e);
                    editor.putString("username",u);
                    editor.putInt("loginId",id).apply();

                    progressDialog.hide();

                    Toast.makeText(LoginPage.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginPage.this, HomePage.class);
                    intent.putExtra("email", email);
                    intent.putExtra("username", u);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast toast=Toast.makeText(LoginPage.this, "Oops! there seems to be some error", Toast.LENGTH_SHORT);
                    toast.setGravity(2,180,320);
                    toast.show();
                }
            }
        }
    }
}
