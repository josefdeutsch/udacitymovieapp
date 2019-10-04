package com.example.myapplication;

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewAdapterViewHolder> {
    private static final String TAG = "CardViewAdapter";
    private final CardViewAdapterOnClickHandler mClickHandler;
    private ArrayList arrayList;

    public interface CardViewAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    public CardViewAdapter(CardViewAdapterOnClickHandler clickHandler, ArrayList arrayList) {
        mClickHandler = clickHandler;
        this.arrayList=arrayList;
    }


    class CardViewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView imageButton;

        public CardViewAdapterViewHolder(View view) {
            super(view);
            imageButton = view.findViewById(R.id.imageButton1);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(Integer.toString(adapterPosition));
        }
    }

    @Override
    public CardViewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.activity_cardviewadapter;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new CardViewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewAdapterViewHolder cardViewAdapterViewHolder, int i) {
        Picasso.get().load(R.drawable.playbutton).into(cardViewAdapterViewHolder.imageButton);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();

    }
}