package com.example.sarvesh.takeajob;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JobDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JobDetailFragment extends Fragment {

    TextView tv1,tv2,tv3,tv4,tv5,tv6;
    Button button;
    static Bundle bundle;
    ApplicationListener listner;

    public JobDetailFragment() {
        // Required empty public constructor
    }

    public static JobDetailFragment newInstance(Bundle b) {
        bundle =b;
        return new JobDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        getActivity().invalidateOptionsMenu();

        View row= inflater.inflate(R.layout.fragment_job_detail, container, false);
        tv1= (TextView) row.findViewById(R.id.job_t);
        tv2= (TextView) row.findViewById(R.id.salary_range);
        tv3= (TextView) row.findViewById(R.id.company_name);
        tv4= (TextView) row.findViewById(R.id.job_location);
        tv5= (TextView) row.findViewById(R.id.desc);
        tv6= (TextView) row.findViewById(R.id.date);
        button= (Button) row.findViewById(R.id.apply_button);

        AdapterData data=bundle.getParcelable("Parcel");

        listner.setParams(data.job_code);

        tv1.setText(data.j_title);
        tv2.setText("₹ "+data.salary_range);
        tv3.setText(data.c_name);
        tv4.setText(data.location);
        tv5.setText(data.description);
        tv6.setText(data.p_date);
        return row;
    }
    public void setApplicationListener(ApplicationListener l){
        this.listner=l;
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().invalidateOptionsMenu();
    }
    public interface ApplicationListener{
        void setParams(int id);
    }
}
