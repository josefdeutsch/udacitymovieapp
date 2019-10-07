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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements DetailActivityPlayButtonAdapter.DetailActivityPlayButtonAdapterOnClickHandler{

    private static final String TAG = "DetailActivity";
    private RecyclerView mRecyclerView;
    private TextView title,releaseDate,average,overview,review;
    private ImageView imageView;
    private ActionBar actionBar;
    private String movieid, path;
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
        setup_Views(getMessage());
        setup_Recyler();
        Log.d(TAG, "onCreate: "+"how many times in onCreate--?");
    }

    private void setup_Recyler(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        DetailActivityPlayButtonAdapter mDetailActivityPlayButtonAdapter = new DetailActivityPlayButtonAdapter(this,new ArrayList(KEYS));
        mRecyclerView.setAdapter(mDetailActivityPlayButtonAdapter);
    }

    private void setup_Views(Messenger traveler) {
        title.setText(traveler.getTitle());
        releaseDate.setText(traveler.getReleaseDate());
        overview.setText(traveler.getOverview());
        average.setText(traveler.getVoteAverage());
        movieid = getMessage().getId();
        path = getMessage().getPosterpath();
        KEYS = getMessage().getStringArrayList();
        review.setText(getMessage().getReview());
        Picasso.get()
                .load(traveler.getPosterpath())
                .resize(500, 500)
                .centerCrop()
                .into(imageView);
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
        Intent intent = new Intent(Config.METADATA);
        intent.putExtra(Config.MOVIEID, movieid);
        intent.putExtra(Config.PATH, path);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void setupActionwithRed() {
        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b20000")));
    }

    public Messenger getMessage(){
        Intent intent = getIntent();
        return intent.getParcelableExtra("messenger");
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


