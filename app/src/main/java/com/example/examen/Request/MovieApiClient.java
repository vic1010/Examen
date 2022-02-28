package com.example.examen.Request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.examen.AppExecutors;
import com.example.examen.Request.Servicio;
import com.example.examen.Respuesta.MovieResponse;
import com.example.examen.consumo.MetodosGet;
import com.example.examen.consumo.MovieModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {
    private MutableLiveData<List<MovieModel>> mMovies;
    //datalive
    private static MovieApiClient instance;
    //haciendo global
    private RetrieveMoviesRunnable retrieveMoviesRunnable;



    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {

        mMovies = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }




    public void searchMovieApi(String query, int pageMumber) {


        if (retrieveMoviesRunnable != null) {
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageMumber);


        final Future myHandler = AppExecutors.getInstance().networkID().submit(retrieveMoviesRunnable);


        AppExecutors.getInstance().networkID().schedule(new Runnable() {
            @Override
            public void run() {
                //cancelado llamada retrofit
                myHandler.cancel(true);
            }
        }, 3000, TimeUnit.MILLISECONDS);

    }


    //data from api by runnable class
    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean CancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            CancelRequest = false;
        }

        @Override
        public void run() {

            //aqui me quede 

            try{
                Response response = getMovies(query, pageNumber).execute();
                if(CancelRequest){
                    return;
                }
                if (response.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieResponse)response.body()).getMovies());
                    if (pageNumber == 1){
                        //data live
                        //background thread
                        mMovies.postValue(list);

                    }else{
                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                }else{
                    System.out.println("error 20");
                    mMovies.postValue(null);
                }
            }catch (IOException e){
                e.printStackTrace();
                mMovies.postValue(null);
            }


            if (CancelRequest) {
                return;
            }
        }

        //aqui hay una clase que no tengo
        private Call<MovieResponse> getMovies(String query, int pageNumber) {
            return Servicio.getMovieApi().searchMovie(
                    "6d67239d81d519d26d1322ee2b5ea249",
                    query,
                    String.valueOf(pageNumber)
            );
        }
            private void cancelRequest () {
                System.out.println("busqueda cancelada");
            CancelRequest = true;
            return;
        }
    }
}



