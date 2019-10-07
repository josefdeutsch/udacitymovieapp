package com.example.myapplication.data;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Config;
import com.example.myapplication.body.MetaData;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavoriteHolder> {
    private List<Favourite> notes = new ArrayList<>();
    private ArrayList arrayList;
    private static final String TAG = "FavouriteAdapter";
    private Context context;
    private FavouriteViewAdapaterOnClickHandler mClickHandler;

    public interface FavouriteViewAdapaterOnClickHandler {
        void onClick(String string);
    }

    public FavouriteAdapter(Context context, FavouriteViewAdapaterOnClickHandler mClickHandler){
        this.mClickHandler=mClickHandler;

    }

    /** I am aware of Generic Adapters, this is a workaround, the favorite is 1st in order @onCreate MainActivity **/
    /** If the arraylist is null, there is no static jsonString, Notes will be queried of DataBase **/
    /** getItemCount() **/

    public FavouriteAdapter(Context context, FavouriteViewAdapaterOnClickHandler mClickHandler, MetaData metaData){
        if (metaData != null) {
            this.arrayList = metaData.getPosterPath();
        }else{
            Toast.makeText(context, "please enter APIKEYS in Config.class",Toast.LENGTH_LONG).show();
        }
        this.mClickHandler=mClickHandler;
    }
    @NonNull
    @Override
    public FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.note_item_picasso;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteHolder noteHolder, int i) {
        if(arrayList==null){
            Favourite currentNote = notes.get(i);
            Log.d(TAG, "onReceive: "+"FavoriteAdapter");
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

    class FavoriteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imageButton;
        public FavoriteHolder(View itemView) {
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
