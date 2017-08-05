package com.example.sarvesh.takeajob;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class FilterFragment extends DialogFragment{

    private Communicator communicator;
    Spinner skill,loc,exp;
    HashMap<String,String> m=new HashMap<>();
    Button cancel,apply;
    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final HashMap<String,String> map=new HashMap<>();

        View view=inflater.inflate(R.layout.fragment_filter, container, false);
        skill= (Spinner) view.findViewById(R.id.skill);
        loc= (Spinner) view.findViewById(R.id.location);
        exp= (Spinner) view.findViewById(R.id.experience);
        cancel= (Button) view.findViewById(R.id.cancel);
        apply= (Button) view.findViewById(R.id.apply);

        skill.setAdapter(ArrayAdapter.createFromResource(getContext(),R.array.skill_content,android.R.layout.simple_spinner_dropdown_item));
        loc.setAdapter(ArrayAdapter.createFromResource(getContext(),R.array.location_content,android.R.layout.simple_spinner_dropdown_item));
        exp.setAdapter(ArrayAdapter.createFromResource(getContext(),R.array.experience_content,android.R.layout.simple_spinner_dropdown_item));


        skill.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    TextView tv = (TextView) view;
                    //Toast.makeText(getContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                    map.put("skill", tv.getText().toString());
                }
                else{
                    map.put("skill", "blank");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    TextView tv = (TextView) view;
                    //Toast.makeText(getContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                    map.put("location", tv.getText().toString());
                }
                else {
                    map.put("location", "blank");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        exp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    TextView tv = (TextView) view;
                    //Toast.makeText(getContext(), tv.getText().toString(), Toast.LENGTH_SHORT).show();
                    map.put("experience", tv.getText().toString());
                }
                else {
                    map.put("experience", "blank");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                communicator.onCommunication(m);
                dismiss();
            }
        });

        m=map;
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Communicator) {
            communicator = (Communicator) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        communicator = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        Display display =((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int height=display.getHeight();

    }

    public interface Communicator{
        // TODO: Update argument type and name
        void onCommunication(HashMap<String,String> m);
    }
}
