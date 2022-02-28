package com.example.examen.modelos;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.examen.consumo.MovieModel;
import com.example.examen.repositorio.MovieRepository;

import java.util.List;

public class  MovieListViewModel extends ViewModel {

    //private MutableLiveData<List<MovieModel>> mMovies = new MutableLiveData<List<MovieModel>>();
    private MovieRepository movieRepository;
    public  MovieListViewModel(){
        movieRepository = MovieRepository.getInstance();
    }



    public LiveData<List<MovieModel>> getMovies(){
        return movieRepository.getMovies();
    }

    //llamada a metodo view-model
    public void searchMovieApi(String query, int pageNumer){
        movieRepository.searchMovieApi(query, pageNumer);
    }

}
