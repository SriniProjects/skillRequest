package com.optimustechproject.project2.Interface;

import com.optimustechproject.project2.Models.CheckLoginPOJO;
import com.optimustechproject.project2.Models.LoginDataPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * Created by satyam on 30/7/17.
 */

public interface CheckLoginRequest {
    @GET("skills_req/CheckLogin.php")
    Call<CheckLoginPOJO> requestResponse(@Query("email") String email);
}
