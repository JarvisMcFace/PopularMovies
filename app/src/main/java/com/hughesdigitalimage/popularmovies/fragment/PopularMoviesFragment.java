package com.hughesdigitalimage.popularmovies.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hughesdigitalimage.popularmovies.BuildConfig;
import com.hughesdigitalimage.popularmovies.R;
import com.hughesdigitalimage.popularmovies.activity.MovieDetailsActivity;
import com.hughesdigitalimage.popularmovies.adapter.MovieAdapter;
import com.hughesdigitalimage.popularmovies.to.MovieDetailsTO;
import com.hughesdigitalimage.popularmovies.to.MoviesTO;
import com.hughesdigitalimage.popularmovies.util.NetworkUtil;
import com.hughesdigitalimage.popularmovies.util.RecyclerViewItemDecorator;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PopularMoviesFragment extends Fragment implements MovieDetailsCallbacks {

    public final static String MOVIE_DB_IMAGE_URL = "http://image.tmdb.org/t/p/w300";

    private static String TAG = "PopularMoviesFragment";

    private View rootView;
    private String result;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<MovieDetailsTO> moviesTOList;
    private MoviesTO moviesTO;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_popular_movies,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.movies_recycler_view);
        int spaceInPixels = 0;
        recyclerView.addItemDecoration(new RecyclerViewItemDecorator(spaceInPixels));

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);


        String url = "http://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.THE_MOVIE_API_DB_KEY;

        if (NetworkUtil.isDeviceConnectedToNetwork(new WeakReference<Context>(getActivity())) ) {
            if (adapter== null) {
                OkHttpHelper okHttpHelper = new OkHttpHelper();
                okHttpHelper.execute(url);
            }
        }else {
            Snackbar.make(rootView,"No Network Connection Please try again",Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onMovieSelected(MovieDetailsTO movieDetailsTO) {

        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra("Test",movieDetailsTO);
        startActivity(intent);

    }

    private void loadData() {

        if ( moviesTOList != null) {
            WeakReference<MovieDetailsCallbacks> weakMovieDetailsCallbacks = new WeakReference<MovieDetailsCallbacks>(this);
            WeakReference<PopularMoviesFragment> weakPopularMoviesFragment =  new WeakReference<PopularMoviesFragment>(this);
            adapter = new MovieAdapter(moviesTOList,weakPopularMoviesFragment,weakMovieDetailsCallbacks);
            recyclerView.setAdapter(adapter);
        }
    }

    private void performOnPostExecute(String jsonResults) {

        Gson gson = new Gson();
        moviesTO = gson.fromJson(jsonResults,MoviesTO.class);

        if (moviesTOList == null) {
            moviesTOList = new ArrayList<>(moviesTO.getResults());
        } else {
            moviesTOList.addAll(moviesTO.getResults());
        }
        loadData();
    }

    public class OkHttpHelper extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);

            Request request = builder.build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                Log.e("OkHttpHelper", "doInBackground: ", e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResults) {
          performOnPostExecute(jsonResults);
        }
    }

}
