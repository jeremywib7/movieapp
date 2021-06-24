package com.androidapp.movieapp.adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.androidapp.movieapp.R;

import static com.androidapp.movieapp.R.*;

public class Popular_View_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

    OnMovieListener onMovieListener;
    ImageView imageView22;
    RatingBar ratingBar22;
    public Popular_View_Holder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);

        this.onMovieListener = onMovieListener;
        imageView22 = itemView.findViewById(id.movie_image_popular);
        ratingBar22 = itemView.findViewById(id.rating_bar_popular);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    }
}