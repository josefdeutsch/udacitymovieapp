package com.example.myapplication.download;

import android.content.Context;
import android.os.AsyncTask;

import com.example.myapplication.JsonUtils;
import com.example.myapplication.OnEventListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DownloadVideoKeys extends AsyncTask<Void, Void, ArrayList<String>> {
    private OnEventListener<String> mCallBack;
    private Context mContext;
    public Exception mException;
    private JsonUtils jsonUtils;
    private String url;
    private final String multipleDownload = "DownloadKeys";

    public void setUrl(String url) {
        this.url = url;
    }

    public DownloadVideoKeys(Context context, JsonUtils utils,OnEventListener callback) {
        mCallBack = callback;
        mContext = context;
        jsonUtils = utils;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> metaData = null;
        try{
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
               metaData = jsonUtils.parseJSONKEY(inputLine);
            in.close();
        }catch (IOException e){

        }
        return metaData;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(multipleDownload);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }
}