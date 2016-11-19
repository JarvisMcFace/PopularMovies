package com.hughesdigitalimage.popularmovies.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hughesdigitalimage.popularmovies.R;
import com.hughesdigitalimage.popularmovies.activity.MovieDetailsActivity;
import com.hughesdigitalimage.popularmovies.adapter.MovieAdapter;
import com.hughesdigitalimage.popularmovies.data.FavoriteMovieContentProvider;
import com.hughesdigitalimage.popularmovies.data.FavoriteMovieCursorHelper;
import com.hughesdigitalimage.popularmovies.fragment.details.MovieDetailsCallbacks;
import com.hughesdigitalimage.popularmovies.fragment.details.MovieDetailsFragment;
import com.hughesdigitalimage.popularmovies.to.PopularMovieDetailsTO;
import com.hughesdigitalimage.popularmovies.to.PopularMoviesTO;
import com.hughesdigitalimage.popularmovies.util.GetTheMoveDatabaseAPIKey;
import com.hughesdigitalimage.popularmovies.util.NetworkUtil;
import com.hughesdigitalimage.popularmovies.util.OkHttpHelper;
import com.hughesdigitalimage.popularmovies.util.OkHttpHelperCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PopularMoviesFragment extends Fragment implements MovieDetailsCallbacks, OkHttpHelperCallback {

    public final static String MOVIE_DB_POSTER_IMAGE_URL = "http://image.tmdb.org/t/p/w300";

    private static String TAG = "PopularMoviesFragment";

    private View rootView;
    private String result;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<PopularMovieDetailsTO> moviesTOList;
    private PopularMoviesTO popularMoviesTO;
    private boolean isShowingFavorites;
    private Menu menu;
    private boolean isTablet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);
        isTablet = getResources().getBoolean(R.bool.is_tablet);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        fetchPopularMovies();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_menu, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
        switchDrawable(isShowingFavorites);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sort_most_popular) {
            sortByMostPopular();
            return true;
        }

        if (id == R.id.sort_top_rated) {
            sortByTopRated();
            return true;
        }

        if (id == R.id.sort_favorite) {
            showFavoriteMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(PopularMovieDetailsTO popularMovieDetailsTO) {

        if (isTablet) {
            MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.getInstance(popularMovieDetailsTO);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.activity_movie_details_fragment,movieDetailsFragment);
            fragmentTransaction.commit();
        } else {
            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
            intent.putExtra(MovieDetailsFragment.EXTRA_MOVIE_DETAILS_TO, popularMovieDetailsTO);
            startActivity(intent);
        }

    }

    @Override
    public void performOnPostExecute(String jsonResults) {

        Gson gson = new Gson();
        popularMoviesTO = gson.fromJson(jsonResults, PopularMoviesTO.class);

        if (moviesTOList == null) {
            moviesTOList = new ArrayList<>(popularMoviesTO.getResults());
        } else {
            moviesTOList.addAll(popularMoviesTO.getResults());
        }
        loadData();
    }

    public void updateFavoriteMovies() {

        if (!isShowingFavorites){
            return;
        }
        ContentResolver contentResolver = getActivity().getApplication().getContentResolver();
        Cursor cursor = contentResolver.query(FavoriteMovieContentProvider.CONTENT_URI,null,null,null,null);
        moviesTOList = FavoriteMovieCursorHelper.retrieveAllFavoriteMovies(cursor);

        if (adapter != null) {
            adapter.setMoveResults(moviesTOList);
            adapter.notifyDataSetChanged();
        }
    }

    private void fetchPopularMovies() {
        if (!NetworkUtil.isDeviceConnectedToNetwork(new WeakReference<Context>(getActivity()))) {
            Snackbar.make(rootView, getString(R.string.no_network_connection), Snackbar.LENGTH_SHORT).show();
            return;
        }

        String url = getString(R.string.popular_movies_url) + GetTheMoveDatabaseAPIKey.execute(getResources());
        if (adapter == null) {
            WeakReference<OkHttpHelperCallback> weakReferenceOkHttpHelperCallback = new WeakReference<OkHttpHelperCallback>(this);
            OkHttpHelper okHttpHelper = new OkHttpHelper(weakReferenceOkHttpHelperCallback);
            okHttpHelper.execute(url);
        } else {
            loadData();
        }
    }

    private void sortByMostPopular() {
        
        if (adapter != null) {
            Collections.sort(moviesTOList, new PopularMoviesComparator());
            loadData();
            adapter.notifyDataSetChanged();
        }
    }

    private void sortByTopRated() {
        if (adapter != null) {
            Collections.sort(moviesTOList, new TopRatedMoviesComparator());
            loadData();
            adapter.notifyDataSetChanged();
        }
    }

    private void loadData() {

        if (moviesTOList != null) {
            WeakReference<MovieDetailsCallbacks> weakMovieDetailsCallbacks = new WeakReference<MovieDetailsCallbacks>(this);
            WeakReference<Context> contextWeakReference = new WeakReference<Context>(getActivity());
            adapter = new MovieAdapter(moviesTOList, contextWeakReference, weakMovieDetailsCallbacks);
            recyclerView.setAdapter(adapter);
        }
        switchDrawable(isShowingFavorites);
    }

    private void showFavoriteMovies() {

        if (isShowingFavorites){
            isShowingFavorites = false;
            adapter=null;
            moviesTOList= null;
            fetchPopularMovies();
        } else {
            isShowingFavorites = true;
            retrieveFavoriteMovies();
        }

        switchDrawable(isShowingFavorites);
    }

    private void retrieveFavoriteMovies() {
        ContentResolver contentResolver = getActivity().getApplication().getContentResolver();
        Cursor cursor = contentResolver.query(FavoriteMovieContentProvider.CONTENT_URI,null,null,null,null);

        if (cursor == null || cursor.getCount() == 0){
            Snackbar.make(rootView, getString(R.string.no_favorites), Snackbar.LENGTH_SHORT).show();
        }

        moviesTOList = FavoriteMovieCursorHelper.retrieveAllFavoriteMovies(cursor);
        loadData();
    }

    private void switchDrawable(boolean isFavorite){
        if (menu == null) {
            return;
        }
        MenuItem menuItem =  menu.findItem(R.id.sort_favorite);
        if (menuItem == null) {
            return;
        }

        if (isFavorite){
            menuItem.setIcon(R.drawable.ic_star_accent);
        } else {
            menuItem.setIcon(R.drawable.ic_star_outline_accent);
        }
    }


}
