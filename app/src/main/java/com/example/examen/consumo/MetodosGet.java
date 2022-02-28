package com.example.examen.consumo;

import com.example.examen.Request.Servicio;
import com.example.examen.Respuesta.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MetodosGet {


    public void getRetrofitRespuesta() {
        MovieApi movieApi = Servicio.getMovieApi();

        Call<MovieResponse> responseCall = movieApi
                .searchMovie("6d67239d81d519d26d1322ee2b5ea249", "Jack Reacher", "1");

        responseCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.code() == 200) {
                    System.out.println("La respuesta es: " + response.body().toString());
                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());

                    for (MovieModel movie : movies) {
                        System.out.println("Lista:" + movie.getRelease_date());
                        System.out.println("Lista:" + movie.getTitle());

                    }
                } else {
                    try {
                        System.out.println("error");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }


            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });

    }
}
