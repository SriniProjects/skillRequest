package com.optimustechproject.project2.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 6/8/17.
 */

public class CreatedTrainingsPOJO {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("trainings")
    @Expose
    private TrainingsPOJO trainings;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TrainingsPOJO getTrainings() {
        return trainings;
    }

    public void setTrainings(TrainingsPOJO trainings) {
        this.trainings = trainings;
    }
}
