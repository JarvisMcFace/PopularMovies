package com.hughesdigitalimage.popularmovies.adapter.video.viewholder;

import android.view.View;
import android.widget.TextView;

import com.hughesdigitalimage.popularmovies.R;
import com.hughesdigitalimage.popularmovies.to.video.MovieVideoResultTO;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

/**
 * Created by David on 11/3/16.
 */

public class VideosTypeDetailHolder extends ChildViewHolder {

    private TextView videoName;
    private View childView;

    public VideosTypeDetailHolder(View itemView) {
        super(itemView);
        videoName = (TextView) itemView.findViewById(R.id.video_type_name);
        childView = itemView.findViewById(R.id.movie_video);
    }

    public void onBind(MovieVideoResultTO movieVideoResult) {
        videoName.setText(movieVideoResult.getName());
    }

    public View getChildView() {
        return childView;
    }

}
