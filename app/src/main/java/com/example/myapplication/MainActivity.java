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
import com.example.myapplication.data.NoteDatabase;
import com.example.myapplication.data.NoteViewModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.myapplication.data.NoteDatabase.getInstance;

public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteViewAdapaterOnClickHandler {
    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private NoteAdapter mCardViewAdapter, mCardViewAdapter2, mCardViewAdapter3;
     // final ??
    private ArrayList<MetaData> popularlist;
    private ArrayList<MetaData> topratedlist;
    private ArrayList<MetaDataPlaceHolder> favoritelist;

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
    public Integer identifier = Constants.POPULAR; // starting screen default,
    private Traveler traveler;
    private final String DETAIL = "https://api.themoviedb.org/3/movie/474350?api_key=e70a89ec254767811eb928163ee008e4&language=en-US";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActionBarWithRed();
        init_ArrayLists();
        OnSaveInstanceState(savedInstanceState);
        init_Views();
        setup_Views();
        NoteDatabase noteDatabase = getInstance(this);

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void setup_Views() {

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mCardViewAdapter3 = new NoteAdapter(this,this);
        mCardViewAdapter = new NoteAdapter(this,this,popularlist.get(FIRST_ITEM)); // weil ClickHandler anders...
        mCardViewAdapter2 = new NoteAdapter(this,this,topratedlist.get(FIRST_ITEM));

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
   //     String string = Integer.toString(notes.size());
    //    Log.d(TAG,string);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
               // sendDatatoActivity(notes);
                mCardViewAdapter3.setNotes(notes);
            }
         /**   public void sendDatatoActivity(List<Note> notes){
                Intent intent = new Intent("ThreadsafeNotelist");
                ArrayList<Note> notes1 = (ArrayList<Note>)notes;
                intent.putParcelableArrayListExtra("list", notes1);
                //String string = notes.get(0).getDescription();
                //intent.putExtra("list", string);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }**/
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(
                msgReceiver, new IntentFilter(Constants.METADATA));
        LocalBroadcastManager.getInstance(this).registerReceiver(
               threadReceiver, new IntentFilter(Constants.QUERYMOVIEID));

     //    Note note = new Note("atitle", anotherString, 2);

        //noteViewModel.deleteAllNotes();
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
        traveler = travels(str);
        intent.putExtra("traveler",traveler);
        super.startActivity(intent);
    }

    public Traveler travels(String str){

        switch(identifier) {
            case Constants.POPULAR:
                Log.d(TAG,"POPULAR");
                return loadedtraveler(popularlist,str);
            case Constants.TOP_RATED:
                Log.d(TAG,"TOP_RATED");
                return loadedtraveler(topratedlist,str);
            case Constants.FAVORITE:
                Log.d(TAG,"FAVORITE");
                return loadedtravelers(favoritelist,str);
            default:
                // code block
        }
        return null;
    }
    public Traveler loadedtraveler(ArrayList<MetaData> arrayList, String str){
        String title = arrayList.get(FIRST_ITEM).getTITLE().get(Integer.parseInt(str)).toString();
        String vote_average = arrayList.get(FIRST_ITEM).getVOTE_AVERAGE().get(Integer.parseInt(str)).toString();
        String release_date = arrayList.get(FIRST_ITEM).getRELEASE_DATE().get(Integer.parseInt(str)).toString();
        String overview = arrayList.get(FIRST_ITEM).getOVERVIEW().get(Integer.parseInt(str)).toString();
        String poster = arrayList.get(FIRST_ITEM).getPosterPath().get(Integer.parseInt(str));
        String id = arrayList.get(FIRST_ITEM).get_id().get(Integer.parseInt(str));
        MetaDataKeyHolder metaDataKeyHolder = getKeys(id);
        ArrayList<String> keys = metaDataKeyHolder.getKeys();
        String string = Integer.toString(keys.size());
        Log.d(TAG, "loadedtraveler: "+string);
        // Traveler muss mit id query holt ein Objekt das weitergegeben wird.

        return new Traveler(title,vote_average,release_date,overview,poster,id,keys);
    }
    public Traveler loadedtravelers(ArrayList<MetaDataPlaceHolder> arrayList, String str){
        String title = arrayList.get(Integer.parseInt(str)).getTITLE();
        String vote_average = arrayList.get(Integer.parseInt(str)).getVOTE_AVERAGE();
        String release_date = arrayList.get(Integer.parseInt(str)).getRELEASE_DATE();
        String overview = arrayList.get(Integer.parseInt(str)).getOVERVIEW();
        String poster = arrayList.get(Integer.parseInt(str)).getPOSTER_PATH();
        String id = arrayList.get(Integer.parseInt(str)).getID();
        MetaDataKeyHolder metaDataKeyHolder = getKeys(id);
        ArrayList<String> keys = metaDataKeyHolder.getKeys();
        String string = Integer.toString(keys.size());
        Log.d(TAG, "loadedtravelers: "+string);
        return new Traveler(title,vote_average,release_date,overview,poster,id,keys);

    }
    private MetaDataKeyHolder getKeys(String id){
       String segment="https://api.themoviedb.org/3/movie/";
       String segment2 ="/videos?api_key=e70a89ec254767811eb928163ee008e4&language=en-US";
       String url = segment+id+segment2;
       Log.d(TAG, "getKeys: "+url);
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

            identifier = Constants.POPULAR;
            listViewDecider = true;
            mRecyclerView.swapAdapter(mCardViewAdapter,true);
            return true;
        }
        if (id == R.id.action_refresh2) {

            identifier = Constants.TOP_RATED;
            listViewDecider = false;
            mRecyclerView.swapAdapter(mCardViewAdapter2,true);
            return true;
        }
        if (id == R.id.action_refresh3) {

            identifier = Constants.FAVORITE;
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

    private BroadcastReceiver msgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String movieid  = intent.getStringExtra(Constants.MOVIEID);
            Log.d(TAG, "onReceive: "+movieid);
            String path  = intent.getStringExtra(Constants.PATH);
            Note note = new Note(movieid, path, 0);
            noteViewModel.insert(note);
        }
    };
    private BroadcastReceiver threadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String movieid  = intent.getStringExtra(Constants.MOVIEID);
            String segment ="https://api.themoviedb.org/3/movie/";
            String segment2 ="?api_key=e70a89ec254767811eb928163ee008e4&language=en-US";
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
            //favoritelist.add(movieid);
            Log.d(TAG, "onReceive: "+movieid);
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

}
