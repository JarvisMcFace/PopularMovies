package com.hughesdigitalimage.popularmovies.fragment.details;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.hughesdigitalimage.popularmovies.R;
import com.hughesdigitalimage.popularmovies.activity.PopularMoviesActivity;
import com.hughesdigitalimage.popularmovies.activity.ReviewDetailActivity;
import com.hughesdigitalimage.popularmovies.adapter.video.MovieVideoAdapter;
import com.hughesdigitalimage.popularmovies.data.FavoriteMovieContentProvider;
import com.hughesdigitalimage.popularmovies.data.FavoriteMoviesContract;
import com.hughesdigitalimage.popularmovies.to.PopularMovieDetailsTO;
import com.hughesdigitalimage.popularmovies.to.movie.MoviesTO;
import com.hughesdigitalimage.popularmovies.to.review.MovieReviewTO;
import com.hughesdigitalimage.popularmovies.to.review.ReviewTO;
import com.hughesdigitalimage.popularmovies.to.video.MovieType;
import com.hughesdigitalimage.popularmovies.to.video.MovieVideoResultTO;
import com.hughesdigitalimage.popularmovies.to.video.MovieVideoTO;
import com.hughesdigitalimage.popularmovies.util.FetchMoviePoster;
import com.hughesdigitalimage.popularmovies.util.GetTheMoveDatabaseAPIKey;
import com.hughesdigitalimage.popularmovies.util.IsMovieAFavoriteMovie;
import com.hughesdigitalimage.popularmovies.util.IsRequestedPackageInstalled;
import com.hughesdigitalimage.popularmovies.util.ListUtils;
import com.hughesdigitalimage.popularmovies.util.NetworkUtil;
import com.hughesdigitalimage.popularmovies.util.StringUtils;
import com.hughesdigitalimage.popularmovies.util.VolleySingleton;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by David on 9/24/16.
 */

public class MovieDetailsFragment extends Fragment implements MovieVideoCallback {

    public final static String MOVIE_DB_DETAILS_IMAGE_URL = "http://image.tmdb.org/t/p/w154";
    public static final String EXTRA_MOVIE_DETAILS_TO = "com.hughesdigitalimage.popularmovies.fragment.movieFragment.movieDetails";
    private static String TAG = "MovieDetailsFragment";

    private View rootView;
    private PopularMovieDetailsTO popularMovieDetailsTO;
    private ImageView detailPoster;
    private ImageView toolbarPoster;
    private View movieDetailContainer;
    private TextView year;
    private TextView runningTime;
    private TextView voteAverage;
    private TextView overview;
    private TextView releasedDate;

    private LinearLayout reviewContainer;
    private ProgressBar progressSpinnerMovieDetails;
    private MoviesTO moviesTO;
    private MovieVideoTO movieVideoTO;
    private MovieReviewTO movieReviewTO;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String movieID;
    private RecyclerView videoRecyclerView;
    private MovieVideoAdapter movieVideoAdapter;
    private ImageView favoriteButton;
    private boolean isFavoriteMovie;
    private boolean isTablet;

    public static MovieDetailsFragment getInstance(PopularMovieDetailsTO popularMovieDetailsTO) {
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        movieDetailsFragment.setPopularMovieDetailsTO(popularMovieDetailsTO);
        return movieDetailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        isTablet = getResources().getBoolean(R.bool.is_tablet);

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null && !isTablet ) {
            popularMovieDetailsTO = bundle.getParcelable(EXTRA_MOVIE_DETAILS_TO);
        }

        if (popularMovieDetailsTO == null) {
            return;
        }
        movieID = String.valueOf(popularMovieDetailsTO.getId());
        setupViews();

        videoRecyclerView = (RecyclerView) rootView.findViewById(R.id.movie_video_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        videoRecyclerView.setLayoutManager(layoutManager);

        setupFavoriteButton();
        fetchMovieDetails();
        fetchMovieVideos();
        fetchMovieReviews();
    }

