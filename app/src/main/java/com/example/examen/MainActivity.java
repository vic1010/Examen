package com.example.examen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.examen.adaptadores.MovieRecyclerView;
import com.example.examen.adaptadores.OnMovieListener;
import com.example.examen.consumo.MetodosGet;
import com.example.examen.consumo.MovieModel;
import com.example.examen.modelos.MovieListViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMovieListener {

    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerAdapter;


    MovieListViewModel movieListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);


        ConfigureRecyclerView();
        ObservandoCambio();
    searchMovieApi("fast",1);
    }







    //cambios
    private  void ObservandoCambio(){
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
            if(movieModels != null){
                for(MovieModel movieModel: movieModels){
                  //  Log.v("Tag","cambio "+movieModel.getTitle());
                    System.out.println(movieModel.getTitle());

                    movieRecyclerAdapter.setMovies(movieModels);
                }
                }

            }
        });
    }


    private void searchMovieApi(String query, int pageNumber){
        movieListViewModel.searchMovieApi(query, pageNumber);

    }

    private void ConfigureRecyclerView(){
        movieRecyclerAdapter = new MovieRecyclerView(this);
        recyclerView.setAdapter(movieRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onMovieClick(int position) {
        Toast.makeText(this, "posicion "+position, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onCategoryClick(String category) {

    }
}
