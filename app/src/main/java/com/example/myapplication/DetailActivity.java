package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private TextView title,releaseDate,average,overview;
    private ImageView imageView;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupActionwithRed();
        init_Views();
        setup_Views(getTraveler());

    }

    private void setup_Views(Traveler traveler) {
        title.setText(traveler.getTITLE());
        releaseDate.setText(traveler.getRELEASE_DATE());
        overview.setText(traveler.getOVERVIEW());
        average.setText(traveler.getVOTE_AVERAGE());


        Picasso.get()
                .load(traveler.getPosterpath())
                .resize(500, 500)
                .centerCrop()
                .into(imageView);
    }

    private void init_Views() {
        title = findViewById(R.id.title);
        releaseDate = findViewById(R.id.releasedate);
        average = findViewById(R.id.average);
        imageView = findViewById(R.id.image);
        overview = findViewById(R.id.overview);
    }

    private void setupActionwithRed() {
        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b20000")));
    }

    public Traveler getTraveler(){

        Intent intent = getIntent();
        return intent.getParcelableExtra("traveler");

    }

}
