package com.hughesdigitalimage.popularmovies.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hughesdigitalimage.popularmovies.R;
import com.hughesdigitalimage.popularmovies.to.MovieDetailsTO;
import com.hughesdigitalimage.popularmovies.util.FetchMoviePoster;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 9/24/16.
 */

public class MovieDetailsFragment extends Fragment {

    public final static String MOVIE_DB_DETAILS_IMAGE_URL = "http://image.tmdb.org/t/p/w92";
    private static String TAG = "MovieDetailsFragment";

    private View rootView;
    private MovieDetailsTO movieDetailsTO;
    private ImageView detailPoster;
    private TextView title;
    private TextView year;
    private TextView runningTime;
    private TextView userRating;
    private TextView overview;


    private ProgressBar progressSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            movieDetailsTO = (MovieDetailsTO) bundle.getSerializable("Test");
        }

        title = (TextView) rootView.findViewById(R.id.movie_title);
        detailPoster = (ImageView) rootView.findViewById(R.id.details_movie_poster);
        progressSpinner = (ProgressBar) rootView.findViewById(R.id.details_progress_spinner);

        year = (TextView) rootView.findViewById(R.id.details_movie_year);
        runningTime = (TextView) rootView.findViewById(R.id.details_movie_running_time);
        userRating = (TextView) rootView.findViewById(R.id.details_movie_user_rating);
        overview = (TextView) rootView.findViewById(R.id.details_movie_overview);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {

        String releaseDate = getReleaseDate();


        year.setText(releaseDate);
        runningTime.setText("120min");
        userRating.setText(movieDetailsTO.getVoteAverage().toString());
        overview.setText(movieDetailsTO.getOverview());

        title.setText(movieDetailsTO.getTitle());
        String retrievePosterURL = MOVIE_DB_DETAILS_IMAGE_URL + movieDetailsTO.getPosterPath();
        FetchMoviePoster.execute(this, retrievePosterURL, detailPoster, progressSpinner);
    }

    private String getReleaseDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date convertedCurrentDate = null;
        try {
            convertedCurrentDate = sdf.parse(movieDetailsTO.getReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(convertedCurrentDate);
    }
}
