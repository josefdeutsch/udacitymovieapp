package com.example.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements CardViewAdapter.CardViewAdapterOnClickHandler{

    private static final String astring= "https://cdn.pixabay.com/photo/2017/11/06/18/39/apple-2924531_960_720.jpg";
    private static final String TAG = "DetailActivity";
    private CardViewAdapter cardViewAdapter;
    private RecyclerView mRecyclerView;
    private TextView title,releaseDate,average,overview,review;
    private ImageView imageView;
    private ActionBar actionBar;
    private Button markAsFavorite;
    private String MOVIEID,TITLE,PATH,REVIEW;
    private Button button;
    private ArrayList<String> KEYS;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle("DetailActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button = findViewById(R.id.deliver);
        permission();
        setupActionwithRed();
        init_Views();
        setup_Views(getTraveler());
        setup_Recyler();
        String string = Integer.toString(KEYS.size());
    }

    private void setup_Recyler(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        CardViewAdapter mCardViewAdapter = new CardViewAdapter(this,new ArrayList(KEYS));
        mRecyclerView.setAdapter(mCardViewAdapter);
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
        PATH = getTraveler().getPosterpath();
        KEYS = getTraveler().getStringArrayList();
        review.setText(getTraveler().getREVIEW());

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
        sendMessageToActivity(astring);
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

        ContentResolver resolver = getContentResolver();
        String[] projection = new String[]{MediaStore.Video.Media.DATA};
        ContentValues[] contentarray = new ContentValues[10];

        for (int i = 0; i <= contentarray.length-1 ; i++) {
            contentarray[i] = new ContentValues();
            contentarray[i].put(MediaStore.Video.Media.DATA,"/http://"+new Integer(i));
        }


        resolver.bulkInsert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentarray);// Call the query method on the resolver with the correct Uri from the contract class

        Cursor cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);

        while (cursor.moveToNext()) {
            int c = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
            Log.d(TAG, "DATA: " + cursor.getString(c));
        }

        Log.d(TAG, MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString());
        Log.d(TAG, MediaStore.Video.Media.DATA.toString());

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }

        }
    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission im Manifest nicht granted.");
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d(TAG, "shouldSHowRequestPermissionRationale...");
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                Log.d(TAG, "requestPermission");
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    private void sendMessageToActivity(String obj) {
        Intent intent = new Intent(Constants.METADATA);
        intent.putExtra(Constants.MOVIEID, MOVIEID);
        intent.putExtra(Constants.PATH, PATH);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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


