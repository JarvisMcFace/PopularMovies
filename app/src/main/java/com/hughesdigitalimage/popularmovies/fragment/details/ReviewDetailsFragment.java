package com.hughesdigitalimage.popularmovies.fragment.details;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hughesdigitalimage.popularmovies.R;

/**
 * Created by David on 11/6/16.
 */
public class ReviewDetailsFragment extends Fragment {

    public static final String MOVIE_TITLE = "com.hughesdigitalimage.popularmovies.fragment.details.title";
    public static final String MOVIE_REVIEW_AURTHUR = "com.hughesdigitalimage.popularmovies.fragment.details.movieReviewArthur";
    public static final String MOVIE_REVIEW_CONTENT = "com.hughesdigitalimage.popularmovies.fragment.details.movieReviewcontent";

    private View root;
    private String title;
    private String arthur;
    private String content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.layout_review_details, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Intent intent = getActivity().getIntent();
        if (intent != null) {
            title = intent.getStringExtra(MOVIE_TITLE);
            arthur = intent.getStringExtra(MOVIE_REVIEW_AURTHUR);
            content = intent.getStringExtra(MOVIE_REVIEW_CONTENT);
        }

        TextView reviewTitle = (TextView) root.findViewById(R.id.review_movie_title);
        reviewTitle.setText(title);
        TextView reviewArthur = (TextView) root.findViewById(R.id.review_movie_arthur);
        reviewArthur.setText(arthur);
        TextView reviewContent = (TextView) root.findViewById(R.id.review_movie_content);
        reviewContent.setText(content);
    }
}
