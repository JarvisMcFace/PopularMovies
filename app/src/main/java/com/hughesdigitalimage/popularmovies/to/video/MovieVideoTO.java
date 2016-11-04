
package com.hughesdigitalimage.popularmovies.to.video;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class MovieVideoTO {

    @SerializedName("id")
    private Integer id;
    @Expose
    private List<MovieResultTO> results = new ArrayList<MovieResultTO>();


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<MovieResultTO> getResults() {
        return results;
    }

    public void setResults(List<MovieResultTO> results) {
        this.results = results;
    }

}
