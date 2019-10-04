package com.example.myapplication.data;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Config;
import com.example.myapplication.MetaData;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.NoteHolder> {
    private List<Favourite> notes = new ArrayList<>();
    private ArrayList arrayList;
    private static final String TAG = "FavouriteAdapter";
    private Context context;
    private NoteViewAdapaterOnClickHandler mClickHandler;

    public interface NoteViewAdapaterOnClickHandler {
        void onClick(String string);
    }
    public FavouriteAdapter(Context context, NoteViewAdapaterOnClickHandler mClickHandler){
        this.mClickHandler=mClickHandler;

    }

    public FavouriteAdapter(Context context, NoteViewAdapaterOnClickHandler mClickHandler, MetaData metaData){
        this.arrayList = metaData.getPosterPath();
        this.mClickHandler=mClickHandler;
    }
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.note_item_picasso;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder noteHolder, int i) {
        if(arrayList==null){
            Favourite currentNote = notes.get(i);
            String movieid = String.valueOf(currentNote.getId());
            sendMessageToActivity(movieid);
            Picasso.get().load(currentNote.getDescription()).into(noteHolder.imageButton);
        }else{
            Picasso.get().load(arrayList.get(i).toString()).into(noteHolder.imageButton);
        }
    }

    @Override
    public int getItemCount() {
        if(arrayList==null)return notes.size();
        return 20;
    }

    public void setNotes(List<Favourite> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imageButton;
        public NoteHolder(View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.imageButton1);
            itemView.setOnClickListener(this);
        }
        public ImageView getImageButton() {
            return imageButton;
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(Integer.toString(adapterPosition));
        }
    }
    private void sendMessageToActivity(String movieid) {
        Intent intent = new Intent(Config.QUERYMOVIEID);
        intent.putExtra(Config.MOVIEID, movieid);
        LocalBroadcastManager.getInstance(this.context).sendBroadcast(intent);
    }
}
