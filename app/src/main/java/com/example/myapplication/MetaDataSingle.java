package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class MetaDataSingle implements Parcelable {


    private  String RESULTS;
    private  String TITLE;
    private  String POSTER_PATH;
    private  String VOTE_AVERAGE;
    private  String RELEASE_DATE;
    private  String OVERVIEW;
    private  String ID;

    public String getRESULTS() {
        return RESULTS;
    }

    public void setRESULTS(String RESULTS) {
        this.RESULTS = RESULTS;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getPOSTER_PATH() {
        return POSTER_PATH;
    }

    public void setPOSTER_PATH(String POSTER_PATH) {
        this.POSTER_PATH = POSTER_PATH;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public MetaDataSingle(){}

    public MetaDataSingle(String TITLE, String POSTER_PATH, String VOTE_AVERAGE, String RELEASE_DATE, String OVERVIEW, String ID) {

        this.TITLE = TITLE;
        this.POSTER_PATH = POSTER_PATH;
        this.VOTE_AVERAGE = VOTE_AVERAGE;
        this.RELEASE_DATE = RELEASE_DATE;
        this.OVERVIEW = OVERVIEW;
        this.ID = ID;
    }

    protected MetaDataSingle(Parcel in) {

        TITLE = in.readString();
        POSTER_PATH = in.readString();
        VOTE_AVERAGE = in.readString();
        RELEASE_DATE = in.readString();
        OVERVIEW = in.readString();
        ID = in.readString();
    }

    public static final Creator<MetaDataSingle> CREATOR = new Creator<MetaDataSingle>() {
        @Override
        public MetaDataSingle createFromParcel(Parcel in) {
            return new MetaDataSingle(in);
        }

        @Override
        public MetaDataSingle[] newArray(int size) {
            return new MetaDataSingle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(TITLE);
        dest.writeString(POSTER_PATH);
        dest.writeString(VOTE_AVERAGE);
        dest.writeString(RELEASE_DATE);
        dest.writeString(OVERVIEW);
        dest.writeString(ID);
    }
}
