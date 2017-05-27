package com.example.sarvesh.takeajob;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

class CustomAdapter extends BaseAdapter {

    private Typeface font;
    private Context context;
    private int layout_id;
    ArrayList<DataItems> data;
    CustomAdapter(Context c, int i, ArrayList<DataItems> d){
        font = Typeface.createFromAsset( c.getAssets(), "fontawesome-webfont.ttf" );
        context=c;
        layout_id=i;
        data=d;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(layout_id, parent, false);
            DataItems dataItem = data.get(position);

            TextView imageView = (TextView) row.findViewById(R.id.image_drawer_list);
            imageView.setText(dataItem.icon);
            TextView textView = (TextView) row.findViewById(R.id.textView2);

            imageView.setTypeface(font);
            //imageView.setImageResource(dataItem.icon);
            textView.setText(dataItem.title);

            return row;
    }
}
