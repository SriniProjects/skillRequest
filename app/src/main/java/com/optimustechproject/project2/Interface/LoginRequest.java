package com.optimustechproject.project2.Interface;



import com.optimustechproject.project2.Models.LoginDataPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by satyam on 23/6/17.
 */

public interface LoginRequest {
    @GET("skillQuest201702/Login.php")
    Call<LoginDataPOJO> requestResponse(@Query("fcm") String fcm);
}
