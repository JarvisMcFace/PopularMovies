package com.hughesdigitalimage.popularmovies.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        String url = getString(R.string.popular_movies_url) + GetTheMoveDatabaseAPIKey.execute(getResources());

        if (NetworkUtil.isDeviceConnectedToNetwork(new WeakReference<Context>(getActivity()))) {
            if (adapter == null) {
                WeakReference<OkHttpHelperCallback> weakReferenceOkHttpHelperCallback = new WeakReference<OkHttpHelperCallback>(this);
                OkHttpHelper okHttpHelper = new OkHttpHelper(weakReferenceOkHttpHelperCallback);
                okHttpHelper.execute(url);
            }
        } else {
            Snackbar.make(rootView, "No Network Connection Please try again", Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMovieSelected(PopularMovieDetailsTO popularMovieDetailsTO) {

        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra("Test", popularMovieDetailsTO);
        startActivity(intent);

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
            WeakReference<PopularMoviesFragment> weakPopularMoviesFragment = new WeakReference<PopularMoviesFragment>(this);
            adapter = new MovieAdapter(moviesTOList, weakPopularMoviesFragment, weakMovieDetailsCallbacks);

            recyclerView.setAdapter(adapter);
        }
    }
}
