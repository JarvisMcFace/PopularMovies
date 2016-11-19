package com.hughesdigitalimage.popularmovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hughesdigitalimage.popularmovies.R;
import com.hughesdigitalimage.popularmovies.fragment.PopularMoviesFragment;

public class PopularMoviesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void updateFavoriteMovies() {

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        PopularMoviesFragment popularMoviesFragment = (PopularMoviesFragment) fragmentManager.findFragmentById(R.id.popular_movie_fragment);

        if (popularMoviesFragment != null) {
            popularMoviesFragment.updateFavoriteMovies();
        }
    }

}
