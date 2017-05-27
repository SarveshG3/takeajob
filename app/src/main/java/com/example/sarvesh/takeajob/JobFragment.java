package com.example.sarvesh.takeajob;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;

public class JobFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    Context c;
    ListView listView;
    SwipeRefreshLayout swipeRefresh;
    View v;
    HashMap<String,String> filterMap=new HashMap<>();
    //ProgressDialog progressDialog;
    ViewGroup con;
    private final String LINK="http://192.168.43.187/takeajob/job_description_home.php";
    TextView tv1,tv2,tv3,tv4,tv5;
    OnFragmentInteractionListener listner;

    public JobFragment() {
        // Required empty public constructor
    }

    void setCommunicator(OnFragmentInteractionListener l){
        listner= l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void makeCall(){
        RequestPackage rp = new RequestPackage();
        rp.setMethod("GET"); //when we want to add parameters in the body of request
        rp.setUri(LINK); // Ipv4  Address Wireless LAN adapter network connection
        rp.setParams(filterMap);

        if (isOnline()) {

            MyFragmentTask task = new MyFragmentTask();
      /*      progressDialog=new ProgressDialog(getContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading Data...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        */  swipeRefresh.setRefreshing(true);
            task.execute(rp);
        }
        else {
            swipeRefresh.setRefreshing(false);
            Toast.makeText(getActivity(), "Connection is Offline", Toast.LENGTH_SHORT).show();

        }
    }

    public void setMaps(HashMap<String,String> data){
        filterMap=data;
        makeCall();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefresh= (SwipeRefreshLayout) v.findViewById(R.id.swiperefreshlayout);
        swipeRefresh.setOnRefreshListener(JobFragment.this);
    }

    @Override
    public void onResume() {
        super.onResume();
        makeCall();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_job, container, false);
        con=container;
        v=view;
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
 /*   public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        c=context;
        filterMap.put("skill", "blank");
        filterMap.put("location","blank");
        filterMap.put("experience","blank");
/*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

   @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

    @Override
    public void onRefresh() {
        makeCall();
        /*if (swipeRefresh!=null) {
            swipeRefresh.setRefreshing(false);
            swipeRefresh.destroyDrawingCache();
            swipeRefresh.clearAnimation();
        }   */
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    interface OnFragmentInteractionListener {
        void onFragmentInteraction(AdapterData data);
    }

   private class MyAdapter extends BaseAdapter{
        ArrayList<AdapterData> dataItem;
        Context context;
        MyAdapter( Context c,ArrayList<AdapterData> d){
            dataItem=d;
            context=c;
        }
       @Override
       public int getCount() {
           return dataItem.size();
       }

       @Override
       public AdapterData getItem(int position) {
           return dataItem.get(position);
       }

       @Override
       public long getItemId(int position) {
           return position;
       }

       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           if (convertView == null) {
               LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               View row = inflater.inflate(R.layout.jobs_single_row, parent, false);

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


   private class MyFragmentTask extends AsyncTask<RequestPackage,Void,ArrayList<AdapterData>>{

       @Override
       protected ArrayList<AdapterData> doInBackground(RequestPackage... params) {

           return ServerCon.fetchJobData(params[0]);

       }

       @Override
       protected void onPostExecute(final ArrayList<AdapterData> data) {
           //progressDialog.hide();
           swipeRefresh.setRefreshing(false);

           if (data == null) {
               Toast.makeText(c,"Oops!, Something went wrong", Toast.LENGTH_SHORT).show();
           }
            else{

                listView = (ListView) v.findViewById(R.id.frag_list);
                listView.setAdapter(new MyAdapter(getContext(), data));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final AdapterData d=data.get(position);
                       // Toast.makeText(getContext(), "Clicked Job at position" + position, Toast.LENGTH_SHORT).show();
                        listner.onFragmentInteraction(d);
                    }
                });
           }
       }
   }
}
