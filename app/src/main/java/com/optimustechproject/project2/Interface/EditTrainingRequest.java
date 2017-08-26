package com.optimustechproject.project2.Interface;

import com.optimustechproject.project2.Models.CreateTrainingPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by satyam on 6/8/17.
 */

public interface EditTrainingRequest {
    @GET("skills_req/EditTraining.php")
    Call<CreateTrainingPOJO> requestResponse(@Query("training_id") String training_id,@Query("name") String name,@Query("latitude") String latitude,@Query("longitude") String longitude,@Query("venue") String venue,@Query("date") String date,@Query("kl1") String kl1,@Query("kl2") String kl2,@Query("kl3") String kl3,@Query("price") String price,@Query("cat_id") String cat_id,@Query("avail") String avail,@Query("duration") String duration,@Query("desc") String desc,@Query("fTime") String fTime,@Query("tTime") String tTime,@Query("fileName") String fileName);
}
