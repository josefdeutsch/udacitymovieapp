package com.example.myapplication.data;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        private PopulateDbAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

          String url = "https://cdn.pixabay.com/photo/2017/11/06/18/39/apple-2924531_960_720.jpg";
          String url2 = "https://cdn.pixabay.com/photo/2017/11/06/18/39/apple-2924531_960_720.jpg";
          String url3 = "https://cdn.pixabay.com/photo/2017/11/06/18/39/apple-2924531_960_720.jpg";

          //noteDao.update(new Note("Title 1", "url", 1));

            noteDao.insert(new Note("Title 1", url, 1));
            noteDao.insert(new Note("Title 2", url2, 2));
            noteDao.insert(new Note("Title 3", url3, 3));

            return null;
        }
    }
}
