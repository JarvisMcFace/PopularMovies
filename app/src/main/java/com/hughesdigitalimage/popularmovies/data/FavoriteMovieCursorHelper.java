package com.hughesdigitalimage.popularmovies.data;

import android.database.Cursor;
import android.util.Log;

import com.hughesdigitalimage.popularmovies.fragment.PopularMoviesFragment;
import com.hughesdigitalimage.popularmovies.to.PopularMovieDetailsTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 11/7/16.
 */

public class FavoriteMovieCursorHelper {


    public static List<PopularMovieDetailsTO> retrieveAllFavoriteMovies(Cursor cursor) {

        List<PopularMovieDetailsTO> favoriteMovieList = new ArrayList<>();

        if (cursor!=null){
            cursor.moveToFirst();
            while (!cursor.isAfterLast() && cursor.getCount()>0){
                PopularMovieDetailsTO popularMovieDetailsTO = getPopularMovieDetailsTO(cursor);

                if (popularMovieDetailsTO != null) {
                    favoriteMovieList.add(popularMovieDetailsTO);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }

        return favoriteMovieList;
    }


    private static PopularMovieDetailsTO getPopularMovieDetailsTO(Cursor cursor) {

        final int movieIDIndex = cursor.getColumnIndex(FavoriteMoviesContract.MOVIE_ID);
        final int titleIndex = cursor.getColumnIndex(FavoriteMoviesContract.TITLE);
        final int overviewIndex = cursor.getColumnIndex(FavoriteMoviesContract.OVERVIEW);
        final int releaseDateIndex = cursor.getColumnIndex(FavoriteMoviesContract.RELEASE_DATE);
        final int posterPathIndex = cursor.getColumnIndex(FavoriteMoviesContract.POSTER_PATH);
        final int backdropPathIndex = cursor.getColumnIndex(FavoriteMoviesContract.BACKDROP_PATH);
        final int voteAverageIndex = cursor.getColumnIndex(FavoriteMoviesContract.VOTE_AVERAGE);
        final int votePopularityIndex = cursor.getColumnIndex(FavoriteMoviesContract.POPULARITY);
        try {

            int movieID = Integer.parseInt(cursor.getString(movieIDIndex));
            String title = cursor.getString(titleIndex);
            String overview =cursor.getString(overviewIndex);
            String releaseDate =cursor.getString(releaseDateIndex);
            String posterPath = cursor.getString(posterPathIndex);
            String backdropPath = cursor.getString(backdropPathIndex);
            Double voteAverage = Double.parseDouble(cursor.getString(voteAverageIndex));
            Double popularity = Double.parseDouble(cursor.getString(votePopularityIndex));

            return new PopularMovieDetailsTO(movieID,title,releaseDate,overview,posterPath,backdropPath,voteAverage,popularity);

        } catch (Exception ex) {
            Log.d(PopularMoviesFragment.class.getSimpleName(), "getPopularMovieDetailsTO: ");
            return null;
        }
    }
}
