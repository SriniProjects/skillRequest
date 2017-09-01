package com.optimustechproject.project2.Interface;

import com.optimustechproject.project2.Models.CheckLoginPOJO;
import com.optimustechproject.project2.Models.EnquiriesPOJO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by satyam on 4/8/17.
 */

public interface EnquiriesRequest {
    @GET("skillQuest201702/Enquiries.php")
    Call<EnquiriesPOJO> requestResponse(@Query("training_id") String t_id, @Query("message") String msg);
}
