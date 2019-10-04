package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.data.Singleton;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements CardViewAdapter.CardViewAdapterOnClickHandler{

    private static final String astring= "https://cdn.pixabay.com/photo/2017/11/06/18/39/apple-2924531_960_720.jpg";
    private static final String TAG = "DetailActivity";
    private RecyclerView mRecyclerView;
    private TextView title,releaseDate,average,overview,review;
    private ImageView imageView;
    private ActionBar actionBar;
    private String MOVIEID,TITLE,PATH,REVIEW,ISAFAVORITE;
    private Button button;
    private ArrayList<String> KEYS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle("DetailActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button = findViewById(R.id.deliver);
        setupActionwithRed();
        init_Views();
        setup_Views(getTraveler());
        setup_Recyler();
        Log.d(TAG, "onCreate: "+"how many times in onCreate--?");
    }

    private void setup_Recyler(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        CardViewAdapter mCardViewAdapter = new CardViewAdapter(this,new ArrayList(KEYS));
        mRecyclerView.setAdapter(mCardViewAdapter);
    }

    private void setup_Views(Messenger traveler) {
        title.setText(traveler.getTitle());
        releaseDate.setText(traveler.getReleaseDate());
        overview.setText(traveler.getOverview());
        average.setText(traveler.getVoteAverage());
        Picasso.get()
                .load(traveler.getPosterpath())
                .resize(500, 500)
                .centerCrop()
                .into(imageView);
        MOVIEID = getTraveler().getId();
        TITLE = getTraveler().getTitle();
        PATH = getTraveler().getPosterpath();
        KEYS = getTraveler().getStringArrayList();
        review.setText(getTraveler().getReview());
        ISAFAVORITE = getTraveler().getIsaFAVORITE();

    }

    private void init_Views() {
        mRecyclerView = findViewById(R.id.recyclerview);
        title = findViewById(R.id.title);
        releaseDate = findViewById(R.id.releasedate);
        average = findViewById(R.id.average);
        imageView = findViewById(R.id.image);
        overview = findViewById(R.id.overview);
        review = findViewById(R.id.review);
    }

    public void deliver(View view){
        Boolean aboolean = Boolean.parseBoolean(ISAFAVORITE);
        if(!aboolean){

            sendMessageToActivity(astring);
        }

    }

    private void setupActionwithRed() {
        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b20000")));
    }

    public Messenger getTraveler(){
        Intent intent = getIntent();
        return intent.getParcelableExtra("messenger");
    }

    private void sendMessageToActivity(String obj) {
        //  Intent intent = new Intent(Config.METADATA);
        //  intent.putExtra(Config.MOVIEID, MOVIEID);
        //  intent.putExtra(Config.PATH, PATH);
        //  LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d(TAG, "sendMessageToActivity:  "+"howmanytimes");

        Singleton singleton = Singleton.getInstance();
        singleton.setMovieid(MOVIEID);
        singleton.setPath(PATH);

    }
    @Override
    public void onResume(){
        super.onResume();

    }
    @Override
    public void onClick(String str) {
        String str1 = KEYS.get(Integer.parseInt(str));
        Intent intent = new Intent(this,YoutubePlayerActivity.class);
        Log.d(TAG, "onClick: "+str1);
        intent.putExtra("url",str1);
        startActivity(intent);
    }
}


