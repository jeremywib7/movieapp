package com.androidapp.movieapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidapp.movieapp.R;
import com.androidapp.movieapp.models.MovieModel;
import com.androidapp.movieapp.utils.Credentials;
import com.bumptech.glide.Glide;

import java.util.List;

public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieModel> mMovies;
    private OnMovieListener onMovieListener;

    private static final int DISPLAY_POP = 1;
    private static final int DISPLAY_SEARCH =  2;

    public MovieRecyclerView(OnMovieListener onMovieListener) {
        this.onMovieListener = onMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if( viewType == DISPLAY_SEARCH){
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,
                    parent, false);
             return new MovieViewHolder(view, onMovieListener);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movies_layout,
                    parent, false);
            return new Popular_View_Holder(view, onMovieListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        int itemViewType =  getItemViewType(i);
        if( itemViewType == DISPLAY_SEARCH){
            //vote average = 10/2 = 5
            ((MovieViewHolder)holder).ratingBar.setRating((mMovies.get(i).getVote_average())/2);
            //glide
            Glide.with(holder.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500/" + mMovies.get(i).getPoster_path())
                    .into(((MovieViewHolder)holder).imageView);
        } else {
            //vote average = 10/2 = 5
            ((Popular_View_Holder)holder).ratingBar22.setRating((mMovies.get(i).getVote_average())/2);
            //glide
            Glide.with(holder.itemView.getContext())
                    .load("https://image.tmdb.org/t/p/w500/" + mMovies.get(i).getPoster_path())
                    .into(((Popular_View_Holder)holder).imageView22);
        }

    }

    @Override
    public int getItemCount() {
        if(mMovies != null) {
            return mMovies.size();
        }
        return 0;
    }

    public void setmMovies(List<MovieModel> mMovies) {
        this.mMovies = mMovies;
        notifyDataSetChanged();
    }

    //get clicked movie id
    public MovieModel getSelectedMovie(int position){
        if(mMovies != null){
            if(mMovies.size() > 0){
                return mMovies.get(position);
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (Credentials.POPULAR) {
            return DISPLAY_POP;
        } else {
            return DISPLAY_SEARCH;
        }
    }
}
