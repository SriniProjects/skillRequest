package com.optimustechproject.project2.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by satyam on 6/8/17.
 */

public class CategoriesPOJO {
    @SerializedName("id")
    @Expose
    private List<String> id = null;
    @SerializedName("cat_name")
    @Expose
    private List<String> catName = null;

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public List<String> getCatName() {
        return catName;
    }

    public void setCatName(List<String> catName) {
        this.catName = catName;
    }
}
