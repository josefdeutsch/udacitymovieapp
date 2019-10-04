package com.example.myapplication;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.Favourite;
import com.example.myapplication.data.FavouriteAdapter;
import com.example.myapplication.data.FavouriteViewModel;
import com.example.myapplication.data.Singleton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


import static com.example.myapplication.Config.MDBAPIKEY;


public class MainActivity extends AppCompatActivity implements FavouriteAdapter.NoteViewAdapaterOnClickHandler {

    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private FavouriteAdapter mCardViewAdapter, mCardViewAdapter2, mCardViewAdapter3;
    private FavouriteViewModel noteViewModel;
    private ArrayList<MetaData> popularlist;
    private ArrayList<MetaData> topratedlist;
    private ArrayList<MetaDataPlaceHolder> favoritelist;

    private final String POPULAR = "https://api.themoviedb.org/3/movie/popular?"+MDBAPIKEY+"&language=en-US&page=1";
    private final String TOPRATED = "https://api.themoviedb.org/3/movie/top_rated?"+MDBAPIKEY+"&language=en-US&page=1";
    private final int FIRST_ITEM = 0;
    private Boolean listViewDecider = true;
    private JsonUtils jsonUtils = new JsonUtils();
    private ActionBar actionBar;
    public Integer identifier = Config.POPULAR;
    private Messenger messenger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActionBarWithRed();
        init_ArrayLists();
        OnSaveInstanceState(savedInstanceState);
        init_Views();
        setup_Views();
    }
    @Override
    public void onResume(){
        super.onResume();

        String movieid =  Singleton.getInstance().getMovieid();
        String path =  Singleton.getInstance().getPath();
        if(movieid!=null&&path!=null){
            Favourite note = new Favourite(Integer.parseInt(movieid), path, 0);
            noteViewModel.insert(note);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    private void setup_Views() {

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mCardViewAdapter3 = new FavouriteAdapter(this,this);
        mCardViewAdapter = new FavouriteAdapter(this,this,popularlist.get(FIRST_ITEM)); // weil ClickHandler anders...
        mCardViewAdapter2 = new FavouriteAdapter(this,this,topratedlist.get(FIRST_ITEM));

        noteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Favourite>>() {
            @Override
            public void onChanged(@Nullable List<Favourite> notes) {
                mCardViewAdapter3.setNotes(notes);
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(
                msgReceiver, new IntentFilter(Config.METADATA));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver, new IntentFilter(Config.QUERYMOVIEID));
         mRecyclerView.setAdapter(mCardViewAdapter);

         //noteViewModel.deleteAllNotes();

    }

    private void init_Views() {
        mRecyclerView = findViewById(R.id.recyclerview);
    }

    private void setupActionBarWithRed() {
        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b20000")));
    }

    private void OnSaveInstanceState(Bundle savedInstanceState) {

        if(savedInstanceState==null||!savedInstanceState.containsKey("popular")||!savedInstanceState.containsKey("toprated")){
            this.popularlist.add(getMetaData(POPULAR));
            this.topratedlist.add(getMetaData(TOPRATED));
        }else{
            this.popularlist= savedInstanceState.getParcelableArrayList("popular");
            this.topratedlist= savedInstanceState.getParcelableArrayList("toprated");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList("popular",popularlist);
        super.onSaveInstanceState(outState);

    }

    private void init_ArrayLists() {
        popularlist = new ArrayList<>();
        topratedlist = new ArrayList<>();
        favoritelist = new ArrayList<>();
    }

    private MetaData getMetaData(String str) {
        try {
            return new Download(str).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(String str) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("pos",str);
        messenger = messenger(str);
        intent.putExtra("messenger", messenger);
        super.startActivity(intent);
    }

    public Messenger messenger(String str){

        switch(identifier) {
            case Config.POPULAR:
                Log.d(TAG,"POPULAR");
                return loadmessenger(popularlist,str);
            case Config.TOP_RATED:
                Log.d(TAG,"TOP_RATED");
                return loadmessenger(topratedlist,str);
            case Config.FAVORITE:
                Log.d(TAG,"FAVORITE");
                return loadmessengers(favoritelist,str);
            default:
                // code block
        }
        return null;
    }

    public Messenger loadmessenger(ArrayList<MetaData> arrayList, String str){
        String title = arrayList.get(FIRST_ITEM).getTitle().get(Integer.parseInt(str)).toString();
        String vote_average = arrayList.get(FIRST_ITEM).getVoteAverage().get(Integer.parseInt(str)).toString();
        String release_date = arrayList.get(FIRST_ITEM).getReleaseDate().get(Integer.parseInt(str)).toString();
        String overview = arrayList.get(FIRST_ITEM).getOverview().get(Integer.parseInt(str)).toString();
        String poster = arrayList.get(FIRST_ITEM).getPosterPath().get(Integer.parseInt(str));
        String id = arrayList.get(FIRST_ITEM).getId().get(Integer.parseInt(str));
        MetaDataKeyHolder metaDataKeyHolder = getKeys(id);
        ArrayList<String> keys = metaDataKeyHolder.getKeys();
        String rev = getReview(id);

        return new Messenger(title,vote_average,release_date,overview,poster,id,keys,rev,"false");
    }

    public Messenger loadmessengers(ArrayList<MetaDataPlaceHolder> arrayList, String str){
        String title = arrayList.get(Integer.parseInt(str)).getTitle();
        String vote_average = arrayList.get(Integer.parseInt(str)).getVoteAverage();
        String release_date = arrayList.get(Integer.parseInt(str)).getReleaseDate();
        String overview = arrayList.get(Integer.parseInt(str)).getOverview();
        String poster = arrayList.get(Integer.parseInt(str)).getPosterPath();
        String id = arrayList.get(Integer.parseInt(str)).getId();
        MetaDataKeyHolder metaDataKeyHolder = getKeys(id);
        ArrayList<String> keys = metaDataKeyHolder.getKeys();
        String rev = getReview(id);
        return new Messenger(title,vote_average,release_date,overview,poster,id,keys,rev,"true");

    }

    private String getReview(String id){

        String segment = "https://api.themoviedb.org/3/movie/";
        String segment2 = "/reviews?"+MDBAPIKEY+"&language=en-US&page=1";
        String url = segment+id+segment2;
        DownloadReview downloadReview = new DownloadReview(url);
        String string = null;
        try {
          string =  downloadReview.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return string;
    }

    private MetaDataKeyHolder getKeys(String id){
       String segment="https://api.themoviedb.org/3/movie/";
       String segment2 ="/videos?"+MDBAPIKEY+"&language=en-US";
       String url = segment+id+segment2;
       DownloadKeys downloadKeys = new DownloadKeys(url);
       MetaDataKeyHolder metaDataKeyHolder = null;
        try {
           metaDataKeyHolder = downloadKeys.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return metaDataKeyHolder;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {

            identifier = Config.POPULAR;
            listViewDecider = true;
            mRecyclerView.swapAdapter(mCardViewAdapter,true);
            return true;
        }
        if (id == R.id.action_refresh2) {

            identifier = Config.TOP_RATED;
            listViewDecider = false;
            mRecyclerView.swapAdapter(mCardViewAdapter2,true);
            return true;
        }
        if (id == R.id.action_refresh3) {

            identifier = Config.FAVORITE;
            mRecyclerView.swapAdapter(mCardViewAdapter3,true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public BroadcastReceiver msgReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(TAG, "onReceive: "+ "HOW MANY TIMES");
            String movieid  = intent.getStringExtra(Config.MOVIEID);
            Log.d(TAG, "onReceive: "+movieid);
            String path  = intent.getStringExtra(Config.PATH);
            Favourite note = new Favourite(Integer.parseInt(movieid), path, 0);
            noteViewModel.insert(note);
        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String movieid  = intent.getStringExtra(Config.MOVIEID);
            String segment ="https://api.themoviedb.org/3/movie/";
            String segment2 ="?"+MDBAPIKEY+"&language=en-US";
            String url = segment.concat(movieid).concat(segment2);

            DownloadDetails downloadDetails = new DownloadDetails(url);
            MetaDataPlaceHolder metaDataSingle = null;
            try {
                 metaDataSingle = downloadDetails.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            favoritelist.add(metaDataSingle);
        }
    };


     class DownloadDetails extends AsyncTask<Void, Void, MetaDataPlaceHolder> {
        String str;
        public DownloadDetails(String str){
            this.str=str;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected MetaDataPlaceHolder doInBackground(Void... voids) {
            MetaDataPlaceHolder metaData = null;
            try{
                URL oracle = new URL(str);
                URLConnection yc = oracle.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        yc.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    metaData = jsonUtils.parseJSONSINGLE(inputLine);
                in.close();

            }catch (IOException e){

            }
            return metaData;
        }

        @Override
        protected void onPostExecute(MetaDataPlaceHolder status){

            super.onPostExecute(status);

        }
    }

     class DownloadKeys extends AsyncTask<Void, Void, MetaDataKeyHolder> {
        String str;
        public DownloadKeys(String str){
            this.str=str;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected MetaDataKeyHolder doInBackground(Void... voids) {
            MetaDataKeyHolder metaData = null;
            try{
                URL oracle = new URL(str);
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
        protected void onPostExecute(MetaDataKeyHolder status){

            super.onPostExecute(status);

        }
    }

     class Download extends AsyncTask<Void, Void, MetaData> {
        String str;
        public Download(String str){
            this.str=str;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected MetaData doInBackground(Void... voids) {
            MetaData metaData = null;
            try{
                URL oracle = new URL(str);
                URLConnection yc = oracle.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        yc.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    metaData = jsonUtils.parseJSON(inputLine);
                in.close();

            }catch (IOException e){
            }
            return metaData;
        }

        @Override
        protected void onPostExecute(MetaData status){
            super.onPostExecute(status);
        }
    }

     class DownloadReview extends AsyncTask<Void, Void, String> {

        String str;
        public DownloadReview(String str){
            this.str=str;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String string = null;
            try{
                URL oracle = new URL(str);
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
        protected void onPostExecute(String status){
            super.onPostExecute(status);
        }
    }

}
