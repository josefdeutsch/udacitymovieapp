package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MetaDataKeyHolder implements Parcelable {

    public MetaDataKeyHolder(ArrayList<String> keys) {
        this.keys = keys;
    }

    public ArrayList<String> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<String> keys) {
        this.keys = keys;
    }

    private ArrayList<String> keys;


    protected MetaDataKeyHolder(Parcel in) {
        keys = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(keys);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MetaDataKeyHolder> CREATOR = new Creator<MetaDataKeyHolder>() {
        @Override
        public MetaDataKeyHolder createFromParcel(Parcel in) {
            return new MetaDataKeyHolder(in);
        }

        @Override
        public MetaDataKeyHolder[] newArray(int size) {
            return new MetaDataKeyHolder[size];
        }
    };
}
