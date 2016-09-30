package com.hughesdigitalimage.popularmovies.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hughesdigitalimage.popularmovies.R;
import com.hughesdigitalimage.popularmovies.to.PopularMovieDetailsTO;
import com.hughesdigitalimage.popularmovies.to.movie.MoviesTO;
import com.hughesdigitalimage.popularmovies.util.FetchMoviePoster;
import com.hughesdigitalimage.popularmovies.util.GetTheMoveDatabaseAPIKey;
import com.hughesdigitalimage.popularmovies.util.NetworkUtil;
import com.hughesdigitalimage.popularmovies.util.OkHttpHelper;
import com.hughesdigitalimage.popularmovies.util.OkHttpHelperCallback;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 9/24/16.
 */

public class MovieDetailsFragment extends Fragment implements OkHttpHelperCallback {

    public final static String MOVIE_DB_DETAILS_IMAGE_URL = "http://image.tmdb.org/t/p/w154";
    public static final String EXTRA_MOVIE_DETAILS_TO = "com.hughesdigitalimage.popularmovies.fragment.movieFragment.movieDetails";
    private static String TAG = "MovieDetailsFragment";

    private View rootView;
    private PopularMovieDetailsTO popularMovieDetailsTO;
    private ImageView detailPoster;
    private ImageView toolbarPoster;
    private TextView year;
    private TextView runningTime;
    private TextView voteAverage;
    private TextView overview;
    private TextView releasedDate;


    private ProgressBar progressSpinner;
    private MoviesTO moviesTO;
    private CollapsingToolbarLayout collapsingToolbarLayout;

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
            popularMovieDetailsTO = (PopularMovieDetailsTO) bundle.getSerializable(EXTRA_MOVIE_DETAILS_TO);
        }


        detailPoster = (ImageView) rootView.findViewById(R.id.details_movie_poster);
        progressSpinner = (ProgressBar) rootView.findViewById(R.id.details_progress_spinner);

        year = (TextView) rootView.findViewById(R.id.details_movie_year);
        runningTime = (TextView) rootView.findViewById(R.id.details_movie_running_time);
        releasedDate = (TextView) rootView.findViewById(R.id.details_movie_released_date);
        voteAverage = (TextView) rootView.findViewById(R.id.details_movie_user_rating);
        overview = (TextView) rootView.findViewById(R.id.details_movie_overview);


        collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar_layout);
        toolbarPoster = (ImageView) getActivity().findViewById(R.id.toolbar_poster);


        fetchMovieDetails();
    }


    @Override
    public void performOnPostExecute(String jsonResults) {
        Gson gson = new Gson();
        moviesTO = gson.fromJson(jsonResults, MoviesTO.class);
        loadData();
    }

    private void loadData() {

        String retrievePosterURL = MOVIE_DB_DETAILS_IMAGE_URL + popularMovieDetailsTO.getPosterPath();
        String retrieveCollapsingToolbarPosterURL = MOVIE_DB_DETAILS_IMAGE_URL + popularMovieDetailsTO.getBackdropPath();
        WeakReference<Context> contextWeakReference = new WeakReference<Context>(getActivity());

        FetchMoviePoster.execute(contextWeakReference, retrievePosterURL, detailPoster, progressSpinner);
        FetchMoviePoster.execute(contextWeakReference, retrieveCollapsingToolbarPosterURL, toolbarPoster, null);

        if (moviesTO != null) {
            String releaseYear = getReleaseYearDate();

            collapsingToolbarLayout.setTitle(popularMovieDetailsTO.getTitle());
            toolbarPoster.setImageAlpha(95);
            year.setText(releaseYear);
            overview.setText(popularMovieDetailsTO.getOverview());

            String runningMinutes = getString(R.string.running_time, String.valueOf(moviesTO.getRuntime()));
            runningTime.setText(runningMinutes);

            String voteAverageDisplayed = getString(R.string.user_rating, String.valueOf(moviesTO.getVoteAverage()));
            voteAverage.setText(voteAverageDisplayed);

            String displayedReleasedDate = getString(R.string.released_date, popularMovieDetailsTO.getReleaseDate());
            releasedDate.setText(displayedReleasedDate);
        }

    }

    private void fetchMovieDetails() {

        if (!NetworkUtil.isDeviceConnectedToNetwork(new WeakReference<Context>(getActivity()))) {
            Snackbar.make(rootView, getString(R.string.no_network_connection), Snackbar.LENGTH_SHORT).show();
            return;
        }

        progressSpinner.setVisibility(View.VISIBLE);
        String movieID = String.valueOf(popularMovieDetailsTO.getId());
        String urlMovieDetails = getString(R.string.movie_basic_informaiton_url, movieID) + GetTheMoveDatabaseAPIKey.execute(getResources());
        WeakReference<OkHttpHelperCallback> weakReferenceOkHttpHelperCallback = new WeakReference<OkHttpHelperCallback>(this);
        OkHttpHelper okHttpHelper = new OkHttpHelper(weakReferenceOkHttpHelperCallback);
        okHttpHelper.execute(urlMovieDetails);
    }

    private String getReleaseYearDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date convertedCurrentDate = null;
        try {
            convertedCurrentDate = sdf.parse(popularMovieDetailsTO.getReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdf.format(convertedCurrentDate);
    }

}
