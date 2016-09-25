package com.hughesdigitalimage.popularmovies.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hughesdigitalimage.popularmovies.R;
import com.hughesdigitalimage.popularmovies.to.MovieDetailsTO;

/**
 * Created by David on 9/24/16.
 */

public class MovieDetailsFragment extends Fragment {

    private static String TAG = "MovieDetailsFragment";
    private MovieDetailsTO movieDetailsTO;
    private TextView movieTitle;

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_details,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            movieDetailsTO = (MovieDetailsTO) bundle.getSerializable("Test");
        }

        movieTitle = (TextView) rootView.findViewById(R.id.movie_title);

        movieTitle.setText(movieDetailsTO.getTitle());
    }

}
