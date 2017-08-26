package com.optimustechproject.project2.Interface;

import com.optimustechproject.project2.Models.CreateTrainingPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by satyam on 25/8/17.
 */

public interface DeleteTrainingRequest {
    @GET("skills_req/DeleteTraining.php")
    Call<CreateTrainingPOJO> requestResponse(@Query("training_id") String training_id);

}
