package com.optimustechproject.project2.Interface;

import com.optimustechproject.project2.Models.CreatedTrainingsPOJO;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by satyam on 6/8/17.
 */

public interface TrainingDetailsRequest {
    @GET("skills_req/TrainingDetails.php")
    Call<CreatedTrainingsPOJO> requestResponse();
}
