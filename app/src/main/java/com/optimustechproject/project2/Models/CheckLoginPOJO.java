package com.optimustechproject.project2.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 30/7/17.
 */

public class CheckLoginPOJO {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private LoginDataumPOJO data;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public LoginDataumPOJO getData() {
        return data;
    }

    public void setData(LoginDataumPOJO data) {
        this.data = data;
    }
}
