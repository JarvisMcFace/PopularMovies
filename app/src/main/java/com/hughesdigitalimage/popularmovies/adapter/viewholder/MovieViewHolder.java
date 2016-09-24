package com.hughesdigitalimage.popularmovies.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.hughesdigitalimage.popularmovies.R;

/**
 * Created by David on 9/22/16.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private ImageView moviePoster;
    private ProgressBar progressSpinner;

    public MovieViewHolder(View view){
        super(view);

        moviePoster = (ImageView) view.findViewById(R.id.movie_poster);
        progressSpinner = (ProgressBar) view.findViewById((R.id.progress_spinner));

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
}
