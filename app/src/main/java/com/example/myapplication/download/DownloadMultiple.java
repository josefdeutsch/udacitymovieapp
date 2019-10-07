package com.example.myapplication.download;

import android.content.Context;
import android.os.AsyncTask;

import com.example.myapplication.JsonUtils;
import com.example.myapplication.body.MetaData;
import com.example.myapplication.OnEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DownloadMultiple extends AsyncTask<Void, Void, MetaData> {

    private OnEventListener<String> mCallBack;
    private Context mContext;
    public Exception mException;
    private JsonUtils jsonUtils;
    private String url;
    private final String multipleDownload = "MultipleDownload";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public DownloadMultiple() {
    }

    public DownloadMultiple(Context context, JsonUtils utils, OnEventListener callback) {
        mCallBack = callback;
        mContext = context;
        jsonUtils = utils;
    }

    @Override
    protected MetaData doInBackground(Void... params) {
        try {
            MetaData metaData = null;
            try{
                URL oracle = new URL(url);
                URLConnection yc = oracle.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        yc.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    metaData = jsonUtils.parseJsonMultiple(inputLine);
                in.close();
            }catch (IOException e){

            }
            return metaData;
        } catch (Exception e) {
            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(MetaData result) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(multipleDownload);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }
}
