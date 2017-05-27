package com.example.sarvesh.takeajob;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.util.HashMap;

public class SignUpPage extends AppCompatActivity {
    EditText et1,et2,et3,et4;
    Button signUpButton;
    static String user,email,pass,c_pass;
    private final String LINK="http://192.168.43.187/takeajob/signup.php";
    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        getSupportActionBar().hide();
     /*   getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
     */
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        et1 = (EditText) findViewById(R.id.editText1);
        et2 = (EditText) findViewById(R.id.editText);
        et3 = (EditText) findViewById(R.id.editText2);
        et4 = (EditText) findViewById(R.id.editText3);
        signUpButton= (Button) findViewById(R.id.button);

        awesomeValidation.addValidation(this, R.id.editText1, "^[a-zA-Z]\\B[a-zA-Z0-9]{4,18}[a-zA-Z0-9]\\b$", R.string.usernameerror);
        awesomeValidation.addValidation(this, R.id.editText, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.editText2, "(?=[#$-/:-?{-~!\"^_`\\[\\]a-zA-Z]*([0-9#$-/:-?{-~!\"^_`\\[\\]]))(?=[#$-/:-?{-~!\"^_`\\[\\]a-zA-Z0-9]*[a-zA-Z])[#$-/:-?{-~!\"^_`\\[\\]a-zA-Z0-9]{4,}", R.string.passworderror);
        awesomeValidation.addValidation(this, R.id.editText3, R.id.editText2, R.string.confirmpassworderror);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate())
                {
                    user = et1.getText().toString().trim();
                    email = et2.getText().toString().trim();
                    pass = et3.getText().toString().trim();
                    c_pass = et4.getText().toString().trim();
                    if(email.contains("gmail.com")||email.contains("gmail.in")||email.contains("gmail.co.in")||email.contains("hotmail.com")||email.contains("goibibo.com")||email.contains("yahoo.com")||email.contains("yahoo.in")||email.contains("yahoo.co.in")||email.contains("rediffmail.com")||email.contains("microsoft.com")){
                        RequestPackage rp = new RequestPackage();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("user", user);
                        map.put("email", email);
                        map.put("pass", pass);

                        rp.setParams(map);
                        rp.setMethod("POST");
                        rp.setUri(LINK);

                        if (isOnline()) {
                            MyTask task = new MyTask();
                            task.execute(rp);
                        }
                        else{
                            Toast.makeText(SignUpPage.this, "Please check your Internet Connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(SignUpPage.this, "Please check your email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isOnline(){
        ConnectivityManager cm= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info.isConnectedOrConnecting();
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent=new Intent(this, LoginPage.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
    */
private class MyTask extends AsyncTask<RequestPackage,Void,String>{

    @Override
    protected String doInBackground(RequestPackage[] params) {
        return ServerCon.uploadData(params[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        /*Intent to HomePage*/
        if (s!=null) {
            if (s.contains("successfull")) {
                Toast.makeText(SignUpPage.this, "You are now registered", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                finish();
                startActivity(intent);

            } else {
                Toast.makeText(SignUpPage.this, "Server responded Error", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(SignUpPage.this, "We are unable to create your profile", Toast.LENGTH_SHORT).show();
        }
    }
}
}