    private void setupViews() {
        detailPoster = (ImageView) rootView.findViewById(R.id.details_movie_poster);
        progressSpinnerMovieDetails = (ProgressBar) rootView.findViewById(R.id.details_progress_spinner);
        year = (TextView) rootView.findViewById(R.id.details_movie_year);
        runningTime = (TextView) rootView.findViewById(R.id.details_movie_running_time);
        releasedDate = (TextView) rootView.findViewById(R.id.details_movie_released_date);
        voteAverage = (TextView) rootView.findViewById(R.id.details_movie_user_rating);
        overview = (TextView) rootView.findViewById(R.id.details_movie_overview);
        reviewContainer = (LinearLayout) rootView.findViewById(R.id.review_container);
        collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar_layout);
        toolbarPoster = (ImageView) getActivity().findViewById(R.id.toolbar_poster);
        movieDetailContainer = rootView.findViewById(R.id.movie_detail_container);
    }

    private void setupFavoriteButton() {

        favoriteButton = (ImageView) rootView.findViewById(R.id.favorite_movie);


        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFavoriteMovieButton();
            }
        });
        Application application = getActivity().getApplication();
        isFavoriteMovie = IsMovieAFavoriteMovie.execute(application, popularMovieDetailsTO.getId().toString());

        showAsFavorite(isFavoriteMovie);
    }

    private void handleFavoriteMovieButton() {
        ContentResolver contentResolver = getActivity().getContentResolver();

        if (popularMovieDetailsTO == null) {
            return;
        }

        if (isFavoriteMovie) {
            isFavoriteMovie = false;
            showAsFavorite(isFavoriteMovie);
            String selection = FavoriteMoviesContract.MOVIE_ID + " = ?";
            String[] selectionArgs = {popularMovieDetailsTO.getId().toString()};
            contentResolver.delete(FavoriteMovieContentProvider.CONTENT_URI, selection, selectionArgs);

        } else {
            isFavoriteMovie = true;
            showAsFavorite(isFavoriteMovie);
            ContentValues contentValues = FavoriteMovieContentProvider.getContentValues(popularMovieDetailsTO);
            contentResolver.insert(FavoriteMovieContentProvider.CONTENT_URI, contentValues);
        }

        if (isTablet) {
            PopularMoviesActivity activity = (PopularMoviesActivity) getActivity();
            activity.updateFavoriteMovies();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movieVideoAdapter != null) {
            movieVideoAdapter.onSaveInstanceState(outState);
        }
    }


    public void performOnResponseMovieDetails(String jsonResults) {
        Gson gson = new Gson();
        moviesTO = gson.fromJson(jsonResults, MoviesTO.class);
        loadData();
        movieDetailContainer.setVisibility(View.VISIBLE);
    }

    private void loadData() {

        String retrievePosterURL = MOVIE_DB_DETAILS_IMAGE_URL + popularMovieDetailsTO.getPosterPath();
        String retrieveCollapsingToolbarPosterURL = MOVIE_DB_DETAILS_IMAGE_URL + popularMovieDetailsTO.getBackdropPath();
        WeakReference<Context> contextWeakReference = new WeakReference<Context>(getActivity());

        FetchMoviePoster.execute(contextWeakReference, retrievePosterURL, detailPoster, progressSpinnerMovieDetails);
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

    public void fetchMovieDetails() {

        if (!NetworkUtil.isDeviceConnectedToNetwork(new WeakReference<Context>(getActivity()))) {
            Snackbar.make(rootView, getString(R.string.no_network_connection), Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (moviesTO != null) {
            return;
        }
        progressSpinnerMovieDetails.setVisibility(View.VISIBLE);
        String urlMovieDetails = getString(R.string.movie_basic_informaiton_url, movieID) + GetTheMoveDatabaseAPIKey.execute(getResources());

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(urlMovieDetails, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        performOnResponseMovieDetails(response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //TODO Show Error message
            }
        });

        // Adding JsonObject request to request queue
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectReq);
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


    public void fetchMovieVideos() {
        if (!NetworkUtil.isDeviceConnectedToNetwork(new WeakReference<Context>(getActivity()))) {
            Snackbar.make(rootView, getString(R.string.no_network_connection), Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (movieVideoTO != null) {
            return;
        }

        String movieVideosURL = getString(R.string.movie_videos_endpoint_url, movieID) + GetTheMoveDatabaseAPIKey.execute(getResources());
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(movieVideosURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        performOnResponseMovieVideos(response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //TODO Show Error message
            }
        });

        // Adding JsonObject request to request queue
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectReq);
    }

    public void fetchMovieReviews() {

        if (movieReviewTO != null) {
            return;
        }
        String movieVideosURL = getString(R.string.movie_videos_review_url, movieID) + GetTheMoveDatabaseAPIKey.execute(getResources());

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(movieVideosURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        performOnResponseMovieReviews(response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //TODO Show Error message
            }
        });

        // Adding JsonObject request to request queue
        VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectReq);
    }

    private void performOnResponseMovieVideos(String movieVideoJSON) {

        Gson gson = new Gson();
        movieVideoTO = gson.fromJson(movieVideoJSON, MovieVideoTO.class);

        if (movieVideoJSON == null) {
            return;
        }


        List<MovieType> movieVideos = getVideoPreviewList();

        if (ListUtils.isEmpty(movieVideos)) {
            return;
            //TODO remove video section nothing to see
        }

        View videoContainer = rootView.findViewById(R.id.movie_vidoes_container);
        videoContainer.setVisibility(View.VISIBLE);
        if (movieVideoAdapter == null) {
            WeakReference<MovieVideoCallback> weakReferenceMovieVideoCallback = new WeakReference<MovieVideoCallback>(this);
            movieVideoAdapter = new MovieVideoAdapter(movieVideos, weakReferenceMovieVideoCallback);
        }

        videoRecyclerView.setAdapter(movieVideoAdapter);

    }

    private void performOnResponseMovieReviews(String movieReviewsJSON) {

        Gson gson = new Gson();
        movieReviewTO = gson.fromJson(movieReviewsJSON, MovieReviewTO.class);

        if (movieReviewsJSON == null) {
            return;
        }

        List<ReviewTO> reviewTOs = movieReviewTO.getResults();

        LinearLayout reviewsCommentary = (LinearLayout) rootView.findViewById(R.id.layout_reviews_commentary);
        if (ListUtils.isEmpty(reviewTOs)) {

            reviewsCommentary.setVisibility(View.GONE);
            return;
        }

        reviewsCommentary.setVisibility(View.VISIBLE);
        LayoutInflater inflator = getActivity().getLayoutInflater();
        reviewContainer.removeAllViews();

        for (final ReviewTO reviewTO : reviewTOs) {

            LinearLayout reviewRow = (LinearLayout) inflator.inflate(R.layout.layout_review, null);
            TextView arthur = (TextView) reviewRow.findViewById(R.id.movie_review_arthur);
            TextView review = (TextView) reviewRow.findViewById(R.id.movie_review_text);

            arthur.setText(reviewTO.getAuthor());
            review.setText(reviewTO.getContent());

            reviewRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ReviewDetailActivity.class);
                    intent.putExtra(ReviewDetailsFragment.MOVIE_TITLE, moviesTO.getTitle());
                    intent.putExtra(ReviewDetailsFragment.MOVIE_REVIEW_AURTHUR, reviewTO.getAuthor());
                    intent.putExtra(ReviewDetailsFragment.MOVIE_REVIEW_CONTENT, reviewTO.getContent());
                    startActivity(intent);
                }
            });


            reviewContainer.addView(reviewRow);
            ;

        }

    }

    private List<MovieType> getVideoPreviewList() {

        List<MovieVideoResultTO> videoResults = movieVideoTO.getResults();

        if (ListUtils.isEmpty(videoResults)) {
            return null;
        }

        List<String> videoTypes = getVideoTypes(videoResults);
        List<MovieType> videos = new ArrayList<MovieType>();

        for (String videoType : videoTypes) {
            List<MovieVideoResultTO> movieVideoResultTOs = getAllVidesOfSameType(videoResults, videoType);
            videos.add(new MovieType(videoType, movieVideoResultTOs));
        }

        return videos;
    }

    private List<MovieVideoResultTO> getAllVidesOfSameType(List<MovieVideoResultTO> videoResults, String videoType) {

        List<MovieVideoResultTO> movieVideoResultTOs = new ArrayList<MovieVideoResultTO>();

        for (MovieVideoResultTO videoResult : videoResults) {

            if (videoType.equalsIgnoreCase(videoResult.getType())) {
                movieVideoResultTOs.add(videoResult);
            }
        }
        return movieVideoResultTOs;
    }

    private List<String> getVideoTypes(List<MovieVideoResultTO> videoResults) {

        List<String> types = new ArrayList<String>();

        for (MovieVideoResultTO videoResult : videoResults) {
            String type = videoResult.getType();
            if (StringUtils.isNotEmpty(type)) {
                types.add(type);
            }
        }

        Set<String> uniqueTypes = new HashSet<String>(types);
        return new ArrayList<String>(uniqueTypes);
    }


    @Override
    public void onMovieSelected(MovieVideoResultTO movieVideoResultTO) {
        String youTubeKey = movieVideoResultTO.getKey();
        String youTubePackage = "com.google.android.youtube";

        WeakReference<Activity> activityWeakReference = new WeakReference<Activity>(getActivity());
        boolean isYouTubeInstalled = IsRequestedPackageInstalled.execute(activityWeakReference, youTubePackage);

        Intent videoIntent;
        if (isYouTubeInstalled) {
            videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youTubeKey));
        } else {
            videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + youTubeKey));
        }
        startActivity(videoIntent);
    }

    private void showAsFavorite(boolean isFavoriteMovie) {

        if (isFavoriteMovie) {
            favoriteButton.setImageResource(R.drawable.ic_star_accent);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_star_outline_accent);
        }
    }

    public void setPopularMovieDetailsTO(PopularMovieDetailsTO popularMovieDetailsTO) {
        this.popularMovieDetailsTO = popularMovieDetailsTO;
    }
}
