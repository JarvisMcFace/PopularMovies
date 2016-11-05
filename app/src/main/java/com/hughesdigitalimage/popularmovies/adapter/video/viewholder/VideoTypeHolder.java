package com.hughesdigitalimage.popularmovies.adapter.video.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hughesdigitalimage.popularmovies.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

/**
 * Created by David on 11/3/16.
 */

public class VideoTypeHolder extends GroupViewHolder {

    private TextView typeTitle;
    private ImageView chevron;

    public VideoTypeHolder(View itemView) {
        super(itemView);

        typeTitle = (TextView) itemView.findViewById(R.id.video_type_title);
        chevron = (ImageView) itemView.findViewById(R.id.video_chevron);
    }

    public void setTypeTitle(ExpandableGroup group) {
        typeTitle.setText(group.getTitle());
    }

    @Override
    public void expand() {
        chevron.setImageResource(R.drawable.ic_chevron_down);
    }

    @Override
    public void collapse() {
        chevron.setImageResource(R.drawable.ic_chevron_up);
    }
}
