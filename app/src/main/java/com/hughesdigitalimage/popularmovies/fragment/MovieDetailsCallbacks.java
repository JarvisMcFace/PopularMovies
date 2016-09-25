package com.hughesdigitalimage.popularmovies.fragment;

import com.hughesdigitalimage.popularmovies.to.PopularMovieDetailsTO;

/**
 * Created by David on 9/24/16.
 */

public interface MovieDetailsCallbacks {

    void onMovieSelected(PopularMovieDetailsTO popularMovieDetailsTO);
}
