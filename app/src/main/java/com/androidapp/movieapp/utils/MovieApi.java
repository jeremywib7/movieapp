package com.androidapp.movieapp.utils;

import com.androidapp.movieapp.models.MovieModel;
import com.androidapp.movieapp.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    //search movies
    //https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher
    @GET("/3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );

    //get popular movie
    //https://api.themoviedb.org/3/search/popular?api_key={api_key}&query=Jack+Reacher
    @GET("3/movie/popular")
    Call<MovieSearchResponse> getPopular(
        @Query("page") int page,
        @Query("api_key") String key
    );

}
