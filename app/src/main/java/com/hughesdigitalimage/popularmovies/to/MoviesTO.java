package com.hughesdigitalimage.popularmovies.to;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 9/21/16.
 */

public class MoviesTO implements Serializable{

    private static final long serialVersionUID = -3748871556194934840L;

    private Integer page;
    private List<ResultTO> results = new ArrayList<ResultTO>();
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

    public List<ResultTO> getResults() {
        return results;
    }

    public void setResults(List<ResultTO> results) {
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

}
