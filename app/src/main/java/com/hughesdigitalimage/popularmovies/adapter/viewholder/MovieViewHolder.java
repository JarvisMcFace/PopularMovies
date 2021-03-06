package com.hughesdigitalimage.popularmovies.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hughesdigitalimage.popularmovies.R;
import com.hughesdigitalimage.popularmovies.fragment.details.MovieDetailsCallbacks;
import com.hughesdigitalimage.popularmovies.to.PopularMovieDetailsTO;

import java.lang.ref.WeakReference;

/**
 * Created by David on 9/22/16.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    private ImageView moviePoster;
    private ProgressBar progressSpinner;
    private PopularMovieDetailsTO popularMovieDetailsTO;
    private WeakReference<MovieDetailsCallbacks> weakMovieDetailsCallbacks;

    public MovieViewHolder(View view, WeakReference<MovieDetailsCallbacks> weakMovieDetailsCallbacks){
        super(view);

        moviePoster = (ImageView) view.findViewById(R.id.movie_poster);
        setMoviePoster(moviePoster);

        progressSpinner = (ProgressBar) view.findViewById((R.id.progress_spinner));
        setProgressSpinner(progressSpinner);

        this.weakMovieDetailsCallbacks = weakMovieDetailsCallbacks;
        itemView.setOnClickListener(this);

    }

    public ImageView getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(ImageView moviePoster) {
        this.moviePoster = moviePoster;
    }

    public ProgressBar getProgressSpinner() {
        return progressSpinner;
    }

    public void setProgressSpinner(ProgressBar progressSpinner) {
        this.progressSpinner = progressSpinner;
    }

    public PopularMovieDetailsTO getPopularMovieDetailsTO() {
        return popularMovieDetailsTO;
    }

    public void setPopularMovieDetailsTO(PopularMovieDetailsTO popularMovieDetailsTO) {
        this.popularMovieDetailsTO = popularMovieDetailsTO;
    }

    @Override
    public void onClick(View v) {

        MovieDetailsCallbacks movieDetailsCallbacks = weakMovieDetailsCallbacks.get();
        movieDetailsCallbacks.onMovieSelected(popularMovieDetailsTO);

    }
}
