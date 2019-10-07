package com.example.myapplication.body;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MetaData implements Parcelable  {

    private ArrayList<String> title;
    private ArrayList<String> voteAverage;
    private ArrayList<String> releaseDate;
    private ArrayList<String> overview;
    private ArrayList<String> posterpatharraylist;
    private ArrayList<String> id;
    private ArrayList<String> videokeys;

    public MetaData(ArrayList<String> posterpatharraylist, ArrayList<String> title, ArrayList<String> voteAverage,
                    ArrayList<String> releaseDate, ArrayList<String> overview, ArrayList<String> id) {

        this.posterpatharraylist=posterpatharraylist;
        this.title = title;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.id = id;

    }

    protected MetaData(Parcel in) {
         posterpatharraylist = in.createStringArrayList();
         title = in.createStringArrayList();
         voteAverage = in.createStringArrayList();
         releaseDate = in.createStringArrayList();
         overview = in.createStringArrayList();
         posterpatharraylist = in.createStringArrayList();
         id = in.createStringArrayList();
         videokeys = in.createStringArrayList();
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

    public ArrayList getTitle() {
        return title;
    }

    public void setTitle(ArrayList title) {
        this.title = title;
    }

    public ArrayList getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(ArrayList voteAverage) {
        this.voteAverage = voteAverage;
    }

    public ArrayList getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(ArrayList releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ArrayList getOverview() {
        return overview;
    }

    public void setOverview(ArrayList overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(title);
        dest.writeStringList(voteAverage);
        dest.writeStringList(releaseDate);
        dest.writeStringList(overview);
        dest.writeStringList(posterpatharraylist);
        dest.writeStringList(id);
        dest.writeStringList(videokeys);
    }

    public ArrayList<String> getVideokeys() {
        return videokeys;
    }

    public void setVideokeys(ArrayList<String> videokeys) {
        this.videokeys = videokeys;
    }

    public ArrayList<String> getId() {
        return id;
    }
}
