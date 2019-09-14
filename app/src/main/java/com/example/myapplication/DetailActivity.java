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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    private TextView title,releaseDate,average,overview;
    private ImageView imageView;
    private ActionBar actionBar;
    private Button markAsFavorite;
    private String MOVIEID,TITLE;

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        permission();
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
}


