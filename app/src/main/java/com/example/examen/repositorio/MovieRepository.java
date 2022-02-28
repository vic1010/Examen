package com.example.examen.repositorio;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.examen.Request.MovieApiClient;
import com.example.examen.consumo.MovieApi;
import com.example.examen.consumo.MovieModel;

import java.util.List;

public class MovieRepository {

    private static MovieRepository instance;
    private MovieApiClient movieApiClient;
    //  private MutableLiveData<List<MovieModel>> mMovies;


    public static MovieRepository getInstance(){
        if(instance == null){
            instance = new MovieRepository();

        }
        return instance;
    }

    private MovieRepository(){
       movieApiClient = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return movieApiClient.getMovies();
    }

    //llamada al metodo
    public void searchMovieApi (String query, int pageNumber){
        movieApiClient.searchMovieApi(query, pageNumber);


    }
}


