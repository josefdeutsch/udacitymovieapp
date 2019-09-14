package com.example.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.data.Flavors;
import com.example.myapplication.data.FlavorsContract;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    private TextView title,releaseDate,average,overview;
    private ImageView imageView;
    private ActionBar actionBar;
    private Button markAsFavorite;
    private String MOVIEID,TITLE;

    Flavors[] flavors = {
            new Flavors("Cupcake", "The first release of Android", R.drawable.cupcake),
            new Flavors("Donut", "The world's information is at your fingertips – " +
                    "search the web, get driving directions... or just watch cat videos.",
                    R.drawable.donut),
            new Flavors("Eclair", "Make your home screen just how you want it. Arrange apps " +
                    "and widgets across multiple screens and in folders. Stunning live wallpapers " +
                    "respond to your touch.", R.drawable.eclair),
            new Flavors("Froyo", "Voice Typing lets you input text, and Voice Actions let " +
                    "you control your phone, just by speaking.", R.drawable.froyo),
            new Flavors("GingerBread", "New sensors make Android great for gaming - so " +
                    "you can touch, tap, tilt, and play away.", R.drawable.gingerbread),
            new Flavors("Honeycomb", "Optimized for tablets, this release opens up new " +
                    "horizons wherever you are.", R.drawable.honeycomb),
            new Flavors("Ice Cream Sandwich", "Android comes of age with a new, refined design. " +
                    "Simple, beautiful and beyond smart.", R.drawable.icecream),
            new Flavors("Jelly Bean", "Android is fast and smooth with buttery graphics. " +
                    "With Google Now, you get just the right information at the right time.",
                    R.drawable.jellybean),
            new Flavors("KitKat", "Smart, simple, and truly yours. A more polished design, " +
                    "improved performance, and new features.", R.drawable.kitkat),
            new Flavors("Lollipop", "A sweet new take on Android. Get the smarts of Android on" +
                    " screens big and small – with the right information at the right moment.",
                    R.drawable.lollipop)
    };

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
        MOVIEID = getTraveler().get_id();
        TITLE = getTraveler().getTITLE();


    }

    private void init_Views() {
        title = findViewById(R.id.title);
        releaseDate = findViewById(R.id.releasedate);
        average = findViewById(R.id.average);
        imageView = findViewById(R.id.image);
        overview = findViewById(R.id.overview);
        markAsFavorite = findViewById(R.id.mark);

    }

    private void setupActionwithRed() {
        actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b20000")));
    }

    public Traveler getTraveler(){
        Intent intent = getIntent();
        return intent.getParcelableExtra("traveler");
    }

    /** Called when the user touches the button */
    public void mark(View view) {

        String[] projection = new String[]{FlavorsContract.FlavorEntry.COLUMN_VERSION_NAME,FlavorsContract.FlavorEntry.COLUMN_DESCRIPTION};

        ContentValues[] flavorValuesArr = new ContentValues[flavors.length];
        // Loop through static array of Flavors, add each to an instance of ContentValues
        // in the array of ContentValues
        for(int i = 0; i < flavors.length; i++){
            flavorValuesArr[i] = new ContentValues();
            flavorValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_ICON, flavors[i].image);
            flavorValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_VERSION_NAME,
                    flavors[i].name);
            flavorValuesArr[i].put(FlavorsContract.FlavorEntry.COLUMN_DESCRIPTION,
                    flavors[i].description);
        }

        // bulkInsert our ContentValues
        getContentResolver().bulkInsert(FlavorsContract.FlavorEntry.CONTENT_URI,
                flavorValuesArr);

        Cursor cursor = getContentResolver().
                query(FlavorsContract.FlavorEntry.CONTENT_URI,projection,null,null,null);

        while (cursor.moveToNext()) {

            int a = cursor.getColumnIndex(FlavorsContract.FlavorEntry.COLUMN_VERSION_NAME);
            int b = cursor.getColumnIndex(FlavorsContract.FlavorEntry.COLUMN_DESCRIPTION);

            Log.d(TAG, "ID: " + cursor.getString(a));
            Log.d(TAG, "TITLE: " + cursor.getString(b));


        }

    }
}
