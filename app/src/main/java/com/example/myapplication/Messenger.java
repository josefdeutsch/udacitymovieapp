package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Messenger implements Parcelable {

    private String title;
    private String voteAverage;
    private String releaseDate;
    private String overview;
    private String posterpath;
    private ArrayList<String> stringArrayList;
    private String review;
    private String isaFAVORITE;
    private String id;

    protected Messenger(Parcel in) {
        title = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        posterpath = in.readString();
        stringArrayList = in.createStringArrayList();
        id = in.readString();
        review = in.readString();
        isaFAVORITE = in.readString();
    }

    public static final Creator<Messenger> CREATOR = new Creator<Messenger>() {
        @Override
        public Messenger createFromParcel(Parcel in) {
            return new Messenger(in);
        }

        @Override
        public Messenger[] newArray(int size) {
            return new Messenger[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Messenger(String title, String voteAverage, String releaseDate, String overview, String posterpath, String ID, ArrayList<String> stringArrayList, String review, String isaFavorite) {
        this.title = title;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.posterpath = posterpath;
        this.id = ID;
        this.stringArrayList=stringArrayList;
        this.review = review;
        this.isaFAVORITE = isaFavorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
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

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getIsaFAVORITE() {
        return isaFAVORITE;
    }

    public void setIsaFAVORITE(String isaFAVORITE) {
        this.isaFAVORITE = isaFAVORITE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(voteAverage);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeString(posterpath);
        dest.writeStringList(stringArrayList);
        dest.writeString(id);
        dest.writeString(review);
        dest.writeString(isaFAVORITE);
    }
}
