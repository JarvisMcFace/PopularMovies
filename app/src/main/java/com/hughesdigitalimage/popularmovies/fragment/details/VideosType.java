package com.hughesdigitalimage.popularmovies.fragment.details;

import com.hughesdigitalimage.popularmovies.to.video.MovieVideoResultTO;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by David on 11/3/16.
 */

public class VideosType extends ExpandableGroup<MovieVideoResultTO> {

    public VideosType(String title, List<MovieVideoResultTO> items) {
        super(title, items);
    }
}
