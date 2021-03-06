package com.hughesdigitalimage.popularmovies.util;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.hughesdigitalimage.popularmovies.data.FavoriteMovieContentProvider;

/**
 * Created by David on 11/7/16.
 */

public class IsMovieAFavoriteMovie {

    public static boolean execute(Application application, String movieID) {

        ContentResolver contentResolver = application.getContentResolver();
        String[] selectionArg = {movieID};
        Cursor cursor = null;
        try {
            Uri uri = FavoriteMovieContentProvider.buildFavoriteMovieWithKey(movieID);
            cursor = contentResolver.query(uri, null, null, selectionArg, null);

            if (cursor == null || cursor.getCount() == 0) {
                return false;
            } else {
                return true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
