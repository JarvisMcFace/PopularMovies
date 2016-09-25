
package com.hughesdigitalimage.popularmovies.to.movie;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ProductionCountryTO implements Serializable{

    private static final long serialVersionUID = -1445723262215761975L;

    @SerializedName("iso_3166_1")
    private String iso31661;
    @SerializedName("name")
    private String name;

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
