
package com.hughesdigitalimage.popularmovies.to.movie;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductionCompanyTO implements Serializable{

    private static final long serialVersionUID = 8255000584959123980L;

    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private Integer id;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
