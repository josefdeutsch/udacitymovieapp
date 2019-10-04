package com.example.myapplication.data;

public class Singleton {

    private static Singleton instance;
    private String movieid;
    private String path;
    private Singleton () {}
    public static Singleton getInstance () {
        if (Singleton.instance == null) {
            Singleton.instance = new Singleton ();
        }
        return Singleton.instance;
    }

    public static void setInstance(Singleton instance) {
        Singleton.instance = instance;
    }

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
