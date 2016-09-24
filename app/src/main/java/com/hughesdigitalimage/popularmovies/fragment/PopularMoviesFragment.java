package com.hughesdigitalimage.popularmovies.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.hughesdigitalimage.popularmovies.adapter.MovieAdapter;
import com.hughesdigitalimage.popularmovies.to.MoviesTO;
import com.hughesdigitalimage.popularmovies.to.ResultTO;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PopularMoviesFragment extends Fragment {

    public final static String MOVIE_DB_IMAGE_URL = "http://image.tmdb.org/t/p/w400";

    private static String TAG = "PopularMoviesFragment";


    private View rootView;
    private String result;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<ResultTO> moviesTOList;
    private WeakReference<Activity> contextWeakReference;

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
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);


        String url = "http://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.THE_MOVIE_API_DB_KEY;


        OkHttpHelper okHttpHelper = new OkHttpHelper();
        okHttpHelper.execute(url);
    }

    private void performOnPostExecute(String jsonResults) {
        result = jsonResults;

        Gson gson = new Gson();
        MoviesTO moviesTO = gson.fromJson(result,MoviesTO.class);

        if (moviesTOList == null) {
            moviesTOList = new ArrayList<>(moviesTO.getResults());
        } else {
            moviesTOList.addAll(moviesTO.getResults());
        }

        contextWeakReference =  new WeakReference<Activity>(getActivity());
        adapter = new MovieAdapter(moviesTOList,contextWeakReference);
        recyclerView.setAdapter(adapter);



        Log.d(TAG, "onActivityCreated() called with: savedInstanceState = [" + result + "]");
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
