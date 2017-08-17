package com.optimustechproject.project2.Interface;

import com.optimustechproject.project2.Models.RegDataPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by satyam on 23/6/17.
 */

public interface RegistrationRequest {
    @GET("skills_req/Registration.php")
    Call<RegDataPOJO> requestResponse(@Query("first_name") String first_name, @Query("last_name") String last_name, @Query("dob") String dob, @Query("gender") String gender, @Query("mobile") String mobile,@Query("latitude") String latitude,@Query("longitude") String longitude, @Query("email") String username, @Query("password") String password,@Query("location") String location, @Query("fcm") String fcm,@Query("fileName") String fileName);
}
