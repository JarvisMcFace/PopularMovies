package com.hughesdigitalimage.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hughesdigitalimage.popularmovies.R;
import com.hughesdigitalimage.popularmovies.adapter.viewholder.MovieViewHolder;
import com.hughesdigitalimage.popularmovies.fragment.MovieDetailsCallbacks;
import com.hughesdigitalimage.popularmovies.fragment.PopularMoviesFragment;
import com.hughesdigitalimage.popularmovies.to.MovieDetailsTO;
import com.hughesdigitalimage.popularmovies.util.FetchMoviePoster;
import com.hughesdigitalimage.popularmovies.util.StringUtils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by David on 9/22/16.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MOVIE_LISTING = 1;
    private static final int VIEW_TYPE_MOVIE_EMPTY_STATE = 2;
    private List<MovieDetailsTO> moveResults;
    private WeakReference<PopularMoviesFragment> weakPopularMoviesFragment;
    private WeakReference<MovieDetailsCallbacks> weakMovieDetailsCallbacks;

    public MovieAdapter(List<MovieDetailsTO> moveResults, WeakReference<PopularMoviesFragment> weakPopularMoviesFragment,WeakReference<MovieDetailsCallbacks> weakMovieDetailsCallbacks) {
        this.moveResults = moveResults;
        this.weakPopularMoviesFragment = weakPopularMoviesFragment;
        this.weakMovieDetailsCallbacks = weakMovieDetailsCallbacks;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_row, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view,weakMovieDetailsCallbacks);

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MovieViewHolder movieViewHolder = (MovieViewHolder) holder;

        final ImageView moviePoster = movieViewHolder.getMoviePoster();
        final ProgressBar progressSpinner = movieViewHolder.getProgressSpinner();

        MovieDetailsTO movieDetailsTO = moveResults.get(position);
        movieViewHolder.setMovieDetailsTO(movieDetailsTO);

        String moviePosterPath = movieDetailsTO.getPosterPath();
        String retrievePosterURL = PopularMoviesFragment.MOVIE_DB_POSTER_IMAGE_URL + moviePosterPath;

        if (StringUtils.isNotEmpty(moviePosterPath)) {
            PopularMoviesFragment fragment = weakPopularMoviesFragment.get();
            FetchMoviePoster.execute(fragment,retrievePosterURL,moviePoster,progressSpinner);
        }

    }


    @Override
    public int getItemCount() {
        return moveResults.size();
    }
}
