package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewAdapterViewHolder>{


    private static final String LOG_TAG = CardViewAdapter.class.getSimpleName();
    private final CardViewAdapterOnClickHandler mClickHandler;
    private ArrayList arrayList;

    public interface CardViewAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    public CardViewAdapter(CardViewAdapterOnClickHandler clickHandler, MetaData metaData) {

        mClickHandler = clickHandler;
        init_Arraylist(metaData);

    }

    private void init_Arraylist(MetaData metaData) {
        if (metaData != null) {
            this.arrayList = metaData.getPosterPath();
        } else {
            Log.d(LOG_TAG, "a problem occurs;- network connection...");
        }
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
       Picasso.get()
        .load(arrayList.get(i).toString())
        .into(cardViewAdapterViewHolder.imageButton);
            System.out.println(i);
    }

    @Override
    public int getItemCount() {

        // maximum results to be found at one page; json raw at moviedb.org.
        return 20;

    }


}