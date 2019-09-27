package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Traveler implements Parcelable {

    private String TITLE;
    private String VOTE_AVERAGE;
    private String RELEASE_DATE;
    private String OVERVIEW;
    private String posterpath;
    private ArrayList<String> stringArrayList;

    protected Traveler(Parcel in) {
        TITLE = in.readString();
        VOTE_AVERAGE = in.readString();
        RELEASE_DATE = in.readString();
        OVERVIEW = in.readString();
        posterpath = in.readString();
        stringArrayList = in.createStringArrayList();
        _id = in.readString();
    }

    public static final Creator<Traveler> CREATOR = new Creator<Traveler>() {
        @Override
        public Traveler createFromParcel(Parcel in) {
            return new Traveler(in);
        }

        @Override
        public Traveler[] newArray(int size) {
            return new Traveler[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    private String _id;

    public Traveler(String TITLE, String VOTE_AVERAGE, String RELEASE_DATE, String OVERVIEW, String posterpath,String ID,ArrayList<String> stringArrayList) {
        this.TITLE = TITLE;
        this.VOTE_AVERAGE = VOTE_AVERAGE;
        this.RELEASE_DATE = RELEASE_DATE;
        this.OVERVIEW = OVERVIEW;
        this.posterpath = posterpath;
        this._id = ID;
        this.stringArrayList=stringArrayList;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getVOTE_AVERAGE() {
        return VOTE_AVERAGE;
    }

    public void setVOTE_AVERAGE(String VOTE_AVERAGE) {
        this.VOTE_AVERAGE = VOTE_AVERAGE;
    }

    public String getRELEASE_DATE() {
        return RELEASE_DATE;
    }

    public void setRELEASE_DATE(String RELEASE_DATE) {
        this.RELEASE_DATE = RELEASE_DATE;
    }

    public String getOVERVIEW() {
        return OVERVIEW;
    }

    public void setOVERVIEW(String OVERVIEW) {
        this.OVERVIEW = OVERVIEW;
    }

    public String getPosterpath() {
        return posterpath;
    }

    public void setPosterpath(String posterpath) {
        this.posterpath = posterpath;
    }

    public ArrayList<String> getStringArrayList() {
        return stringArrayList;
    }

    public void setStringArrayList(ArrayList<String> stringArrayList) {
        this.stringArrayList = stringArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(TITLE);
        dest.writeString(VOTE_AVERAGE);
        dest.writeString(RELEASE_DATE);
        dest.writeString(OVERVIEW);
        dest.writeString(posterpath);
        dest.writeStringList(stringArrayList);
        dest.writeString(_id);
    }
}
