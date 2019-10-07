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

public class DownloadReview extends AsyncTask<Void, Void, String> {

    private OnEventListener<String> mCallBack;
    private Context mContext;
    public Exception mException;
    private JsonUtils jsonUtils;
    private String url;
    private final String multipleDownload = "DownloadReview";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public DownloadReview() {
    }

    public DownloadReview(Context context, JsonUtils utils,OnEventListener callback) {
        mCallBack = callback;
        mContext = context;
        jsonUtils = utils;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String string = null;
        try{
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                string = jsonUtils.parseJSONREVIEW(inputLine);
            in.close();

        }catch (IOException e){
        }
        return string;
    }

    @Override
    protected void onPostExecute(String result) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(multipleDownload);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }
}