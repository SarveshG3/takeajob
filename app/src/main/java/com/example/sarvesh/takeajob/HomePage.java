package com.example.sarvesh.takeajob;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomePage extends AppCompatActivity implements FilterFragment.Communicator,JobFragment.OnFragmentInteractionListener,JobDetailFragment.ApplicationListener{

    DrawerLayout drawerLayout;
    ListView drawerList;
    FrameLayout frameLayout;
    String[] drawerContent;
    String[] drawerIcon;
    static  int jobId;
    ActionBarDrawerToggle mDrawerToggle;
    CharSequence title;
    LinearLayout mlinearLayout;
    TextView tv1,tv2;
    CircleImageView circleImageView;

    void generateFrag(){
        JobFragment jfrag=new JobFragment();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction t=fm.beginTransaction();
        t.add(R.id.content_frame,jfrag,"jobfrag");
        t.commit();
        jfrag.setCommunicator(this);
    }

    @Override
    protected void onResume() {

        super.onResume();
        String imgPath=getSharedPreferences("takeajobdata",MODE_PRIVATE).getString("pathtoimage",null);
        if(imgPath!=null) {
            loadImageFromStorage(imgPath);
        }
        String username=getSharedPreferences("takeajobdata", MODE_PRIVATE).getString("username",null);
        tv1.setText(username);
        String email=getSharedPreferences("takeajobdata", MODE_PRIVATE).getString("email",null);
        tv2.setText(email);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        title="TakeAJob";
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setIcon(R.drawable.main_icon);

        tv1= (TextView) findViewById(R.id.tv_1);
        tv2= (TextView) findViewById(R.id.tv_2);
        circleImageView= (CircleImageView) findViewById(R.id.profile_image);

        String u=getSharedPreferences("takeajobdata",MODE_PRIVATE).getString("username","Username");
        String e=getSharedPreferences("takeajobdata",MODE_PRIVATE).getString("email","Email");

        tv1.setText(u);
        tv2.setText(e);

        frameLayout= (FrameLayout) findViewById(R.id.content_frame);
        drawerContent=getResources().getStringArray(R.array.drawer_content);
        drawerIcon=getResources().getStringArray(R.array.drawer_content_image);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList= (ListView) findViewById(R.id.left_drawer);

        mlinearLayout= (LinearLayout) findViewById(R.id.linear_layout1);

        ArrayList<DataItems> dataItems=new ArrayList<>();
        for(int k=0;k<drawerContent.length;k++){
            dataItems.add(new DataItems(drawerIcon[k],drawerContent[k]));
        }
        drawerList.setAdapter(new CustomAdapter(this, R.layout.drawer_list_item, dataItems));
        //set the listner to handle click events over the elements of drawerList
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        //(SELF)this toggle object handles the event of toggling between open and closed state of the drawer.
        //(SELF)ActionBarDrawerToggle implements the Drawer.DrawerToggle interface which contains the declaration of onDrawerOpened and onDrawerClosed methods.
        mDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close){
           /*
           ( SELF)these two methods need not overridden as there implementation is internal,will see in the code that follows
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                return super.onOptionsItemSelected(item);
            }

            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                super.onConfigurationChanged(newConfig);
            }
            */
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };


        // Set the drawer toggle as the DrawerListener
        //(SELF)failing to set this listner will result in drawer not notifying the action bar home icon to change on toggle and the three stripped icon will not change.
        drawerLayout.addDrawerListener(mDrawerToggle);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomePage.this,ProfileActivity.class);
                startActivity(i);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      //(SELF)it enables the icon before the actionbar title if not set true will not show any arrow icon for Options menu.
        getSupportActionBar().setHomeButtonEnabled(true);              //don't know wy use this--not making any diference in this case.
        generateFrag();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(mlinearLayout);
        JobDetailFragment dfrag= (JobDetailFragment) getSupportFragmentManager().findFragmentByTag("dfrag");

        if(dfrag!=null) {
            menu.findItem(R.id.filter).setVisible(!drawerOpen&&!dfrag.isVisible());
            menu.findItem(R.id.save).setVisible(!drawerOpen && dfrag.isVisible());
        }
        else{
            menu.findItem(R.id.filter).setVisible(!drawerOpen);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    /*(SELF) this method is overridden to tell that the navigation icon over the Options menu bar is clicked */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    // Pass the event to ActionBarDrawerToggle, if it returns
    // true, then it has handled the app icon touch event
        /*( SELF)here mDrawerToggle.onOptionsItemSelected() method invocation is a call to the onOptionsItemSelected() method of the ActionBarDrawerToggle class which returns true if the action bar icon was clicked.*/
    if (mDrawerToggle.onOptionsItemSelected(item)) {

        return true;
    }
    else {
        switch(item.getItemId()){
            case  R.id.filter:{
                if(item.isVisible()) {
                    FilterFragment fragment = new FilterFragment();
                    //fragment.setCancelable(false);
                    getSupportFragmentManager().beginTransaction().add(fragment, "filterfrag").commit();
                }
                break;
            }

            case R.id.save: {
                if (item.isVisible()) {
                    SharedPreferences preferences = getSharedPreferences("takeajobdata", MODE_PRIVATE);
                    int loginId = preferences.getInt("loginId", 0);
                    if (loginId != 0) {
                        String link = "http://192.168.43.187/takeajob/saveJob.php";
                        RequestPackage rp = new RequestPackage();
                        rp.setMethod("POST"); //when we want to add parameters in the body of request
                        rp.setUri(link); // Ipv4  Address Wireless LAN adapter network connection

                        HashMap<String, String> params = new HashMap<>();
                        params.put("loginId", String.valueOf(loginId));
                        params.put("jobId", String.valueOf(jobId));

                        rp.setParams(params);

                        if (isOnline()) {

                            HomePage.MyNetTask task = new HomePage.MyNetTask();
                            task.execute(rp);
                        } else {
                            Toast.makeText(this, "Connection is Offline", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "You must be logged In", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                        startActivity(intent);
                    }
                }
                break;
            }
        }
    }
    return super.onOptionsItemSelected(item);
    }

    /*( SELF)onPostCreate is an Activity Lifecycle method which syncs the state of the navigation icon on being clicked*/
    //if not overridden, will result in the navigation icon remain same on clicks.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
//handling screen rotations
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onFragmentInteraction(AdapterData data) {
        Bundle bundle=new Bundle();
        bundle.putParcelable("Parcel",data);
        JobDetailFragment jobDetailFragment=JobDetailFragment.newInstance(bundle);
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,jobDetailFragment,"dfrag");
        transaction.addToBackStack("addJobFragment");
        transaction.commit();
        jobDetailFragment.setApplicationListener(this);
    }


    @Override
    public void setParams(int id) {
        jobId=id;
    }

    @Override
    public void onCommunication(HashMap<String,String> map) {
        JobFragment jfrag= (JobFragment) getSupportFragmentManager().findFragmentByTag("jobfrag");
        jfrag.setMaps(map);
    }


    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        //int save = -1;
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView v= (TextView) view.findViewById(R.id.textView2);
            String title=v.getText().toString();
            Log.d("SERVER","Clicked at "+position);
            //parent.getChildAt(position).setBackgroundColor(ContextCompat.getColor(HomePage.this, R.color.colorPrimary));
            //if (save != -1 && save != position){
            //    parent.getChildAt(save).setBackgroundColor(Color.WHITE);
            //}
            //save = position;
            Log.d("SERVER","Draver OnClick"+position);
            switch(position){
                case 0:{
                        generateFrag();
                        invalidateOptionsMenu();
                        break;}
                case 1:{
                        Intent intent=new Intent(HomePage.this,ProfileActivity.class);
                        intent.putExtra("title",title);
                        startActivity(intent);
                        break;}
                case 2:{
                        Intent intent=new Intent(HomePage.this,AppliedJobsActivity.class);
                        intent.putExtra("title",title);
                        startActivity(intent);
                        break;}
                case 3:{
                        Intent intent=new Intent(HomePage.this,SavedJobsActivity.class);
                        intent.putExtra("title",title);
                        startActivity(intent);
                        break;}
                case 4:{

                        break;}
                case 5:{
                        Intent intent=new Intent(HomePage.this,FeedbackActivity.class);
                        intent.putExtra("title",title);
                        startActivity(intent);
                        break;}
                case 6:{
                        Intent intent=new Intent(HomePage.this,AboutUsActivity.class);
                        intent.putExtra("title",title);
                        startActivity(intent);
                        break;}
                case 7:{
                        SharedPreferences preferences=getSharedPreferences("takeajobdata",MODE_PRIVATE);
                        preferences.edit().putInt("loginId",0).apply();
                        tv1.setText("");
                        tv2.setText("");
                        Intent intent=new Intent(HomePage.this,LoginPage.class);
                        startActivity(intent);
                        break;}

            }

            selectItem(position);
        }
        void selectItem(int p){
            drawerList.setItemChecked(p,true);
            drawerLayout.closeDrawer(mlinearLayout);
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    public void mApply(View v){
        SharedPreferences preferences = getSharedPreferences("takeajobdata", MODE_PRIVATE);
        int loginId = preferences.getInt("loginId", 0);
        if (loginId != 0) {
            String link = "http://192.168.43.187/takeajob/applyJob.php";
            RequestPackage rp = new RequestPackage();
            rp.setMethod("POST"); //when we want to add parameters in the body of request
            rp.setUri(link); // Ipv4  Address Wireless LAN adapter network connection

            HashMap<String, String> params = new HashMap<>();
            params.put("loginId", String.valueOf(loginId));
            params.put("jobId", String.valueOf(jobId));

            rp.setParams(params);

            if (isOnline()) {

                HomePage.MyApplyTask task = new HomePage.MyApplyTask();
                task.execute(rp);
            } else {
                Toast.makeText(this, "Connection is Offline", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "You must be logged In", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(intent);
        }
    }


    private class MyNetTask extends AsyncTask<RequestPackage,Void,String> {

        @Override
        protected String doInBackground(RequestPackage... params) {
            return  ServerCon.saveJob(params[0]);

        }

        @Override
        protected void onPostExecute(String s) {
        /*Intent to HomePage*/
            if(s!=null){
                if (s.contains("successfull")) {
                    Toast.makeText(HomePage.this, "You job is saved", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().popBackStack();
            //        getSupportFragmentManager().findFragmentByTag("jobfrag").onCreate(null);
                } else {
                    Toast.makeText(HomePage.this, "Failure! Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private class MyApplyTask extends AsyncTask<RequestPackage,Void,String> {

        @Override
        protected String doInBackground(RequestPackage... params) {
            return  ServerCon.applyJob(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
        /*Intent to HomePage*/
            if(s!=null){
                if (s.contains("successfull")) {
                    Toast.makeText(HomePage.this, "You Successfull Applied for this Job", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().popBackStack();
                    //        getSupportFragmentManager().findFragmentByTag("jobfrag").onCreate(null);
                } else {
                    Toast.makeText(HomePage.this, "Failure! Try Again", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
class DataItems{
    String icon;
    String title;

    DataItems(String i,String s){
        icon=i;
        title=s;
    }
}
