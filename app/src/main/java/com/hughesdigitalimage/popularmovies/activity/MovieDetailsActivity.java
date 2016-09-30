package com.hughesdigitalimage.popularmovies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hughesdigitalimage.popularmovies.R;
import com.hughesdigitalimage.popularmovies.fragment.MovieDetailsFragment;

/**
 * Created by David on 9/24/16.
 */

public class MovieDetailsActivity extends AppCompatActivity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.movie_detail_fragment, new MovieDetailsFragment())
                .commit();
    }
}
