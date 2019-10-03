package com.example.myapplication.data;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FavouriteViewModel extends AndroidViewModel {
    private static final String TAG = "FavouriteViewModel";
    private FavouriteRepository repository;
    private LiveData<List<Favourite>> allNotes;

    public FavouriteViewModel(@NonNull Application application) {
        super(application);
        repository = new FavouriteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public void insert(Favourite note) {
        Log.d(TAG, "insert: "+"HOW MANY TIMES");
        repository.insert(note);
    }

    public void update(Favourite note) {
        repository.update(note);
    }

    public void delete(Favourite note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Favourite>> getAllNotes() {
        return allNotes;
    }
}
