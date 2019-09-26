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
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.Note;
import com.example.myapplication.data.NoteAdapter;
import com.example.myapplication.data.NoteViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteViewAdapaterOnClickHandler {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private NoteAdapter mCardViewAdapter, mCardViewAdapter2, mCardViewAdapter3;
     // final ??
    private ArrayList<MetaData> popularlist;
    private ArrayList<MetaData> topratedlist;
    private final String POPULAR = "https://api.themoviedb.org/3/movie/popular?api_key=e70a89ec254767811eb928163ee008e4&language=en-US&page=1";
    private final String TOPRATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=e70a89ec254767811eb928163ee008e4&language=en-US&page=1";
    private  final int FIRST_ITEM = 0;
    private NoteViewModel noteViewModel;
    // set permission to Popular if true,
    // set permission to TopRated if false
    private Boolean listViewDecider = true;
    private static final String anotherString= "https://cdn.pixabay.com/photo/2017/11/06/18/39/apple-2924531_960_720.jpg";
    private JsonUtils jsonUtils = new JsonUtils();
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActionBarWithRed();
        init_ArrayLists();
        OnSaveInstanceState(savedInstanceState);
        init_Views();
        setup_Views();
        queryDataBase();
    }

    @Override
    public void onResume(){
        super.onResume();

    }
    private void setup_Views() {

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mCardViewAdapter3 = new NoteAdapter();
        mCardViewAdapter = new NoteAdapter(this,popularlist.get(FIRST_ITEM)); // weil ClickHandler anders...
        mCardViewAdapter2 = new NoteAdapter(this,topratedlist.get(FIRST_ITEM));

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {

            @Override
            public void onChanged(@Nullable List<Note> notes) {
                mCardViewAdapter3.setNotes(notes);
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(
                msgReceiver, new IntentFilter("MetaData"));
        Note note = new Note("atitle", anotherString, 2);
     //   noteViewModel.deleteAllNotes();
        noteViewModel.insert(note);
        noteViewModel.insert(note);
        noteViewModel.insert(note);
        noteViewModel.insert(note);
       // mRecyclerView.setAdapter(adapter);
        mRecyclerView.setAdapter(mCardViewAdapter);

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
        Traveler traveler = travels(str);
        intent.putExtra("traveler",traveler);
        super.startActivity(intent);
    }

    public Traveler travels(String str){

        if(listViewDecider){
            String title = popularlist.get(FIRST_ITEM).getTITLE().get(Integer.parseInt(str)).toString();
            String vote_average = popularlist.get(FIRST_ITEM).getVOTE_AVERAGE().get(Integer.parseInt(str)).toString();
            String release_date = popularlist.get(FIRST_ITEM).getRELEASE_DATE().get(Integer.parseInt(str)).toString();
            String overview = popularlist.get(FIRST_ITEM).getOVERVIEW().get(Integer.parseInt(str)).toString();
            String poster = popularlist.get(FIRST_ITEM).getPosterPath().get(Integer.parseInt(str));
            String id = popularlist.get(FIRST_ITEM).get_id().get(Integer.parseInt(str));
            return new Traveler(title,vote_average,release_date,overview,poster,id);
        }else if(!listViewDecider){
            String title = topratedlist.get(FIRST_ITEM).getTITLE().get(Integer.parseInt(str)).toString();
            String vote_average = topratedlist.get(FIRST_ITEM).getVOTE_AVERAGE().get(Integer.parseInt(str)).toString();
            String release_date = topratedlist.get(FIRST_ITEM).getRELEASE_DATE().get(Integer.parseInt(str)).toString();
            String overview = topratedlist.get(FIRST_ITEM).getOVERVIEW().get(Integer.parseInt(str)).toString();
            String poster = topratedlist.get(FIRST_ITEM).getPosterPath().get(Integer.parseInt(str));
            String id = popularlist.get(FIRST_ITEM).get_id().get(Integer.parseInt(str));
            return new Traveler(title,vote_average,release_date,overview,poster,id);
        }else if(listViewDecider=null){

        }
        return null;
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
            //listViewDecider = true;
            mRecyclerView.swapAdapter(mCardViewAdapter,true);
            return true;
        }
        if (id == R.id.action_refresh2) {
          //  listViewDecider = false;
            mRecyclerView.swapAdapter(mCardViewAdapter2,true);
            return true;
        }
        if (id == R.id.action_refresh3) {
        //  listViewDecider = null;
            mRecyclerView.swapAdapter(mCardViewAdapter3,true);
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void queryDataBase(){}

    private BroadcastReceiver msgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String string  = intent.getStringExtra("MetaData");
            Note note = new Note("atitle", string, 10);
            //noteViewModel.insert(note);
           // noteViewModel.deleteAllNotes();
            Log.d(TAG, "MetaData"+" "+string);
        }
    };
    public View.OnClickListener navigateTo(final Class<?> clazz) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, clazz);
                startActivity(intent);
            }
        };
    }

}
