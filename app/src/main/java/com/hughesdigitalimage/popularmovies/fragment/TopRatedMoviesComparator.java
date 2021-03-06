package com.hughesdigitalimage.popularmovies.fragment;

import com.hughesdigitalimage.popularmovies.to.PopularMovieDetailsTO;

import java.util.Comparator;

/**
 * Created by David on 9/26/16.
 */
public class TopRatedMoviesComparator implements Comparator<PopularMovieDetailsTO> {
    @Override
    public int compare(PopularMovieDetailsTO first, PopularMovieDetailsTO second) {
        return Double.compare(second.getVoteAverage(),first.getVoteAverage());
    }
}
