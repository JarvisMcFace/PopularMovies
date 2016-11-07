package com.hughesdigitalimage.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by David on 11/6/16.
 */

public class FavoriteMovieDbAdapter {

    public static final String FAVORITE_MOVIE_TABLE = "FavoriteMovie";

    public static final String CREATE_FAVORITE_MOVIE_TABLE =
            "CREATE TABLE IF NOT EXISTS "
                    + FAVORITE_MOVIE_TABLE + " ("
                    + FavoriteMoviesContract.FAVORITE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + FavoriteMoviesContract.MOVIE_ID + " TEXT NOT NULL, "
                    + FavoriteMoviesContract.TITLE + " TEXT NOT NULL, "
                    + FavoriteMoviesContract.OVERVIEW + " TEXT NOT NULL, "
                    + FavoriteMoviesContract.RELEASE_DATE + " TEXT NOT NULL, "
                    + FavoriteMoviesContract.POSTER_PATH + " TEXT NOT NULL, "
                    + FavoriteMoviesContract.BACKDROP_PATH + " TEXT NOT NULL)";


    private final Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public FavoriteMovieDbAdapter(Context context) {
        this.context = context;
    }

    public FavoriteMovieDbAdapter open() throws SQLException {
        databaseHelper = new DatabaseHelper(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close () {
        sqLiteDatabase.close();
    }

    public long insertFavoriteMovie(ContentValues contentValues) {
        return sqLiteDatabase.insert(FAVORITE_MOVIE_TABLE,null,contentValues);
    }


}
