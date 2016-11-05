package com.hughesdigitalimage.popularmovies.to.video;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by David on 11/3/16.
 */

public class MovieType extends ExpandableGroup<MovieVideoResultTO> {

    private String movieType;

    public MovieType(String title, List<MovieVideoResultTO> items) {
        super(title, items);
    }
}