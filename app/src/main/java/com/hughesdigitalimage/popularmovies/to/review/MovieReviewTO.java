package com.hughesdigitalimage.popularmovies.to.review;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class MovieReviewTO implements Parcelable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("page")
    private Integer page;
    @SerializedName("results")
    private List<ReviewTO> results = new ArrayList<ReviewTO>();
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("total_results")
    private Integer totalResults;


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    public List<ReviewTO> getResults() {
        return results;
    }
    public void setResults(List<ReviewTO> results) {
        this.results = results;
    }
    public Integer getTotalPages() {
        return totalPages;
    }
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
    public Integer getTotalResults() {
        return totalResults;
    }
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }


    protected MovieReviewTO(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        page = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            results = new ArrayList<ReviewTO>();
            in.readList(results, ReviewTO.class.getClassLoader());
        } else {
            results = null;
        }
        totalPages = in.readByte() == 0x00 ? null : in.readInt();
        totalResults = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
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
        if (totalPages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalPages);
        }
        if (totalResults == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalResults);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieReviewTO> CREATOR = new Parcelable.Creator<MovieReviewTO>() {
        @Override
        public MovieReviewTO createFromParcel(Parcel in) {
            return new MovieReviewTO(in);
        }

        @Override
        public MovieReviewTO[] newArray(int size) {
            return new MovieReviewTO[size];
        }
    };
}