package com.optimustechproject.project2.Interface;

import com.optimustechproject.project2.Models.CreatedTrainingsPOJO;
import com.optimustechproject.project2.Models.LoginDataPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by satyam on 6/8/17.
 */

public interface CreatedTrainingsRequest {
    @GET("skills_req/CreatedTrainings.php")
    Call<CreatedTrainingsPOJO> requestResponse();
}
