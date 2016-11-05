package com.hughesdigitalimage.popularmovies.fragment.details;

import com.hughesdigitalimage.popularmovies.to.video.MovieVideoResultTO;

/**
 * Created by David on 11/4/16.
 */

public interface MovieVideoCallback {

    void onMovieSelected(MovieVideoResultTO movieVideoResultTO);
}
