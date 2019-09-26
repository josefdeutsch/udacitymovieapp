package com.example.myapplication.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MetaData;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private List<Note> notes = new ArrayList<>();
    private ArrayList arrayList;
    private static final String TAG = "NoteAdapter";

    private NoteViewAdapaterOnClickHandler mClickHandler;
    public interface NoteViewAdapaterOnClickHandler {
        void onClick(String string);
    }
    public NoteAdapter(){}

    public NoteAdapter(NoteViewAdapaterOnClickHandler mClickHandler, MetaData metaData){
        this.arrayList = metaData.getPosterPath();
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
            Note currentNote = notes.get(i);
            Picasso.get().load(currentNote.getDescription()).into(noteHolder.imageButton);
            Log.d(TAG,currentNote.getDescription());
        }else{
            Picasso.get().load(arrayList.get(i).toString()).into(noteHolder.imageButton);
        }
    }

    @Override
    public int getItemCount() {
        if(arrayList==null)return notes.size();
        return 20; // notes.size();
    }

    public void setNotes(List<Note> notes) {
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
}
