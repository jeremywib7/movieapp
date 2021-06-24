package com.androidapp.movieapp.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.androidapp.movieapp.AppExecutors;
import com.androidapp.movieapp.models.MovieModel;
import com.androidapp.movieapp.response.MovieSearchResponse;
import com.androidapp.movieapp.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {
    //Live data for search
    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;

    //global runnable
    private RetrieveMoviesRunable retrieveMoviesRunable;

    //Live data(popular movies)
    private MutableLiveData<List<MovieModel>> mMoviesPop;

    //popular runnable
    private RetrieveMoviesRunablePop retrieveMoviesRunablePop;

    public static MovieApiClient getInstance() {
        if(instance == null) {
            instance = new MovieApiClient();
        } return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMoviesPop = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }
    public LiveData<List<MovieModel>> getMoviesPop() {
        return mMoviesPop;
    }

    public void searchMoviesApi(String query, int pageNumber) {
        if(retrieveMoviesRunable != null) {
            retrieveMoviesRunable = null;
        }

        retrieveMoviesRunable = new RetrieveMoviesRunable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().NetworkIO().submit(retrieveMoviesRunable);

        AppExecutors.getInstance().NetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);

    }

    public void searchMoviesPop(int pageNumber) {
        if(retrieveMoviesRunablePop != null) {
            retrieveMoviesRunablePop = null;
        }

        retrieveMoviesRunablePop = new RetrieveMoviesRunablePop(pageNumber);

        final Future myHandler2 = AppExecutors.getInstance().NetworkIO().submit(retrieveMoviesRunablePop);

        AppExecutors.getInstance().NetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                myHandler2.cancel(true);
            }
        }, 1000, TimeUnit.MILLISECONDS);

    }

    private class RetrieveMoviesRunable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getMovies(query, pageNumber).execute();

                if (cancelRequest) {
                    return;
                }
                if(response.code() == 200 ){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if(pageNumber == 1){
                        mMovies.postValue(list);

                    } else {
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                } else {
                    String error = response.body().toString();
                    Log.v("Tag", "Error" + error);
                    mMovies.postValue(null);
                }
            } catch (IOException e){
                e.printStackTrace();
                mMovies.postValue(null);

            }

            if (cancelRequest) {
                return;
            }
        }

        private Call<MovieSearchResponse> getMovies(String query, int pageNumber){
            return Servicey.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    pageNumber
            );
        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelling search request");
            cancelRequest = true;
        }


    }

    private class RetrieveMoviesRunablePop implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunablePop(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response2 = getPop(pageNumber).execute();

                if (cancelRequest) {
                    return;
                }
                if(response2.code() == 200 ){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
                    if(pageNumber == 1){
                        mMoviesPop.postValue(list);
                    } else {
                        List<MovieModel> currentMovies = mMoviesPop.getValue();
                        currentMovies.addAll(list);
                        mMoviesPop.postValue(currentMovies);
                    }
                } else {
                    String error = response2.body().toString();
                    Log.v("Tag", "Error" + error);
                    mMoviesPop.postValue(null);
                }
            } catch (IOException e){
                e.printStackTrace();
                mMoviesPop.postValue(null);
            }
        }
        //search method
        private Call<MovieSearchResponse> getPop(int pageNumber){
            return Servicey.getMovieApi().getPopular(
                    pageNumber,
                    Credentials.API_KEY
            );
        }

        private void cancelRequest() {
            Log.v("Tag", "Cancelling search request");
            cancelRequest = true;
        }


    }

}
