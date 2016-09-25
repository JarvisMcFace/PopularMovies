package com.hughesdigitalimage.popularmovies.util;

import android.content.res.Resources;

import com.hughesdigitalimage.popularmovies.BuildConfig;
import com.hughesdigitalimage.popularmovies.R;

/**
 * Created by David on 9/24/16.
 */
public class GetTheMoveDatabaseAPIKey {

    public static String execute(Resources resources) {

        if (resources == null) {
            return null;
        }

        String movieAPIDBKey = resources.getString(R.string.the_movie_db_api);

        if (StringUtils.isNotEmpty(movieAPIDBKey)) {
            return movieAPIDBKey;
        } else  {
            return BuildConfig.THE_MOVIE_API_DB_KEY;
        }

    }
}



