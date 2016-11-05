package com.hughesdigitalimage.popularmovies.adapter.video;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hughesdigitalimage.popularmovies.R;
import com.hughesdigitalimage.popularmovies.adapter.video.viewholder.VideoTypeHolder;
import com.hughesdigitalimage.popularmovies.adapter.video.viewholder.VideosTypeDetailHolder;
import com.hughesdigitalimage.popularmovies.fragment.details.MovieVideoCallback;
import com.hughesdigitalimage.popularmovies.to.video.MovieVideoResultTO;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by David on 11/3/16.
 */

public class MovieVideoAdapter extends ExpandableRecyclerViewAdapter<VideoTypeHolder, VideosTypeDetailHolder> {

    private WeakReference<MovieVideoCallback> weakReferenceMovieVideoCallback;

    public MovieVideoAdapter(List<? extends ExpandableGroup> groups, WeakReference<MovieVideoCallback> weakReferenceMovieVideoCallback) {
        super(groups);
        this.weakReferenceMovieVideoCallback = weakReferenceMovieVideoCallback;
    }

    @Override
    public VideoTypeHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_video_type, parent, false);
        return new VideoTypeHolder(view);
    }

    @Override
    public VideosTypeDetailHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_video_details, parent, false);
        return new VideosTypeDetailHolder(view);
    }

    @Override
    public void onBindChildViewHolder(VideosTypeDetailHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final MovieVideoResultTO movieVideoResult = (MovieVideoResultTO) group.getItems().get(childIndex);
        holder.onBind(movieVideoResult);

        View childView = holder.getChildView();
        childView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MovieVideoCallback movieVideoCallback  = weakReferenceMovieVideoCallback.get();
                movieVideoCallback.onMovieSelected(movieVideoResult);
            }
        });
    }

    @Override
    public void onBindGroupViewHolder(VideoTypeHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setTypeTitle(group);
    }
}
