package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MetaData implements Parcelable  {

    private ArrayList TITLE;
    private ArrayList VOTE_AVERAGE;
    private ArrayList RELEASE_DATE;
    private ArrayList OVERVIEW;
    private ArrayList<String> posterpatharraylist;

    public ArrayList<String> get_id() {
        return _id;
    }

    public void set_id(ArrayList<String> _id) {
        this._id = _id;
    }

    private ArrayList<String> _id;

    public MetaData(ArrayList<String> posterpatharraylist,ArrayList<String> TITLE, ArrayList<String> VOTE_AVERAGE, ArrayList<String> RELEASE_DATE, ArrayList<String> OVERVIEW, ArrayList<String> _id) {

        this.posterpatharraylist=posterpatharraylist;
        this.TITLE=TITLE;
        this.VOTE_AVERAGE=VOTE_AVERAGE;
        this.RELEASE_DATE=RELEASE_DATE;
        this.OVERVIEW=OVERVIEW;
        this._id = _id;
    }

    protected MetaData(Parcel in) {
        posterpatharraylist = in.createStringArrayList();
    }

    public static final Creator<MetaData> CREATOR = new Creator<MetaData>() {
        @Override
        public MetaData createFromParcel(Parcel in) {
            return new MetaData(in);
        }

        @Override
        public MetaData[] newArray(int size) {
            return new MetaData[size];
        }
    };

    public  void setPosterPath(ArrayList<String> posterpatharraylist){
        this.posterpatharraylist=posterpatharraylist;
   }

    public ArrayList<String> getPosterPath(){
        return this.posterpatharraylist;
    }

    public ArrayList getTITLE() {
        return TITLE;
    }

    public void setTITLE(ArrayList TITLE) {
        this.TITLE = TITLE;
    }

    public ArrayList getVOTE_AVERAGE() {
        return VOTE_AVERAGE;
    }

    public void setVOTE_AVERAGE(ArrayList VOTE_AVERAGE) {
        this.VOTE_AVERAGE = VOTE_AVERAGE;
    }

    public ArrayList getRELEASE_DATE() {
        return RELEASE_DATE;
    }

    public void setRELEASE_DATE(ArrayList RELEASE_DATE) {
        this.RELEASE_DATE = RELEASE_DATE;
    }

    public ArrayList getOVERVIEW() {
        return OVERVIEW;
    }

    public void setOVERVIEW(ArrayList OVERVIEW) {
        this.OVERVIEW = OVERVIEW;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(posterpatharraylist);
    }
}
