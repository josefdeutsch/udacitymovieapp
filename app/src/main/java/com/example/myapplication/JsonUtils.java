package com.example.myapplication;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class JsonUtils {
    private static final String TAG = "JsonUtils";
    private final static String RESULTS = "results";
    private final static String TITLE = "original_title";
    private final static String POSTER_PATH = "poster_path";
    private final static String VOTE_AVERAGE = "vote_average";
    private final static String RELEASE_DATE = "release_date";
    private final static String OVERVIEW = "overview";
    private final static String ID = "id";
    private final String http = "http://image.tmdb.org/t/p/w185";

    public MetaData parseJSON(String json) {

        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> average = new ArrayList<>();
        ArrayList<String> releaseDate = new ArrayList<>();
        ArrayList<String> overview = new ArrayList<>();
        ArrayList<String> posterpatharraylist = new ArrayList<>();
        ArrayList<String> _ids = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        System.out.println("in json :"+json);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i <= jsonArray.length()-1 ; i++) {
                posterpatharraylist = supply(posterpatharraylist, sb, jsonArray, i);
                System.out.println(posterpatharraylist.get(i));
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                title.add(jsonObject1.optString(TITLE));
                average.add(jsonObject1.optString(VOTE_AVERAGE));
                releaseDate.add(jsonObject1.optString(RELEASE_DATE));
                overview.add(jsonObject1.optString(OVERVIEW));
                _ids.add(jsonObject1.optString(ID));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MetaData(posterpatharraylist,title,average,releaseDate,overview,_ids);

    }
    private ArrayList<String> supply(ArrayList<String> posterpatharraylist, StringBuilder sb, JSONArray jsonArray, int i) throws JSONException {
        sb.append(http);
        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
        String str = jsonObject1.optString("poster_path");
        sb.append(str);
        posterpatharraylist.add(sb.toString());
        sb.setLength(0);
        return posterpatharraylist;
    }
    private String supplySingle(JSONObject jsonObject,StringBuilder sb){
        sb.append(http);
        String str = jsonObject.optString("poster_path");
        sb.append(str);
        return sb.toString();
    }
    public MetaDataSingle parseJSONSINGLE(String json) {

        String RESULTS = null;
        String TITLE = null;
        String POSTER_PATH = null;
        String VOTE_AVERAGE = null;
        String RELEASE_DATE = null;
        String OVERVIEW = null;
        String ID = null;

        StringBuilder sb = new StringBuilder();
        Log.d(TAG, "parseJSONSINGLE: "+json);
        JSONObject jsonObject = null;

        try {
                jsonObject = new JSONObject(json);

                POSTER_PATH = supplySingle(jsonObject,sb);
                TITLE = jsonObject.optString(this.TITLE);
                VOTE_AVERAGE = jsonObject.optString(this.VOTE_AVERAGE);
                RELEASE_DATE = jsonObject.optString(this.RELEASE_DATE);
                OVERVIEW = jsonObject.optString(this.OVERVIEW);
                ID = jsonObject.optString(this.ID);
                Log.d(TAG, "onReceive: "+TITLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // String TITLE, String POSTER_PATH, String VOTE_AVERAGE, String RELEASE_DATE, String OVERVIEW, String ID
        return new MetaDataSingle(TITLE,POSTER_PATH,VOTE_AVERAGE,RELEASE_DATE,OVERVIEW,ID);
    }
}
