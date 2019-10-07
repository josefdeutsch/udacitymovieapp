package com.example.myapplication;

import org.jetbrains.annotations.NotNull;

public class Config {

    public static final String YoutubeAPIKEY = "";
    public static final String MDBAPIKEY = "";

   ///////////////////////////////////////////////////////////////////////////////////////////////

   public static final String POPULARURL = "https://api.themoviedb.org/3/movie/popular?api_key="+MDBAPIKEY+"&language=en-US&page=1";
   public static final String TOPRATEDURL = "https://api.themoviedb.org/3/movie/top_rated?api_key="+MDBAPIKEY+"&language=en-US&page=1";
   public static final String MOVIEDETAILSURL = "https://api.themoviedb.org/3/movie/?api_key="+MDBAPIKEY+"&language=en-US";


    public static final int POPULAR = 0;
    public static final int TOP_RATED = 1;
    public static final int FAVORITE = 2;

    public static final String MOVIEID = "movieid";
    public static final String KEY = "key";
    public static final String RESULTS = "results";
    public static final String PATH = "posterpath";
    public static final String METADATA = "MetaData";
    public static final String QUERYMOVIEID = "queryMovieId";


  public static String concatinatewithmovieid(String movieid) {
   String segment ="https://api.themoviedb.org/3/movie/";
   String segment2 ="?api_key="+MDBAPIKEY+"&language=en-US";
   String url = segment.concat(movieid).concat(segment2);
   return url;
  }

  public static String concatenatewithKeys(String id) {
   String segment="https://api.themoviedb.org/3/movie/";
   String segment2 ="/videos?api_key="+MDBAPIKEY+"&language=en-US";
   return segment + id + segment2;
 }

 public static String concatinatewithReview(String id) {
   String segment = "https://api.themoviedb.org/3/movie/";
   String segment2 = "/reviews?api_key="+MDBAPIKEY+"&language=en-US&page=1";
   return segment+id+segment2;
 }
}
