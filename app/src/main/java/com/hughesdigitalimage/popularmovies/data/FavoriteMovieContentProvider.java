package com.hughesdigitalimage.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.hughesdigitalimage.popularmovies.to.PopularMovieDetailsTO;

/**
 * Created by David on 11/6/16.
 */

public class FavoriteMovieContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.hughesdigitalimage.favorite.movie.contentprovider";
    private static final String BASE_PATH = "favoritemovie";

    private static final int FAVORITE_MOVIES = 100;

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private FavoriteMovieDbAdapter favoriteMovieDbAdapter;

    public FavoriteMovieContentProvider() {
    }

    @Override
    public boolean onCreate() {
        FavoriteMovieDbAdapter dbAdapter = new FavoriteMovieDbAdapter(getContext());
        favoriteMovieDbAdapter = dbAdapter.open();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }



    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = favoriteMovieDbAdapter.insertFavoriteMovie(values);
        Context context = getContext();
        if (context != null) {
            ContentResolver contentResolver = context.getContentResolver();
            contentResolver.notifyChange(uri, null);
        }
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return favoriteMovieDbAdapter.deleteFavoirteMovie(selection,selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, BASE_PATH, FAVORITE_MOVIES);
        return uriMatcher;
    }

    public static ContentValues getContentValues(PopularMovieDetailsTO popularMovieDetailsTO ) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMoviesContract.MOVIE_ID, popularMovieDetailsTO.getId());
        contentValues.put(FavoriteMoviesContract.TITLE, popularMovieDetailsTO.getTitle());
        contentValues.put(FavoriteMoviesContract.OVERVIEW, popularMovieDetailsTO.getOverview());
        contentValues.put(FavoriteMoviesContract.RELEASE_DATE, popularMovieDetailsTO.getReleaseDate());
        contentValues.put(FavoriteMoviesContract.POSTER_PATH, popularMovieDetailsTO.getPosterPath());
        contentValues.put(FavoriteMoviesContract.BACKDROP_PATH, popularMovieDetailsTO.getBackdropPath());

        return contentValues;
    }
}
