package com.example.sarvesh.takeajob;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class AppliedJobsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout swipeRefreshLayout;
    ListView list;
    int loginId;
    String title;

    private final String LINK="http://192.168.43.187/takeajob/job_applications_fetch.php";
    TextView tv1,tv2,tv3,tv4,tv5;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle=savedInstanceState;
        setContentView(R.layout.activity_applied_jobs);

        title=getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setIcon(R.drawable.main_icon);

        list= (ListView) findViewById(R.id.applied_list);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_job_applied);

        swipeRefreshLayout.setOnRefreshListener(this);

        SharedPreferences sharedPreferences=getSharedPreferences("takeajobdata",MODE_PRIVATE);
        loginId=sharedPreferences.getInt("loginId",0);
        makeAplliedJobsDataCall();
    }

    private  void makeAplliedJobsDataCall(){
        swipeRefreshLayout.setRefreshing(true);

        if(loginId==0){
            Toast.makeText(this, "You need to login First", Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(this,LoginPage.class);
            startActivity(intent);
            finish();
        }
        else {
            HashMap<String ,String> params=new HashMap<>();
            params.put("loginId",String.valueOf(loginId));
            RequestPackage rp = new RequestPackage();
            rp.setUri(LINK);
            rp.setMethod("GET");
            rp.setParams(params);

            if (isOnline()) {

                MyAppliedTask task = new MyAppliedTask();

                task.execute(rp);
            } else {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "Connection is Offline", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting())
            return true;
        else
            return false;
    }

    @Override
    public void onRefresh() {
        makeAplliedJobsDataCall();
    }


    private class MyAppliedTask extends AsyncTask<RequestPackage,Void,ArrayList<AdapterData>> {

        @Override
        protected ArrayList<AdapterData> doInBackground(RequestPackage... params) {

            return ServerCon.fetchJobData(params[0]);

        }

        @Override
        protected void onPostExecute(final ArrayList<AdapterData> data) {
            //progressDialog.hide();
            swipeRefreshLayout.setRefreshing(false);

            if (data == null) {
                Toast.makeText(AppliedJobsActivity.this, "Oops!, Something went wrong", Toast.LENGTH_SHORT).show();
            }
            else{
                list.setAdapter(new MyCustomAdapter(getApplicationContext(), data));
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final AdapterData d=data.get(position);
                        Intent intent=new Intent(AppliedJobsActivity.this,JobDetailActivity.class);
                        intent.putExtra("detailsParcel",d);
                        intent.putExtra("fromIntent","Applied");
                        intent.putExtra("title",title);
                        startActivity(intent);
                        // Toast.makeText(getContext(), "Clicked Job at position" + position, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private class MyCustomAdapter extends BaseAdapter{

        Context context=null;
        ArrayList<AdapterData> dataItem=null;
        MyCustomAdapter(Context c,ArrayList<AdapterData> d){
            context=c;
            dataItem=d;
        }
        @Override
        public int getCount() {
            return dataItem.size();
        }

        @Override
        public Object getItem(int position) {
            return dataItem.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                View row = getLayoutInflater().inflate(R.layout.jobs_single_row, parent, false);
                tv1 = (TextView) row.findViewById(R.id.job_description);
                tv2 = (TextView) row.findViewById(R.id.exp);
                tv3 = (TextView) row.findViewById(R.id.location);
                tv4 = (TextView) row.findViewById(R.id.post_date);
                tv5 = (TextView) row.findViewById(R.id.qualification);

                AdapterData data = dataItem.get(position);

                tv1.setText(data.j_title + " - " + data.c_name + " - " + data.j_type);
                tv2.setText(data.exp + " yrs");
                tv3.setText(data.location);
                tv4.setText(data.p_date);
                tv5.setText(data.qual);

                return row;
            }
            else return convertView;
        }
    }

}
