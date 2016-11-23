package com.hughesdigitalimage.popularmovies.to;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 9/21/16.
 */

public class PopularMoviesTO implements Parcelable {


    private Integer page;
    private List<PopularMovieDetailsTO> results = new ArrayList<PopularMovieDetailsTO>();
    @SerializedName("total_results")
    private Integer totalResults;
    @SerializedName("total_pages")
    private Integer totalPages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<PopularMovieDetailsTO> getResults() {
        return results;
    }

    public void setResults(List<PopularMovieDetailsTO> results) {
        this.results = results;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }


    protected PopularMoviesTO(Parcel in) {
        page = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            results = new ArrayList<PopularMovieDetailsTO>();
            in.readList(results, PopularMovieDetailsTO.class.getClassLoader());
        } else {
            results = null;
        }
        totalResults = in.readByte() == 0x00 ? null : in.readInt();
        totalPages = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (page == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(page);
        }
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
        if (totalResults == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalResults);
        }
        if (totalPages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalPages);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PopularMoviesTO> CREATOR = new Parcelable.Creator<PopularMoviesTO>() {
        @Override
        public PopularMoviesTO createFromParcel(Parcel in) {
            return new PopularMoviesTO(in);
        }

        @Override
        public PopularMoviesTO[] newArray(int size) {
            return new PopularMoviesTO[size];
        }
    };
}