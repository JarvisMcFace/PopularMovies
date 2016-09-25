package com.hughesdigitalimage.popularmovies.util;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

/**
 * Created by David on 9/24/16.
 */

public class FetchMoviePoster {

    public static void execute(Fragment fragment, String posterURL, ImageView imageView, final ProgressBar progressBar) {

        Glide.with(fragment)
                .load(posterURL)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }
}
