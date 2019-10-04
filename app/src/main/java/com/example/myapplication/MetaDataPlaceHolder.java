package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class MetaDataPlaceHolder implements Parcelable {


    private  String results;
    private  String title;
    private  String posterPath;
    private  String voteAverage;
    private  String releaseDate;
    private  String overview;
    private  String id;
    private  String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MetaDataPlaceHolder(){}

    public MetaDataPlaceHolder(String title, String posterPath, String voteAverage, String releaseDate, String overview, String id) {
        this.title = title;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.id = id;
    }
    public MetaDataPlaceHolder(String title, String posterPath, String voteAverage, String releaseDate, String overview, String id, String key) {
        this.title = title;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.id = id;
        this.key = key;
    }

    protected MetaDataPlaceHolder(Parcel in) {

        title = in.readString();
        posterPath = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        id = in.readString();
        key = in.readString();
    }

    public static final Creator<MetaDataPlaceHolder> CREATOR = new Creator<MetaDataPlaceHolder>() {
        @Override
        public MetaDataPlaceHolder createFromParcel(Parcel in) {
            return new MetaDataPlaceHolder(in);
        }

        @Override
        public MetaDataPlaceHolder[] newArray(int size) {
            return new MetaDataPlaceHolder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(voteAverage);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeString(id);
        dest.writeString(key);
    }
}
