package com.example.myapplication;


import com.example.myapplication.body.MetaData;

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
    private final static String KEY = "key";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private final String http = "http://image.tmdb.org/t/p/w185";

    public MetaData parseJsonMultiple(String json) {

        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> average = new ArrayList<>();
        ArrayList<String> releaseDate = new ArrayList<>();
        ArrayList<String> overview = new ArrayList<>();
        ArrayList<String> posterpatharraylist = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);

            for (int i = 0; i <= jsonArray.length()-1 ; i++) {
                posterpatharraylist = supply(posterpatharraylist, sb, jsonArray, i);
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                title.add(jsonObject1.optString(TITLE));
                average.add(jsonObject1.optString(VOTE_AVERAGE));
                releaseDate.add(jsonObject1.optString(RELEASE_DATE));
                overview.add(jsonObject1.optString(OVERVIEW));
                ids.add(jsonObject1.optString(ID));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MetaData(posterpatharraylist,title,average,releaseDate,overview,ids);

    }
    private ArrayList<String> supply(ArrayList<String> posterpatharraylist, StringBuilder sb, JSONArray jsonArray, int i) throws JSONException {
        sb.append(http);
        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
        String str = jsonObject1.optString(POSTER_PATH);
        sb.append(str);
        posterpatharraylist.add(sb.toString());
        sb.setLength(0);
        return posterpatharraylist;
    }
    public MetaData parseJsonSingle(String json) {

        String RESULTS = null;
        String TITLE = null;
        String POSTER_PATH = null;
        String VOTE_AVERAGE = null;
        String RELEASE_DATE = null;
        String OVERVIEW = null;
        String ID = null;

        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> average = new ArrayList<>();
        ArrayList<String> releaseDate = new ArrayList<>();
        ArrayList<String> overview = new ArrayList<>();
        ArrayList<String> posterpatharraylist = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        JSONObject jsonObject = null;

        try {
                jsonObject = new JSONObject(json);
                POSTER_PATH = supplySingle(jsonObject,sb);
                posterpatharraylist.add(POSTER_PATH);
                TITLE = jsonObject.optString(JsonUtils.TITLE);
                title.add(TITLE);
                VOTE_AVERAGE = jsonObject.optString(JsonUtils.VOTE_AVERAGE);
                average.add(VOTE_AVERAGE);
                RELEASE_DATE = jsonObject.optString(JsonUtils.RELEASE_DATE);
                releaseDate.add(RELEASE_DATE);
                OVERVIEW = jsonObject.optString(JsonUtils.OVERVIEW);
                overview.add(OVERVIEW);
                ID = jsonObject.optString(JsonUtils.ID);
                ids.add(ID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new MetaData(posterpatharraylist,title,average,releaseDate,overview,ids);
    }
    private String supplySingle(JSONObject jsonObject,StringBuilder sb){
        sb.append(http);
        String str = jsonObject.optString(POSTER_PATH);
        sb.append(str);
        return sb.toString();
    }

    public ArrayList<String> parseJSONKEY(String json) {

        String string = null;
        ArrayList<String> key = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);
            for (int i = 0; i <= jsonArray.length()-1 ; i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                string = jsonObject1.optString(KEY);
                key.add(string);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return key;
    }

    public String parseJSONREVIEW(String json) {
        StringBuilder sb = new StringBuilder();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(RESULTS);

            for (int i = 0; i <= jsonArray.length() - 1; i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String author = jsonObject1.optString(AUTHOR);
                String content = jsonObject1.optString(CONTENT);
                sb.append(author + '\n').append(content + '\n');
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
