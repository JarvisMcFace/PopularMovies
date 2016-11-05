package com.hughesdigitalimage.popularmovies.to.video;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class MovieVideoTO implements Parcelable {

    @SerializedName("id")
    private Integer id;
    @Expose
    private List<MovieVideoResultTO> results = new ArrayList<MovieVideoResultTO>();


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieVideoResultTO> getResults() {
        return results;
    }

    public void setResults(List<MovieVideoResultTO> results) {
        this.results = results;
    }


    protected MovieVideoTO(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            results = new ArrayList<MovieVideoResultTO>();
            in.readList(results, MovieVideoResultTO.class.getClassLoader());
        } else {
            results = null;
        }
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
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieVideoTO> CREATOR = new Parcelable.Creator<MovieVideoTO>() {
        @Override
        public MovieVideoTO createFromParcel(Parcel in) {
            return new MovieVideoTO(in);
        }

        @Override
        public MovieVideoTO[] newArray(int size) {
            return new MovieVideoTO[size];
        }
    };
}