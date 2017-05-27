package com.example.sarvesh.takeajob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    CircleImageView circleImageView;
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv12;
    Typeface face;
    private final String LINK="http://192.168.43.187/takeajob/profile_fetch.php";
    ProgressDialog progressDialog;
    String name,age,desg,exp,contact,c_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);

        face=Typeface.createFromAsset(getAssets(),"fontawesome-webfont.ttf");

        tv1 = (TextView) findViewById(R.id.proUser);
        tv2 = (TextView) findViewById(R.id.proName);
        tv3 = (TextView) findViewById(R.id.proEmail);
        tv4 = (TextView) findViewById(R.id.age);
        tv5 = (TextView) findViewById(R.id.proExp);
        tv6 = (TextView) findViewById(R.id.proDesignation);
        tv7 = (TextView) findViewById(R.id.brief_case);
        tv7.setTypeface(face);
        tv8 = (TextView) findViewById(R.id.building);
        tv8.setTypeface(face);
        tv9 = (TextView) findViewById(R.id.contact);
        tv9.setTypeface(face);
        tv10 = (TextView) findViewById(R.id.exp);
        tv10.setTypeface(face);
        tv11=(TextView) findViewById(R.id.proContact);
        tv12=(TextView) findViewById(R.id.proCompany);

        String imgPath=getSharedPreferences("takeajobdata",MODE_PRIVATE).getString("pathtoimage",null);
        if(imgPath!=null) {
            loadImageFromStorage(imgPath);
        }
        setData();

    }
    void setData(){
        String user=getSharedPreferences("takeajobdata",MODE_PRIVATE).getString("username","blank");

        HashMap<String,String> map=new HashMap<>();
        map.put("username",user);
        RequestPackage rp=new RequestPackage();
        rp.setMethod("GET");
        rp.setUri(LINK);
        rp.setParams(map);
        if (isOnline()) {
            ProfileActivity.MyFetchTask task = new ProfileActivity.MyFetchTask();
            task.execute(rp);
        }
        else{
            Toast.makeText(getApplicationContext(), "Please check your Internet Connectivity", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }
    private boolean isOnline(){
        ConnectivityManager cm= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info.isConnectedOrConnecting();
    }
    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            circleImageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.edit){
            Intent intent=new Intent(this,EditProfileActivity.class);
            intent .putExtra("name",name);
            intent.putExtra("age",age);
            intent .putExtra("designation",desg);
            intent .putExtra("contact",contact);
            intent .putExtra("c_name",c_name);
            intent .putExtra("experience",exp);

            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyFetchTask extends AsyncTask<RequestPackage,Void,List<String>> {

        @Override
        protected List<String> doInBackground(RequestPackage... params) {
            return  ServerCon.fetchProfileData(params[0]);
        }

        @Override
        protected void onPostExecute(List<String> s) {
            progressDialog.dismiss();

            if(s==null){
                Toast.makeText(ProfileActivity.this, "Profile Details not available at the moment", Toast.LENGTH_SHORT).show();
            }
            else{
                String user=getSharedPreferences("takeajobdata",MODE_PRIVATE).getString("username","user");
                tv1.setText(user);
                String email=getSharedPreferences("takeajobdata", MODE_PRIVATE).getString("email","email");
                tv3.setText(email);
                name=s.get(0);
                age=s.get(1);
                desg=s.get(2);
                contact=s.get(3);
                c_name=s.get(4);
                exp=s.get(5);

                tv2.setText(name);
                tv4.setText(age);
                tv5.setText(exp);
                tv6.setText(desg);
                tv11.setText(contact);
                tv12.setText(c_name);

            }
        }
    }
}
