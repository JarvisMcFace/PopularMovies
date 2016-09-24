package com.hughesdigitalimage.popularmovies.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.hughesdigitalimage.popularmovies.R;
import com.hughesdigitalimage.popularmovies.adapter.viewholder.MovieViewHolder;
import com.hughesdigitalimage.popularmovies.fragment.PopularMoviesFragment;
import com.hughesdigitalimage.popularmovies.to.ResultTO;
import com.hughesdigitalimage.popularmovies.util.StringUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by David on 9/22/16.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_MOVIE_LISTING = 1;
    private static final int VIEW_TYPE_MOVIE_EMPTY_STATE = 2;
    private List<ResultTO> moveResults;
    private WeakReference<Activity> contextWeakReference;

    public MovieAdapter(List<ResultTO> moveResults, WeakReference<Activity> contextWeakReference) {
        this.moveResults = moveResults;
        this.contextWeakReference = contextWeakReference;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_movie_row, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ImageView moviePoster = ((MovieViewHolder) holder).getMoviePoster();
        final ProgressBar progressSpinner = ((MovieViewHolder) holder).getProgressSpinner();

        progressSpinner.setVisibility(View.VISIBLE);
        String moviePosterPath = moveResults.get(position).getPosterPath();

        if (StringUtils.isEmpty(moviePosterPath)) {
            //TOOD display error
            return;
        }

        Activity activity = contextWeakReference.get();
        String posterURL = PopularMoviesFragment.MOVIE_DB_IMAGE_URL + moviePosterPath;
        Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], posterURL = [" + posterURL + "]");

//        Glide.with(activity)
//                .load(posterURL)
//                .dontAnimate()
//
//                .into(moviePoster);


        Glide.with(activity)
                .load(posterURL)
                .crossFade()

                .fitCenter()
                .into(new GlideDrawableImageViewTarget(moviePoster) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        progressSpinner.setVisibility(View.GONE);
                    }
                });


    }


    @Override
    public int getItemCount() {
        return moveResults.size();
    }
}
