package com.example.sarvesh.takeajob;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Sarvesh on 19-04-2017.
 */
class AdapterData implements Parcelable {
    String j_type;
    String p_date;
    String j_title;
    String exp;
    String qual;
    String c_name;
    String location;
    String description;
    String salary_range;
    int job_code;
    AdapterData(ArrayList<String> data){
        j_type=data.get(0);
        p_date=data.get(1);
        j_title=data.get(2);
        exp=data.get(3);
        qual=data.get(4);
        c_name=data.get(5);
        location=data.get(6);
        description=data.get(7);
        salary_range=data.get(8)+" - "+data.get(9);
        job_code=Integer.parseInt(data.get(10));
    }

    protected AdapterData(Parcel in) {
        j_type = in.readString();
        p_date = in.readString();
        j_title = in.readString();
        exp = in.readString();
        qual = in.readString();
        c_name = in.readString();
        location = in.readString();
        description = in.readString();
        salary_range = in.readString();
        job_code=in.readInt();
    }

    public static final Creator<AdapterData> CREATOR = new Creator<AdapterData>() {
        @Override
        public AdapterData createFromParcel(Parcel in) {
            return new AdapterData(in);
        }

        @Override
        public AdapterData[] newArray(int size) {
            return new AdapterData[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(j_type);
        dest.writeString(p_date);
        dest.writeString(j_title);
        dest.writeString(exp);
        dest.writeString(qual);
        dest.writeString(c_name);
        dest.writeString(location);
        dest.writeString(description);
        dest.writeString(salary_range);
        dest.writeInt(job_code);
    }
}
