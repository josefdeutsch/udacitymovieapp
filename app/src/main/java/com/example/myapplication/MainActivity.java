package com.example.myapplication;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.body.MetaData;
import com.example.myapplication.data.Favourite;
import com.example.myapplication.data.FavouriteAdapter;
import com.example.myapplication.data.FavouriteViewModel;
import com.example.myapplication.download.DownloadDetails;
import com.example.myapplication.download.DownloadMultiple;
import com.example.myapplication.download.DownloadReview;
import com.example.myapplication.download.DownloadVideoKeys;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements FavouriteAdapter.FavouriteViewAdapaterOnClickHandler {

    private static final String TAG = "MainActivity";
    public Integer identifier = Config.POPULAR;
    private RecyclerView mRecyclerView;
    private FavouriteAdapter popular, toprated, favorite;
    private FavouriteViewModel favouriteViewModel;
    private ArrayList<MetaData> popularlist;
    private ArrayList<MetaData> topratedlist;
    private ArrayList<MetaData> favoritelist;
    /**index 0 firstPage of themoviedb**/
    private final int FIRST_ITEM = 0;
    private ActionBar actionBar;

    private Messenger messenger;
    private JsonUtils jsonUtils = new JsonUtils();

    /** download the whole JSON String Popular per List. www.themoviedb.com **/
    private DownloadMultiple downloadPopularList
            = new DownloadMultiple(this, jsonUtils, new OnEventListener<String>() {
        @Override
        public void onSuccess(String object) {

        }

        @Override
        public void onFailure(Exception e) {
            Toast.makeText(getApplicationContext(), "failed, download Popularlist ",Toast.LENGTH_LONG).show();
        }
    });
    /** download the whole JSON String Popular per List. www.themoviedb.com **/
    private DownloadMultiple downloadTopratedList
            = new DownloadMultiple(this,jsonUtils, new OnEventListener<String>() {
        @Override
        public void onSuccess(String object) {

        }

        @Override
        public void onFailure(Exception e) {
            Toast.makeText(getApplicationContext(), "failed, download Topratedlist ",Toast.LENGTH_LONG).show();
        }
    });
    /** download VideoKeys dynamic per request(movieid) **/
    private DownloadVideoKeys downloadVideoKeys
            = new DownloadVideoKeys(this, jsonUtils, new OnEventListener<String>() {
        @Override
        public void onSuccess(String object) {

        }

        @Override
        public void onFailure(Exception e) {
            Toast.makeText(getApplicationContext(), "failed, download VideoKeys ",Toast.LENGTH_LONG).show();
        }
    });
    /** download Review dynamic per request(movieid) **/
    private DownloadReview downloadReview
            = new DownloadReview(this,jsonUtils, new OnEventListener<String>() {
        @Override
        public void onSuccess(String object) {

        }

        @Override
        public void onFailure(Exception e) {
            Toast.makeText(getApplicationContext(), "failed, download Review ",Toast.LENGTH_LONG).show();
        }
    });

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
        Log.d(TAG, "onResume: ");


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
        /** 3 different RecylcerViewAdapters of same Type to be swapped per menu item **/

        favorite = new FavouriteAdapter(this,this);
        popular = new FavouriteAdapter(this,this,popularlist.get(FIRST_ITEM));
        toprated = new FavouriteAdapter(this,this,topratedlist.get(FIRST_ITEM));
        /** Connect LiveData Observer with RecyclerViewAdapter **/
        /** the goal is to have access to in OnBindFunction, retrieve index per movieid via
         * FavouriteViewAdapaterOnClickHandler Interface **/

        favouriteViewModel = ViewModelProviders.of(this).get(FavouriteViewModel.class);
        favouriteViewModel.getAllNotes().observe(this, new Observer<List<Favourite>>() {
            @Override
            public void onChanged(@Nullable List<Favourite> notes) {
                favorite.setNotes(notes);
            }
        });
        /** Managers to connect Adapter with Activity **/
        /** Managers to connect Activity with Activity **/

        LocalBroadcastManager.getInstance(this).registerReceiver(
                msgReceiver, new IntentFilter(Config.METADATA));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver, new IntentFilter(Config.QUERYMOVIEID));
        mRecyclerView.setAdapter(favorite);
        //favouriteViewModel.deleteAllNotes();
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
            supply_popularList(downloadPopularList, Config.POPULARURL, this.popularlist);
            supply_topratedList(downloadTopratedList, Config.TOPRATEDURL, this.topratedlist);

        }else{
            this.popularlist= savedInstanceState.getParcelableArrayList("popular");
            this.topratedlist= savedInstanceState.getParcelableArrayList("toprated");
        }
    }

    private void supply_topratedList(DownloadMultiple downloadTopratedList, String topratedurl, ArrayList<MetaData> topratedlist) {
        try {
            downloadTopratedList.setUrl(topratedurl);
            topratedlist.add(downloadTopratedList.execute().get());

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void supply_popularList(DownloadMultiple downloadPopularList, String popularurl, ArrayList<MetaData> popularlist) {
        try {
            downloadPopularList.setUrl(popularurl);
            popularlist.add(downloadPopularList.execute().get());

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void supply_favoriteList(DownloadDetails downloadDetails) {
        try {
            favoritelist.add(downloadDetails.execute().get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    /** get  index "str" retrieve Recylcer View, put data to it **/
    @Override
    public void onClick(String str) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("pos",str);
        messenger = messenger(str);
        intent.putExtra("messenger", messenger);
        super.startActivity(intent);
    }

    /** decide between a static or dynamic modus**/
    /** static  : json loaded into compiletime@OnSaveInstanceState(savedInstanceState); onCreate**/
    /** dynamic : json loaded into runtime** @BroadcastReceiver broadcastReceiver **/

    public Messenger messenger(String str){
        switch(identifier) {
            case Config.POPULAR:
                Log.d(TAG,"POPULAR");
                return loadStaticDataIntoDetailActivty(popularlist,str);
            case Config.TOP_RATED:
                Log.d(TAG,"TOP_RATED");
                return loadStaticDataIntoDetailActivty(topratedlist,str);
            case Config.FAVORITE:
                Log.d(TAG,"FAVORITE");
                return loadDynamicDataIntoDetailActivity(favoritelist,str);
            default:
                // code block
        }
        return null;
    }

    public Messenger loadStaticDataIntoDetailActivty(ArrayList<MetaData> arrayList, String str){
        Integer index = Integer.parseInt(str);
        return getMessenger(arrayList, index);
    }
    public Messenger loadDynamicDataIntoDetailActivity(ArrayList<MetaData> arrayList, String str){
        Integer index = Integer.parseInt(str);
        return getMessenger(arrayList, index, FIRST_ITEM);
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
            favoritelist.clear();
            identifier = Config.POPULAR;
            mRecyclerView.swapAdapter(popular,true);
            return true;
        }
        if (id == R.id.action_refresh2) {
            favoritelist.clear();
            identifier = Config.TOP_RATED;
            mRecyclerView.swapAdapter(toprated,true);
            return true;
        }
        if (id == R.id.action_refresh3) {
            favoritelist.clear();
            identifier = Config.FAVORITE;
            mRecyclerView.swapAdapter(favorite,true);
            favoritelist.clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**insert a new note to dB, to be seen in AdapterView**/
    public BroadcastReceiver msgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String movieid  = intent.getStringExtra(Config.MOVIEID);
            String path  = intent.getStringExtra(Config.PATH);
            Favourite note = new Favourite(Integer.parseInt(movieid), path, 0);
            favouriteViewModel.insert(note);
        }
    };

    /**query OnBindfunction in RecyclerViewAdapter, download Details dynamic**/
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String movieid  = intent.getStringExtra(Config.MOVIEID);
            String url = Config.concatinatewithmovieid(movieid);
            DownloadDetails downloadDetails
                    = new DownloadDetails(getApplicationContext(), jsonUtils, new OnEventListener<String>() {
                @Override
                public void onSuccess(String object) {

                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(getApplicationContext(), "failed, download MovieDetails ",Toast.LENGTH_LONG).show();
                }
            });
            downloadDetails.setUrl(url);
            supply_favoriteList(downloadDetails);
            Log.d(TAG, "onReceive: "+favoritelist.size());
        }
    };
    private ArrayList<String> getVideoKeyList(String id) {
        downloadVideoKeys.setUrl(Config.concatenatewithKeys(id));
        ArrayList<String> keys = null;
        try {
            keys = downloadVideoKeys.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return keys;
    }
    private String getReviews(String id) {
        String string = null;
        downloadReview.setUrl(Config.concatinatewithReview(id));
        try {
            string = downloadReview.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return string;
    }
    @NotNull
    private Messenger getMessenger(ArrayList<MetaData> arrayList, Integer index) {
        String title = arrayList.get(FIRST_ITEM).getTitle().get(index).toString();
        String vote_average = arrayList.get(FIRST_ITEM).getVoteAverage().get(index).toString();
        String release_date = arrayList.get(FIRST_ITEM).getReleaseDate().get(index).toString();
        String overview = arrayList.get(FIRST_ITEM).getOverview().get(index).toString();
        String poster = arrayList.get(FIRST_ITEM).getPosterPath().get(index);
        String id = arrayList.get(FIRST_ITEM).getId().get(index);
        ArrayList<String> keys = getVideoKeyList(id);
        String reviews = getReviews(id);
        return new Messenger(title,vote_average,release_date,overview,poster,id,keys,reviews);
    }

    @NotNull
    private Messenger getMessenger(ArrayList<MetaData> arrayList, Integer index, int first_item) {
        String title = arrayList.get(index).getTitle().get(first_item).toString();
        String vote_average = arrayList.get(index).getVoteAverage().get(first_item).toString();
        String release_date = arrayList.get(index).getReleaseDate().get(first_item).toString();
        String overview = arrayList.get(index).getOverview().get(first_item).toString();
        String poster = arrayList.get(index).getPosterPath().get(first_item);
        String id = arrayList.get(index).getId().get(first_item);
        ArrayList<String> keys = getVideoKeyList(id);
        String reviews = getReviews(id);
        return new Messenger(title, vote_average, release_date, overview, poster, id, keys, reviews);
    }
}
